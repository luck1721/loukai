package com.example.demo.web.controller;

import cn.com.citycloud.hcs.common.auth.AuthHolder;
import cn.com.citycloud.hcs.common.utils.ConvertUtils;
import cn.com.citycloud.hcs.common.web.DataType;
import cn.com.citycloud.hcs.common.web.ParamType;
import cn.com.citycloud.hcs.common.web.swagger.ApiBody;
import cn.com.citycloud.hcs.common.web.swagger.ApiData;
import cn.com.citycloud.hcs.common.web.swagger.ApiProperty;
import cn.com.citycloud.hcs.common.web.view.ModelBuilder;
import cn.hutool.core.lang.Validator;
import cn.hutool.crypto.SecureUtil;
import com.example.demo.bll.config.VerificationConfig;
import com.example.demo.bll.entity.User;
import com.example.demo.bll.entity.VerificationLog;
import com.example.demo.bll.service.VerificationLogService;
import com.example.demo.bll.service.impl.RegisterService;
import com.example.demo.bll.service.impl.UserSecurityService;
import com.example.demo.bll.shiro.EasyTypeToken;
import com.example.demo.web.domain.vo.LoginResultVo;
import io.swagger.annotations.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.UUID;

/**
 * @author lk
 * @date 2021/7/22
 */
@RestController("coreRegisterController")
@RequestMapping("/oauth/register")
public class RegisterController {
	private static final String sessionIdHeaderName = "Session-Id";
	private static final String verifyType = "register";

	@Autowired
	private RegisterService registerService;
	@Autowired
	private UserSecurityService userSecurityService;
	@Autowired
	private VerificationLogService verificationLogService;
	@Autowired
	private VerificationConfig verificationConfig;

	@Order(1)
	@ApiOperation("注册用户")
	@ApiBody(value = "RegisterUser", name = "registerUser", description = "注册用户", properties = {
			@ApiProperty(name = "username", value = "用户名，只能包含字母数字和_.符号且必须以字母开头，长度不能少于6位", dataType = DataType.string, required = true),
			@ApiProperty(name = "password", value = "密码，不能含有空格", dataType = DataType.string, required = true),
			@ApiProperty(name = "fullname", value = "姓名", dataType = DataType.string, allowEmptyValue = true),
			@ApiProperty(name = "email", value = "邮箱", dataType = DataType.string, required = true),
			@ApiProperty(name = "phone", value = "手机号", dataType = DataType.string, allowEmptyValue = true)
	})
	@ApiData(value = "Register", description = "注册信息", properties = {
			@ApiProperty(name = "registerId", value = "注册ID", dataType = DataType.string, required = true),
			@ApiProperty(name = "user", value = "用户信息", dataTypeClass = User.class, required = true)
	})
	@PostMapping("")
	public Map<String, Object> register(String username,String password,String fullname,String email,String phone) {
		Assert.isTrue(username.matches("^[A-Za-z][\\w\\.]{5,}$"), "用户名只能包含字母数字和_.符号且必须以字母开头，长度不能少于6位!");
		Assert.isTrue(password.matches("^\\S+$"), "密码不能含有空格!");
		Assert.isTrue(Validator.isEmail(email), "邮箱必须包含@符号，只能包含字母，数字，下划线(_)，点号(.)！");
		if (StringUtils.isNotBlank(phone)) {
			Assert.isTrue(Validator.isMobile(phone), "手机号格式不正确！");
		}
		Assert.isTrue(!userSecurityService.isSecurityEmail(email), "邮箱已被注册！");
		User user = new User();
		user.setName(username);
		user.setPassword(SecureUtil.md5(password));
		user.setEmail(email);
		user.setPhone(phone);
		String registerId = UUID.randomUUID().toString().replaceAll("\\-", "");
		user = registerService.registerUser(registerId, user);
		registerId = registerService.noticeRegisterUser(registerId, user);
		return new ModelBuilder("registerId", registerId).add("user", user).build();
	}

	@Order(2)
	@ApiOperation("验证注册并重定向")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "registerId", value = "注册ID", dataType = DataType.string, required = true, paramType = ParamType.path),
			@ApiImplicitParam(name = "redirect_uri", value = "重定向地址，默认跳转到首页", dataType = DataType.string, allowEmptyValue = true, paramType = ParamType.query)
	})
	@ApiResponses({
			@ApiResponse(code = HttpServletResponse.SC_FOUND, message = "验证注册成功后跳转到redirect_uri，默认跳转到首页")
	})
	@GetMapping("/{registerId}")
	public String registerRedirect(@PathVariable String registerId, @RequestParam(value = "redirect_uri", required = false) String redirectUri, HttpServletRequest request) {
		request.setAttribute("redirect", true);
		verifyRegister(registerId, request);
		return redirectUri;
	}

	private void verifyRegister(String registerId, HttpServletRequest request) {
		Assert.hasText(registerId, "注册ID不能为空!");
		User user = registerService.registerUser(registerId);
		Assert.notNull(user, "注册ID[" + registerId + "]已失效!");
		try {
			user = registerService.addUser(user);
		} finally {
			registerService.removeRegisterUser(registerId);
		}
		login(user, request);
	}

	@Order(3)
	@ApiOperation("获取注册验证码")
	@ApiImplicitParam(name = "phone", value = "手机号", dataType = DataType.string, required = true, paramType = ParamType.query)
	@GetMapping("/verification")
	public String getRegisterVerification(String phone, HttpServletResponse response, HttpSession session) {
		Assert.isTrue(Validator.isMobile(phone), "手机号格式不正确！");
		Assert.isTrue(!userSecurityService.isSecurityPhone(phone), "手机号已被注册！");
		Assert.isTrue(verificationLogService.checkGenerateTimes(verifyType, phone), "获取验证码次数已达上限！");
		Assert.isTrue(verificationLogService.checkGenerateInterval(verifyType, phone), "获取验证码过于频繁，请稍后再试！");
		String verification = verificationLogService.generateVerification(verifyType, phone, session.getId(), null);
		verification = registerService.noticeVerification(phone, verification);
		response.setHeader(sessionIdHeaderName, session.getId());
		return verification;
	}

	@Order(3)
	@ApiOperation("验证码注册用户")
	@ApiBody(value = "VerificationRegisterUser", name = "registerUser", description = "验证码注册用户", properties = {
			@ApiProperty(name = "verification", value = "验证码", dataType = DataType.string, required = true),
			@ApiProperty(name = "password", value = "密码，不能含有空格", dataType = DataType.string, allowEmptyValue = true),
			@ApiProperty(name = "fullname", value = "姓名", dataType = DataType.string, allowEmptyValue = true),
			@ApiProperty(name = "email", value = "邮箱", dataType = DataType.string, allowEmptyValue = true),
			@ApiProperty(name = "phone", value = "手机号", dataType = DataType.string, required = true)
	})
	@PostMapping("/verification")
	public LoginResultVo verificationRegister(String verification,String password,String fullname,String email,String phone, HttpServletRequest request, HttpSession session) {
		if (StringUtils.isNotBlank(password)) {
			Assert.isTrue(password.matches("^\\S+$"), "密码不能含有空格!");
		}
		if (StringUtils.isNotBlank(email)) {
			Assert.isTrue(Validator.isEmail(email), "邮箱必须包含@符号，只能包含字母，数字，下划线(_)，点号(.)！");
		}
		Assert.isTrue(Validator.isMobile(phone), "手机号格式不正确！");
		VerificationLog verificationLog = verificationLogService.findVerificationLog(verifyType, phone);
		Assert.notNull(verificationLog, "验证码已失效，请重新获取！");
		Assert.isTrue(verificationLog.getTimes() <= verificationConfig.getMaxVerifyTimes(), "连续输入验证码错误的次数过多，请重新获取！");
		Assert.isTrue(verificationLogService.verify(verificationLog, verification, session.getId(), null), "验证码错误！");
		User user = new User();
		if (StringUtils.isNotBlank(password)) {
			user.setPassword(AuthHolder.encryptPassword(password));
		}
		user.setName(StringUtils.isBlank(fullname) ? phone : fullname);
		user.setEmail(email);
		user.setPhone(phone);
		user = registerService.addUser(user, AuthHolder.encryptPassword(UUID.randomUUID().toString()));
		return login(user, request);
	}

	private LoginResultVo login(User user, HttpServletRequest request) {
		if (AuthHolder.isLogin()) {
			try {
				SecurityUtils.getSubject().logout();
			} catch (Exception e) {
			}
		}
		String username = user.getName();
		boolean success = true;
		try {
			UsernamePasswordToken usernamePasswordToken = new EasyTypeToken(user.getName());
			SecurityUtils.getSubject().login(usernamePasswordToken);
		} catch (Exception e) {
			success = false;
			throw e;
		} finally {

		}
		ModelBuilder modelBuilder = new ModelBuilder("user", user);
		Session session = SecurityUtils.getSubject().getSession(false);
		if (session != null) {
			modelBuilder.add("sessionId", session.getId());
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
		modelBuilder.add("accessToken",accessToken);
		return ConvertUtils.convert(modelBuilder.build(), LoginResultVo.class);
	}
}
