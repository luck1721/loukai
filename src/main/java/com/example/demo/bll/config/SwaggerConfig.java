package com.example.demo.bll.config;

import cn.com.citycloud.hcs.common.web.DataType;
import cn.com.citycloud.hcs.common.web.ParamType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;

/**
 * @author lk
 * @date 2021/1/19
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket createRestApi(){
		Docket docket = new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo());
		docket.globalOperationParameters(Arrays.asList(new ParameterBuilder().name("Access-Token")
				.description("授权Token").modelRef(new ModelRef(DataType.string)).parameterType(ParamType.header).allowEmptyValue(true).build()));
		return docket.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any()).build();
	}

	private ApiInfo apiInfo(){
		return new ApiInfoBuilder()
				.title("SpringBoot API Doc")
				.description("This is a restful api document of Spring Boot.")
				.version("1.0")
				.build();
	}

}
