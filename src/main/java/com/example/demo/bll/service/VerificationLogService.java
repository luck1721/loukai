package com.example.demo.bll.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.bll.entity.VerificationLog;

/**
 * (VerificationLog)表服务接口
 *
 * @author loukai
 * @since 2021-07-16 15:28:41
 */
public interface VerificationLogService extends IService<VerificationLog> {

	String generateVerification(String verifyType, String phone, String sessionId, String userId);

	public boolean checkGenerateTimes(String verifyType, String phone);

	public boolean checkGenerateInterval(String verifyType, String phone);

	public VerificationLog findVerificationLog(String verifyType, String phone);

	public boolean verify(VerificationLog verificationLog, String verification, String sessionId, String userId);

}
