package com.mylar.lib.simple.controller;

import com.mylar.lib.simple.configuration.MyBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangz
 * @date 2021/9/21 0021 22:05
 */
@Slf4j
@RestController
@RequestMapping("/bean")
public class BeanController {

    @Autowired
    private MyBean myBean;

    @GetMapping("/myBean")
    public String myBean() {
        String myBean = this.myBean.toString();
        log.info(myBean);
        return myBean;
    }
}
