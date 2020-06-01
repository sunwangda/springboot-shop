package com.shop.user.client.controller;

//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
import java.util.UUID;

//import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.amqp.rabbit.annotation.RabbitHandler;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

//import com.alibaba.fastjson.JSON;
//import com.google.common.collect.Maps;
import com.inner.sdk.enums.LoginInfo.LoginTypeEnum;
import com.inner.sdk.model.UserInfo;
import com.inner.sdk.request.UserInfoRequest;
import com.inner.sdk.service.UserService;
//import com.inner.sdk.utils.IpAddress;
import com.inner.sdk.utils.JsonResult;
import com.shop.user.client.controller.ex.LoginTypeIsNotExistException;
import com.shop.user.client.controller.ex.RequestParamsIsNullException;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping(value = "user")
public class UserController extends BaseController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Reference
	private UserService userService;

	@Autowired
	private JedisPool jedisPool;

	@Autowired
	RabbitTemplate rabbitTemplate; // 使用RabbitTemplate,这提供了接收/发送等等方法

//	@Autowired
//	private SearchService searchService;
//	
//	@Autowired
//    private GoodsRepository goodsRepository;
	
//	@Autowired
//	private JavaMailSender javaMailSender;

	@RequestMapping(value = "login", method = RequestMethod.POST)
	@ApiOperation(value = "检查登录", notes = "检测登录（admin，wx，qq）")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "loginType", value = "登录类型", required = false, type = "String"),
			@ApiImplicitParam(paramType = "query", name = "admin", value = "用户名", required = false, type = "String"),
			@ApiImplicitParam(paramType = "query", name = "password", value = "密码", required = false, type = "String") })
	public JsonResult Login(String loginType, String admin, String password, HttpSession session) {
		if (admin == null || password == null) {
			throw new RequestParamsIsNullException("请求参数为null");
		}
		if (LoginTypeEnum.getLoginTypeEnum(loginType) == null) {
			throw new LoginTypeIsNotExistException("登录方式不存在");
		}
		UserInfo data = null;
		if (LoginTypeEnum.ADMIN_LOGIN.equals(LoginTypeEnum.getLoginTypeEnum(loginType))) {
			data = userService.loginService(admin, password);
		}
		String token = UUID.randomUUID().toString();
		Jedis j = jedisPool.getResource();
		String key = data.getUserId() + "user";
		if (j.exists(key)) {
			j.del(key);
			System.out.println("删除存在的redis，key");
		}
		j.set(key, token);
		j.expire(key, 600);
		session.setAttribute("userId", data.getUserId());
		return new JsonResult(SUCCESS, data);
	}

	@RequestMapping(value = "register", method = RequestMethod.POST)
	@ApiOperation(value = "注册", notes = "注册")
	public JsonResult register(UserInfoRequest request) {
		userService.register(request);
		return new JsonResult(SUCCESS);
	}

	@RequestMapping(value = "test", method = RequestMethod.POST)
	@ApiOperation(value = "注册", notes = "注册")
	public JsonResult test() {
		System.out.println("sso测试正常");
		return new JsonResult(SUCCESS);
	}
	
	
	/**
	 * rabbitmq+websocket实现消息实时推送
	 */
//	@RequestMapping(value = "sendDirectMessage", method = RequestMethod.GET)
//	@ApiOperation(value = "消息推送", notes = "消息推送")
//	public String sendDirectMessage(String messageData, String type, String tousername) {
//		String messageId = String.valueOf(UUID.randomUUID());
//		String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//		Map<String, Object> map = new HashMap<>();
//		map.put("messageId", messageId);
//		map.put("messageData", messageData);
//		map.put("createTime", createTime);
//		map.put("type", type);
//		map.put("tousername", tousername);
//		// 将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
//
//		rabbitTemplate.convertAndSend("TestDirectExchange", "TestDirectRouting", map);
//		return "ok";
//	}
//	
//	@RabbitListener(queues = "TestDirectQueue")
//	@RabbitHandler
//	public void process(Map testMessage) {
//		WebSocket socket = new WebSocket();
//		Object tousername = testMessage.get("tousername");
//		Map<String, Object> map1 = Maps.newHashMap();
//		map1.put("messageType", 4);
//		map1.put("textMessage", testMessage.get("messageData"));
//		map1.put("fromusername", "官方");
//		map1.put("tousername", tousername);
//		map1.put("type", testMessage.get("type"));
//		try {
//			Thread.sleep(5000);
//			if ("0".equals(String.valueOf(tousername))) {
//				socket.sendMessageAll(JSON.toJSONString(map1), "");
//			}
//			socket.sendMessageTo(JSON.toJSONString(map1), String.valueOf(tousername));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println("DirectReceiver消费者收到消息  : " + testMessage.toString());
//	}
	
	/**
	 * 测试主题交换机
	 * @param testMessage
	 */

//	@RequestMapping(value = "sendTopicMessage1", method = RequestMethod.GET)
//	public String sendTopicMessage1() {
//		String messageId = String.valueOf(UUID.randomUUID());
//		String messageData = "message: M A N ";
//		String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//		Map<String, Object> manMap = new HashMap<>();
//		manMap.put("messageId", messageId);
//		manMap.put("messageData", messageData);
//		manMap.put("createTime", createTime);
//		rabbitTemplate.convertAndSend("topicExchange", "topic.man", manMap);
//		return "ok";
//	}
//
//	@RequestMapping(value = "sendTopicMessage2", method = RequestMethod.GET)
//	public String sendTopicMessage2() {
//		String messageId = String.valueOf(UUID.randomUUID());
//		String messageData = "message: woman is all ";
//		String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//		Map<String, Object> womanMap = new HashMap<>();
//		womanMap.put("messageId", messageId);
//		womanMap.put("messageData", messageData);
//		womanMap.put("createTime", createTime);
//		rabbitTemplate.convertAndSend("topicExchange", "topic.woman", womanMap);
//		return "ok";
//	}

	
	/**
	 * 监听主题交换机
	 */

//	@RabbitListener(queues = "topic.man")
//	@RabbitHandler
//	public void TopicManReceiver(Map testMessage) {
//		System.out.println("DirectReceiver消费者收到消息  : " + testMessage.toString());
//	}
//
//	@RabbitListener(queues = "topic.woman")
//	@RabbitHandler
//	public void TopicTotalReceiver(Map testMessage) {
//		System.out.println("DirectReceiver消费者收到消息  : " + testMessage.toString());
//	}
	
	/**
	 * 根据用户ip获取地址
	 * @param request
	 * @return
	 */
	
//	@RequestMapping(value = "getAddressByIp", method = RequestMethod.POST)
//	@ApiOperation(value = "获取用户地址根据ip", notes = "获取用户地址根据ip")
//	public JsonResult getAddressByIp(HttpServletRequest request) {
//		String ip = IpAddress.getRemortIP(request);
//		String data = IpAddress.findOne("39.100.237.144");
//		return new JsonResult(SUCCESS, data);
//	}

	/**
	 * 搜索功能
	 * 
	 * @param request
	 * @return
	 */
//	@RequestMapping(value = "search", method = RequestMethod.GET)
//	@ApiOperation(value = "查询", notes = "查询")
//	public JsonResult search(SearchRequest request) {
//		return new JsonResult(SUCCESS,searchService.search(request));
//	}
	
	/**
	 * rabbitmq添加数据
	 */
	
//	@RequestMapping(value = "add", method = RequestMethod.GET)
//	@ApiOperation(value = "添加", notes = "添加")
//	public JsonResult add(String str, long id ,String brand) {
//		Goods goods = new Goods();
//		goods.setId(id);
//		goods.setLevel((long) 1);
//		goods.setName(str);
//		goods.setBrand(brand);
//		goodsRepository.save(goods);
//		return new JsonResult(SUCCESS,goods);
//	}
	
	/**
	 * 测试邮箱发送
	 */
//	@RequestMapping(path = "/csemail", method = RequestMethod.POST)
//	@ApiOperation(value = "邮箱", notes = "邮箱")
//	public JsonResult csemail() throws Exception {
//
//		SimpleMailMessage msg = new SimpleMailMessage();
//        msg.setSubject("Test subject");
//        msg.setText("Test body");
//        //发送邮件的邮箱
//        msg.setFrom("s17695748297@163.com");
//        msg.setSentDate(new Date());
//        //接受邮件的邮箱
//        msg.setTo("s17695748297@163.com");
//    	javaMailSender.send(msg);
//		return null;
//	}
	
}