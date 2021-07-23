package com.example.demo.bll.entity;

import cn.com.citycloud.hcs.common.task.log.expiredclean.ExpiredClean;
import com.baomidou.mybatisplus.annotation.*;
import com.example.demo.bll.anon.SensitiveData;
import com.example.demo.bll.anon.SensitiveField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 业务日志(SysBizLog)表
 *
 * @author lk
 * @date 2020-08-18 13:44:54
 */
@Data
@NoArgsConstructor
@SensitiveData
@TableName("sys_biz_log")
public class SysBizLog implements Serializable, ExpiredClean<String> {
	private static final long serialVersionUID = 1L;
	/**
	 * 项目ID
	 */
	//private Integer projectId;

	/**
	 * 日志ID
	 */
	@TableId(type = IdType.AUTO)
	private Integer logId;

	/**
	 * 用户编号
	 */
	private String userNo;

	/**
	 * 用户名称
	 */
	private String userName;

	/**
	 * 日志信息
	 */
	private String logMsg;

	/**
	 * IP地址
	 */
	@SensitiveField
	private String ip;

	/**
	 * 操作时间
	 */
	private Date actionTime;

	/**
	 * 操作的功能ID
	 */
	private String funId;

	/**
	 * 操作的功能名称
	 */
	private String funName;

	/**
	 * 交互数据
	 */
	private String bizData;

	/**
	 * 请求地址
	 */
	private String reqUrl;

	/**
	 * 状态：0-正常 1-错误
	 */
	private Integer status;

	@Version
	private Integer version;

	@TableField(exist=false)
	private User user;


	@Override
	public String getId() {
		return logId.toString();
	}

	@Override
	public Date getExpiredTime() {
		return actionTime;
	}
}
