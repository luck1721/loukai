package com.example.demo.bll.service.notice;

import cn.com.citycloud.hcs.common.domain.BaseBean;
import cn.com.citycloud.hcs.common.domain.Bean;
import cn.com.citycloud.hcs.common.utils.ConvertUtils;
import com.example.demo.bll.enums.NoticeType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 多重通知接口简单实现类
 *
 * @date 2020年5月17日
 * @author huanglj
 */
public class SimpleMultiNotice extends BaseBean implements MultiNotice {
	private static final long serialVersionUID = 1937270521597896181L;

	/** 主题 */
	private final String subject;
	/** 接收者 */
	private final List<String> receivers = new ArrayList<>();
	/** 抄送者 */
	private final List<String> duplicators = new ArrayList<>();
	/** 密件抄送者 */
	private final List<String> blinders = new ArrayList<>();
	/** 内容 */
	private final String content;
	/** 参数 */
	private final Map<String, Object> params = new HashMap<>();
	/** 附件 */
	private final Map<String, NoticeMultipart> multiparts = new HashMap<>();
	/** 多重通知映射 */
	private final Map<NoticeType, SimpleMultiNotice> multiNoticeMapping = new HashMap<>();
	/** 临时构建类型 */
	private transient NoticeType type;

	public SimpleMultiNotice(String content) {
		this(null, content);
	}

	public SimpleMultiNotice(String subject, String content) {
		this.subject = subject;
		this.content = content;
	}

	public SimpleMultiNotice(String subject, String content, Map<String, Object> params) {
		this(subject, content);
		addParams(params);
	}

	public String getSubject() {
		return subject;
	}

	public List<String> getReceivers() {
		return receivers;
	}

	public List<String> getDuplicators() {
		return duplicators;
	}

	public List<String> getBlinders() {
		return blinders;
	}

	public String getContent() {
		return content;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public Map<String, NoticeMultipart> getMultiparts() {
		return multiparts;
	}

	@Override
	public SimpleMultiNotice getNotice(NoticeType type) {
		return multiNoticeMapping.get(type);
	}

	/*
	 * build method
	 */
	public SimpleMultiNotice addParam(String name, Object param) {
		params.put(name, param);
		return this;
	}

	public SimpleMultiNotice addParams(Map<String, Object> params) {
		this.params.putAll(params);
		return this;
	}

	public SimpleMultiNotice addParams(Bean params) {
		return addParams(ConvertUtils.convertMap(params));
	}

	public SimpleMultiNotice type(NoticeType type) {
		this.type = type;
		multiNoticeMapping.put(type, new SimpleMultiNotice(subject, content, params));
		return this;
	}

	public SimpleMultiNotice addMultipart(String name, String path) {
		(type == null ? this : getNotice(type)).multiparts.put(name, new NoticeMultipart(path));
		return this;
	}

	public SimpleMultiNotice addMultiparts(Map<String, String> multiparts) {
		multiparts.forEach((name, path) -> {
			addMultipart(name, path);
		});
		return this;
	}

	public SimpleMultiNotice addContentMultipart(String name, String path) {
		return addMultipart(CONTENT_MULTIPART_NAME_PREFIX + name, path);
	}

	public SimpleMultiNotice addContentMultipart(String contentId, String type, String path) {
		return addContentMultipart(contentId + "." + type, path);
	}

	public SimpleMultiNotice addContentMultiparts(Map<String, String> multiparts) {
		multiparts.forEach((name, path) -> {
			addContentMultipart(name, path);
		});
		return this;
	}

	public SimpleMultiNotice addReceivers(String... receivers) {
		for (String receiver : receivers) {
			(type == null ? this : getNotice(type)).receivers.add(receiver);
		}
		return this;
	}

	public SimpleMultiNotice addReceivers(List<String> receivers) {
		(type == null ? this : getNotice(type)).receivers.addAll(receivers);
		return this;
	}

	public SimpleMultiNotice addDuplicators(String... duplicators) {
		for (String duplicator : duplicators) {
			(type == null ? this : getNotice(type)).duplicators.add(duplicator);
		}
		return this;
	}

	public SimpleMultiNotice addDuplicators(List<String> duplicators) {
		(type == null ? this : getNotice(type)).duplicators.addAll(duplicators);
		return this;
	}

	public SimpleMultiNotice addBlinders(String... blinders) {
		for (String blinder : blinders) {
			(type == null ? this : getNotice(type)).blinders.add(blinder);
		}
		return this;
	}

	public SimpleMultiNotice addBlinders(List<String> blinders) {
		(type == null ? this : getNotice(type)).blinders.addAll(blinders);
		return this;
	}

	public static SimpleMultiNotice notice(String content) {
		return new SimpleMultiNotice(content);
	}

	public static SimpleMultiNotice notice(String subject, String content) {
		return new SimpleMultiNotice(subject, content);
	}

	public static SimpleMultiNotice notice(NoticeTemplate noticeTemplate) {
		return notice(noticeTemplate.getSubject(), noticeTemplate.getTemplate());
	}

}
