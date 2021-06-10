package com.example.demo.bll.service.notice;

import com.example.demo.bll.enums.NoticeType;

/**
 * @author lk
 * @date 2021/2/4
 */
public interface NoticeService {
	/**
	 * 发送文本通知
	 *
	 * @param notice
	 * @date 2020年5月16日
	 * @author huanglj
	 */
	void send(Notice notice);

	/**
	 * 发送模版通知
	 *
	 * @param notice
	 * @date 2020年5月16日
	 * @author huanglj
	 */
	void sendTemplate(Notice notice);

	/**
	 * 发送文本通知
	 *
	 * @param type
	 * @param notice
	 * @date 2020年5月16日
	 * @author huanglj
	 */
	default void send(NoticeType type, Notice notice) {
		send(notice);
	}

	public void send(MultiNoticeType multiType, Notice notice);

	/**
	 * 发送模版通知
	 *
	 * @param type
	 * @param notice
	 * @date 2020年5月16日
	 * @author huanglj
	 */
	default void sendTemplate(NoticeType type, Notice notice) {
		sendTemplate(notice);
	}
}
