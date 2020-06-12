package com.shop.user.client;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.shop.user.client.config.NettyWebsocketServer;

@SpringBootApplication
@EnableWebMvc
@EnableDubbo
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class ShopUserClientApplication implements CommandLineRunner{
	
	 @Autowired
	 private NettyWebsocketServer nettyServer;
	
    public static void main(String[] args) {
        SpringApplication.run(ShopUserClientApplication.class, args);
    }
    
    @Override
    public void run(String... args) throws Exception {
    	 //netty服务启动的端口号不可和SpringBoot启动类的端口号重复
        nettyServer.start(18083);
        //服务停止时关闭nettyServer
        Runtime.getRuntime().addShutdownHook(new Thread(() -> nettyServer.destroy()));
    }

}
