package com.example.demo.bll.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.bll.config.VerificationConfig;
import com.example.demo.bll.entity.VerificationLog;
import com.example.demo.bll.mapper.VerificationLogMapper;
import com.example.demo.bll.service.VerificationLogService;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * (VerificationLog)表服务实现类
 *
 * @author loukai
 * @since 2021-07-16 15:28:42
 */
@Service("verificationLogService")
public class VerificationLogServiceImpl extends ServiceImpl<VerificationLogMapper, VerificationLog> implements VerificationLogService {

	@Autowired
	private VerificationLogMapper verificationLogMapper;
	@Autowired
	private VerificationConfig verificationConfig;

	public String generateVerification(String verifyType, String phone, String sessionId, String userId) {
		VerificationLog verificationLog = new VerificationLog();
		verificationLog.setSessionId(sessionId);
		verificationLog.setUserId(userId);
		verificationLog.setVerifyType(verifyType);
		verificationLog.setPhone(phone);
		verificationLog.setVerification(String.valueOf(RandomUtils.nextInt((int)Math.pow(10,
				verificationConfig.getLength() - 1), (int)Math.pow(10, verificationConfig.getLength()))));
		verificationLog.setVerified(false);
		verificationLog.setTimes(0);
		verificationLog.setTime(new Date());
		verificationLogMapper.insert(verificationLog);
		return verificationLog.getVerification();
	}

	public boolean checkGenerateTimes(String verifyType, String phone) {
		LambdaQueryWrapper<VerificationLog> queryWrapper = new LambdaQueryWrapper<VerificationLog>();
		queryWrapper.eq(VerificationLog::getPhone,phone).eq(VerificationLog::getVerifyType,verifyType);
		Integer maxGenerateTimes = verificationConfig.getMaxGenerateTimes().get(verifyType.replaceAll("\\:$", ""));
		return verificationLogMapper.selectCount(queryWrapper) <= (maxGenerateTimes != null ? maxGenerateTimes : verificationConfig.getDefaultMaxGenerateTimes());
	}

	public boolean checkGenerateInterval(String verifyType, String phone) {
		VerificationLog verificationLog = findVerificationLog(verifyType, phone);
		return verificationLog == null ? true : System.currentTimeMillis() - verificationLog.getTime().getTime() >= verificationConfig.getMinGenerateInterval().toMillis();
	}

	public VerificationLog findVerificationLog(String verifyType, String phone) {
		LambdaQueryWrapper<VerificationLog> queryWrapper = new LambdaQueryWrapper<VerificationLog>();
		queryWrapper.eq(VerificationLog::getVerifyType,verifyType).eq(VerificationLog::getPhone,phone)
				.gt(VerificationLog::getTime,DateUtils.addSeconds(new Date(), -(int)verificationConfig.getExpiration().getSeconds()))
		.orderByDesc(VerificationLog::getTime).last("limit 1");
		VerificationLog verificationLog =verificationLogMapper.selectOne(queryWrapper);
		return verificationLog == null || Boolean.TRUE.equals(verificationLog.getVerified()) ? null : verificationLog;
	}

	public boolean verify(VerificationLog verificationLog, String verification, String sessionId, String userId) {
		boolean verified = checkVerificationLog(verificationLog, verification, sessionId, userId);
		LambdaUpdateWrapper<VerificationLog> updateWrapper = new LambdaUpdateWrapper<VerificationLog>();
		if(verified) {
			updateWrapper.set(VerificationLog::getVerified,true);
		} else {
			updateWrapper.set(VerificationLog::getTimes,verificationLog.getTimes()+1);
		}
		verificationLogMapper.update(verificationLog, updateWrapper);
		return verified;
	}

	private boolean checkVerificationLog(VerificationLog verificationLog, String verification, String sessionId, String userId) {
		/*if(StringUtils.isNotBlank(verificationLog.getSessionId()) && !verificationLog.getSessionId().equals(sessionId)) {
			return false;
		}*/
		if(StringUtils.isNotBlank(verificationLog.getUserId()) && !verificationLog.getUserId().equals(userId)) {
			return false;
		}
		if(!StringUtils.equals(verificationLog.getVerification(), verification)) {
			return false;
		}
		return true;
	}
}
