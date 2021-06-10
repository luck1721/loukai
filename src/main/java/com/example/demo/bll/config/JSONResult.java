package com.example.demo.bll.config;

import com.example.demo.bll.util.DateUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author lk
 * @date 2021/1/22
 */
@Data
@NoArgsConstructor
public class JSONResult<T> implements Serializable {

	private static final long serialVersionUID = -1850473388169342606L;

	@ApiModelProperty(value = "返回数据")
	private T data;

	@ApiModelProperty(value = "成功标记", position = 1)
	private Boolean success;

	@ApiModelProperty(value = "结果码", position = 2)
	private String code;

	@ApiModelProperty(value = "结果信息", position = 3)
	private String message;

	@ApiModelProperty(value = "数据返回时间", position = 99)
	private String lastTime;

	@ApiModelProperty(value = "返回跳转页面", position = 4)
	private String url;

	protected JSONResult(T data, boolean success, ResultEnum resultEnum) {
		this(data, success, resultEnum.getCode(), resultEnum.getMessage());
	}

	protected JSONResult(T data, boolean success, String code, String message) {
		this.data = data;
		this.success = success;
		this.code = code;
		this.message = message;
		this.lastTime = getLastDateTime();
	}

	public JSONResult(T data, boolean success) {
		this.data = data;
		this.success = success;
	}

	private String getLastDateTime() {
		return DateUtil.formatCurrentDateToString();
	}

	/**
	 * 正常返回json结果
	 * @return json结果
	 */
	public static JSONResult<Boolean> ok() {
		return ok(true, ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage());
	}

	/**
	 * 正常返回json结果
	 * @param data 数据
	 * @param <X> 数据类型
	 * @return json结果
	 */
	public static <X> JSONResult<X> ok(X data) {
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
	public static <X> JSONResult<X> ok(X data, String code, String message) {
		return new JSONResult<>(data, true, code, message);
	}

	/**
	 * 错误返回结果
	 * @param data 错误数据
	 * @param <X> 错误数据类型
	 * @return 错误返回结果
	 */
	public static <X> JSONResult<X> error(X data) {
		return error(data, ResultEnum.UNKNOWN);
	}

	/**
	 * 错误返回结果
	 * @param data 错误数据
	 * @param resultEnum 错误信息枚举
	 * @param <X> 错误数据类型
	 * @return 错误返回结果
	 */
	public static <X> JSONResult<X> error(X data, ResultEnum resultEnum) {
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
	public static <X> JSONResult<X> error(X data, String code, String message) {
		return new JSONResult<>(data, false, code, message);
	}

	@AllArgsConstructor
	@Getter
	public enum ResultEnum {

		/**
		 * 成功
		 */
		SUCCESS("2000", "OK"),

		/**
		 * 参数非法
		 */
		ILLEGAL_ARGUMENT("4001", "参数非法"),

		/**
		 * 无权访问
		 */
		ILLEGAL_AUTHORITY("4011", "无权访问"),

		/**
		 * 通用service异常
		 */
		SERVICE_ERROR("9998", "后台异常"),

		/**
		 * 未知的错误
		 */
		UNKNOWN("9999", "未知的错误"),

		/**
		 * 登录失败
		 */
		LOGIN_ERROR("4012", "登录失败");

		/**
		 * 返回码
		 */
		private final String code;

		/**
		 * 返回信息
		 */
		private final String message;
	}

}
