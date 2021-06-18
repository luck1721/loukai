package com.example.demo.web.domain.vo;

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
@ApiModel(value = "事件集合")
public class EventVO {

	private static final long serialVersionUID = 6356224423669531987L;

	@ApiModelProperty(value = "网格事件",required = true)
	private GridEventCreateVO event;

	@ApiModelProperty(value = "事件类型")
	private EventTypeVO eventType;
}
