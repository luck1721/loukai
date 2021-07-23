package com.example.demo.web.domain.vo;

import cn.com.citycloud.hcs.common.domain.BaseBean;
import com.example.demo.bll.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author lk
 * @date 2021/7/16
 */
@ApiModel(
		value = "LoginResult",
		description = "登录成功响应结果"
)
public class LoginResultVo extends BaseBean {
	private static final long serialVersionUID = 1140233132442750326L;
	@ApiModelProperty("用户信息")
	private User user;
	@ApiModelProperty("Session ID")
	private String sessionId;
	@ApiModelProperty("accessToken")
	private String accessToken;

	public LoginResultVo() {
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getSessionId() {
		return this.sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
}
