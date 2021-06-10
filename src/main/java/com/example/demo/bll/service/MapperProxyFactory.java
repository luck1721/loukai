package com.example.demo.bll.service;

import com.example.demo.bll.handle.TimeLogHandler;
import com.example.demo.bll.service.impl.Executor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Proxy;

/**
 * @author lk
 * @date 2021/3/11
 */
@Service
public class MapperProxyFactory {
	private ExecutorInterface executor;

	public MapperProxyFactory() {
		executor = (ExecutorInterface) Proxy.newProxyInstance(
				Executor.class.getClassLoader(),
				new Class[]{ExecutorInterface.class},
				new TimeLogHandler(new Executor())
		);
	}

	public void invoke() {
		executor.execute(1, 2);
	}
}
