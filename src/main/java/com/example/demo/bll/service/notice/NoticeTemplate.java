package com.example.demo.bll.service.notice;

import cn.com.citycloud.hcs.common.domain.Bean;

/**
 * 通知模版接口
 *
 * @date 2020年6月1日
 * @author huanglj
 */
public interface NoticeTemplate extends Bean {

	/**
	 * 主题
	 *
	 * @return
	 * @date 2020年6月1日
	 * @author huanglj
	 */
	default String getSubject() {
		return null;
	}

	/**
	 * 模版
	 *
	 * @return
	 * @date 2020年6月1日
	 * @author huanglj
	 */
	public String getTemplate();

}
