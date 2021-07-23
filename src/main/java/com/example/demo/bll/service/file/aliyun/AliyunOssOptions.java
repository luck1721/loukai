package com.example.demo.bll.service.file.aliyun;


/** 阿里云OSS存储配置
 * @Author: wangting
 * @Description:
 * @Date: Created in 10:09 2020/10/9
 */

public class AliyunOssOptions {

	private String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";

	private String bucketName = "shenqisuji";

	private String accessKeyId;

	private String accessKeySecret;

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public String getAccessKeyId() {
		return accessKeyId;
	}

	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}

	public String getAccessKeySecret() {
		return accessKeySecret;
	}

	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
	}
}
