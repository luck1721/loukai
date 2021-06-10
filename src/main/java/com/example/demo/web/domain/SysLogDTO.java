package com.example.demo.web.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lk
 * @date 2021/1/20
 */
@Data
@NoArgsConstructor
public class SysLogDTO implements Serializable {
	private Short logType;

	private Long logId;

	private Long projectId;

	private String userNo;

	private String userName;

	private String logMsg;

	private String ip;

	private Date actionTime;

	private String funId;

	private String funName;

	private String bizData;

	private Date happenTime;

	private String password;

	private Date logonTime;

	private Date logoffTime;

	private String reqUrl;

	private Short status;
}
