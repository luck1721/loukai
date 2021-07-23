package com.example.demo.bll.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * 用户参数配置
 *
 * @date 2020年4月12日
 * @author huanglj
 */
@Configuration("coreUserConfig")
@ConfigurationProperties("auth.user")
public class UserConfig {

	private String tenantAdminUsernamePrefix = "admin.";
	private String tenantAdminDefaultPassword = "123456";
	private String agentAdminUsernamePrefix = "agent.";
	private String agentAdminDefaultPassword = "123456";
	/** 找回密码页面地址 */
	private String retrievePasswordPageUri = "/";
	/** 验证账号安全页面地址 */
	private String verificationUserSecurityPageUri = "/";
	/** 安全邮箱设置页面地址 */
	private String updateSecurityEmailPageUri = "/";
	/** 解绑第三方账号页面地址 */
	private String deleteThirdPartyUserPageUri = "/";
	/** （手机号）绑定第三方账号超时时间，默认为空值取session超时时间，0或负数表示不超时 */
	private Duration bindThirdPartyUserTimeout;

	public String getTenantAdminUsernamePrefix() {
		return tenantAdminUsernamePrefix;
	}

	public void setTenantAdminUsernamePrefix(String tenantAdminUsernamePrefix) {
		this.tenantAdminUsernamePrefix = tenantAdminUsernamePrefix;
	}

	public String getTenantAdminDefaultPassword() {
		return tenantAdminDefaultPassword;
	}

	public void setTenantAdminDefaultPassword(String tenantAdminDefaultPassword) {
		this.tenantAdminDefaultPassword = tenantAdminDefaultPassword;
	}

	public String getAgentAdminUsernamePrefix() {
		return agentAdminUsernamePrefix;
	}

	public void setAgentAdminUsernamePrefix(String agentAdminUsernamePrefix) {
		this.agentAdminUsernamePrefix = agentAdminUsernamePrefix;
	}

	public String getAgentAdminDefaultPassword() {
		return agentAdminDefaultPassword;
	}

	public void setAgentAdminDefaultPassword(String agentAdminDefaultPassword) {
		this.agentAdminDefaultPassword = agentAdminDefaultPassword;
	}

	public String getRetrievePasswordPageUri() {
		return retrievePasswordPageUri;
	}

	public void setRetrievePasswordPageUri(String retrievePasswordPageUri) {
		this.retrievePasswordPageUri = retrievePasswordPageUri;
	}

	public String getVerificationUserSecurityPageUri() {
		return verificationUserSecurityPageUri;
	}

	public void setVerificationUserSecurityPageUri(String verificationUserSecurityPageUri) {
		this.verificationUserSecurityPageUri = verificationUserSecurityPageUri;
	}

	public String getUpdateSecurityEmailPageUri() {
		return updateSecurityEmailPageUri;
	}

	public void setUpdateSecurityEmailPageUri(String updateSecurityEmailPageUri) {
		this.updateSecurityEmailPageUri = updateSecurityEmailPageUri;
	}

	public String getDeleteThirdPartyUserPageUri() {
		return deleteThirdPartyUserPageUri;
	}

	public void setDeleteThirdPartyUserPageUri(String deleteThirdPartyUserPageUri) {
		this.deleteThirdPartyUserPageUri = deleteThirdPartyUserPageUri;
	}

	public Duration getBindThirdPartyUserTimeout() {
		return bindThirdPartyUserTimeout;
	}

	public void setBindThirdPartyUserTimeout(Duration bindThirdPartyUserTimeout) {
		this.bindThirdPartyUserTimeout = bindThirdPartyUserTimeout;
	}

}
