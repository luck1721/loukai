package com.example.demo.bll.service.notice;

import cn.com.citycloud.hcs.common.domain.Bean;
import org.springframework.core.io.InputStreamSource;

import java.util.List;
import java.util.Map;

/**
 * @author lk
 * @date 2021/2/4
 */
public interface Notice extends Bean {
	/** 内容模版名称前缀 */
	String CONTENT_TEMPLETE_NAME_PREFIX = "template:";
	/** 内容附件名称前缀 */
	String CONTENT_MULTIPART_NAME_PREFIX = "content:";

	/**
	 * 主题
	 *
	 * @return
	 * @date 2020年5月13日
	 * @author huanglj
	 */
	default String getSubject() {
		return null;
	}

	/**
	 * 发送者
	 *
	 * @return
	 * @date 2020年5月13日
	 * @author huanglj
	 */
	default String getSender() {
		return null;
	}

	/**
	 * 接收者
	 *
	 * @return
	 * @date 2020年5月13日
	 * @author huanglj
	 */
	public List<String> getReceivers();

	/**
	 * 抄送者
	 *
	 * @return
	 * @date 2020年5月13日
	 * @author huanglj
	 */
	default List<String> getDuplicators() {
		return null;
	}

	/**
	 * 密件抄送者
	 *
	 * @return
	 * @date 2020年5月13日
	 * @author huanglj
	 */
	default List<String> getBlinders() {
		return null;
	}

	/**
	 * 内容
	 *
	 * @return
	 * @date 2020年5月13日
	 * @author huanglj
	 */
	public String getContent();

	/**
	 * 参数
	 *
	 * @return
	 * @date 2020年5月13日
	 * @author huanglj
	 */
	public Map<String, Object> getParams();

	/**
	 * 附件
	 *
	 * @return
	 * @date 2020年5月13日
	 * @author huanglj
	 */
	default Map<String, ? extends InputStreamSource> getMultiparts() {
		return null;
	}
}
