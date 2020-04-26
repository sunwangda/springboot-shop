package com.shop.user.center.tasks;

import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

@Component
public class AsyncTasksComponent {
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
//	@Async
//	public Future<String> task() throws InterruptedException {
//		long begin = System.currentTimeMillis();
//		Thread.sleep(2000L);
//		long end = System.currentTimeMillis();
//		logger.info("任务swd耗时=" + (end -begin));
//		return new AsyncResult<String>("任务swd");
//	}
//	
//	@Async
//	public Future<String> task1() throws InterruptedException {
//		long begin = System.currentTimeMillis();
//		Thread.sleep(2000L);
//		long end = System.currentTimeMillis();
//		logger.info("任务1耗时=" + (end -begin));
//		return new AsyncResult<String>("任务1");
//	}
}
