package com.shop.user.center.config;

import javax.annotation.PostConstruct;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

/**
 * 2.7.0版本及以上 在dubbo-admin显示元数据的配置，
 *
 * 需要注意， dubbo-admin和服务提供者引入的dubbo为同一版本才行
 *
 * @author TimFruit
 * @date 20-3-3 下午11:49
 */
@EnableDubbo
@Configuration
public class DubboConfig implements EnvironmentAware {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());
	private Environment env;

	@Override
	public void setEnvironment(Environment environment) {
		this.env = environment;
	}

	// 2.7.0 版本以上
	// https://blog.csdn.net/wangxq0224/article/details/99304253
	// 用于fix dubbo admin :
	// "无元数据信息，请升级至Dubbo2.7及以上版本，或者查看application.properties中关于config center的配置，详见
	// 这里"
	// https://github.com/apache/dubbo-admin/wiki/Dubbo-Admin%E9%85%8D%E7%BD%AE%E8%AF%B4%E6%98%8E
	@PostConstruct
	public void postInitAdminMeta() {
		final String REGISTRY_ADDRESS = "dubbo.registry.address";
		String registryAddress = env.getProperty(REGISTRY_ADDRESS);
		if (!StringUtils.hasText(registryAddress)) {
			log.warn(REGISTRY_ADDRESS + "属性没有配置值");
			return;
		}
		if (!registryAddress.startsWith("zookeeper")) {
			log.info("注册中心不是zookeeper");
			return;
		}

		// 注册中心为zookeeper, 修复元数据问题

		String data = REGISTRY_ADDRESS + "=" + registryAddress;

		final String META_REPORT_ADDRESS = "dubbo.metadata-report.address";
		String reportAddress = env.getProperty(META_REPORT_ADDRESS);
		if (StringUtils.hasText(reportAddress)) {
			data = data + "\n" + META_REPORT_ADDRESS + "=" + reportAddress;
		}

		log.info("\n== data: {}", data);

		// warn: 多个注册中心未测试
		String connectString = registryAddress.replace("zookeeper://", "");

		RetryPolicy retryPolicy = new RetryNTimes(2, 1000);
		CuratorFramework zkClient = CuratorFrameworkFactory.newClient(connectString, retryPolicy);
		zkClient.start();
		try {
			String nodePath = "/dubbo/config/dubbo/dubbo.properties";
			if (zkClient.checkExists().forPath(nodePath) == null) {
				zkClient.create().creatingParentsIfNeeded().forPath(nodePath, data.getBytes());
			} else {
				zkClient.setData().forPath(nodePath, data.getBytes());
				System.out.println("1111111111111111111111111");
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			zkClient.close();
		}
	}

}