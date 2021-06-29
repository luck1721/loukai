package com.example.demo.bll.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo.bll.entity.SysBizLog;
import com.example.demo.bll.service.SysBizLogService;
import com.example.demo.web.domain.SysLogDTO;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author lk
 * @date 2021/1/20
 */
public class LogsAllInterceptor implements HandlerInterceptor, EnvironmentAware {

	private static Environment environment;

	@Value("${loukai.platform.gatewayLog:false}")
	private Boolean gatewayLog;

	@Autowired(required = false)
	private SysBizLogService bizLogService;

	/**
	 * 请求开始时间
	 */
	private static final String LOGGER_SEND_TIME = "send_time";
	/**
	 * 请求日志实体标识
	 */
	private static final String LOGGER_ENTITY = "_logger_entity";

	private static final String UNKNOWN = "unknown";


	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String userName =  (String) SecurityUtils.getSubject().getPrincipal();

		//创建日志实体类
		//获取session
		//String sessionId = request.getRequestedSessionId();
		SysLogDTO loggerInfos = new SysLogDTO();
		//获取请求路径
		String uri = request.getRequestURI();
		//请求参数信息
		String paramData = JSON.toJSONString(request.getParameterMap(),
				SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteMapNullValue);
		//设置客户端IP
		loggerInfos.setIp(this.getIpAddr(request));

		if(!StringUtils.isEmpty(userName)){
			loggerInfos.setUserName(userName);
		}else{
			loggerInfos.setUserName("免登录");
			loggerInfos.setUserNo("免登录");
		}

		//设置请求方法
		loggerInfos.setFunName(request.getMethod());

		//设置请求地址
		loggerInfos.setReqUrl(uri);

		//设置请求开始时间
		long currentTimeMillis = System.currentTimeMillis();
		loggerInfos.setHappenTime(new Date());

		request.setAttribute(LOGGER_SEND_TIME, currentTimeMillis);
		//设置请求实体到request中，方便afterCompletion方法调用
		request.setAttribute(LOGGER_ENTITY, loggerInfos);
		return true;

	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		String userName =  (String) SecurityUtils.getSubject().getPrincipal();
		//获取请求错误码
		int status = response.getStatus();
		//当前时间
		long currentTimeMillis = System.currentTimeMillis();
		//获取请求开始时间
		long time = (long) request.getAttribute(LOGGER_SEND_TIME);
		//获取请求日志实体
		SysLogDTO loggerInfos = (SysLogDTO) request.getAttribute(LOGGER_ENTITY);
		if(!StringUtils.isEmpty(userName)){
			loggerInfos.setUserName(userName);
			loggerInfos.setUserNo(userName);
		}
		//设置返回时间
		loggerInfos.setActionTime(new Date());
		//设置返回错误码
		loggerInfos.setStatus(Short.valueOf(status + ""));
		//设置返回值
		loggerInfos.setLogMsg(JSON.toJSONString(request.getAttribute("logger_return"),
				SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteMapNullValue));

		//执行将日志写入数据库
		if (gatewayLog && !loggerInfos.getUserName().equals("免登录")) {
			SysBizLog sysBizLog = new SysBizLog();
			BeanUtils.copyProperties(loggerInfos, sysBizLog);
			bizLogService.saveLog(sysBizLog);
		}
	}


	@Override
	public void setEnvironment(Environment environment) {
		LogsAllInterceptor.environment = environment;
	}


	/**
	 * 获取 IP地址
	 * 使用 Nginx等反向代理软件， 则不能通过 request.getRemoteAddr()获取 IP地址
	 * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，
	 * X-Forwarded-For中第一个非 unknown的有效IP字符串，则为真实IP地址
	 */
	public String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
	}

}
