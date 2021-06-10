package com.example.demo.bll.enums;

/**
 * 文件存储类型
 *
 * @date 2019年8月22日
 * @author huanglj
 */
public enum FileStorageType {
	/**
	 * 本地存储
	 */
	local("local"),
	/**
	 * seaweedfs存储
	 */
	seaweed("seaweed"),
	/**
	 * 阿里云存储
	 */
	aliyun("aliyun");

	private String name;

	FileStorageType(String name) {
		this.name = name;
	}
}
