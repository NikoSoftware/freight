package net.xiaomotou.freight.poOrder.controller;


import net.xiaomotou.freight.poOrder.mapper.PoOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

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
@RequestMapping("/poOrder")
public class PoOrderController {

    @Autowired
    PoOrderMapper poOrderMapper;


}
