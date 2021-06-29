package com.example.demo.bll.generator;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author lk
 * @date 2021/6/22
 */
public class MysqlGenerator {
	public static String scanner(String tip) {
		Scanner scanner = new Scanner(System.in);
		StringBuilder help = new StringBuilder();
		help.append("请输入" + tip + "：");
		System.out.println(help.toString());
		if (scanner.hasNext()) {
			String ipt = scanner.next();
			if (StringUtils.isNotEmpty(ipt)) {
				return ipt;
			}
		}
		throw new MybatisPlusException("请输入正确的" + tip + "！");
	}

	public static void main(String[] args) {
		// 代码生成器
		AutoGenerator mpg = new AutoGenerator();

		// 全局配置
		GlobalConfig gc = new GlobalConfig();
		String projectPath = "D:/demo/springboot-demo";
		gc.setOutputDir(projectPath + "/src/main/java");
		gc.setAuthor("loukai");
		gc.setOpen(false);
		gc.setBaseResultMap(true);
		gc.setBaseColumnList(true);
		//gc.setControllerName("SSSSScontroller");
		// 是否覆盖已有文件
		gc.setFileOverride(true);
		mpg.setGlobalConfig(gc);

		// 数据源配置
		DataSourceConfig dsc = new DataSourceConfig();
		dsc.setUrl("jdbc:mysql://localhost:3306/springboot?useUnicode=true&characterEncoding=utf8&useSSL=true&zeroDateTimeBehavior=convertToNull&serverTimezone=Asia/Shanghai");
		// dsc.setSchemaName("public");
		dsc.setDriverName("com.mysql.jdbc.Driver");
		dsc.setUsername("root");
		dsc.setPassword("123456");
		mpg.setDataSource(dsc);

		// 包配置
		PackageConfig pc = new PackageConfig();
		//pc.setModuleName(scanner("模块名"));
		pc.setParent(null);
		// 这个地址是生成的配置文件的包路径
		pc.setEntity("com.example.demo.bll.entity");
		pc.setService("com.example.demo.bll.service");
		pc.setServiceImpl("com.example.demo.bll.service.impl");
		pc.setController("com.example.demo.web.controller");
		pc.setMapper("com.example.demo.bll.mapper");
		mpg.setPackageInfo(pc);
		// 自定义配置
		InjectionConfig cfg = new InjectionConfig() {
			@Override
			public void initMap() {
				// to do nothing
			}
		};

		// 如果模板引擎是 freemarker
		String templatePath = "/templates/mapper.xml.ftl";
		// 如果模板引擎是 velocity
		//String templatePath = "/templates/mapper.xml.vm";

		// 自定义输出配置
		List<FileOutConfig> focList = new ArrayList<>();
		// 自定义配置会被优先输出
		focList.add(new FileOutConfig(templatePath) {
			@Override
			public String outputFile(TableInfo tableInfo) {
				// 自定义输出文件名
				return projectPath + "/src/main/resources/mapper"
						+ "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
			}
		});

		cfg.setFileOutConfigList(focList);
		mpg.setCfg(cfg);

		// 配置模板
		TemplateConfig templateConfig = new TemplateConfig();

//		 //配置自定义输出模板
		// 不需要其他的类型时，直接设置为null就不会成对应的模版了
		//templateConfig.setEntity("...");
		//templateConfig.setService(null);
		//templateConfig.setController(null);
		//templateConfig.setServiceImpl(null);
        // 自定义模板配置，可以 copy 源码 mybatis-plus/src/main/resources/templates 下面内容修改，
		// 放置自己项目的 src/main/resources/templates 目录下, 默认名称一下可以不配置，也
		// 可以自定义模板名称 只要放到目录下，名字不变 就会采用这个模版 下面这句有没有无所谓
		// 模版去github上看地址：
		/**https://github.com/baomidou/mybatis-plus/tree/3.0/mybatis-plus-generator/src/main/resources/templates*/
		//templateConfig.setEntity("/templates/entity.java");
		templateConfig.setXml(null);
		mpg.setTemplate(templateConfig);

		// 策略配置
		StrategyConfig strategy = new StrategyConfig();
		strategy.setNaming(NamingStrategy.underline_to_camel);
		strategy.setColumnNaming(NamingStrategy.underline_to_camel);
		//strategy.setSuperEntityClass("com.example.demo.bll.entity.BaseEntity");
		strategy.setSuperMapperClass("com.baomidou.mybatisplus.core.mapper.BaseMapper");
		strategy.setSuperServiceClass("com.baomidou.mybatisplus.extension.service.IService");
		strategy.setSuperServiceImplClass("com.baomidou.mybatisplus.extension.service.impl.ServiceImpl");
		strategy.setEntityLombokModel(false);
		//strategy.setRestControllerStyle(false);
		//strategy.setSuperControllerClass("com.cikers.ps.controller.MysqlController");
		strategy.setInclude(scanner("表名"));
		// 设置继承的父类字段
		//strategy.setSuperEntityColumns("id","modifiedBy","modifiedOn","createdBy","createdOn");
		//strategy.setControllerMappingHyphenStyle(true);
		//strategy.setTablePrefix(pc.getModuleName() + "_");
		mpg.setStrategy(strategy);
		mpg.setTemplateEngine(new FreemarkerTemplateEngine());
		mpg.execute();
	}

}
