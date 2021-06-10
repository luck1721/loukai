package com.example.demo.bll.domain.routing;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 路由上下文
 * 
 * @created 2017年1月13日
 * @author  huanglj
 */
public class RoutingContext<S> {

	@SuppressWarnings("rawtypes")
	private static final Map<Object, RoutingContext> instanceCache = new ConcurrentHashMap<>();

	/**
	 * 根据router对象获取RoutingContext实例
	 * 
	 * @param router
	 * @return
	 * @created 2017年1月15日
	 * @author  huanglj
	 */
	@SuppressWarnings("unchecked")
	public static <S, D> RoutingContext<S> getInstance(D router) {
		RoutingContext<S> holder = (RoutingContext<S>)instanceCache.get(router);
		if(holder == null) {
			synchronized (instanceCache) {
				holder = (RoutingContext<S>)instanceCache.get(router);
				if(holder == null) {
					holder = new RoutingContext<>();
					instanceCache.put(router, holder);
				}
			}
		}
		return holder;
	}

	private final ThreadLocal<S> sourceHolder = new ThreadLocal<>();

	/**
	 * 获取源(包内部调用)
	 * 
	 * @return
	 * @created 2017年1月15日
	 * @author  huanglj
	 */
	public S getSource() {
		return sourceHolder.get();
	}

	/**
	 * 设置源(路由前调用)
	 * 
	 * @param source
	 * @created 2017年1月15日
	 * @author  huanglj
	 */
	public void setSource(S source) {
		sourceHolder.set(source);
	}

	/**
	 * 移除源(路由后调用)
	 * 
	 * @param source
	 * @created 2017年1月19日
	 * @author  huanglj
	 */
	public void removeSource() {
		sourceHolder.remove();
	}

}
