package com.example.demo.bll.interceptor;

import com.example.demo.bll.anon.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author lk
 * @date 2021/9/29
 */
@Component
public class ResponseResultInterceptor implements HandlerInterceptor {
	//标记名称
	public static final String RESPONSE_RESULT_ANN = "RESPONSE-RESULT-ANN";
	private static final Logger logger = LoggerFactory.getLogger(ResponseResultInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//请求的方法
		if (handler instanceof HandlerMethod) {
			final HandlerMethod handlerMethod = (HandlerMethod) handler;
			final Class<?> clazz = handlerMethod.getBeanType();
			final Method method = handlerMethod.getMethod();
			// 判断是否在类对象上添加了注解
			if (clazz.isAnnotationPresent(ResponseResult.class)) {
				// 设置此请求返回体，需要包装，往下传递，在ResponseBodyAdvice接口进行判断
				request.setAttribute(RESPONSE_RESULT_ANN, clazz.getAnnotation(ResponseResult.class));
			} else if (method.isAnnotationPresent(ResponseResult.class)) {
				request.setAttribute(RESPONSE_RESULT_ANN, method.getAnnotation(ResponseResult.class));
			}
		}
		return true;
	}
}

