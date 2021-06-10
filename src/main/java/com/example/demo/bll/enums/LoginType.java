package com.example.demo.bll.enums;

/**
 * @author lk
 * @date 2021/6/1
 */
public enum LoginType {
	PASSWORD("password"), // 密码登录
	NOPASSWD("nopassword"); // 免密登录

	private String code;// 状态值

	private LoginType(String code) {
		this.code = code;
	}
	public String getCode () {
		return code;
	}
}
