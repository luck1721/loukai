package com.example.demo.bll.entity;

import cn.com.citycloud.hcs.common.domain.BaseBean;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author lk
 * @date 2020/12/9
 */
@Data
@NoArgsConstructor
public class GridEventCreate extends BaseBean {

	private static final long serialVersionUID = -364001956726695299L;
	private String title;

	private String description;

	private String thirtyPartyEventId;

	private String thirtyPartyEventType;

	private Integer status;

	private String collector;

	private Date collectTime;

	private String  processor;

	private Date limitingTime;

}
