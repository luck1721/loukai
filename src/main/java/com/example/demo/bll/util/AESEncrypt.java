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
public class AESEncrypt implements EncryptUtil {


	/**
	 * 加密
	 *
	 * @param declaredFields paramsObject所声明的字段
	 * @param paramsObject   mapper中paramsType的实例
	 * @return T
	 * @throws IllegalAccessException 字段不可访问异常
	 */
	@Override
	public <T> T encrypt(Field[] declaredFields, T paramsObject) throws IllegalAccessException {
		for (Field field : declaredFields) {
			//取出所有被EncryptDecryptField注解的字段
			SensitiveField sensitiveField = field.getAnnotation(SensitiveField.class);
			if (!Objects.isNull(sensitiveField)) {
				field.setAccessible(true);
				Object object = field.get(paramsObject);
				//暂时只实现String类型的加密
				if (object instanceof String) {
					String value = (String) object;
					//加密  这里我使用自定义的AES加密工具
					field.set(paramsObject, AESUtil.encrypt(value,AESUtil.getAssetsDevPwdField()));
				}
			}
		}
		return paramsObject;
	}
}
