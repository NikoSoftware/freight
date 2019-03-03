package net.xiaomotou.freight.controller;


import net.xiaomotou.freight.redis.RedisUtil;
import net.xiaomotou.freight.util.HttpUtils;
import net.xiaomotou.freight.util.UrlResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;


@RestController
public class ManagerController {

    private String wechatHost="https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private RedisUtil redisUtil;
    @RequestMapping(value="/getWeChart", method = RequestMethod.GET)
    public String setWechatCode(@NotNull String code){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("appid","wxb450b284a4ddabad");
        hashMap.put("secret","1f3d234e529e5c8ce543bcaffeaffe7e");
        hashMap.put("js_code",code);
        hashMap.put("grant_type","authorization_code");
        String data = "a";
        try {
            UrlResult urlResult = HttpUtils.sendGet(wechatHost,null,hashMap,"utf_8");
            data = urlResult.getHtmlContent();
        } catch (IOException e) {
            e.printStackTrace();
        }

       // redisUtil.set("time",new Date(),100);
        return data;
    }

}
