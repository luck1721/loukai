package com.example.demo.bll.domain.routing;

/**
 * 路由规则
 * 
 * @created 2017年1月13日
 * @author  huanglj
 */
@FunctionalInterface
public interface RoutingRule<S, D> {

	/**
	 * 路由到目的地
	 * 
	 * @param source 路由源
	 * @return
	 * @created 2017年1月13日
	 * @author  huanglj
	 */
	public D routedToDestination(S source);

}
