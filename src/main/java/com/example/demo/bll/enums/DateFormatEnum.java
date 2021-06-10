package com.example.demo.bll.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lk
 * @date 2021/1/22
 */
@Getter
@AllArgsConstructor
public enum DateFormatEnum {

	/**
	 * 日期格式化 yyyy
	 */
	YYYY("yyyy"),

	/**
	 * 日期格式化 MM
	 */
	MM("MM"),

	/**
	 * 日期格式化 dd
	 */
	DD("dd"),

	/**
	 * 日期格式化 yyyyMM
	 */
	YYYYMM("yyyyMM"),

	/**
	 * 日期格式化 yyyy-MM
	 */
	YYYYMM_BAR("yyyy-MM"),

	/**
	 * 日期格式化 yyyy/MM
	 */
	YYYYMM_SLASH("yyyy/MM"),

	/**
	 * 日期格式化 yyyyMMdd
	 */
	YYYYMMDD("yyyyMMdd"),
	/**
	 * 日期格式化 yyMMdd
	 */
	YYMMDD("yyMMdd"),

	/**
	 * 日期格式化 yyyy-MM-dd
	 */
	YYYYMMDD_BAR("yyyy-MM-dd"),

	/**
	 * 日期格式化 yyyy/MM/dd
	 */
	YYYYMMDD_SLASH("yyyy/MM/dd"),

	/**
	 * 日期格式化 yyyy年MM月dd日
	 */
	YYYYMMDD_CHN("yyyy年MM月dd日"),

	/**
	 * 日期和时间格式化24小时制 yyyy-MM-dd hh24:mm
	 */
	YYYYMMDDHH24MM_BAR("yyyy-MM-dd HH:mm"),

	/**
	 * 日期和时间格式化24小时制 HH:mm
	 */
	HH24MM("HH:mm"),

	/**
	 * 日期和时间格式化12小时制 yyyy-MM-dd hh:mm:ss
	 */
	YYYYMMDDHH12MMSS_BAR("yyyy-MM-dd hh:mm:ss"),
	/**
	 * 日期和时间格式化24小时制 yyyy-MM-dd hh24:mm:ss
	 */
	YYYYMMDDHH24MMSS_BAR("yyyy-MM-dd HH:mm:ss"),

	/**
	 * 日期格式化 yyyy年MM月dd日 hh24:mm:ss
	 */
	YYYYMMDDHH24MMSS_CHN("yyyy年MM月dd日 HH:mm:ss"),

	/**
	 * 日期格式化 yyyy-MM-dd HH:mm:ss.SSS 例如：2019-09-18 10:46:04.037
	 */
	YYYYMMDDHH24MMSSSSS_BAR("yyyy-MM-dd HH:mm:ss.SSS"),

	/**
	 * 日期格式化 yyyyMMddHHmmss 例如：2019-09-18 10:46:04
	 */
	YYYYMMDDHH24MMSS("yyyyMMddHHmmss"),

	/**
	 * 日期格式化 yyyyMMddHHmmssSSS 例如：2019-09-18 10:46:04.037
	 */
	YYYYMMDDHH24MMSSSSS("yyyyMMddHHmmssSSS");

	/**
	 * 格式
	 */
	private String format;

}
