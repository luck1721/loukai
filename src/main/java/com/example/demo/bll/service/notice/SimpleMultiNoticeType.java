package com.example.demo.bll.service.notice;

import cn.com.citycloud.hcs.common.domain.BaseBean;
import com.example.demo.bll.enums.NoticeType;

import java.util.ArrayList;
import java.util.List;

/**
 * 多重通知类型接口简单实现类
 *
 * @date 2020年5月18日
 * @author huanglj
 */
public class SimpleMultiNoticeType extends BaseBean implements MultiNoticeType {
	private static final long serialVersionUID = 3189456977332989869L;

	/** 主题 */
	private final List<NoticeType> noticeTypes = new ArrayList<>();

	public List<NoticeType> getNoticeTypes() {
		return noticeTypes;
	}

	/*
	 * build method
	 */
	public SimpleMultiNoticeType addNoticeTypes(NoticeType... noticeTypes) {
		for (NoticeType noticeType : noticeTypes) {
			this.noticeTypes.add(noticeType);
		}
		return this;
	}

	public SimpleMultiNoticeType addNoticeTypes(List<NoticeType> noticeTypes) {
		this.noticeTypes.addAll(noticeTypes);
		return this;
	}

	public static SimpleMultiNoticeType type(NoticeType... noticeTypes) {
		return new SimpleMultiNoticeType().addNoticeTypes(noticeTypes);
	}

	public static SimpleMultiNoticeType type(List<NoticeType> noticeTypes) {
		return new SimpleMultiNoticeType().addNoticeTypes(noticeTypes);
	}

}
