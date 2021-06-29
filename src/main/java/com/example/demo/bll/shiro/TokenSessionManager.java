package com.example.demo.bll.shiro;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * @author lk
 * @date 2021/6/25
 */
public class TokenSessionManager extends DefaultWebSessionManager {
	private static final Logger log = LoggerFactory.getLogger(TokenSessionManager.class);

	@Override
	protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
		String accessToken = ((HttpServletRequest)request).getHeader("Access-Token");
		if(StringUtils.isBlank(accessToken)) {
			return null;
		}
		try {
			return new String(Base64.decodeBase64(accessToken), "UTF-8");
		} catch (Exception e) {
			try {
				return new String(Base64.decodeBase64(accessToken));
			} catch (Exception ex) {
				log.error("无效的Token：" + accessToken + "！", ex);
				return null;
			}
		}
	}

}
