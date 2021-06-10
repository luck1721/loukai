/*
package com.example.demo.processor;

import com.example.demo.web.controller.BaseController;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

*/
/**
 * bean定义后置处理器
 * @author lk
 * @date 2020/5/22
 *//*

public class LkBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {
	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
		AbstractBeanDefinition rdb = BeanDefinitionBuilder.rootBeanDefinition(BaseController.class).getBeanDefinition();
		beanDefinitionRegistry.registerBeanDefinition("hello",rdb);
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

	}
}
*/
