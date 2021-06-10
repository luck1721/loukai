package com.example.demo.bll.enums;

import lombok.Getter;

/**
 * @author lk
 * @date 2021/1/22
 */
@Getter
public enum DateTypeEnum {
	/**
	 * 年
	 */
	YEAR,

	/**
	 * 月
	 */
	MONTH,

	/**
	 * 周
	 */
	WEEK,

	/**
	 * 天
	 */
	DAY,

	/**
	 * 时
	 */
	HOUR,

	/**
	 * 分
	 */
	MINUTE,

	/**
	 * 秒
	 */
	SECOND,

	/**
	 * 毫秒
	 */
	MILLISECOND,

	/**
	 * 一个月中第几天(from 1 to 31)
	 */
	DAY_OF_MONTH,

	/**
	 * 一年中的第几天(from 1 to 365, or 366 in a leap year)
	 */
	DAY_OF_YEAR
}
