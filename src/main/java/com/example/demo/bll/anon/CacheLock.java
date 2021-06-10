package com.example.demo.bll.anon;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheLock {
	String lockedPrefix() default "";   //redis 锁key的前缀
	long expireTime() default 10;
}
