package com.mylar.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.mylar.lib.base",
        "com.mylar.sample",
        "com.mylar.lib.redis"
})
public class MylarSampleApplication {

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(MylarSampleApplication.class, args);
    }
}