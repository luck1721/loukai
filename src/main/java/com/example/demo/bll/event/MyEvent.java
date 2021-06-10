package com.example.demo.bll.event;

import com.example.demo.bll.entity.User;
import org.springframework.context.ApplicationEvent;

/**
 * 自定义事件
 * @author lk
 * @date 2021/6/2
 */
public class MyEvent extends ApplicationEvent {

	private User user;

	public MyEvent(Object source, User user) {
		super(source);
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
