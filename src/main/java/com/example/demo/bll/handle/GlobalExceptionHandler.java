package com.example.demo.bll.handle;

import com.example.demo.bll.config.JSONResult;
import com.example.demo.bll.exception.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 * @author lk
 * @date 2021/1/18
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(Exception.class)
	public JSONResult handleException(Exception e) {
		if (e instanceof ApplicationException) {
			logger.error("错误信息",e);
			return JSONResult.error(null, ((ApplicationException) e).getErrorCode()+"", e.getMessage());
		}
		if (e instanceof Exception) {
			logger.error("错误信息",e);
			return  JSONResult.error(e.getMessage(), JSONResult.ResultEnum.SERVICE_ERROR);
		}
		return null;
	}
}
