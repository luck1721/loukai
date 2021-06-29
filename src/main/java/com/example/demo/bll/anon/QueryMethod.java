package com.example.demo.bll.anon;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface QueryMethod {
	/**
	 * 字段名
	 */
	String field() default "";

	/**
	 * 匹配方式
	 */
	String method() default "";
}
