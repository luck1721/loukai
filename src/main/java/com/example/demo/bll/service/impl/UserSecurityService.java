package com.example.demo.bll.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.bll.config.UserConfig;
import com.example.demo.bll.dao.UserDao;
import com.example.demo.bll.entity.User;
import com.example.demo.bll.entity.UserSecurity;
import com.example.demo.bll.enums.NoticeTemplate;
import com.example.demo.bll.enums.NoticeType;
import com.example.demo.bll.service.SysUserService;
import com.example.demo.bll.service.notice.NoticeService;
import com.example.demo.bll.service.notice.SimpleMultiNotice;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 用户安全服务接口
 *
 * @date 2020年5月23日
 * @author huanglj
 */
@Service("coreUserSecurityService")
@CacheConfig(cacheNames="userSecurityCache")
public class UserSecurityService extends SecurityService{
	private static final String securityEmailCacheName = "securityEmailCache";

	@Autowired
	private UserDao userDao;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private TinyUrlLogService tinyUrlLogService;
	@Autowired
	private UserConfig userConfig;

	@Cacheable(value = securityEmailCacheName, key = "#securityId")
	public UserSecurity securityEmail(String securityId) {
		return null;
	}

	@CachePut(value = securityEmailCacheName, key = "#securityId")
	public UserSecurity securityEmail(String securityId, UserSecurity userSecurity) {
		return userSecurity;
	}

	@CacheEvict(value = securityEmailCacheName, key = "#securityId")
	public void removeSecurityEmail(String securityId) {
	}

	public String noticeUserSecurity(String verificationId, UserSecurity userSecurity) {
		SimpleMultiNotice notice = SimpleMultiNotice.notice(NoticeTemplate.userSecurity);
		notice.addParams(userSecurity.getUser());
		String url = userConfig.getVerificationUserSecurityPageUri() + "?verificationId=" + verificationId;
		if(StringUtils.isNotBlank(userSecurity.getSecurityPhone())) {
			url += "&phone=" + userSecurity.getSecurityPhone();
		}
		notice.addParam("tinyUrl", tinyUrlLogService.generateTinyUrl(url));
		notice.type(NoticeType.email).addReceivers(userSecurity.getSecurityEmail());
		noticeService.sendTemplate(NoticeType.email, notice);
		return "账号安全验证链接已发送至邮箱【" + userSecurity.getSecurityEmail()  + "】，请登录邮箱完成账号安全验证。";
	}

	public String noticeVerification(String phone, String verification) {
		SimpleMultiNotice notice = SimpleMultiNotice.notice(NoticeTemplate.userSecurity);
		notice.addParam("verification", verification);
		notice.type(NoticeType.sms).addReceivers(phone);
		noticeService.sendTemplate(NoticeType.sms, notice);
		return "账号安全验证码已发送至手机号【" + phone + "】，请读取短信完成账号安全验证。";
	}

	public String noticeSecurityEmail(String securityId, UserSecurity userSecurity) {
		SimpleMultiNotice notice = SimpleMultiNotice.notice(NoticeTemplate.securityEmail);
		notice.addParams(userSecurity.getUser());
		String url = userConfig.getUpdateSecurityEmailPageUri() + "?securityId=" + securityId;
		notice.addParam("tinyUrl", tinyUrlLogService.generateTinyUrl(url));
		notice.type(NoticeType.email).addReceivers(userSecurity.getSecurityEmail());
		noticeService.sendTemplate(NoticeType.email, notice);
		return "绑定邮箱验证链接已发送至邮箱【" + userSecurity.getSecurityEmail()  + "】，请登录邮箱绑定邮箱。";
	}

	public String noticeSecurityPhone(String phone, String verification) {
		SimpleMultiNotice notice = SimpleMultiNotice.notice(NoticeTemplate.securityPhone);
		notice.addParam("verification", verification);
		notice.type(NoticeType.sms).addReceivers(phone);
		noticeService.sendTemplate(NoticeType.sms, notice);
		return "绑定手机号验证码已发送至手机号【" + phone + "】，请读取短信绑定手机号。";
	}

	public void updateSecurityEmail(String userId, String securityEmail) {
		Optional<User> user = userDao.findById(new Long(userId));
		user.get().setEmail(securityEmail);
		userDao.save(user.get());
		securityEmail(userId, securityEmail);
	}

	public void updateSecurityPhone(String userId, String securityPhone) {
		Optional<User> user = userDao.findById(new Long(userId));
		user.get().setPhone(securityPhone);
		userDao.save(user.get());
		securityPhone(userId, securityPhone);
	}

	public void deleteUserSecurity(String userId) {
		LambdaQueryWrapper<UserSecurity> queryWrapper = new LambdaQueryWrapper<UserSecurity>();
		queryWrapper.eq(UserSecurity::getUserId,userId);
		authUserSecurityDao.delete(queryWrapper);
	}

}
