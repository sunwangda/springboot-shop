package com.shop.user.client.intercepter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public class AuthorityInterceptor extends HandlerInterceptorAdapter{
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private JedisPool jedisPool;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		logger.info("请求" + request.getRequestURI() + JSON.toJSONString(request.getParameterMap()));
		Object userId = request.getSession().getAttribute("userId");
		System.out.println(userId);
		Jedis jedis = jedisPool.getResource();
		String key = userId+"user";
		Boolean result = jedis.exists(key);
		if(jedis != null) {
			jedis.close();
		}
		System.out.println(result);
		if(!result) {
			return false;
		}
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		logger.info("执行完方法之后进执行(Controller方法调用之后)，但是此时还没进行视图渲染");
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		logger.info("整个请求都处理完咯，DispatcherServlet也渲染了对应的视图咯，此时我可以做一些清理的工作了");
	}
}
