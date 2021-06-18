package com.example.demo.web.controller;

import com.example.demo.bll.entity.Role;
import com.example.demo.bll.entity.User;
import com.example.demo.bll.service.LoginService;
import com.example.demo.bll.shiro.EasyTypeToken;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录
 * @author lk
 * @date 2021/1/19
 */
@RestController
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
		SecurityUtils.getSubject().login(usernamePasswordToken);
		request.getSession().setAttribute("loginUserId",user.getName());
		loginService.updateLoginStatus(user.getName(),1);
		return "login ok!";
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
		return "ssoLogin ok!";
	}

	/**
	 * 添加用户
	 * @param user
	 * @return
	 */
	@PostMapping(value = "/addUser")
	public String addUser(@RequestBody User user) {
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
	public String login(HttpServletRequest request) {
		return "请登录";
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
