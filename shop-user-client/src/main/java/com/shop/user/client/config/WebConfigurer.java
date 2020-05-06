package com.shop.user.client.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.shop.user.client.intercepter.AuthorityInterceptor;

@Configuration
public class WebConfigurer extends WebMvcConfigurationSupport{
	
	@Autowired
    private AuthorityInterceptor authorityInterceptor;
	
	@Override
	protected void addInterceptors(InterceptorRegistry registry) {
		List<String> excludePath = new ArrayList<String>();
		excludePath.add("/swagger-ui.html/**");
		excludePath.add("/v2/**");
		excludePath.add("/webjars/**");
		excludePath.add("/swagger-resources/**");
		excludePath.add("/user/login/**");
		registry.addInterceptor(authorityInterceptor)
				.addPathPatterns("/**")
				.excludePathPatterns(excludePath);
	}
	
	@Override

    protected void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("swagger-ui.html")

                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")

                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
	
	/**

     * 使用@Bean注解注入第三方的解析框架（fastJson）

     *

     * @return

     */

    @Bean

    public HttpMessageConverters fastJsonHttpMessageConverters() {

// 1、首先需要先定义一个convert转换消息对象

        FastJsonHttpMessageConverter fastConverter =new FastJsonHttpMessageConverter();

        // 2、添加fastJson的配置信息，比如：是否要格式化返回的json数据

        FastJsonConfig fastJsonConfig =new FastJsonConfig();

        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);

        // 3、在convert中添加配置信息

        fastConverter.setFastJsonConfig(fastJsonConfig);

        return new HttpMessageConverters(fastConverter);

    }
	
}
