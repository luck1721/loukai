package com.example.demo.bll.handle;

import com.example.demo.bll.config.JSONResult;
import com.example.demo.bll.exception.ApplicationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 * @author lk
 * @date 2021/1/18
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	public JSONResult handleException(Exception e) {
		if (e instanceof ApplicationException) {
			return JSONResult.error(null, ((ApplicationException) e).getErrorCode()+"", e.getMessage());
		}
		if (e instanceof Exception) {
			return  JSONResult.error(e.getMessage(), JSONResult.ResultEnum.SERVICE_ERROR);
		}
		return null;
	}
}
