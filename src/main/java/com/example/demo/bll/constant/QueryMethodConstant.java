package com.example.demo.bll.constant;

/**
 * mybatis plus常用的查询方式
 * @author lk
 * @date 2021/6/23
 */
public interface QueryMethodConstant {
	/**
	 * 相同
	 */
	String EQ = "EQ";

	/**
	 * 不相同
	 */
	String NE = "NE";

	/**
	 * 相似，左右模糊(like '%值%')
	 */
	String LIKE = "LIKE";

	/**
	 * 相似，左模糊(like '%值')
	 */
	String LIKE_LIFT = "LIKE_LIFT";

	/**
	 * 相似，右模糊(like '值%')
	 */
	String LIKE_RIGHT = "LIKE_RIGHT";

	/**
	 * 不相似 (not like '%值%')
	 */
	String NOT_LIKE = "NOT_LIKE";

	/**
	 * 大于
	 */
	String GT = "GT";

	/**
	 * 大于等于
	 */
	String GE = "GE";

	/**
	 * 小于
	 */
	String LT = "LT";

	/**
	 * 小于等于
	 */
	String LE = "LE";
}
