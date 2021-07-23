package com.example.demo.bll.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 短URL参数配置
 *
 * @date 2020年5月17日
 * @author huanglj
 */
@Configuration
@ConfigurationProperties("tiny")
public class TinyUrlConfig {

	private String host = "http://localhost";
	private String basePath = "/oauth/tiny";

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

}
