package com.example.demo.bll.config;

import cn.com.citycloud.hcs.common.auth.MyCacheManager;
import com.example.demo.bll.listener.ShiroSessionListener;
import com.example.demo.bll.shiro.CybSimpleCredentialsMatcher;
import com.example.demo.bll.shiro.TokenSessionManager;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lk
 * @date 2021/1/19
 */
@Configuration
public class ShiroConfig {

	@Configuration
	@ConfigurationProperties("auth.credential")
	public static class AuthCredentialProperties {
		/** 加密算法 */
		private String hashAlgorithm = Md5Hash.ALGORITHM_NAME;
		/** 加密次数 */
		private int hashIterations = 1;
		/** 盐值 */
		private boolean hashSalted;
		/** 十六进制编码 */
		private boolean hexEncoded = true;

		public String getHashAlgorithm() {
			return hashAlgorithm;
		}

		public void setHashAlgorithm(String hashAlgorithm) {
			this.hashAlgorithm = hashAlgorithm;
		}

		public int getHashIterations() {
			return hashIterations;
		}

		public void setHashIterations(int hashIterations) {
			this.hashIterations = hashIterations;
		}

		public boolean isHashSalted() {
			return hashSalted;
		}

		public void setHashSalted(boolean hashSalted) {
			this.hashSalted = hashSalted;
		}

		public boolean isHexEncoded() {
			return hexEncoded;
		}

		public void setHexEncoded(boolean hexEncoded) {
			this.hexEncoded = hexEncoded;
		}
	}

	@Configuration
	@ConfigurationProperties("auth.session")
	public static class AuthSessionProperties {
		/** sessionId在cookie中的名称 */
		private String name = "AUTHSESSIONID";
		/** session在cache中的名称 */
		private String cacheName = "webSessionCache";
		/** session超时时间 */
		private Duration timeout = Duration.ofMinutes(30);
		/** 校验session间隔时间 */
		private Duration validationInterval = Duration.ofMinutes(30);
		/** 是否启用session校验 */
		private boolean validationEnabled;
		/** 是否删除无效的session */
		private boolean deleteInvalid = true;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getCacheName() {
			return cacheName;
		}

		public void setCacheName(String cacheName) {
			this.cacheName = cacheName;
		}

		public Duration getTimeout() {
			return timeout;
		}

		public void setTimeout(Duration timeout) {
			this.timeout = timeout;
		}

		public Duration getValidationInterval() {
			return validationInterval;
		}

		public void setValidationInterval(Duration validationInterval) {
			this.validationInterval = validationInterval;
		}

		public boolean isValidationEnabled() {
			return validationEnabled;
		}

		public void setValidationEnabled(boolean validationEnabled) {
			this.validationEnabled = validationEnabled;
		}

		public boolean isDeleteInvalid() {
			return deleteInvalid;
		}

		public void setDeleteInvalid(boolean deleteInvalid) {
			this.deleteInvalid = deleteInvalid;
		}
	}

	@Autowired
	private AuthCredentialProperties credentialProperties;
	@Autowired
	private AuthSessionProperties sessionProperties;

	@Bean
	public CybSimpleCredentialsMatcher cybHashedCredentialsMatcher() {
		CybSimpleCredentialsMatcher cybSimpleCredentialsMatcher = new CybSimpleCredentialsMatcher();
		//MD5加密
		cybSimpleCredentialsMatcher.setHashAlgorithmName(credentialProperties.hashAlgorithm);
		cybSimpleCredentialsMatcher.setHashIterations(credentialProperties.hashIterations);
		cybSimpleCredentialsMatcher.setHashSalted(credentialProperties.isHashSalted());
		cybSimpleCredentialsMatcher.setStoredCredentialsHexEncoded(credentialProperties.isHexEncoded());
		return cybSimpleCredentialsMatcher;
	}

	// 将自己的验证方式加入容器
	@Bean
	public MyShiroRealm myShiroRealm(CybSimpleCredentialsMatcher cybSimpleCredentialsMatcher) {
		MyShiroRealm myShiroRealm = new MyShiroRealm();
		myShiroRealm.setCredentialsMatcher(cybSimpleCredentialsMatcher);
		return myShiroRealm;
	}

	// 权限管理，配置主要是Realm的管理认证
	@Bean
	public SecurityManager securityManager(MyCacheManager myCacheManager, CybSimpleCredentialsMatcher cybSimpleCredentialsMatcher) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(myShiroRealm(cybSimpleCredentialsMatcher));
		securityManager.setCacheManager(myCacheManager);
		securityManager.setSessionManager(sessionManager());
		return securityManager;
	}

	@Bean
	public ShiroSessionListener shiroSessionListener() {
		ShiroSessionListener shiroSessionListener = new ShiroSessionListener();
		return shiroSessionListener;
	}

	@Bean
	public SessionIdGenerator sessionIdGenerator() {
		return new JavaUuidSessionIdGenerator();
	}

	/**
	 * 配置保存sessionId的cookie
	 * 注意：这里的cookie 不是上面的记住我 cookie 记住我需要一个cookie session管理 也需要自己的cookie
	 * @return
	 */
	@Bean("sessionIdCookie")
	public SimpleCookie sessionIdCookie(){
		//这个参数是cookie的名称
		SimpleCookie simpleCookie = new SimpleCookie("sid");
		//setcookie的httponly属性如果设为true的话，会增加对xss防护的安全系数。它有以下特点：

		//setcookie()的第七个参数
		//设为true后，只能通过http访问，javascript无法访问
		//防止xss读取cookie
		simpleCookie.setHttpOnly(true);
		simpleCookie.setPath("/");
		//maxAge=-1表示浏览器关闭时失效此Cookie
		simpleCookie.setMaxAge(-1);
		return simpleCookie;
	}

	@Bean
	public SessionDAO sessionDAO() {
		EnterpriseCacheSessionDAO enterpriseCacheSessionDAO = new EnterpriseCacheSessionDAO();
		//使用ehCacheManager
		//enterpriseCacheSessionDAO.setCacheManager(ehCacheManager());
		//设置session缓存的名字 默认为 shiro-activeSessionCache
		//enterpriseCacheSessionDAO.setActiveSessionsCacheName("shiro-activeSessionCache");
		//sessionId生成器
		enterpriseCacheSessionDAO.setSessionIdGenerator(sessionIdGenerator());
		return enterpriseCacheSessionDAO;
	}

	@Bean
	public MyCacheManager myCacheManager(@Autowired(required = false) @Qualifier("cacheManager") CacheManager cacheManager) {
		if (cacheManager == null) {
			cacheManager = new ConcurrentMapCacheManager();
		}
		return new MyCacheManager(cacheManager);
	}

	@Bean
	public SessionManager sessionManager(){
		/*DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		//这里可以不设置。Shiro有默认的session管理。如果缓存为Redis则需改用Redis的管理
		sessionManager.setGlobalSessionTimeout(24*60*60*1000);
		sessionManager.setDeleteInvalidSessions(true);
		sessionManager.setSessionValidationSchedulerEnabled(true);
		sessionManager.setSessionIdUrlRewritingEnabled(false);*/
		TokenSessionManager sessionManager = new TokenSessionManager();
		sessionManager.setGlobalSessionTimeout(sessionProperties.getTimeout().toMillis());
		sessionManager.setSessionValidationInterval(sessionProperties.getValidationInterval().toMillis());
		sessionManager.setSessionValidationSchedulerEnabled(sessionProperties.isValidationEnabled());
		sessionManager.setDeleteInvalidSessions(sessionProperties.isDeleteInvalid());
		//sessionManager.setSessionIdCookie(new SimpleCookie(sessionProperties.getName()));
		Collection<SessionListener> listeners = new ArrayList<>();
		listeners.add(shiroSessionListener());
		sessionManager.setSessionIdCookie(sessionIdCookie());
		sessionManager.setSessionListeners(listeners);
		sessionManager.setSessionDAO(sessionDAO());
		return sessionManager;
	}

	// Filter工厂，设置对应的过滤条件和跳转条件
	@Bean
	public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		Map<String, String> filterMap = new HashMap<String, String>();
		// 登出
		//filterMap.put("/logout", "logout");
		// swagger
		filterMap.put("/swagger**/**", "anon");
		filterMap.put("/doc**", "anon");
		filterMap.put("/webjars/**", "anon");
		filterMap.put("/v2/**", "anon");
		filterMap.put("/api/ssoLogin", "anon");
		filterMap.put("/api/user_sync", "anon");
		filterMap.put("/oauth/**", "anon");
		filterMap.put("/file/**", "anon");
		// 对所有用户认证
		filterMap.put("/**", "authc");
		// 登录
		shiroFilterFactoryBean.setLoginUrl("/api/login");
		// 首页
		shiroFilterFactoryBean.setSuccessUrl("/index");
		// 错误页面，认证不通过跳转
		shiroFilterFactoryBean.setUnauthorizedUrl("/error");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
		return shiroFilterFactoryBean;
	}

	// 加入注解的使用，不加入这个注解不生效
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}

}
