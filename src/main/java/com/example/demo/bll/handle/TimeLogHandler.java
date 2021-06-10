package com.example.demo.bll.handle;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author lk
 * @date 2021/3/11
 */
public class TimeLogHandler implements InvocationHandler {
	private Object target;

	public TimeLogHandler(Object target) {
		this.target = target;
	}
	@Override
	public Object invoke(Object o, Method method, Object[] args) throws Throwable {
		System.out.println("start：" + System.nanoTime());
		Object result = method.invoke(target, args);
		System.out.println("end：" + System.nanoTime());
		return result;
	}
}
