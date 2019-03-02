package net.xiaomotou.freight.priceVersion.controller;


import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author niko
 * @since 2019-02-24
 */
@RestController
@RequestMapping("/priceVersion")
public class PriceVersionController {

    @ResponseBody
    @RequestMapping("/getAllPriceVersion")
    public String getAllPriceVersion(){



        return "{这是一条数据}";
    }


}
