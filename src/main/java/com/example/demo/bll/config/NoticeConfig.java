package com.example.demo.bll.config;

import cn.com.citycloud.hcs.common.client.httpclient.ClientOptions;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author lk
 * @date 2021/2/4
 */
@Configuration
@ConfigurationProperties("notice")
public class NoticeConfig {

	private String templatePath = "notice";
	private EmailConfig email = new EmailConfig();
	private SmsConfig sms = new SmsConfig();

	public String getTemplatePath() {
		return templatePath;
	}

	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}

	public EmailConfig getEmail() {
		return email;
	}

	public void setEmail(EmailConfig email) {
		this.email = email;
	}

	public SmsConfig getSms() {
		return sms;
	}

	public void setSms(SmsConfig sms) {
		this.sms = sms;
	}

	public static class EmailConfig {
		private String templateSuffix = ".html";
		private String sender;

		public String getTemplateSuffix() {
			return templateSuffix;
		}

		public void setTemplateSuffix(String templateSuffix) {
			this.templateSuffix = templateSuffix;
		}

		public String getSender() {
			return sender;
		}

		public void setSender(String sender) {
			this.sender = sender;
		}
	}

	public static class SmsConfig extends ClientOptions {
		private static final long serialVersionUID = -2689139697096812904L;

		private String templateSuffix = ".txt";
		private String sender;

		public SmsConfig() {
			setProtocol("https");
			setAuthenticationRequesting(true);
		}

		public String getTemplateSuffix() {
			return templateSuffix;
		}

		public void setTemplateSuffix(String templateSuffix) {
			this.templateSuffix = templateSuffix;
		}

		public String getSender() {
			return sender;
		}

		public void setSender(String sender) {
			this.sender = sender;
		}
	}

}
