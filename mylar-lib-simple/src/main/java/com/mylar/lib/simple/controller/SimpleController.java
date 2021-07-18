package com.mylar.lib.simple.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Simple
 *
 * @author wangz
 * @date 2021/7/18 0018 10:29
 */
@Slf4j
@RestController
@RequestMapping("/simple")
public class SimpleController {

    @GetMapping("/time")
    public String time() {
        String time = new Date().toString();
        log.info(time);
        return time;
    }

}
