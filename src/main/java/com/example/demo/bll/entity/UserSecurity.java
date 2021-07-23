package com.example.demo.bll.entity;

import cn.com.citycloud.hcs.common.data.jdbc.Entity;
import cn.com.citycloud.hcs.common.data.jdbc.annotation.Column;
import cn.com.citycloud.hcs.common.data.jdbc.annotation.Reference;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author lk
 * @date 2021/7/22
 */
@Data
public class UserSecurity extends Entity<Long> {
	private static final long serialVersionUID = -6647020322397139701L;
	@Column(
			required = true
	)
	@Reference(User.class)
	private String userId;
	@Column(
			required = true
	)
	private String initialPassword;
	@Column
	private String securityEmail;
	@Column
	private String securityPhone;
	@TableField(exist=false)
	private User user;

	public UserSecurity() {
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getInitialPassword() {
		return this.initialPassword;
	}

	public void setInitialPassword(String initialPassword) {
		this.initialPassword = initialPassword;
	}

	public String getSecurityEmail() {
		return this.securityEmail;
	}

	public void setSecurityEmail(String securityEmail) {
		this.securityEmail = securityEmail;
	}

	public String getSecurityPhone() {
		return this.securityPhone;
	}

	public void setSecurityPhone(String securityPhone) {
		this.securityPhone = securityPhone;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isUnmodifiedPassword() {
		return this.user != null && !StringUtils.isBlank(this.initialPassword) ? StringUtils.equals(this.initialPassword, this.user.getPassword()) : false;
	}

	public boolean isVerifiedEmail() {
		return this.user != null && !StringUtils.isBlank(this.securityEmail) ? StringUtils.equals(this.securityEmail, this.user.getEmail()) : false;
	}

	public boolean isVerifiedPhone() {
		return this.user != null && !StringUtils.isBlank(this.securityPhone) ? StringUtils.equals(this.securityPhone, this.user.getPhone()) : false;
	}
}
