package com.example.demo.bll.web;

import cn.com.citycloud.hcs.common.domain.converter.StringToDateConverter;
import cn.com.citycloud.hcs.common.domain.converter.StringToDateTimeConverter;
import com.example.demo.bll.interceptor.LogsAllInterceptor;
import com.example.demo.bll.interceptor.MyInterceptor;
import com.example.demo.bll.interceptor.ResponseResultInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.context.request.async.TimeoutCallableProcessingInterceptor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    /*@Resource
    private AutoIdempotentInterceptor autoIdempotentInterceptor;*/


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
		ResponseResultInterceptor responseResultInterceptor = new ResponseResultInterceptor();
        registry.addInterceptor(logsAllInterceptor()).addPathPatterns("/api/**");
		registry.addInterceptor(myInterceptor()).addPathPatterns("/async");
		registry.addInterceptor(responseResultInterceptor);
    }

	/**
	 * 对前端传过来的时间戳变为date
	 * @param registry
	 */
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new StringToDateConverter());
		registry.addConverter(new StringToDateTimeConverter());
	}

	@Bean("logsAllInterceptor")
	public LogsAllInterceptor logsAllInterceptor(){
		return new LogsAllInterceptor();
	}

	@Bean("myInterceptor")
	public MyInterceptor myInterceptor(){
		return new MyInterceptor();
	}

	@Override
	public void configureAsyncSupport(final AsyncSupportConfigurer configurer) {
		configurer.setDefaultTimeout(20000);
		configurer.registerCallableInterceptors(timeoutInterceptor());
	}
	@Bean
	public TimeoutCallableProcessingInterceptor timeoutInterceptor() {
		return new TimeoutCallableProcessingInterceptor();
	}

}
