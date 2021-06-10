package com.example.demo.bll.service.notice;

import cn.com.citycloud.hcs.common.domain.BaseBean;
import org.springframework.core.io.InputStreamSource;

import java.util.List;
import java.util.Map;

/**
 * @author lk
 * @date 2021/2/4
 */
public class SimpleNotice extends BaseBean implements Notice {
	private static final long serialVersionUID = 4417978750070279488L;

	/** 主题 */
	private String subject;
	/** 发送者 */
	private String sender;
	/** 接收者 */
	private List<String> receivers;
	/** 抄送者 */
	private List<String> duplicators;
	/** 密件抄送者 */
	private List<String> blinders;
	/** 内容 */
	private String content;
	/** 参数 */
	private Map<String, Object> params;
	/** 附件 */
	private Map<String, ? extends InputStreamSource> multiparts;

	/**
	 * Constructor
	 *
	 * @date 2020年5月16日
	 * @author huanglj
	 */
	public SimpleNotice() {
	}

	/**
	 * Constructor
	 *
	 * @param subject
	 * @param sender
	 * @param receivers
	 * @param duplicators
	 * @param blinders
	 * @param content
	 * @param params
	 * @param multiparts
	 * @date 2020年5月16日
	 * @author huanglj
	 */
	public SimpleNotice(String subject, String sender, List<String> receivers, List<String> duplicators,
						List<String> blinders, String content, Map<String, Object> params, Map<String, ? extends InputStreamSource> multiparts) {
		this.subject = subject;
		this.sender = sender;
		this.receivers = receivers;
		this.duplicators = duplicators;
		this.blinders = blinders;
		this.content = content;
		this.params = params;
		this.multiparts = multiparts;
	}

	/**
	 * Constructor
	 *
	 * @param notice
	 * @date 2020年5月16日
	 * @author huanglj
	 */
	public SimpleNotice(Notice notice) {
		this(notice.getSubject(), notice.getSender(), notice.getReceivers(), notice.getDuplicators(),
				notice.getBlinders(), notice.getContent(), notice.getParams(), notice.getMultiparts());
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public List<String> getReceivers() {
		return receivers;
	}

	public void setReceivers(List<String> receivers) {
		this.receivers = receivers;
	}

	public List<String> getDuplicators() {
		return duplicators;
	}

	public void setDuplicators(List<String> duplicators) {
		this.duplicators = duplicators;
	}

	public List<String> getBlinders() {
		return blinders;
	}

	public void setBlinders(List<String> blinders) {
		this.blinders = blinders;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public Map<String, ? extends InputStreamSource> getMultiparts() {
		return multiparts;
	}

	public void setMultiparts(Map<String, ? extends InputStreamSource> multiparts) {
		this.multiparts = multiparts;
	}

}
