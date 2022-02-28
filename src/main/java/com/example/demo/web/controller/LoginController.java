package com.example.demo.web.controller;

import cn.hutool.json.JSONUtil;
import com.example.demo.bll.anon.ResponseResult;
import com.example.demo.bll.config.JSONResult;
import com.example.demo.bll.entity.Role;
import com.example.demo.bll.entity.User;
import com.example.demo.bll.entity.UserSync;
import com.example.demo.bll.service.LoginService;
import com.example.demo.bll.shiro.EasyTypeToken;
import org.apache.commons.codec.binary.Base64;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录
 * @author lk
 * @date 2021/1/19
 */
@RestController
@ResponseResult
@RequestMapping("/api")
public class LoginController {

	@Autowired
	private LoginService loginService;


	/**
	 * POST登录
	 * @param
	 * @return
	 */
	@PostMapping(value = "/login")
	public String login(@RequestBody User user,HttpServletRequest request) {
		// 添加用户认证信息
		UsernamePasswordToken usernamePasswordToken = new EasyTypeToken(user.getName(), user.getPassword());
		// 进行验证，这里可以捕获异常，然后返回对应信息

		try {
			SecurityUtils.getSubject().login(usernamePasswordToken);
		} catch (AuthenticationException e) {
			throw new RuntimeException("用户名或密码错误",e);
		}
		request.getSession().setAttribute("loginUserId",user.getName());
		Session session = SecurityUtils.getSubject().getSession(false);
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
		loginService.updateLoginStatus(user.getName(),1);
		return "login ok! accessToken = " + accessToken;
	}

	@PostMapping(value = "/user_sync")
	public String login(@RequestBody UserSync userSync, HttpServletRequest request) {
		System.out.println(JSONUtil.toJsonStr(userSync));
		return "";
	}

	/**
	 * POST登录
	 * @param
	 * @return
	 */
	@PostMapping(value = "/ssoLogin")
	public String ssoLogin(@RequestBody User user) {
		// 添加用户认证信息
		UsernamePasswordToken usernamePasswordToken = new EasyTypeToken(user.getName());
		// 进行验证，这里可以捕获异常，然后返回对应信息
		SecurityUtils.getSubject().login(usernamePasswordToken);
		Session session = SecurityUtils.getSubject().getSession(false);
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
		return "ssoLogin ok! accessToken = " + accessToken;
	}

	/**
	 * 添加用户
	 * @param user
	 * @return
	 */
	@PostMapping(value = "/addUser")
	public String addUser(@RequestBody User user) {
		SimpleHash simpleHash = new SimpleHash("MD5", user.getPassword(), null, 0);
		user.setPassword(simpleHash.toHex());
		user = loginService.addUser(user);
		return "addUser is ok! \n" + user;
	}

	/**
	 * 添加角色
	 * @param role
	 * @return
	 */
	@PostMapping(value = "/addRole")
	public String addRole(@RequestBody Role role) {
		role = loginService.addRole(role);
		return "addRole is ok! \n" + role;
	}

	/**
	 * 注解的使用
	 * @return
	 */
	@RequiresRoles("admin")
	@RequiresPermissions("create")
	@GetMapping(value = "/create")
	public String create() {
		return "Create success!";
	}

	@GetMapping(value = "/index")
	public String index() {
		return "index page!";
	}

	@GetMapping(value = "/error")
	public String error() {
		return "error page!";
	}

	/**
	 * 退出的时候是get请求，主要是用于退出
	 * @return
	 */
	@GetMapping(value = "/login")
	public JSONResult login(HttpServletRequest request) {
		return JSONResult.error(null,"401","请登录");
	}

	@RequiresAuthentication
	@GetMapping(value = "/logout")
	public String logout(HttpServletRequest request) {
		loginService.updateLoginStatus(request.getSession().getAttribute("loginUserId").toString(),0);
		Subject subject = SecurityUtils.getSubject();
		if(subject.isAuthenticated()) {
			subject.logout();
		}
		return "logout";
	}

	@GetMapping("/publish")
	public String getRequestInfo(HttpServletRequest request) {
		loginService.getUser();
		return "success";
	}
}
