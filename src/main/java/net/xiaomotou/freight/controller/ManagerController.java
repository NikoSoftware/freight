package net.xiaomotou.freight.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import net.xiaomotou.freight.model.ImageModel;
import net.xiaomotou.freight.net.HttpUtils;
import net.xiaomotou.freight.net.ResultModel;
import net.xiaomotou.freight.net.UrlResult;
import net.xiaomotou.freight.poOrder.entity.PoOrder;
import net.xiaomotou.freight.poOrder.entity.PoOrderAndUser;
import net.xiaomotou.freight.poOrder.mapper.PoOrderMapper;
import net.xiaomotou.freight.poUser.entity.PoUser;
import net.xiaomotou.freight.poUser.entity.WechartOpenId;
import net.xiaomotou.freight.poUser.mapper.PoUserMapper;
import net.xiaomotou.freight.priceVersion.entity.KeyValue;
import net.xiaomotou.freight.priceVersion.entity.PriceVersion;
import net.xiaomotou.freight.priceVersion.entity.PriceVersionInfo;
import net.xiaomotou.freight.priceVersion.mapper.PriceVersionMapper;
import net.xiaomotou.freight.redis.RedisUtil;
import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class ManagerController {

    private String wechatHost = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    PoUserMapper poUserMapper;
    @Autowired
    PriceVersionMapper priceVersionMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private PoOrderMapper poOrderMapper;

    @RequestMapping(value = "/getWeChart", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String setWechatCode(@NotNull String code, @NotNull String nickName, String avatarUrl, String gender) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("appid", "wxb450b284a4ddabad");
        hashMap.put("secret", "1f3d234e529e5c8ce543bcaffeaffe7e");
        hashMap.put("js_code", code);
        hashMap.put("grant_type", "authorization_code");
        String data = "";
        try {
            UrlResult urlResult = HttpUtils.sendGet(wechatHost, null, hashMap);
            if (urlResult.getStatusCode() == 200) {
                data = urlResult.getHtmlContent();
            } else {
                data = "";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        WechartOpenId wechartOpenId = JSON.parseObject(data, WechartOpenId.class);

        PoUser updateInfo = null;
        ResultModel<PoUser> resultModel = new ResultModel<>();
        if (!TextUtils.isEmpty(wechartOpenId.getOpenid())) {
            PoUser poUser = new PoUser();
            poUser.setOpenId(wechartOpenId.getOpenid());
            poUser.setNickName(nickName);
            poUser.setGender(gender);
            poUser.setAvatarUrl(avatarUrl);
            QueryWrapper<PoUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("openId", poUser.getOpenId());
            updateInfo = poUserMapper.selectOne(queryWrapper);
            if (updateInfo == null) {
                poUserMapper.insert(poUser);
            } else {
                UpdateWrapper<PoUser> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("openId", poUser);
                poUser.setAdministrator(updateInfo.getAdministrator());
                poUserMapper.update(poUser, updateWrapper);
            }

            resultModel.setData(poUser);
            System.out.println(poUser.toString());
        } else {
            resultModel.setCode(ResultModel.ERROR)
                    .setMsg(wechartOpenId.getErrmsg());
        }
        data = JSON.toJSONString(resultModel);
        return data;
    }

    @RequestMapping(value = "/addVersion") //, method = RequestMethod.POST
    public String addVersion(@NotNull Double totalVolume, @NotNull Double totalWeight, @NotNull String volumeMap, @NotNull String weightMap) {

        HashMap<Double, Double> volumeHashMap = (HashMap<Double, Double>) JSON.parseObject(volumeMap, new TypeReference<Map<Double, Double>>() {
        });
        HashMap<Double, Double> weightHashMap = (HashMap<Double, Double>) JSON.parseObject(weightMap, new TypeReference<Map<Double, Double>>() {
        });

        PriceVersion priceVersion = new PriceVersion();
        priceVersion.setNum(0);
        priceVersion.setVolumes(JSON.toJSONString(volumeHashMap));
        priceVersion.setWeights(JSON.toJSONString(weightHashMap));
        priceVersion.setToVolume(totalVolume);
        priceVersion.setToWeight(totalWeight);
        priceVersion.setCurPrice(0d);
        priceVersion.setCurVolume(0d);
        priceVersion.setCurWeight(0d);
        priceVersion.setCreateTime(LocalDateTime.now());

        System.out.println(JSON.toJSONString(priceVersion));

        ResultModel resultModel = new ResultModel();
        int count = 0;
        try {
            count = priceVersionMapper.insert(priceVersion);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (count == 0) {
            resultModel.setCode(ResultModel.ERROR)
                    .setMsg("Parameter error");
        } else {
            resultModel.setCode(ResultModel.OK);
        }
        return resultModel.toString();

    }


    @RequestMapping(value = "/setPrice", method = RequestMethod.POST)
    public String setPrice(@NotNull Double price) {

        PriceVersion priceVersion = priceVersionMapper.getMaxPriceVersion();
        priceVersion.setCurPrice(price);

        UpdateWrapper<PriceVersion> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("versionId", priceVersion.getVersionId());

        ResultModel resultModel = new ResultModel();
        int count = 0;
        try {
            count = priceVersionMapper.update(priceVersion, updateWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (count == 0) {
            resultModel.setCode(ResultModel.ERROR)
                    .setMsg("Parameter error");
        } else {
            resultModel.setCode(ResultModel.OK);
        }
        return resultModel.toString();
    }


    @RequestMapping(value = "/getVersion", method = RequestMethod.GET)
    public String getVersion() {

        PriceVersion priceVersion = priceVersionMapper.getMaxPriceVersion();
        QueryWrapper<PoOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("version", priceVersion.getVersionId());
        List<PoOrder> poOrders = poOrderMapper.selectList(queryWrapper);


        PriceVersionInfo priceInfo = new PriceVersionInfo();
        priceInfo.setVersionId(priceVersion.getVersionId());
        priceInfo.setToWeight(priceVersion.getToWeight());
        priceInfo.setToVolume(priceVersion.getToVolume());
        priceInfo.setCreateTime(priceInfo.getCreateTime());
        priceInfo.setCurPrice(priceVersion.getCurPrice());
        if (poOrders != null) {
            Integer count = 0;
            Double weightFlag = 0d;
            Double volumeFlag = 0d;
            for (PoOrder poOrder : poOrders) {
                if (poOrder.getStatus().equals("1")) {
                    weightFlag += poOrder.getWeight();
                    volumeFlag += poOrder.getVolume();
                    count++;
                }
            }
            priceInfo.setNum(count);
            priceInfo.setCurWeight(weightFlag);
            priceInfo.setCurVolume(volumeFlag);
        }

        ArrayList<KeyValue> volumeList = new ArrayList<>();
        HashMap<Double, Double> mapVolume = (HashMap<Double, Double>) JSON
                .parseObject(priceVersion.getVolumes(), new TypeReference<Map<Double, Double>>() {
                });
        for (Double keys : mapVolume.keySet()) {
            KeyValue keyValue = new KeyValue();
            keyValue.setKey(keys);
            keyValue.setRate(keys * 100 / priceInfo.getToVolume());
            keyValue.setValue(mapVolume.get(keys));
            volumeList.add(keyValue);
        }
        volumeList.sort((o1, o2) -> (int) ((o1.getKey() - o2.getKey()) * 100));
        double fv = 0d;
        for (int i = 0; i < volumeList.size(); i++) {
            KeyValue keyValue = volumeList.get(i);
            if (i == 0) {
                fv = keyValue.getRate();
                continue;
            } else {
                double fz = keyValue.getRate();
                volumeList.get(i).setRate(keyValue.getRate() - fv);
                fv = fz;
            }
        }
        priceInfo.setVolumes(volumeList);
        //重量
        ArrayList<KeyValue> weightList = new ArrayList<>();
        HashMap<Double, Double> weightMap = (HashMap<Double, Double>) JSON
                .parseObject(priceVersion.getWeights(), new TypeReference<Map<Double, Double>>() {
                });
        for (Double keys : weightMap.keySet()) {
            KeyValue keyValue = new KeyValue();
            keyValue.setKey(keys);
            keyValue.setRate(keys * 100 / priceInfo.getToWeight());
            keyValue.setValue(weightMap.get(keys));
            weightList.add(keyValue);
        }
        weightList.sort((o1, o2) -> (int) ((o1.getKey() - o2.getKey()) * 100));

        double fw = 0d;
        for (int i = 0; i < weightList.size(); i++) {
            KeyValue keyValue = weightList.get(i);
            if (i == 0) {
                fw = keyValue.getRate();
                continue;
            } else {
                double fz = keyValue.getRate();
                weightList.get(i).setRate(keyValue.getRate() - fw);
                fw = fz;
            }
        }
        priceInfo.setWeights(weightList);

        ArrayList<ImageModel> list = new ArrayList<>();

        for (int i = 0; i <4 ; i++) {
            ImageModel imageModel = new ImageModel();
            imageModel.setImgPath("/image/pexels-photo-"+(i+1)+".jpeg");

          //   imageModel.setUrl("https://www.xiaomotou.net"); 图片跳转网页

            list.add(imageModel);
        }
        priceInfo.setImageList(list);

        return JSON.toJSONString(priceInfo);
    }


    @RequestMapping(value = "/submitOrder", method = RequestMethod.POST)
    public String submitOrder(PoOrder poOrder) {

        PriceVersion priceVersion = priceVersionMapper.getMaxPriceVersion();
        poOrder.setVersion(priceVersion.getVersionId());
        poOrder.setStatus("0");
        poOrder.setCreateTime(LocalDateTime.now());
        System.out.println(poOrder.toString());
        ResultModel resultModel = new ResultModel();
        int count = 0;
        try {
            count = poOrderMapper.insert(poOrder);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (count == 0) {
            resultModel.setCode(ResultModel.ERROR)
                    .setMsg("Parameter error");
        } else {
            resultModel.setCode(ResultModel.OK);
        }
        return resultModel.toString();
    }

    @RequestMapping(value = "/getOrder", method = RequestMethod.GET)
    public String getOrder(@NotNull String openId) {
        QueryWrapper<PoOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openId", openId);
        queryWrapper.orderByDesc("createTime");

        List<PoOrder> priceVersionList = poOrderMapper.selectList(queryWrapper);
        return JSON.toJSONString(priceVersionList);
    }

    @RequestMapping(value = "/managerAllOrder", method = RequestMethod.GET)
    public String managerAllOrder() {

        PriceVersion priceVersion = priceVersionMapper.getMaxPriceVersion();

        QueryWrapper<PoOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("version", priceVersion.getVersionId());
        queryWrapper.orderByDesc("createTime");

        List<PoOrderAndUser> priceVersionList = poOrderMapper.getPoOrderAndUser(priceVersion.getVersionId());
        return JSON.toJSONString(priceVersionList);
    }


    @RequestMapping(value = "/optionsOrderManager", method = RequestMethod.POST)
    public String optionsOrderManager(Integer id, String status) {
        ResultModel resultModel = new ResultModel();

        if (id == null || TextUtils.isEmpty(status) || !(status.equals("1") || status.equals("2") || status.equals("0"))) {
            resultModel.setCode(ResultModel.ERROR);
            resultModel.setMsg("Parameter error");
            return resultModel.toString();
        }

        try {
            poOrderMapper.updateStatus(id, status);
            resultModel.setCode(ResultModel.OK);
        } catch (Exception e) {
            e.printStackTrace();
            resultModel.setCode(ResultModel.ERROR)
                    .setMsg("Parameter error");
        }
        return resultModel.toString();
    }

    @RequestMapping(value = "/getImageList", method = RequestMethod.GET)
    public String getImageList(){
        ArrayList<ImageModel> list = new ArrayList<>();

        for (int i = 0; i <4 ; i++) {
            ImageModel imageModel = new ImageModel();
            imageModel.setImgPath("/image/pexels-photo-"+(i+1)+".jpeg");
            list.add(imageModel);
        }
        return  JSON.toJSONString(list);
    }



}
