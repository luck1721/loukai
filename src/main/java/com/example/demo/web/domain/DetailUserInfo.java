package com.example.demo.web.domain;

import com.example.demo.bll.entity.Role;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author lk
 * @date 2021/1/20
 */
@Data
public class DetailUserInfo implements Serializable {

	/**
	 * 租户id
	 */
	private Integer projectId;

	private Integer userId;

	private String userNo;

	private String userName;

	private Integer sex;

	/**
	 * 数据权限
	 */
	private Integer userGrade;

	/**
	 * 电话
	 */
	private String telephone;

	private Integer groupId;

	private String groupNo;

	private String groupName;

	private Integer groupLevel;

	private Integer deptId;

	private String deptName;

	private String ownRoles;

	private Integer currentSiteId;

	private String currentSiteName;

	private Integer platformId;

	private String fullPath;

	/**
	 * 用户类型（USER_TYPE）
	 */
	private Integer userType;

	/**
	 * 访问认证
	 */
	private String accessToken;

	/**
	 * 角色列表
	 */
	private List<Role> roles;

}
