package com.example.demo.web.controller;


import cn.com.citycloud.hcs.common.web.DataType;
import cn.com.citycloud.hcs.common.web.ParamType;
import cn.com.citycloud.hcs.common.web.ResponseData;
import cn.com.citycloud.hcs.common.web.swagger.ApiBody;
import cn.com.citycloud.hcs.common.web.swagger.ApiData;
import cn.com.citycloud.hcs.common.web.swagger.ApiProperty;
import cn.hutool.core.lang.Validator;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.example.demo.bll.config.VerificationConfig;
import com.example.demo.bll.entity.User;
import com.example.demo.bll.entity.VerificationLog;
import com.example.demo.bll.service.LoginService;
import com.example.demo.bll.service.VerificationLogService;
import com.example.demo.bll.shiro.EasyTypeToken;
import com.example.demo.web.domain.vo.LoginResultVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.binary.Base64;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * (VerificationLog)表控制层
 *
 * @author loukai
 * @since 2021-07-16 15:28:58
 */
@RestController
@RequestMapping("/oauth/login/verification")
public class VerificationLogController extends ApiController {

	private static final String sessionIdHeaderName = "Session-Id";
	private static final String verifyType = "login";

	@Autowired
	private LoginService loginService;
	/**
	 * 服务对象
	 */
	@Autowired
	private VerificationLogService verificationLogService;

	@Autowired
	private VerificationConfig verificationConfig;

	@Order(1)
	@ApiOperation("获取登录验证码")
	@ApiImplicitParam(name = "phone", value = "手机号", dataType = DataType.string, required = true, paramType = ParamType.query)
	@ApiData(value="LoginVerification",description="登录验证码",properties={
			@ApiProperty(name = "verification", value = "验证码", dataType = DataType.string, required = true)
	})
	@ResponseData
	@GetMapping("")
	public String getLoginVerification(String phone, HttpServletResponse response, HttpSession session) {
		Assert.hasText(phone, "手机号不能为空!");
		Assert.isTrue(Validator.isMobile(phone), "手机号格式不正确！");
		User user = loginService.findByName(phone);
		Assert.notNull(user, "手机号未注册！");
		Assert.isTrue(verificationLogService.checkGenerateTimes(verifyType, phone), "获取验证码次数已达上限！");
		Assert.isTrue(verificationLogService.checkGenerateInterval(verifyType, phone), "获取验证码过于频繁，请稍后再试！");
		String verification = verificationLogService.generateVerification(verifyType, phone, session.getId(), user.getId().toString());
		verification = loginService.noticeLoginVerification(phone, verification);
		response.setHeader(sessionIdHeaderName, session.getId());
		return verification;
	}

	@Order(1)
	@ApiOperation("验证码登录")
	@ApiBody(value="VerificationLogin",name="login",description="验证码登录信息",properties={
			@ApiProperty(name="phone",value="手机号",dataType=DataType.string,required=true),
			@ApiProperty(name="verification",value="验证码",dataType=DataType.string,required=true)
	})
	@ResponseData
	@PostMapping("")
	public LoginResultVo verificationLogin(String phone,String verification, HttpServletRequest request, HttpSession session) {
		Assert.hasText(phone, "手机号不能为空!");
		Assert.isTrue(Validator.isMobile(phone), "手机号格式不正确！");
		Assert.hasText(verification, "验证码不能为空!");
		VerificationLog verificationLog = verificationLogService.findVerificationLog(verifyType, phone);
		Assert.notNull(verificationLog, "验证码已失效，请重新获取！");
		Assert.isTrue(verificationLog.getTimes() <= verificationConfig.getMaxVerifyTimes(), "连续输入验证码错误的次数过多，请重新获取！");
		User user = loginService.findByName(phone);
		Assert.notNull(user, "手机号未注册！");
		Assert.isTrue(verificationLogService.verify(verificationLog, verification, session.getId(), user.getId().toString()), "验证码错误！");
		String username = user.getName();
		boolean success = true;
		try {
			EasyTypeToken userToken = new EasyTypeToken(username);
			SecurityUtils.getSubject().login(userToken);
		} catch (Exception e) {
			success = false;
			throw e;
		} finally {
					}
		String accessToken = null;
		if(session != null) {
			Object sessionId = session.getId();
			if(sessionId != null) {
				try {
					accessToken = Base64.encodeBase64String(sessionId.toString().getBytes("UTF-8"));
				} catch (Exception e) {
					accessToken = Base64.encodeBase64String(sessionId.toString().getBytes());
				}
			}
		}
		LoginResultVo vo = new LoginResultVo();
		vo.setUser(user);
		vo.setAccessToken(accessToken);
		return vo;
	}
}
