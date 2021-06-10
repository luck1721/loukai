package com.example.demo.bll.service.notice;

import cn.com.citycloud.hcs.common.domain.routing.RoutingRule;
import com.example.demo.bll.enums.NoticeType;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * @author lk
 * @date 2021/2/4
 */
public class NoticeServiceRoutingRule implements RoutingRule<NoticeType, NoticeService> {

	private Map<String, NoticeService> serviceMapping;
	private String defaultKey = MultiNoticeType.DEFAULT_NOTICE_TYPE.name();

	public NoticeServiceRoutingRule(Map<String, NoticeService> serviceMapping) {
		this.serviceMapping = serviceMapping;
	}

	public String getDefaultKey() {
		return defaultKey;
	}

	public void setDefaultKey(String defaultKey) {
		this.defaultKey = defaultKey;
	}

	/**
	 * @param type
	 * @return
	 * @date 2020年5月16日
	 * @author huanglj
	 * @see cn.com.citycloud.hcs.common.domain.routing.RoutingRule#routedToDestination(java.lang.Object)
	 */
	@Override
	public NoticeService routedToDestination(NoticeType type) {
		String key = type == null ? defaultKey : type.name();
		Assert.isTrue(serviceMapping.containsKey(key), "无效的通知类型：" + key + "！");
		return serviceMapping.get(key);
	}
}

