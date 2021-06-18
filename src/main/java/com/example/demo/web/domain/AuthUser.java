package com.example.demo.web.domain;

import java.io.Serializable;

/**
 * @author lk
 * @date 2021/6/11
 */
public class AuthUser implements Serializable {

	private String userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
