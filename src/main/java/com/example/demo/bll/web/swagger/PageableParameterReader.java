package com.example.demo.bll.web.swagger;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelReference;
import springfox.documentation.schema.ResolvedTypes;
import springfox.documentation.schema.TypeNameExtractor;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.contexts.ModelContext;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spi.service.contexts.ParameterContext;

import java.util.ArrayList;
import java.util.List;

import static springfox.documentation.spi.schema.contexts.ModelContext.inputParam;

/**
 * 分页的情况下swagger上增加当前页数、页数大小、排序参数
 * @author lk
 * @date 2021/6/22
 */
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class PageableParameterReader implements OperationBuilderPlugin {
	private final TypeNameExtractor nameExtractor;
	private final TypeResolver resolver;
	private final ResolvedType pageableType;

	@Autowired
	public PageableParameterReader(TypeNameExtractor nameExtractor, TypeResolver resolver) {
		this.nameExtractor = nameExtractor;
		this.resolver = resolver;
		this.pageableType = resolver.resolve(Page.class);
	}

	@Override
	public void apply(OperationContext context) {
		List<ResolvedMethodParameter> methodParameters = context.getParameters();
		List<Parameter> parameters = new ArrayList();

		for (ResolvedMethodParameter methodParameter : methodParameters) {
			ResolvedType resolvedType = methodParameter.getParameterType();

			if (pageableType.equals(resolvedType)) {
				ParameterContext parameterContext = new ParameterContext(methodParameter,
						new ParameterBuilder(),
						context.getDocumentationContext(),
						context.getGenericsNamingStrategy(),
						context);
				Function<ResolvedType, ? extends ModelReference> factory = createModelRefFactory(parameterContext);

				ModelReference intModel = factory.apply(resolver.resolve(Integer.TYPE));
				ModelReference stringModel = factory.apply(resolver.resolve(List.class, String.class));

				parameters.add(new ParameterBuilder()
						.parameterType("query")
						.name("current")
						.modelRef(intModel)
						.description("Results page you want to retrieve (0..N)").build());
				parameters.add(new ParameterBuilder()
						.parameterType("query")
						.name("size")
						.modelRef(intModel)
						.description("Number of records per page").build());
				parameters.add(new ParameterBuilder()
						.parameterType("query")
						.name("orders")
						.modelRef(stringModel)
						.allowMultiple(true)
						.description("Sorting criteria in the format: property(,asc|desc). "
								+ "Default sort order is ascending. "
								+ "Multiple sort criteria are supported.")
						.build());
				context.operationBuilder().parameters(parameters);
			}
		}
	}

	@Override
	public boolean supports(DocumentationType delimiter) {
		return true;
	}

	private Function<ResolvedType, ? extends ModelReference> createModelRefFactory(ParameterContext context) {
		ModelContext modelContext = inputParam(
				context.getGroupName(),
				context.resolvedMethodParameter().getParameterType(),
				context.getDocumentationType(),
				context.getAlternateTypeProvider(),
				context.getGenericNamingStrategy(),
				context.getIgnorableParameterTypes());
		return ResolvedTypes.modelRefFactory(modelContext, nameExtractor);
	}
}
