package com.mylar.lib.quartz;

import com.mylar.lib.quartz.core.LocalConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

/**
 * Quartz
 *
 * @author wangz
 * @date 2021/7/18 0018 10:29
 */
@SpringBootApplication
public class MylarLibQuartzApplication {

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(MylarLibQuartzApplication.class, args);

        LocalConfig.environment = context.getBean(Environment.class);
    }

}
