package com.example.demo.bll.handle;

import cn.hutool.json.JSONUtil;
import com.example.demo.bll.anon.ResponseResult;
import com.example.demo.bll.config.JSONResult;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 响应结果封装
 * @author lk
 * @date 2021/9/29
 */
@ControllerAdvice
public class ResponseResultHandler implements ResponseBodyAdvice<Object> {

	// 标记名称
	public static final String RESPONSE_RESULT_ANN = "RESPONSE-RESULT-ANN";


	// 判断是否要执行 beforeBodyWrite 方法，true为执行，false不执行，有注解标记的时候处理返回值
	@Override
	public boolean supports(MethodParameter arg0, Class<? extends HttpMessageConverter<?>> arg1) {
		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = sra.getRequest();
		// 判断请求是否有包装标记
		ResponseResult responseResultAnn = (ResponseResult) request.getAttribute(RESPONSE_RESULT_ANN);
		return responseResultAnn == null ? false : true;
	}

	// 对返回值做包装处理
	@Override
	public Object beforeBodyWrite(Object body, MethodParameter arg1, MediaType arg2,
								  Class<? extends HttpMessageConverter<?>> arg3, ServerHttpRequest arg4, ServerHttpResponse arg5) {
		if (body instanceof JSONResult) {
			return body;
		}else if (body instanceof String) {
			return JSONUtil.toJsonStr(JSONResult.ok(body));
		}
		return JSONResult.ok(body);
	}
}
