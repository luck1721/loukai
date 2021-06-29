package com.example.demo.bll.shiro;

import com.example.demo.bll.enums.LoginType;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

/**
 * @author lk
 * @date 2021/6/1
 */
public class CybSimpleCredentialsMatcher extends HashedCredentialsMatcher {
	@Override
	public boolean doCredentialsMatch(AuthenticationToken authcToken, AuthenticationInfo info) {
		EasyTypeToken tk = (EasyTypeToken) authcToken;
		//如果是免密登录直接返回true
		if(tk.getType().equals(LoginType.NOPASSWD)){
			return true;
		}
		//不是免密登录，调用父类的方法
		return super.doCredentialsMatch(tk, info);
	}
}
