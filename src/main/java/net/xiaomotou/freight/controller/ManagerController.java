package net.xiaomotou.freight.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ManagerController {

    @RequestMapping("/")
    public String getJson(){

        return "{msg:\"这是一个json测试\"}";
    }

}
