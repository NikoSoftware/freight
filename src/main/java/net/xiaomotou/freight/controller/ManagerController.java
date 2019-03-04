package net.xiaomotou.freight.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import net.xiaomotou.freight.net.ResultModel;
import net.xiaomotou.freight.poUser.entity.PoUser;
import net.xiaomotou.freight.poUser.entity.WechartOpenId;
import net.xiaomotou.freight.poUser.mapper.PoUserMapper;
import net.xiaomotou.freight.priceVersion.entity.KeyValue;
import net.xiaomotou.freight.priceVersion.entity.PriceVersion;
import net.xiaomotou.freight.priceVersion.entity.PriceVersionInfo;
import net.xiaomotou.freight.priceVersion.mapper.PriceVersionMapper;
import net.xiaomotou.freight.redis.RedisUtil;
import net.xiaomotou.freight.net.HttpUtils;
import net.xiaomotou.freight.net.UrlResult;
import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.*;


@RestController
public class ManagerController {

    private String wechatHost = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    PoUserMapper poUserMapper;
    @Autowired
    PriceVersionMapper priceVersionMapper;

    @Autowired
    private RedisUtil redisUtil;

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
    public String addVersion() {

        HashMap<Double, Double> hashMap = new HashMap<>();
        hashMap.put(30.5, 120.5);
        hashMap.put(15.5, 100.4);
        hashMap.put(50.5, 100.4);
        hashMap.put(80.5, 90.4);
        hashMap.put(120.0, 60.4);

        String data = JSON.toJSONString(hashMap);


        return data;
    }

    @RequestMapping(value = "/getVersion", method = RequestMethod.GET)
    public String getVersion() {

        QueryWrapper<PriceVersion> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("versionId");

        PriceVersion priceVersion = priceVersionMapper.selectOne(queryWrapper);
        PriceVersionInfo priceInfo = new PriceVersionInfo();
        priceInfo.setVersionId(priceVersion.getVersionId());
        priceInfo.setNum(priceVersion.getNum());
        priceInfo.setToWeight(priceVersion.getToWeight());
        priceInfo.setToVolume(priceVersion.getToVolume());
        priceInfo.setCurPrice(priceVersion.getCurPrice());
        priceInfo.setCurVolume(priceVersion.getCurVolume());
        priceInfo.setCreateTime(priceInfo.getCreateTime());
        ArrayList<KeyValue> volumeList = new ArrayList<>();
        HashMap<Double, Double> mapVolume = (HashMap<Double, Double>) JSON
                .parseObject(priceVersion.getVolumes(),new TypeReference<Map<Double, Double>>() {});
        for (Double keys : mapVolume.keySet()) {
            KeyValue keyValue = new KeyValue();
            keyValue.setKey(keys);
            keyValue.setRate(keys*100/priceInfo.getToVolume());
            keyValue.setValue(mapVolume.get(keys));
            volumeList.add(keyValue);
        }
        volumeList.sort((o1, o2) -> (int) ((o1.getKey() - o2.getKey()) * 100));
        priceInfo.setVolumes(volumeList);

        ArrayList<KeyValue> weightList = new ArrayList<>();
        HashMap<Double, Double> weightMap = (HashMap<Double, Double>) JSON
                .parseObject(priceVersion.getWeights(),new TypeReference<Map<Double, Double>>() {});
        for (Double keys : weightMap.keySet()) {
            KeyValue keyValue = new KeyValue();
            keyValue.setKey(keys);
            keyValue.setRate(keys*100/priceInfo.getToWeight());
            keyValue.setValue(weightMap.get(keys));
            weightList.add(keyValue);
        }
        weightList.sort((o1, o2) -> (int) ((o1.getKey() - o2.getKey()) * 100));
        priceInfo.setWeights(weightList);

        return JSON.toJSONString(priceInfo);
    }


}
