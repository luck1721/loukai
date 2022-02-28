package com.example.demo.bll.utils;

import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.engine.EngineFileType;
import cn.smallbun.screw.core.engine.EngineTemplateType;
import cn.smallbun.screw.core.execute.DocumentationExecute;
import cn.smallbun.screw.core.process.ProcessConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.util.ArrayList;

/**
 * @author lk
 * @date 2021/4/13
 */
public class DocumentGenerationUtils {

	/**
	 * 文档生成
	 */
	static void documentGeneration() {
		//数据源
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
		///hikariConfig.setJdbcUrl("jdbc:mysql://10.12.107.157:3306/zhuzhou?useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8");
		hikariConfig.setJdbcUrl("jdbc:mysql://10.12.107.157:3306/zhuzhou_cockpit?useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8");
		hikariConfig.setUsername("root");
		hikariConfig.setPassword("Admin_cci@123");
		//hikariConfig.setPassword("cci_Zhuzhou_@123");
		//设置可以获取tables remarks信息
		hikariConfig.addDataSourceProperty("useInformationSchema", "true");
		hikariConfig.setMinimumIdle(2);
		hikariConfig.setMaximumPoolSize(5);
		DataSource dataSource = new HikariDataSource(hikariConfig);
		//生成配置
		EngineConfig engineConfig = EngineConfig.builder()
				//生成文件路径
				.fileOutputDir("d:/新桌面")
				//打开目录
				.openOutputDir(true)
				//文件类型
				.fileType(EngineFileType.HTML)
				//生成模板实现
				.produceType(EngineTemplateType.freemarker)
				//自定义文件名称
				.fileName("数据库表结构").build();

		//忽略表
		ArrayList<String> ignoreTableName = new ArrayList<>();
		ignoreTableName.add("test_user");
		ignoreTableName.add("test_group");
		//忽略表前缀
		ArrayList<String> ignorePrefix = new ArrayList<>();
		ignorePrefix.add("test_");
		//忽略表后缀
		ArrayList<String> ignoreSuffix = new ArrayList<>();
		ignoreSuffix.add("_test");
		//ArrayList<String> tablePrefix = new ArrayList<>();
		//tablePrefix.add("sp");
		ProcessConfig processConfig = ProcessConfig.builder()
				//指定生成逻辑、当存在指定表、指定表前缀、指定表后缀时，将生成指定表，其余表不生成、并跳过忽略表配置
				//根据名称指定表生成
				.designatedTableName(new ArrayList<>())
				//根据表前缀生成
				//.designatedTablePrefix(tablePrefix)
				//根据表后缀生成
				.designatedTableSuffix(new ArrayList<>())
				//忽略表名
				.ignoreTableName(ignoreTableName)
				//忽略表前缀
				.ignoreTablePrefix(ignorePrefix)
				//忽略表后缀
				.ignoreTableSuffix(ignoreSuffix).build();
		//配置
		Configuration config = Configuration.builder()
				//版本
				.version("1.0.0")
				//描述
				.description("数据库设计文档生成")
				//数据源
				.dataSource(dataSource)
				//生成配置
				.engineConfig(engineConfig)
				//生成配置
				.produceConfig(processConfig)
				.build();
		//执行生成
		new DocumentationExecute(config).execute();
	}

	public static void main(String[] args) {
		//DocumentGenerationUtils.documentGeneration();
		String message="404021000100130B1105192A02000000000000000000000100010001020C002323";
		String lastChar = message.substring(0,message.length() -4);
		String data[] = message.split("2323");
		System.out.println(lastChar);
	}
}
