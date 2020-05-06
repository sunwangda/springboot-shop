package com.shop.user.client;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication
@EnableDubbo
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class ShopUserClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopUserClientApplication.class, args);
    }

}
