package com.example.demo.bll.config;

import com.example.demo.bll.listener.ShiroSessionListener;
import com.example.demo.bll.shiro.CybSimpleCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

	@Bean
	public CybSimpleCredentialsMatcher cybHashedCredentialsMatcher() {
		CybSimpleCredentialsMatcher cybSimpleCredentialsMatcher = new CybSimpleCredentialsMatcher();

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
	public SecurityManager securityManager(CybSimpleCredentialsMatcher cybSimpleCredentialsMatcher) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(myShiroRealm(cybSimpleCredentialsMatcher));
		return securityManager;
	}

	@Bean
	public ShiroSessionListener shiroSessionListener() {
		ShiroSessionListener shiroSessionListener = new ShiroSessionListener();
		return shiroSessionListener;
	}

	@Bean
	public SessionManager sessionManager(){
		DefaultSessionManager shiroSessionManager = new DefaultSessionManager();
		//这里可以不设置。Shiro有默认的session管理。如果缓存为Redis则需改用Redis的管理
		Collection<SessionListener> listeners = new ArrayList<>();
		listeners.add(shiroSessionListener());
		return shiroSessionManager;
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
		filterMap.put("/webjars/**", "anon");
		filterMap.put("/v2/**", "anon");
		filterMap.put("/ssoLogin", "anon");
		filterMap.put("/oauth/**", "anon");
		filterMap.put("/file/**", "anon");
		// 对所有用户认证
		filterMap.put("/**", "authc");
		// 登录
		shiroFilterFactoryBean.setLoginUrl("/login");
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
