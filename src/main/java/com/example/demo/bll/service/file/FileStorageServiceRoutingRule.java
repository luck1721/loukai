package com.example.demo.bll.service.file;

import cn.com.citycloud.hcs.common.domain.routing.RoutingRule;
import com.example.demo.bll.enums.FileStorageType;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * 文件存储服务路由规则
 *
 * @date 2019年12月24日
 * @author huanglj
 */
public class FileStorageServiceRoutingRule implements RoutingRule<FileStorage, FileStorageService> {

	private final Map<String, FileStorageService> serviceMapping;
	private String defaultKey = FileStorageType.local.name();

	public FileStorageServiceRoutingRule(Map<String, FileStorageService> serviceMapping) {
		this.serviceMapping = serviceMapping;
	}

	public String getDefaultKey() {
		return defaultKey;
	}

	public void setDefaultKey(String defaultKey) {
		this.defaultKey = defaultKey;
	}

	public void addService(String key, FileStorageService service) {
		serviceMapping.put(key, service);
	}

	/**
	 * @see RoutingRule#routedToDestination(Object)
	 * @param fileStorage
	 * @return
	 * @date 2019年12月24日
	 * @author huanglj
	 */
	@Override
	public FileStorageService routedToDestination(FileStorage fileStorage) {
		Assert.notNull(fileStorage, "fileStorage不能为空!");
		String key = fileStorage.getStorageType() == null ? defaultKey : fileStorage.getStorageType().name();
		Assert.isTrue(serviceMapping.containsKey(key), "无效的文件存储类型：" + key + "！");
		return serviceMapping.get(key);
	}

}
