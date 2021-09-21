package com.mylar.lib.simple.controller;

import com.mylar.lib.simple.config.AccountConfig;
import com.mylar.lib.simple.config.AddressConfig;
import com.mylar.lib.simple.config.BirthdayConfig;
import com.mylar.lib.simple.config.ScoreConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 控制器-配置
 *
 * @author wangz
 * @date 2021/9/21 0021 19:27
 */
@Slf4j
@RestController
@RequestMapping("/config")
public class ConfigController {

    /**
     * 配置-地址
     */
    @Autowired
    private AddressConfig addressConfig;

    /**
     * 配置-生日
     */
    @Autowired
    private BirthdayConfig birthdayConfig;

    /**
     * 配置-分数
     */
    @Autowired
    private ScoreConfig scoreConfig;

    /**
     * 配置-账户
     */
    @Autowired
    private AccountConfig accountConfig;

    /**
     * 环境变量
     */
    @Autowired
    private Environment environment;

    /**
     * 地址
     *
     * @return 响应
     */
    @GetMapping("/address")
    public String address() {
        String address = this.addressConfig.toString();
        log.info(address);
        return address;
    }

    /**
     * 生日
     *
     * @return 响应
     */
    @GetMapping("/birthday")
    public String birthday() {
        String birthday = this.birthdayConfig.toString();
        log.info(birthday);
        return birthday;
    }

    /**
     * 分数
     *
     * @return 响应
     */
    @GetMapping("/score")
    public String score() {
        String score = this.scoreConfig.toString();
        log.info(score);
        return score;
    }

    /**
     * 账户
     *
     * @return 响应
     */
    @GetMapping("/account")
    public String account() {
        String account = this.accountConfig.toString();
        log.info(account);
        return account;
    }

    /**
     * 账户
     *
     * @return 响应
     */
    @GetMapping("/environment")
    public String environment(String property) {
        String propertyValue = this.environment.getProperty(property);
        log.info(propertyValue);
        return propertyValue;
    }
}
