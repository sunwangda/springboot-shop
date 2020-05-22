package com.shop.user.client;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@EnableDubbo
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class ShopUserClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopUserClientApplication.class, args);
    }

}
