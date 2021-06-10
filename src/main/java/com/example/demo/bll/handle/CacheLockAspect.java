package com.example.demo.bll.handle;

import com.example.demo.bll.anon.CacheLock;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 分布式锁使定时任务在集群环境下只执行一次
 * @author lk
 * @date 2021/3/11
 */

@Aspect
@Slf4j
@Component
public class CacheLockAspect {

	private static final String LOCK_VALUE = "locked";

	@Autowired
	private RedisTemplate redisTemplate;

	@Around("execution(* *.*(..)) && @annotation(com.example.demo.bll.anon.CacheLock)")
	public void cacheLockPoint(ProceedingJoinPoint pjp) throws Exception {
		System.out.println("分布式锁");
		Method cacheMethod = null;
		for (Method method : pjp.getTarget().getClass().getMethods()) {
			if (null != method.getAnnotation(CacheLock.class)) {
				cacheMethod = method;
				break;
			}
		}
		try {
			String lockKey = cacheMethod.getAnnotation(CacheLock.class).lockedPrefix();
			long timeOut = cacheMethod.getAnnotation(CacheLock.class).expireTime();
			if (null == lockKey) {
				throw new Exception("锁名称为空");
			}
			if (redisTemplate.getConnectionFactory().getConnection().setNX(lockKey.getBytes(), LOCK_VALUE.getBytes())) {
				redisTemplate.getConnectionFactory().getConnection().expire(lockKey.getBytes(), timeOut);
				log.info("method:{}获取锁:{},开始运行！", cacheMethod, lockKey);
				pjp.proceed();
				return;
			}
			log.info("method:{}未获取锁:{},运行失败！", cacheMethod, lockKey);
		} catch (Throwable e) {
			log.error("method:{},运行错误！", cacheMethod, e);
			throw new Exception("运行错误");
		}

	}
}
