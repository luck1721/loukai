package com.example.demo.bll.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * 验证码参数配置
 *
 * @date 2020年5月24日
 * @author huanglj
 */
@Configuration
@ConfigurationProperties("verification")
public class VerificationConfig {
	/** 验证码长度 */
	private int length = 6;
	/** 同一手机号最小生成间隔时长 */
	private Duration minGenerateInterval = Duration.ofSeconds(30);
	/** 验证码有效时长 */
	private Duration expiration = Duration.ofMinutes(5);
	/** 同一验证码最大验证次数 */
	private int maxVerifyTimes = 3;
	/** 默认的当日同一手机号最大生成次数 */
	private int defaultMaxGenerateTimes = 5;
	/** 当日同一手机号最大生成次数；key: 验证类型, value: 最大生成次数 */
	private Map<String, Integer> maxGenerateTimes = new HashMap<>();

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public Duration getMinGenerateInterval() {
		return minGenerateInterval;
	}

	public void setMinGenerateInterval(Duration minGenerateInterval) {
		this.minGenerateInterval = minGenerateInterval;
	}

	public Duration getExpiration() {
		return expiration;
	}

	public void setExpiration(Duration expiration) {
		this.expiration = expiration;
	}

	public int getMaxVerifyTimes() {
		return maxVerifyTimes;
	}

	public void setMaxVerifyTimes(int maxVerifyTimes) {
		this.maxVerifyTimes = maxVerifyTimes;
	}

	public int getDefaultMaxGenerateTimes() {
		return defaultMaxGenerateTimes;
	}

	public void setDefaultMaxGenerateTimes(int defaultMaxGenerateTimes) {
		this.defaultMaxGenerateTimes = defaultMaxGenerateTimes;
	}

	public Map<String, Integer> getMaxGenerateTimes() {
		return maxGenerateTimes;
	}

	public void setMaxGenerateTimes(Map<String, Integer> maxGenerateTimes) {
		this.maxGenerateTimes = maxGenerateTimes;
	}

}
