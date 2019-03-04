package net.xiaomotou.freight.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import net.xiaomotou.freight.net.ResultModel;
import net.xiaomotou.freight.poUser.entity.PoUser;
import net.xiaomotou.freight.poUser.entity.WechartOpenId;
import net.xiaomotou.freight.poUser.mapper.PoUserMapper;
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
import java.util.HashMap;


@RestController
public class ManagerController {

    private String wechatHost="https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    PoUserMapper poUserMapper;

    @Autowired
    private RedisUtil redisUtil;

    @RequestMapping(value="/getWeChart", method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public String setWechatCode(@NotNull String code,@NotNull String nickName,String avatarUrl,String gender){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("appid","wxb450b284a4ddabad");
        hashMap.put("secret","1f3d234e529e5c8ce543bcaffeaffe7e");
        hashMap.put("js_code",code);
        hashMap.put("grant_type","authorization_code");
        String data = "";
        try {
            UrlResult urlResult = HttpUtils.sendGet(wechatHost,null,hashMap);
            if(urlResult.getStatusCode()== 200){
                data = urlResult.getHtmlContent();
            }else{
                data ="";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        WechartOpenId wechartOpenId = JSON.parseObject(data, WechartOpenId.class);

        PoUser updateInfo = null;
        ResultModel<PoUser> resultModel = new ResultModel<>();
        if(!TextUtils.isEmpty(wechartOpenId.getOpenid())){
            PoUser poUser = new PoUser();
            poUser.setOpenId(wechartOpenId.getOpenid());
            poUser.setNickName(nickName);
            poUser.setGender(gender);
            poUser.setAvatarUrl(avatarUrl);
            QueryWrapper<PoUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("openId",poUser.getOpenId());
            updateInfo = poUserMapper.selectOne(queryWrapper);
            if(updateInfo==null){
                poUserMapper.insert(poUser);
            }else{
                UpdateWrapper<PoUser> updateWrapper = new UpdateWrapper<>();
                poUser.setAdministrator(updateInfo.getAdministrator());
                poUserMapper.update(poUser,updateWrapper);
            }

            resultModel.setData(poUser);
            System.out.println(poUser.toString());
        }else{
            resultModel.setCode(ResultModel.ERROR)
            .setMsg(wechartOpenId.getErrmsg());
        }
        data = JSON.toJSONString(resultModel);
        return data;

    }

}
