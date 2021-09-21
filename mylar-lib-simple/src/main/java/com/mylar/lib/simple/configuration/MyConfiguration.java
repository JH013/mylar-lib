package com.mylar.lib.simple.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangz
 * @date 2021/9/21 0021 22:03
 */
@Configuration
public class MyConfiguration {

    public MyConfiguration() {
        System.out.println("MyConfiguration init.");
    }

    @Bean(initMethod = "init", destroyMethod = "destroy")
    public MyBean myBean() {
        MyBean myBean = new MyBean();
        myBean.setName("test name");
        myBean.setValue("test value");
        return myBean;
    }
}
