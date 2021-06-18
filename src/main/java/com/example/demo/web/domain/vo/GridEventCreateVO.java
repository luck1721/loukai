package com.example.demo.web.domain.vo;

import cn.com.citycloud.hcs.common.domain.BaseBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author lk
 * @date 2020/12/9
 */
@Data
@NoArgsConstructor
@ApiModel(value = "新增网格事件")
public class GridEventCreateVO extends BaseBean {

	private static final long serialVersionUID = -364001956726695299L;
	@ApiModelProperty(value = "标题",required = true)
	private String title;

	@ApiModelProperty(value = "内容",required = true)
	private String description;

	@ApiModelProperty(value = "第三方唯一ID",required = true)
	private String thirtyPartyEventId;

	@ApiModelProperty(value = "第三方事件类型")
	private String thirtyPartyEventType;

	@ApiModelProperty(value = "状态",required = true)
	private Integer status;

	@ApiModelProperty(value = "采集人",required = true)
	private String collector;

	@ApiModelProperty(value = "采集时间",required = true)
	private Date collectTime;

	@ApiModelProperty(value = "当前处理人",required = true)
	private String  processor;

	@ApiModelProperty(value = "限定世间",required = true)
	private Date limitingTime;

}
