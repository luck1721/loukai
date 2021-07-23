package com.example.demo.bll.service.impl;

import com.example.demo.bll.config.RegisterConfig;
import com.example.demo.bll.config.TinyUrlConfig;
import com.example.demo.bll.dao.UserDao;
import com.example.demo.bll.entity.User;
import com.example.demo.bll.enums.NoticeTemplate;
import com.example.demo.bll.enums.NoticeType;
import com.example.demo.bll.service.notice.MultiNoticeService;
import com.example.demo.bll.service.notice.SimpleMultiNotice;
import com.example.demo.bll.service.notice.SimpleMultiNoticeType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

/**
 * @author lk
 * @date 2021/7/22
 */
@Service("coreRegisterService")
public class RegisterService {
	private static final String registerCacheName = "registerCache";

	@Autowired
	private UserDao userDao;
	@Autowired
	private UserSecurityService userSecurityService;
	@Autowired
	private MultiNoticeService noticeService;
	@Autowired
	private TinyUrlConfig tinyUrlConfig;
	@Autowired
	private TinyUrlLogService tinyUrlLogService;
	@Autowired
	private RegisterConfig registerConfig;

	@Cacheable(value = registerCacheName, key = "#registerId")
	public User registerUser(String registerId) {
		return null;
	}

	@CachePut(value = registerCacheName, key = "#registerId")
	public User registerUser(String registerId, User user) {
		return user;
	}

	@CacheEvict(value = registerCacheName, key = "#registerId")
	public void removeRegisterUser(String registerId) {
	}

	public String noticeRegisterUser(String registerId, User user) {
		if(!registerConfig.isNotice()) {
			return registerId;
		}
		SimpleMultiNotice notice = SimpleMultiNotice.notice(NoticeTemplate.register);
		notice.addParams(user);
		String url = tinyUrlConfig.getHost() + "/oauth/register/" + registerId + "?redirect_uri=" + registerConfig.getRedirectUri();
		notice.addParam("tinyUrl", tinyUrlLogService.generateTinyUrl(url));
		notice.type(NoticeType.email).addReceivers(user.getEmail());
		noticeService.sendTemplate(SimpleMultiNoticeType.type(NoticeType.email), notice);
		return "用户注册验证链接已发送至邮箱【" + user.getEmail()  + "】，请登录邮箱完成注册。";
	}

	public User addUser(User user) {
		try {
			userDao.save(user);
		} catch (DuplicateKeyException e) {
			throw new RuntimeException("用户[" + user.getName() + "]已存在！", e);
		}
		try {
			userSecurityService.securityEmail(user.getId().toString(), user.getEmail());
		} catch (DuplicateKeyException e) {
			throw new RuntimeException("邮箱[" + user.getEmail() + "]已存在！", e);
		}
		return user;
	}

	public String noticeVerification(String phone, String verification) {
		if(!registerConfig.isNotice()) {
			return verification;
		}
		SimpleMultiNotice notice = SimpleMultiNotice.notice(NoticeTemplate.register);
		notice.addParam("verification", verification);
		notice.type(NoticeType.sms).addReceivers(phone);
		noticeService.sendTemplate(SimpleMultiNoticeType.type(NoticeType.sms), notice);
		return "用户注册验证码已发送至手机号【" + phone + "】，请读取短信完成注册。";
	}

	public User addUser(User user, String initialPassword) {
		boolean hasPassword = StringUtils.isNotBlank(user.getPassword());
		if(!hasPassword) {
			user.setPassword(initialPassword);
		}
		try {
			userDao.save(user);
		} catch (DuplicateKeyException e) {
			throw new RuntimeException("用户[" + user.getName() + "]已存在！", e);
		}
		if(!hasPassword) {
			userSecurityService.securityInit(user.getId().toString(), initialPassword);
		}
		try {
			userSecurityService.securityPhone(user.getId().toString(), user.getPhone());
		} catch (DuplicateKeyException e) {
			throw new RuntimeException("手机号[" + user.getPhone() + "]已存在！", e);
		}
		return user;
	}

}
