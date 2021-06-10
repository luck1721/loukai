package com.example.demo.bll.service.impl;

import com.example.demo.bll.service.AsyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author lk
 * @date 2021/6/1
 */
@Service
public class AsyncServiceImpl implements AsyncService {
	private static final Logger logger = LoggerFactory.getLogger(AsyncServiceImpl.class);

	@Override
	@Async("asyncServiceExecutor")
	public void executeAsync(String token) {
		logger.info("start executeAsync");
		System.out.println(token);
		System.out.println("异步线程要做的事情");
		System.out.println("可以在这里执行批量插入等耗时的事情");
		logger.info("end executeAsync");
	}
}
