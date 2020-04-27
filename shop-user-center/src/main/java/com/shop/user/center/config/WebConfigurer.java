package com.shop.user.center.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.shop.user.center.intercepter.AuthorityInterceptor;

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
	
}
