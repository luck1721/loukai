package com.example.demo.web.controller;

import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.request.AuthGithubRequest;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.request.AuthWeChatOpenRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	@RequestMapping("/render/{source}")
	public void renderAuth(HttpServletResponse response) throws IOException {
		AuthRequest authRequest = getAuthRequest();
		String authorizeUrl = authRequest.authorize(AuthStateUtils.createState());
		response.sendRedirect(authorizeUrl);
	}

	/**
	 * 用户在确认第三方平台授权（登录）后， 第三方平台会重定向到该地址，并携带code、state等参数
	 *
	 * @param callback 第三方回调时的入参
	 * @return 第三方平台的用户信息
	 */
	@RequestMapping("/callback/{source}")
	public Object login(AuthCallback callback) {
		AuthRequest authRequest = getAuthRequest();
		return authRequest.login(callback);
	}

	/**
	 * 获取授权Request
	 *
	 * @return AuthRequest
	 */
	private AuthRequest getAuthRequest() {
		return new AuthGithubRequest(AuthConfig.builder()
				.clientId("d01a06f55c6d5210f616")
				.clientSecret("970dc0b2905f9e073a035f5b0a82100f69c83676")
				.redirectUri("http://127.0.0.1:8080/oauth/callback/github")
				.build());
	}
}
