package com.shop.user.center;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan({"com.shop.user.center.dao"})
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class}) //没有配置数据库得时候写这个可以启动springboot项目
public class ShopUserCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopUserCenterApplication.class, args);
    }

}
