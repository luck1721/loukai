package com.example.demo.bll.entity;

import java.io.Serializable;

/**
 * @author lk
 * @date 2021/10/15
 */
public class UserSync implements Serializable {

	private String signature;

	private String token;

	private String timestamp;

	private String nonce;

	private String operation;

	private User data;

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public User getData() {
		return data;
	}

	public void setData(User data) {
		this.data = data;
	}
}
