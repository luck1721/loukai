package com.example.demo.bll.util;

import java.lang.reflect.Field;

/**
 * @author lk
 * @date 2021/4/12
 */
public interface EncryptUtil {

	/**
	 * 加密
	 *
	 * @param declaredFields paramsObject所声明的字段
	 * @param paramsObject   mapper中paramsType的实例
	 * @return T
	 * @throws IllegalAccessException 字段不可访问异常
	 */
	<T> T encrypt(Field[] declaredFields, T paramsObject) throws IllegalAccessException;
}
