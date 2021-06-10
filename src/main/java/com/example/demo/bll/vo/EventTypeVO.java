package com.example.demo.bll.vo;

import cn.com.citycloud.hcs.common.domain.BaseBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lk
 * @date 2020/12/9
 */
@Data
@NoArgsConstructor
@ApiModel(value = "事件类型")
public class EventTypeVO extends BaseBean {

	@ApiModelProperty(value = "一级分类")
	private String firstLevel;

	@ApiModelProperty(value = "二级分类")
	private String secondLevel;

	@ApiModelProperty(value = "三级分类")
	private String threeLevel;
}
