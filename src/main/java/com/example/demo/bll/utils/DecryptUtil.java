package com.example.demo.bll.utils;

/**
 * @author lk
 * @date 2021/4/12
 */
public interface DecryptUtil {
	/**
	 * 解密
	 *
	 * @param result resultType的实例
	 * @return T
	 * @throws IllegalAccessException 字段不可访问异常
	 */
	<T> T decrypt(T result) throws IllegalAccessException;
}
