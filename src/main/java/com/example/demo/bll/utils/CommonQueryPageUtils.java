package com.example.demo.bll.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.bll.anon.QueryMethod;
import com.example.demo.bll.constant.QueryMethodConstant;
import com.example.demo.web.domain.param.PageParam;
import com.example.demo.web.domain.param.QueryPage;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Locale;

/**
 * 分页查询工具类
 * @author lk
 * @date 2021/6/23
 */
public class CommonQueryPageUtils {
	/**
	 * 正序
	 */
	private static final String ASC = "asc";

	/**
	 * 倒序
	 */
	private static final String DESC = "desc";

	/**
	 * 通用的带排序功能的分页查询
	 */
	public static <T> QueryPage<T> commonQueryPage(PageParam param, QueryWrapper<T> queryWrapper) {
		//构建page
		//根据传入的排序设置order
		//排序字段(格式：字段名:排序方式，字段名:排序方式 （asc正序，desc倒序） 示例:id:desc,age:asc)
		Page<T> page = new Page<>(param.getPage(), param.getLimit());
		String orders = param.getOrders();
		if (StringUtils.isNotBlank(orders)) {
			String[] splitArr = orders.split(",");
			for (String str : splitArr) {
				if (StringUtils.isBlank(str)) {
					continue;
				}
				String[] strArr = str.split(":");
				if (strArr.length != 2 || StringUtils.isBlank(strArr[0]) || StringUtils.isBlank(strArr[1])) {
					continue;
				}
				if (ASC.equals(strArr[1].toLowerCase(Locale.ROOT))) {
					page.addOrder(OrderItem.asc(strArr[0]));
					continue;
				}
				if (DESC.equals(strArr[1].toLowerCase(Locale.ROOT))) {
					page.addOrder(OrderItem.desc(strArr[0]));
				}
			}
		}
		Class<? extends PageParam> clazz = param.getClass();
		if (clazz != PageParam.class) {
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				//设置对象的访问权限，保证对private的属性可以访问
				field.setAccessible(true);
				QueryMethod annotation = field.getAnnotation(QueryMethod.class);
				try {
					//属性没有值则跳过
					if (null == field.get(param)) {
						continue;
					}
					//没有加@QueryMethod 默认属性名为字段名，默认匹配方式为eq
					if (null == annotation) {
						queryWrapper.eq(field.getName(), field.get(param));
						continue;
					}

					switch (annotation.method()) {
						case QueryMethodConstant.EQ:
							queryWrapper.eq(annotation.field(), field.get(param));
							break;
						case QueryMethodConstant.NE:
							queryWrapper.ne(annotation.field(), field.get(param));
							break;
						case QueryMethodConstant.LIKE:
							queryWrapper.like(annotation.field(), field.get(param));
							break;
						case QueryMethodConstant.LIKE_LIFT:
							queryWrapper.likeLeft(annotation.field(), field.get(param));
							break;
						case QueryMethodConstant.LIKE_RIGHT:
							queryWrapper.likeRight(annotation.field(), field.get(param));
							break;
						case QueryMethodConstant.GT:
							queryWrapper.gt(annotation.field(), field.get(param));
							break;
						case QueryMethodConstant.GE:
							queryWrapper.ge(annotation.field(), field.get(param));
							break;
						case QueryMethodConstant.LT:
							queryWrapper.lt(annotation.field(), field.get(param));
							break;
						case QueryMethodConstant.LE:
							queryWrapper.le(annotation.field(), field.get(param));
							break;
						default:
							;
					}
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}

		}
		QueryPage queryPage = new QueryPage();
		queryPage.setPage(page);
		queryPage.setQueryWrapper(queryWrapper);
		return queryPage;
	}
}
