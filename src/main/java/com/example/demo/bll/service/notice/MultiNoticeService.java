package com.example.demo.bll.service.notice;

import cn.com.citycloud.hcs.common.task.log.failureretry.FailureRetry;
import cn.com.citycloud.hcs.common.task.log.failureretry.FailureRetryExecuteException;
import cn.com.citycloud.hcs.common.task.log.failureretry.FailureRetryTaskHandler;
import cn.com.citycloud.hcs.common.utils.ConvertUtils;
import com.example.demo.bll.enums.NoticeType;
import com.example.demo.bll.service.impl.FileUploadLogService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 多重通知服务类
 *
 * @date 2020年5月16日
 * @author huanglj
 */
@Service
public class MultiNoticeService implements NoticeService {
	private static final Logger log = LoggerFactory.getLogger(MultiNoticeService.class);

	@Autowired
	@Qualifier("noticeService")
	private NoticeService noticeService;
	@Autowired
	private FileUploadLogService fileUploadLogService;
	@Autowired
	private FailureRetryTaskHandler<?, ?> failureRetryTaskHandler;

	@Override
	public void send(Notice notice) {
		send((NoticeType) null, notice);
	}

	@Override
	public void sendTemplate(Notice notice) {
		sendTemplate(null, notice);
	}

	@Override
	public void send(NoticeType type, Notice notice) {
		noticeService.send(type, notice);
	}

	@Override
	public void send(MultiNoticeType multiType, Notice notice) {

	}

	@Override
	public void sendTemplate(NoticeType type, Notice notice) {
		noticeService.sendTemplate(type, notice);
	}

	/**
	 * 发送文本通知
	 *
	 * @param multiType
	 * @param multiNotice
	 * @date 2020年5月16日
	 * @author huanglj
	 */
	public void send(MultiNoticeType multiType, MultiNotice multiNotice) {
		send(multiType, multiNotice, false, (args, times) -> {
			try {
				send((NoticeType) args[0], buildNotice(args));
			} catch (Exception e) {
				throw new FailureRetryExecuteException(true, e);
			}
			return null;
		});
	}

	/**
	 * 发送模版通知
	 *
	 * @param multiType
	 * @param multiNotice
	 * @date 2020年5月16日
	 * @author huanglj
	 */
	public void sendTemplate(MultiNoticeType multiType, MultiNotice multiNotice) {
		send(multiType, multiNotice, true, (args, times) -> {
			try {
				sendTemplate((NoticeType) args[0], buildNotice(args));
			} catch (Exception e) {
				throw new FailureRetryExecuteException(true, e);
			}
			return null;
		});
	}

	@SuppressWarnings("unchecked")
	private Notice buildNotice(Serializable[] args) {
		SimpleNotice notice = new SimpleNotice((Notice)args[1]);
		notice.setParams((Map<String, Object>)args[2]);
		if(MapUtils.isNotEmpty(notice.getMultiparts())) {
			Map<String, InputStreamSource> multiparts = new HashMap<>();
			notice.getMultiparts().forEach((String name, InputStreamSource multipart) -> {
				if(multipart instanceof NoticeMultipart) {
					multipart = parseNoticeMultipart((NoticeMultipart)multipart);
				}
				if(multipart != null) {
					multiparts.put(name, multipart);
				}
			});
			notice.setMultiparts(multiparts);
		}
		return notice;
	}

	private InputStreamSource parseNoticeMultipart(NoticeMultipart multipart) {
		return () -> {
			try {
				return fileUploadLogService.downloadFileStream(multipart.getPath());
			} catch (IOException e) {
				log.error("multipart[" + multipart.getPath() + "] parse error!", e);
				return null;
			}
		};
	}

	private void send(MultiNoticeType multiType, MultiNotice multiNotice, boolean template, FailureRetry<?> failureRetry) {
		List<NoticeType> types = multiType.getNoticeTypes();
		if (CollectionUtils.isEmpty(types)) {
			types = Arrays.asList(MultiNoticeType.DEFAULT_NOTICE_TYPE);
		}
		for (NoticeType type : types) {
			failureRetryTaskHandler.getTaskExecutor().execute(() -> {
				Notice notice = multiNotice.getNotice(type);
				if (notice == null) {
					notice = multiNotice;
				}
				Map<String, Object> params = null;
				boolean success = false;
				try {
					params = mergeParams(multiType, notice.getParams());
					failureRetryTaskHandler.execute(failureRetry, type, notice, (Serializable)params);
					success = true;
				} catch (Exception e) {
					log.error("Send " + type + " notice[" + notice.getSubject() + "] error!", e);
					Throwable cause = e.getCause();
					while (cause != null) {
						if(cause instanceof MailSendException) {
							MailSendException mse = (MailSendException)cause;
							Map<Object, Exception> failedMessages = mse.getFailedMessages();
							if(MapUtils.isNotEmpty(failedMessages)) {
								failedMessages.forEach((message, ex) -> {
									log.error(message.toString(), ex);
								});
							}
							break;
						}
						cause = cause.getCause();
					}
				}
			});
		}
	}

	private Map<String, Object> mergeParams(MultiNoticeType multiType, Map<String, Object> params) {
		Map<String, Object> mergeParams = ConvertUtils.convertMap(multiType);
		if (mergeParams == null) {
			mergeParams = new HashMap<>();
		}
		if (MapUtils.isNotEmpty(params)) {
			mergeParams.putAll(params);
		}
		return mergeParams;
	}

}
