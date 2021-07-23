package com.example.demo.bll.service.file.aliyun;

import cn.com.citycloud.hcs.common.task.TaskExecutor;
import com.example.demo.bll.config.FileConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AliyunOssConfiguration {

	@Bean
	@ConditionalOnBean(FileConfig.AliyunOssConfig.class)
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix ="file.aliyunoss",name ="enabled",havingValue="true")
	public AliyunOssOperations aliyunOssOperations(
			@Qualifier("aliyunOssConfig") FileConfig.AliyunOssConfig aliyunOssConfig,
			@Autowired(required=false) TaskExecutor taskExecutor) {
		AliyunOssTemplate operations = new AliyunOssTemplate(aliyunOssConfig);
		operations.setTaskExecutor(taskExecutor);
		return operations;
	}

	@Bean
	public AliyunOssFileStorageService aliyunOssFileStorageService(
			@Autowired(required=false) @Qualifier("aliyunOssOperations") AliyunOssOperations aliyunOssOperations) {
		AliyunOssFileStorageService service = new AliyunOssFileStorageService();
		service.setAliyunOssOperations(aliyunOssOperations);
		return service;
	}

}
