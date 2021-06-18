package com.example.demo.bll.listener;

import com.example.demo.bll.service.impl.LoginServiceImpl;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListenerAdapter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.annotation.WebListener;

/**
 * @author lk
 * @date 2021/6/18
 */
@WebListener
public class ShiroSessionListener extends SessionListenerAdapter {

	@Resource
	private LoginServiceImpl loginService;

	// session创建
	@Override
	public void onStart(Session session) {
		super.onStart(session);
		System.out.println("session创建，sessionId：" + session.getId());
	}

	// session停止
	@Override
	public void onStop(Session session) {
		System.out.println("session停止，sessionId:" + session.getId() +"，用户id：" + session.getAttribute("loginUserId"));
	}

	// session失效
	@Override
	public void onExpiration(Session session) {
		// 重置登录状态
		loginService.updateLoginStatus(session.getAttribute("loginUserId").toString(), 0);
		System.out.println("session失效，sessionId:" + session.getId() +"，用户id：" + session.getAttribute("loginUserId"));
	}

}
