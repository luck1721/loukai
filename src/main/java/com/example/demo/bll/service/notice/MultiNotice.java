package com.example.demo.bll.service.notice;

import com.example.demo.bll.enums.NoticeType;

/**
 * 多重通知接口
 *
 * @date 2020年5月16日
 * @author huanglj
 */
public interface MultiNotice extends Notice {

	/**
	 * 根据通知类型获取通知对象
	 *
	 * @return
	 * @date 2020年5月16日
	 * @author huanglj
	 */
	default Notice getNotice(NoticeType type) {
		return this;
	}

}
