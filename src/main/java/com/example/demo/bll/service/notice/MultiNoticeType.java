package com.example.demo.bll.service.notice;

import cn.com.citycloud.hcs.common.domain.Bean;
import com.example.demo.bll.enums.NoticeType;

import java.util.Arrays;
import java.util.List;

/**
 * 多重通知类型
 * @author lk
 * @date 2021/2/4
 */
public interface MultiNoticeType extends Bean {
	/** 默认通知类型 */
	NoticeType DEFAULT_NOTICE_TYPE = NoticeType.sms;

	/**
	 * 通知类型
	 *
	 * @return
	 * @date 2020年5月16日
	 * @author huanglj
	 */
	default List<NoticeType> getNoticeTypes() {
		return Arrays.asList(DEFAULT_NOTICE_TYPE);
	}

}
