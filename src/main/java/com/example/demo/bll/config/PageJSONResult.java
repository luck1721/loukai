package com.example.demo.bll.config;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author lk
 * @date 2021/1/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PageJSONResult<T> extends JSONResult<List<T>> {

	@ApiModelProperty(value = "总记录数", position = 4)
	private long totalCount;

	@ApiModelProperty(value = "当前页", position = 5)
	private long current;

	@ApiModelProperty(value = "每页行数", position = 6)
	private long size;

	private PageJSONResult(Page<T> pageQuery, boolean success, ResultEnum resultEnum) {
		this(pageQuery, success, resultEnum.getCode(), resultEnum.getMessage());
	}

	private PageJSONResult(Page<T> pageQuery, boolean success, String code, String message) {
		super(pageQuery.getRecords(), success, code, message);
		this.totalCount = pageQuery.getTotal();
		this.current = pageQuery.getCurrent();
		this.size = pageQuery.getSize();
	}

	/**
	 * 正常返回json结果
	 * @param data 数据
	 * @param <X> 数据类型
	 * @return json结果
	 */
	public static <X> PageJSONResult<X> ok(Page<X> data) {
		return ok(data, ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage());
	}

	/**
	 * 正常返回json结果，自定义返回码和返回信息
	 * @param data 数据
	 * @param code 自定义返回码
	 * @param message 自定义返回信息
	 * @param <X> 数据类型
	 * @return json结果
	 */
	public static <X> PageJSONResult<X> ok(Page<X> data, String code, String message) {
		return new PageJSONResult<>(data, true, code, message);
	}

	/**
	 * 错误返回结果
	 * @param data 错误数据
	 * @param <X> 错误数据类型
	 * @return 错误返回结果
	 */
	public static <X> PageJSONResult<X> error(Page<X> data) {
		return error(data, ResultEnum.UNKNOWN);
	}

	/**
	 * 错误返回结果
	 * @param data 错误数据
	 * @param resultEnum 错误信息枚举
	 * @param <X> 错误数据类型
	 * @return 错误返回结果
	 */
	public static <X> PageJSONResult<X> error(Page<X> data, ResultEnum resultEnum) {
		return error(data, resultEnum.getCode(), resultEnum.getMessage());
	}

	/**
	 * 错误返回结果，自定义返回码和返回信息
	 * @param data 错误数据
	 * @param code 自定义返回码
	 * @param message 自定义返回信息
	 * @param <X> 错误数据类型
	 * @return 错误返回结果
	 */
	public static <X> PageJSONResult<X> error(Page<X> data, String code, String message) {
		return new PageJSONResult<>(data, false, code, message);
	}

}
