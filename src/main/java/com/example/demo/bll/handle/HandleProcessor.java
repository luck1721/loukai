package com.example.demo.bll.handle;

import com.example.demo.bll.anon.HandleType;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 策略模式
 * @author lk
 * @date 2020/4/30
 */
@Component
public class HandleProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        Map<String,Class> map = new HashMap<>();
        //ClassPathScanningCandidateComponentProvider是Spring提供的工具，可以按自定义的类型，查找classpath下符合要求的class文件。
        // true：默认TypeFilter生效，这种模式会查询出许多不符合你要求的class名
        //获取指定包下注解为HandleType的类
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false); // 不使用默认的TypeFilter
        provider.addIncludeFilter(new AnnotationTypeFilter(HandleType.class));
        Set<BeanDefinition> beanDefinitionSet = provider.findCandidateComponents("com.example.demo.bll.service.impl");
        for (BeanDefinition bean : beanDefinitionSet) {
            try {
                //通过反射获取实例
                Class clazz = Class.forName(bean.getBeanClassName());
                HandleType type = (HandleType)clazz.getAnnotation(HandleType.class);
                String key = type.value();
                map.put(key,clazz);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        HandleContext handle = new HandleContext(map);
        beanFactory.registerSingleton(HandleContext.class.getName(),handle);
    }
}
