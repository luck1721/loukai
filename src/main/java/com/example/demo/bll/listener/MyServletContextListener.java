package com.example.demo.bll.listener;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;

/**
 * 使用ApplicationListener来初始化一些数据到application域中的监听器
 * @author lk
 * @date 2021/6/2
 */
@Component
public class MyServletContextListener implements ApplicationListener<ContextRefreshedEvent> {

	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		// 先获取到application上下文
		ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
		// 获取对应的service
		//UserService userService = applicationContext.getBean(UserService.class);
		//User user = userService.getUser();
		// 获取application域对象，将查到的信息放到application域中
		ServletContext application = applicationContext.getBean(ServletContext.class);
		application.setAttribute("user", "321");
	}
}
