package net.xiaomotou.freight.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class ManagerController {


    @RequestMapping("/")
    public String getJson(){
        ModelAndView modelAndView = new ModelAndView();

        return "{msg:\"这是一个json\"}";
    }

}
