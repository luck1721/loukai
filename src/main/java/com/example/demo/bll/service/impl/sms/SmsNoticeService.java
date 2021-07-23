package com.example.demo.bll.service.impl.sms;

import cn.com.citycloud.hcs.common.client.httpclient.ClientOperations;
import cn.com.citycloud.hcs.common.client.httpclient.ClientTemplate;
import cn.com.citycloud.hcs.common.client.httpclient.Form;
import cn.com.citycloud.hcs.common.client.httpclient.Result;
import cn.com.citycloud.hcs.common.domain.exception.ServiceAccessException;
import cn.com.citycloud.hcs.common.task.log.failureretry.FailureRetry;
import cn.com.citycloud.hcs.common.task.log.failureretry.FailureRetryExecuteException;
import cn.com.citycloud.hcs.common.task.log.failureretry.FailureRetryTaskHandler;
import cn.com.citycloud.hcs.common.utils.ConvertUtils;
import com.example.demo.bll.config.NoticeConfig;
import com.example.demo.bll.domain.Bean;
import com.example.demo.bll.enums.NoticeType;
import com.example.demo.bll.service.notice.MultiNoticeType;
import com.example.demo.bll.service.notice.Notice;
import com.example.demo.bll.service.notice.NoticeService;
import com.example.demo.bll.service.notice.SimpleNotice;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailSendException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lk
 * @date 2021/2/4
 */
public class SmsNoticeService implements NoticeService, InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(SmsNoticeService.class);

	/**
	 * 客户端操作对象
	 */
	private ClientOperations clientOperations;
	/**
	 * 模版（在类路径下的）目录
	 */
	private String templateDir = "templates";
	@Autowired
	private NoticeConfig noticeConfig;
	@Autowired
	private FailureRetryTaskHandler<?, ?> failureRetryTaskHandler;

	public ClientOperations getClientOperations() {
		return clientOperations;
	}

	public void setClientOperations(ClientOperations clientOperations) {
		this.clientOperations = clientOperations;
	}

	public String getTemplateDir() {
		return templateDir;
	}

	public void setTemplateDir(String templateDir) {
		this.templateDir = templateDir;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (clientOperations == null) {
			clientOperations = new ClientTemplate(noticeConfig.getSms());
		}
	}

	@Override
	public void send(Notice notice) {
		Assert.hasText(notice.getContent(), "Sms content can't be empty!");
		Assert.notEmpty(notice.getReceivers(), "Sms receiver can't be empty!");
		if (logger.isDebugEnabled()) {
			logger.debug("Send sms[" + notice.getContent() + "]"
					+ (notice.getParams() == null ? "" : notice.getParams()) + " to " + notice.getReceivers() + "...");
		}
		String content = parseSmsContent(notice.getContent(), notice.getParams());
		String sender = notice.getSender();
		if (StringUtils.isBlank(sender)) {
			sender = noticeConfig.getSms().getSender();
		}
		if (StringUtils.isNotBlank(sender)) {
			content = "【" + sender + "】" + content;
		}
		for (String receiver : notice.getReceivers()) {
			Result result = null;
			try {
				Form form = new Form().add("appid", noticeConfig.getSms().getUsername())
						.add("signature", noticeConfig.getSms().getPassword())
						.add("to", receiver).add("content", content);
				result = clientOperations.post("/message/send", form);
			} catch (Exception e) {
				throw new ServiceAccessException(e);
			}
			if (logger.isDebugEnabled()) {
				logger.debug("Send sms result: " + result + ".");
			}
			if (result == null || !"success".equalsIgnoreCase(result.getString("status"))) {
				throw new ServiceAccessException("Send sms error: " + result + "!");
			}
		}
	}

	@Retryable(maxAttempts = 3, backoff = @Backoff(value = 3000, multiplier = 1.5))
	public void send(MultiNoticeType multiType, Notice notice) {
		send(multiType, notice, false, (args, times) -> {
			try {
				send((NoticeType) args[0], buildNotice(args));
			} catch (Exception e) {
				throw new FailureRetryExecuteException(true, e);
			}
			return null;
		});
	}

	private Notice buildNotice(Serializable[] args) {
		SimpleNotice notice = new SimpleNotice((Notice)args[1]);
		notice.setParams((Map<String, Object>)args[2]);
		return notice;
	}

	private void send(MultiNoticeType multiType, Notice notice, boolean template, FailureRetry<?> failureRetry) {
		List<NoticeType> types = multiType.getNoticeTypes();
		if (CollectionUtils.isEmpty(types)) {
			types = Arrays.asList(MultiNoticeType.DEFAULT_NOTICE_TYPE);
		}
		for (NoticeType type : types) {
			failureRetryTaskHandler.getTaskExecutor().execute(() -> {
				Map<String, Object> params = null;
				boolean success = false;
				try {
					params = mergeParams(multiType, notice.getParams());
					failureRetryTaskHandler.execute(failureRetry, type, notice, (Serializable)params);
					success = true;
				} catch (Exception e) {
					logger.error("Send " + type + " notice[" + notice.getSubject() + "] error!", e);
					Throwable cause = e.getCause();
					while (cause != null) {
						if(cause instanceof MailSendException) {
							MailSendException mse = (MailSendException)cause;
							Map<Object, Exception> failedMessages = mse.getFailedMessages();
							if(MapUtils.isNotEmpty(failedMessages)) {
								failedMessages.forEach((message, ex) -> {
									logger.error(message.toString(), ex);
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

	@Override
	public void sendTemplate(Notice notice) {
		String template = notice.getContent();
		if (StringUtils.isBlank(template)) {
			template = "";
		}
		SimpleNotice templateNotice = new SimpleNotice(notice);
		templateNotice.setContent(Notice.CONTENT_TEMPLETE_NAME_PREFIX + noticeConfig.getTemplatePath() + "/"
				+ NoticeType.sms + "/" + template + noticeConfig.getSms().getTemplateSuffix());
		send(templateNotice);
	}

	private String parseSmsContent(String content, Map<String, Object> params) {
		if (content.startsWith(Notice.CONTENT_TEMPLETE_NAME_PREFIX)) {
			content = parseSmsTemplate(content.substring(Notice.CONTENT_TEMPLETE_NAME_PREFIX.length()));
		}
		if (MapUtils.isNotEmpty(params)) {
			for (Map.Entry<String, Object> entry : handleParams(params).entrySet()) {
				String name = entry.getKey();
				Object value = entry.getValue();
				content = content.replaceAll("\\$\\{" + name + "\\}", value == null ? "" : value.toString());
			}
		}
		return content;
	}

	private String parseSmsTemplate(String template) {
		Assert.hasText(templateDir, "Template dir can't be empty!");
		ClassPathResource resource = new ClassPathResource(templateDir + "/" + template);
		try (InputStream inputStream = resource.getInputStream()) {
			byte[] content = IOUtils.toByteArray(resource.getInputStream());
			return new String(content, clientOperations.getClientOptions().getEncoding());
		} catch (Exception e) {
			ReflectionUtils.rethrowRuntimeException(e);
			return null;
		}
	}

	private Map<String, Object> handleParams(Map<String, Object> params) {
		Map<String, Object> handleParams = new HashMap<>();
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			handleParams.putAll(buildParams(entry.getKey(), entry.getValue()));
		}
		return handleParams;
	}

	private Map<String, Object> buildParams(String name, Object value) {
		Map<String, Object> params = new HashMap<>();
		if (value instanceof Map || value instanceof Bean) {
			Map<String, Object> map = ConvertUtils.convertMap(value);
			if (MapUtils.isNotEmpty(map)) {
				for (Map.Entry<String, Object> entry : map.entrySet()) {
					params.putAll(buildParams(name + "." + entry.getKey(), entry.getValue()));
				}
			}
			value = map;
		} else if (value instanceof List || value != null && value.getClass().isArray()) {
			List<Object> list = ConvertUtils.convertList(value, Object.class);
			if (CollectionUtils.isNotEmpty(list)) {
				for (int i = 0; i < list.size(); i++) {
					params.putAll(buildParams(name + "." + i, list.get(i)));
				}
			}
			value = list;
		}
		params.put(name, value);
		return params;
	}

}
