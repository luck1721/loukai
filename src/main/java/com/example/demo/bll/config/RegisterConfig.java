package com.example.demo.bll.config;

import cn.com.citycloud.hcs.common.domain.BaseBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 注册配置参数
 *
 * @date 2020年5月16日
 * @author huanglj
 */
@Configuration("coreRegisterConfig")
@ConfigurationProperties(value = "auth.user.register")
public class RegisterConfig extends BaseBean {
	private static final long serialVersionUID = 1394421962691043753L;

	/** 是否发送通知 */
	private boolean notice = true;
	/** 重定向地址 */
	private String redirectUri = "/";
	/** 默认用户名前缀 */
	private String defaultUsernamePrefix = "u_";

	public boolean isNotice() {
		return notice;
	}

	public void setNotice(boolean notice) {
		this.notice = notice;
	}

	public String getRedirectUri() {
		return redirectUri;
	}

	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}

	public String getDefaultUsernamePrefix() {
		return defaultUsernamePrefix;
	}

	public void setDefaultUsernamePrefix(String defaultUsernamePrefix) {
		this.defaultUsernamePrefix = defaultUsernamePrefix;
	}

}
