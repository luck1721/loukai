package com.example.demo.web.controller;

import com.example.demo.bll.shiro.EasyTypeToken;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.*;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author lk
 * @date 2021/6/10
 */
@RestController
@RequestMapping("/oauth")
public class JustAuthController {
	/**
	 * 获取授权链接并跳转到第三方授权页面
	 *
	 * @param response response
	 * @throws IOException response可能存在的异常
	 */
	@GetMapping("/render/{source}")
	public void renderAuth(@PathVariable String source,HttpServletResponse response) throws IOException {
		AuthRequest authRequest = getAuthRequest(source);
		String authorizeUrl = authRequest.authorize(AuthStateUtils.createState());
		response.sendRedirect(authorizeUrl);
	}

	/**
	 * 用户在确认第三方平台授权（登录）后， 第三方平台会重定向到该地址，并携带code、state等参数
	 *
	 * @param callback 第三方回调时的入参
	 * @return 第三方平台的用户信息
	 */
	@PostMapping("/callback/{source}")
	public Object login(@PathVariable String source, AuthCallback callback) {
		AuthRequest authRequest = getAuthRequest(source);
		AuthResponse response = authRequest.login(callback);
		AuthUser data = (AuthUser) response.getData();
		// 添加用户认证信息
		UsernamePasswordToken usernamePasswordToken = new EasyTypeToken(data.getUsername());
		// 进行验证，这里可以捕获异常，然后返回对应信息
		SecurityUtils.getSubject().login(usernamePasswordToken);
		return data.getUsername();
	}

	/**
	 * 获取授权Request
	 *
	 * @return AuthRequest
	 */
	private AuthRequest getAuthRequest(String source) {
		AuthConfig.AuthConfigBuilder config = AuthConfig.builder()
				.clientId("d01a06f55c6d5210f616")
				.clientSecret("970dc0b2905f9e073a035f5b0a82100f69c83676")
				.redirectUri("http://localhost:8080/oauth/callback/github");
		AuthRequest authRequest = null;
		switch (source)
		{
			case "github":
				authRequest = new AuthGithubRequest(config.build());
				break;
			case "qq":
				authRequest = new AuthQqRequest(config.build());
				break;
			case "weixin":
				authRequest = new AuthWeChatOpenRequest(config.build());
				break;
			case "weixin_mp":
				authRequest = new AuthWeChatMpRequest(config.build());
				break;
			case "weixin_qy":
				authRequest = new AuthWeChatOpenRequest(config.build());
				break;
			case "ding_talk":
				authRequest = new AuthDingTalkRequest(config.build());
		}
		return authRequest;
	}

}
