package com.example.demo.bll.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.demo.bll.entity.UserSecurity;
import com.example.demo.bll.mapper.UserSecurityDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author lk
 * @date 2021/7/22
 */
@Service("authUserSecurityService")
@CacheConfig(
		cacheNames = {"userSecurityCache"}
)
public class SecurityService {
	@Autowired
	protected UserSecurityDao authUserSecurityDao;

	public SecurityService() {
	}

	@Cacheable(
			key = "#verificationId"
	)
	public UserSecurity verifyUserSecurity(String verificationId) {
		return null;
	}

	@CachePut(
			key = "#verificationId"
	)
	public UserSecurity verifyUserSecurity(String verificationId, UserSecurity userSecurity) {
		return userSecurity;
	}

	@CacheEvict(
			key = "#verificationId"
	)
	public void removeVerifyUserSecurity(String verificationId) {
	}

	public UserSecurity getUserSecurity(String userId) {
		UserSecurity userSecurity = this.authUserSecurityDao.getByUserId(userId);
		return userSecurity == null ? new UserSecurity() : userSecurity;
	}

	public void securityInit(String userId, String initialPassword) {
		UserSecurity userSecurity = new UserSecurity();
		userSecurity.setUserId(userId);
		userSecurity.setInitialPassword(initialPassword);
		this.authUserSecurityDao.insert(userSecurity);
	}

	public void securityEmail(String userId, String securityEmail) {
		LambdaUpdateWrapper<UserSecurity> query = new LambdaUpdateWrapper<UserSecurity>();
		query.eq(UserSecurity::getUserId,userId);
		UserSecurity security = this.authUserSecurityDao.selectOne(query);
		LambdaUpdateWrapper<UserSecurity> queryWrapper = new LambdaUpdateWrapper<UserSecurity>();
		queryWrapper.eq(UserSecurity::getSecurityEmail,securityEmail);
		if (this.authUserSecurityDao.update(security, queryWrapper) <= 0) {
			UserSecurity userSecurity = new UserSecurity();
			userSecurity.setUserId(userId);
			userSecurity.setSecurityEmail(securityEmail);
			this.authUserSecurityDao.insert(userSecurity);
		}

	}

	public void securityPhone(String userId, String securityPhone) {
		LambdaUpdateWrapper<UserSecurity> query = new LambdaUpdateWrapper<UserSecurity>();
		query.eq(UserSecurity::getUserId,userId);
		UserSecurity security = this.authUserSecurityDao.selectOne(query);
		LambdaUpdateWrapper<UserSecurity> queryWrapper = new LambdaUpdateWrapper<UserSecurity>();
		queryWrapper.eq(UserSecurity::getUserId,userId).set(UserSecurity::getSecurityPhone,securityPhone);
		if (this.authUserSecurityDao.update(security, queryWrapper) <= 0) {
			UserSecurity userSecurity = new UserSecurity();
			userSecurity.setUserId(userId);
			userSecurity.setSecurityPhone(securityPhone);
			this.authUserSecurityDao.insert(userSecurity);
		}

	}

	public boolean isSecurityEmail(String securityEmail) {
		LambdaQueryWrapper<UserSecurity> queryWrapper = new LambdaQueryWrapper<UserSecurity>();
		queryWrapper.eq(UserSecurity::getSecurityEmail,securityEmail);
		return this.authUserSecurityDao.selectCount(queryWrapper) > 0;
	}

	public boolean isSecurityPhone(String securityPhone) {
		LambdaQueryWrapper<UserSecurity> queryWrapper = new LambdaQueryWrapper<UserSecurity>();
		queryWrapper.eq(UserSecurity::getSecurityPhone,securityPhone);
		return this.authUserSecurityDao.selectCount(queryWrapper) > 0;
	}
}
