package com.example.demo.bll.config;

import cn.com.citycloud.hcs.common.client.httpclient.ClientOptions;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author lk
 * @date 2021/7/23
 */
@Configuration
@ConfigurationProperties("wechat")
public class WeChatConfig extends ClientOptions {

	private String appId;
	private String secret;
	private String grantType = "authorization_code";

	public WeChatConfig() {
		setProtocol("https");
		setAuthenticationRequesting(true);
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getGrantType() {
		return grantType;
	}

	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}
}
