package com.mylar.lib.rabbitmq;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRabbit
@SpringBootApplication
public class MylarLibRabbitmqApplication {

	public static void main(String[] args) {
		SpringApplication.run(MylarLibRabbitmqApplication.class, args);
	}

}
