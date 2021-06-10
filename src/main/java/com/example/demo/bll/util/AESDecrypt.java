package com.example.demo.bll.util;

import com.example.demo.bll.anon.SensitiveField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * @author lk
 * @date 2021/4/12
 */
@Component
public class AESDecrypt implements DecryptUtil {

	/**
	 * 解密
	 *
	 * @param result resultType的实例
	 * @return T
	 * @throws IllegalAccessException 字段不可访问异常
	 */
	@Override
	public <T> T decrypt(T result) throws IllegalAccessException {
		//取出resultType的类
		Class<?> resultClass = result.getClass();
		Field[] declaredFields = resultClass.getDeclaredFields();
		for (Field field : declaredFields) {
			//取出所有被EncryptDecryptField注解的字段
			SensitiveField sensitiveField = field.getAnnotation(SensitiveField.class);
			if (!Objects.isNull(sensitiveField)) {
				field.setAccessible(true);
				Object object = field.get(result);
				//只支持String的解密
				if (object instanceof String) {
					String value = (String) object;
					//对注解的字段进行逐一解密
					field.set(result, AESUtil.decrypt(value,AESUtil.getAssetsDevPwdField()));
				}
			}
		}
		return result;
	}
}
