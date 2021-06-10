package com.example.demo.bll.listener;

import com.example.demo.bll.entity.User;
import com.example.demo.bll.event.MyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 自定义监听器，监听MyEvent事件
 * @author lk
 * @date 2021/6/2
 */
@Component
public class MyEventListener implements ApplicationListener<MyEvent> {
	@Override
	public void onApplicationEvent(MyEvent myEvent) {
		// 把事件中的信息获取到
		User user = myEvent.getUser();
		// 处理事件，实际项目中可以通知别的微服务或者处理其他逻辑等等
		System.out.println("用户名：" + user.getName());
		System.out.println("密码：" + user.getPassword());

	}
}
