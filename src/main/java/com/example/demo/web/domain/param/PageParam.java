package com.example.demo.web.domain.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lk
 * @date 2021/6/23
 */
@Data
@ApiModel(value = "PageParam", description = "分页参数")
public class PageParam {
	/**
	 * 页码
	 */
	@ApiModelProperty(value = "页码，不传则默认1", example = "1")
	private Integer page = 1;

	/**
	 * 每页条数
	 */
	@ApiModelProperty(value = "每页条数，不传则默认10", example = "10")
	private Integer limit = 10;

	/**
	 * 排序字段(格式：字段名:排序方式，字段名:排序方式 （asc正序，desc倒序） 示例:id:desc,age:asc)
	 */
	@ApiModelProperty(value = "排序字段(格式：字段名:排序方式，字段名:排序方式 （asc正序，desc倒序） 示例:id:desc,age:asc)")
	private String orders;
}
