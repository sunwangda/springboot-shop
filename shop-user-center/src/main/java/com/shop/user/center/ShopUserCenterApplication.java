package com.shop.user.center;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class}) //没有配置数据库得时候写这个可以启动springboot项目
public class ShopUserCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopUserCenterApplication.class, args);
    }

}
