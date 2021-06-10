package com.example.demo.bll.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;

/**
 * 分布式锁解决并发问题
 * @author lk
 * @date 2020/5/25
 */
public class RedisLock {
	@Autowired
	private StringRedisTemplate redisTemplate;

	/**
	 * 加锁
	 * @param key
	 * @param value 当前时间+超时时间
	 * @return
	 */
	public boolean lock(String key, String value){
		if(redisTemplate.opsForValue().setIfAbsent(key, value)){
			return true;
		}
		String currentValue = redisTemplate.opsForValue().get(key);
		//如果锁过期
		if(!StringUtils.isEmpty(currentValue) && Long.parseLong(currentValue) < System.currentTimeMillis()){
			//获取上一个锁的时间
			String oldValue = redisTemplate.opsForValue().getAndSet(key, value);
			if(!StringUtils.isEmpty(oldValue) && currentValue.equals(oldValue)){
				return true;
			}
		}
		return false;
	}

	/**
	 * 解锁
	 * @param key
	 * @param value 当前时间+超时时间
	 */
	public void unlock(String key, String value){
		try{
			String currentValue = redisTemplate.opsForValue().get(key);
			if(!StringUtils.isEmpty(currentValue) && currentValue.equals(value)){
				redisTemplate.opsForValue().getOperations().delete(key);
			}
		}catch (Exception e){
			System.out.println("【Redis分布式锁】 解锁异常 {}" + e.getMessage());
		}
	}
}
