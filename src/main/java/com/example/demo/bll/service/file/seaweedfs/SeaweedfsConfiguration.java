package com.example.demo.bll.service.file.seaweedfs;

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
public class SeaweedfsConfiguration {

	@Bean
	@ConditionalOnBean(FileConfig.SeaweedfsConfig.class)
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix ="file.seaweedfs",name ="enabled",havingValue="true")
	public SeaweedfsOperations seaweedfsOperations(
			@Qualifier("seaweedfsConfig") FileConfig.SeaweedfsConfig seaweedfsConfig,
			@Autowired(required=false) TaskExecutor taskExecutor) {
		SeaweedfsTemplate operations = new SeaweedfsTemplate(seaweedfsConfig);
		operations.setTaskExecutor(taskExecutor);
		return operations;
	}

	@Bean
	public SeaweedfsFileStorageService seaweedfsFileStorageService(
			@Autowired(required=false) @Qualifier("seaweedfsOperations") SeaweedfsOperations seaweedfsOperations) {
		SeaweedfsFileStorageService service = new SeaweedfsFileStorageService();
		service.setSeaweedfsOperations(seaweedfsOperations);
		return service;
	}

}
