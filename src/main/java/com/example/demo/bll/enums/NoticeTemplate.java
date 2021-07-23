package com.example.demo.bll.enums;

import cn.com.citycloud.hcs.common.domain.Valueable;

/**
 * 通知模版
 *
 * @date 2020年5月16日
 * @author huanglj
 */
public enum NoticeTemplate implements com.example.demo.bll.service.notice.NoticeTemplate, Valueable<NoticeTemplate, String> {
	register("用户注册验证"),
	login("用户登录验证"),
	retrievePassword("找回密码验证"),
	userSecurity("账号安全验证"),
	securityEmail("绑定邮箱验证"),
	securityPhone("绑定手机号验证"),
	thirdUser("解绑第三方账号验证");

	/** 通知主题 */
	private final String value;

	private NoticeTemplate(String value) {
		this.value = value;
	}

	@Override
	public NoticeTemplate type() {
		return this;
	}

	@Override
	public String value() {
		return value;
	}

	@Override
	public String getSubject() {
		return value;
	}

	@Override
	public String getTemplate() {
		return name();
	}

}
