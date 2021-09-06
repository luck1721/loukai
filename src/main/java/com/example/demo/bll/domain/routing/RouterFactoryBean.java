package com.example.demo.bll.domain.routing;

import com.example.demo.bll.utils.ClassUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.FactoryBeanNotInitializedException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 路由工厂
 * @author lk
 * @date 2021/1/28
 */
public class RouterFactoryBean<S, D> implements FactoryBean<D>, ApplicationContextAware, InitializingBean {

	private final Class<D> destinationInterface;
	private RoutingRule<S, D> routingRule;
	private String routingRuleBeanName;
	private Class<S> sourceClass;
	private ApplicationContext applicationContext;
	private boolean init;

	@SuppressWarnings("unchecked")
	public RouterFactoryBean() {
		Class<?> actualTypeArgument = ClassUtils.getActualTypeArguments(FactoryBean.class, getClass())[0];
		destinationInterface = actualTypeArgument == Object.class ? null : (Class<D>)actualTypeArgument;
	}

	public RouterFactoryBean(Class<D> destinationInterface) {
		this.destinationInterface = destinationInterface;
	}

	public RoutingRule<S, D> getRoutingRule() {
		return routingRule;
	}

	public void setRoutingRule(RoutingRule<S, D> routingRule) {
		this.routingRule = routingRule;
	}

	public String getRoutingRuleBeanName() {
		return routingRuleBeanName;
	}

	public void setRoutingRuleBeanName(String routingRuleBeanName) {
		this.routingRuleBeanName = routingRuleBeanName;
	}

	public Class<S> getSourceClass() {
		return sourceClass;
	}

	public void setSourceClass(Class<S> sourceClass) {
		this.sourceClass = sourceClass;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(destinationInterface, "Property destinationInterface can't be null!");
		Assert.isTrue(destinationInterface.isInterface(), "Property destinationInterface[" + destinationInterface + "] must be a interface!");
		if(routingRule == null) {
			Assert.notNull(routingRuleBeanName, "Property routingRule and routingRuleBeanName can't all be null!");
		} else {
			init = true;
		}
	}

	@SuppressWarnings("unchecked")
	private void checkInit() {
		if (destinationInterface == null) {
			throw new FactoryBeanNotInitializedException();
		}
		if(init) {
			return;
		}
		routingRule = applicationContext.getBean(routingRuleBeanName, RoutingRule.class);
		Assert.notNull(routingRule, "Property routingRuleBeanName invalid!");
		init = true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public D getObject() throws Exception {
		checkInit();
		return (D) Proxy.newProxyInstance(destinationInterface.getClassLoader(), new Class[] { destinationInterface }, new InvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				if(method.getDeclaringClass() == Object.class) {
					if("equals".equals(method.getName()) && args != null && args.length == 1) {
						return proxy == args[0];
					}
					return method.invoke(RouterFactoryBean.this, args);
				}
				D destination = routingRule.routedToDestination(extractSource((D)proxy, method, args));
				Assert.notNull(destination, "Can't route valid destination!");
				Assert.notNull(destinationInterface.isInstance(destination), "Destination[" + destination + "] must be instance of " + destinationInterface + "!");
				try {
					return method.invoke(destination, args);
				} catch (InvocationTargetException e) {
					throw e.getTargetException();
				}
			}
		});
	}

	@Override
	public Class<D> getObjectType() {
		return destinationInterface;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	/**
	 * 获取路由源，子类可重写
	 *
	 * @param router
	 * @param method
	 * @param args
	 * @return
	 * @created 2017年1月15日
	 * @author  huanglj
	 */
	@SuppressWarnings("unchecked")
	protected S extractSource(D router, Method method, Object[] args) {
		if(sourceClass != null && args != null && args.length > 0) {
			for (Object arg : args) {
				if(sourceClass.isInstance(arg)) {
					return (S)arg;
				}
			}
		}
		return (S) RoutingContext.getInstance(router).getSource();
	}

}

