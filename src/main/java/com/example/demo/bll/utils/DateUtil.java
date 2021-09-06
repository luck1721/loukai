package com.example.demo.bll.utils;

import com.example.demo.bll.constant.SymbolType;
import com.example.demo.bll.enums.DateFormatEnum;
import com.example.demo.bll.enums.DateTypeEnum;
import org.apache.commons.lang3.time.DateUtils;

import java.lang.management.ManagementFactory;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author lk
 * @date 2021/1/22
 */
public class DateUtil {

	/**
	 * 返回当前日期格式化后的字符串
	 *
	 * @return 格式化后的日期
	 */
	public static String formatCurrentDateToString() {
		return formatDateToString(null, DateFormatEnum.YYYYMMDDHH24MMSS_BAR);
	}

	/**
	 * 返回当前日期格式化后的字符串
	 *
	 * @param dateFormat 日期格式
	 * @return 格式化后的日期
	 */
	public static String formatCurrentDateToString(DateFormatEnum dateFormat) {
		return formatDateToString(null, dateFormat);
	}

	/**
	 * 返回日期格式化后的字符串
	 *
	 * @param date 日期
	 * @param dateFormat 日期格式
	 * @return 格式化后的日期
	 */
	public static String formatDateToString(Date date, DateFormatEnum dateFormat) {
		LocalDateTime localDateTime = dateToLocalDateTime(date);
		return formatLocalDateTimeToString(localDateTime, dateFormat);
	}

	/**
	 * 返回日期格式化后的字符串
	 *
	 * @param localDateTime 日期
	 * @param dateFormat 日期格式
	 * @return 格式化后的日期
	 */
	public static String formatLocalDateTimeToString(LocalDateTime localDateTime,
													 DateFormatEnum dateFormat) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dateFormat.getFormat());
		return localDateTime.format(dtf);
	}

	/**
	 * 返回日期格式化后的字符串
	 *
	 * @param localTime 日期
	 * @param dateFormat 日期格式
	 * @return 格式化后的日期
	 */
	public static String formatLocalTimeToString(LocalTime localTime, DateFormatEnum dateFormat) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dateFormat.getFormat());
		return localTime.format(dtf);
	}

	/**
	 * 返回日期字符串解析后的日期（日期字符串不合法返回null）
	 *
	 * @param dateStr 日期字符串
	 * @param dateFormat 日期格式
	 * @return 日期字符串解析后的日期 {@link DateFormatEnum}
	 */
	public static Date formatStringToDate(String dateStr, DateFormatEnum dateFormat) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dateFormat.getFormat());
		if (dateStr.contains(SymbolType.COLON)) {
			LocalDateTime dateTime;
			try {
				dateTime = LocalDateTime.parse(dateStr, dtf);
			} catch (DateTimeParseException e) {
				return null;
			}
			return localDateTimeToDate(dateTime);
		} else {
			LocalDate date;
			try {
				date = LocalDate.parse(dateStr, dtf);
			} catch (DateTimeParseException e) {
				return null;
			}
			return localDateToDate(date);
		}
	}

	/**
	 * 获取当前日期的开始时间（00:00:00）
	 *
	 * @return 当前日期的开始时间（00:00:00）
	 */
	public static Date getStartDateTimeOfCurrentDay() {
		return getStartDateTimeOfDay(dateToLocalDate(null));
	}

	/**
	 * 获取指定日期的开始时间（00:00:00）
	 *
	 * @param date 日期
	 * @return 指定日期的开始时间（00:00:00）
	 */
	public static Date getStartDateTimeOfDay(Date date) {
		return getStartDateTimeOfDay(dateToLocalDate(date));
	}

	/**
	 * 获取指定日期的开始时间（00:00:00）
	 *
	 * @param date 日期
	 * @return 指定日期的开始时间（00:00:00）
	 */
	public static Date getStartDateTimeOfDay(LocalDate date) {
		return localDateTimeToDate(LocalDateTime.of(date, LocalTime.MIN));
	}

	/**
	 * 获取当前日期的结束时间（23:59:59）
	 *
	 * @return 当前日期的结束时间（23:59:59）
	 */
	public static Date getEndDateTimeOfCurrentDay() {
		return getEndDateTimeOfDay(dateToLocalDate(null));
	}

	/**
	 * 获取指定日期的结束时间（23:59:59）
	 *
	 * @param date 日期
	 * @return 指定日期的结束时间（23:59:59）
	 */
	public static Date getEndDateTimeOfDay(Date date) {
		return getEndDateTimeOfDay(dateToLocalDate(date));
	}

	/**
	 * 获取指定日期的结束时间（23:59:59）
	 *
	 * @param date 日期
	 * @return 指定日期的结束时间（23:59:59）
	 */
	public static Date getEndDateTimeOfDay(LocalDate date) {
		return localDateTimeToDate(LocalDateTime.of(date, LocalTime.MAX));
	}

	/**
	 * 对一个时间对象加时长（同理也可对年/月/日/时/分/秒）
	 *
	 * @param date 待处理时间
	 * @param duration 增加时长【为负数表示减去多少个指定单位的时间】
	 * @param dateTypeEnum 日期时间类型 {@link DateTypeEnum}
	 * @return Date 修改后的时间
	 */
	public static Date modifyDate(Date date, long duration, DateTypeEnum dateTypeEnum) {
		LocalDateTime d1 = dateToLocalDateTime(date);
		LocalDateTime d2 = null;
		switch (dateTypeEnum) {
			case YEAR:
				d2 = d1.plusYears(duration);
				break;
			case MONTH:
				d2 = d1.plusMonths(duration);
				break;
			case WEEK:
				d2 = d1.plusWeeks(duration);
				break;
			case DAY:
				d2 = d1.plusDays(duration);
				break;
			case HOUR:
				d2 = d1.plusHours(duration);
				break;
			case MINUTE:
				d2 = d1.plusMinutes(duration);
				break;
			default:
				d2 = d1.plusSeconds(duration);
				break;
		}
		return localDateTimeToDate(d2);
	}

	/**
	 * 获取具体的时间
	 *
	 * @param date 日期
	 * @param dateTypeEnum 日期时间类型 {@link DateTypeEnum}
	 * @return 具体的时间
	 */
	public static long getSpecificTime(Date date, DateTypeEnum dateTypeEnum) {
		return getSpecificTime(dateToLocalDateTime(date), dateTypeEnum);
	}

	/**
	 * 获取具体的时间
	 *
	 * @param date 日期
	 * @param dateTypeEnum 日期时间类型 {@link DateTypeEnum}
	 * @return 具体的时间
	 */
	public static long getSpecificTime(LocalDateTime date, DateTypeEnum dateTypeEnum) {
		long specific = 0L;
		if (null == date) {
			return specific;
		}
		switch (dateTypeEnum) {
			case YEAR:
				specific = date.getYear();
				break;
			case MONTH:
				specific = date.getMonthValue();
				break;
			case DAY_OF_MONTH:
				specific = date.getDayOfMonth();
				break;
			case DAY_OF_YEAR:
				specific = date.getDayOfYear();
				break;
			case HOUR:
				specific = date.getHour();
				break;
			case MINUTE:
				specific = date.getMinute();
				break;
			case SECOND:
				specific = date.getSecond();
				break;
			default:
				specific = date.toInstant(ZoneOffset.of("+8")).toEpochMilli();
				break;
		}
		return specific;
	}

	/**
	 * 比较日期（精确到天）
	 *
	 * @param firstDate 第一个日期
	 * @param secondDate 第二个日期
	 * @param dateFormat 指定日期格式
	 * @return int 1    firstDate < secondDate 0    firstDate == secondDate -1   firstDate >
	 * secondDate
	 */
	public static int compareDate(String firstDate, String secondDate, DateFormatEnum dateFormat) {
		return compareDate(formatStringToDate(firstDate, dateFormat),
				formatStringToDate(secondDate, dateFormat));
	}

	/**
	 * 比较日期（精确到秒）
	 *
	 * @param firstDate 第一个日期
	 * @param secondDate 第二个日期
	 * @param dateFormat 指定日期格式
	 * @return int 1    firstDate < secondDate 0    firstDate == secondDate -1   firstDate >
	 * secondDate
	 */
	public static int compareDateTime(String firstDate, String secondDate,
									  DateFormatEnum dateFormat) {
		return compareDateTime(formatStringToDate(firstDate, dateFormat),
				formatStringToDate(secondDate, dateFormat));
	}

	/**
	 * 比较日期（精确到天）
	 *
	 * @param firstDate 第一个日期
	 * @param secondDate 第二个日期
	 * @return int 1    firstDate < secondDate 0    firstDate == secondDate -1   firstDate >
	 * secondDate
	 */
	public static int compareDate(Date firstDate, Date secondDate) {
		LocalDate ld1 = dateToLocalDate(firstDate);
		LocalDate ld2 = dateToLocalDate(secondDate);
		if (ld1.isEqual(ld2)) {
			return 0;
		}
		return ld1.isBefore(ld2) ? 1 : -1;
	}

	/**
	 * 比较日期（精确到秒）
	 *
	 * @param firstDate 第一个日期
	 * @param secondDate 第二个日期
	 * @return int 1    firstDate < secondDate 0    firstDate == secondDate -1   firstDate >
	 * secondDate
	 */
	public static int compareDateTime(Date firstDate, Date secondDate) {
		LocalDateTime ld1 = dateToLocalDateTime(firstDate);
		LocalDateTime ld2 = dateToLocalDateTime(secondDate);
		if (ld1.isEqual(ld2)) {
			return 0;
		}
		return ld1.isBefore(ld2) ? 1 : -1;
	}

	/**
	 * 指定日期是否在当前日期之前
	 *
	 * @param year 年份
	 * @param month 月份
	 * @param day 天
	 * @return boolean
	 */
	public static boolean isBeforeCurrentDate(int year, int month, int day) {
		LocalDate currentLocalDate = LocalDate.now();
		LocalDate localDate = LocalDate.of(year, month, day);
		System.out.println(localDate);
		return localDate.isBefore(currentLocalDate);
	}

	/**
	 * 获取当前时间所在月的第一天日期
	 *
	 * @return Date 所在月的第一天日期, 时分秒为 00:00:00
	 */
	public static Date getFirstDayOfCurrentMonth() {
		return getFirstDayOfMonth(LocalDate.now());
	}

	/**
	 * 获取指定日期所在月的第一天日期
	 *
	 * @param date 目标日期（如果为空，则默认使用当前日期）
	 * @return Date 所在月的第一天日期, 时分秒为 00:00:00
	 */
	public static Date getFirstDayOfMonth(Date date) {
		return getFirstDayOfMonth(dateToLocalDate(date));
	}

	/**
	 * 获取指定日期所在月的第一天日期
	 *
	 * @param date 目标日期（如果为空，则默认使用当前日期）
	 * @return Date 所在月的第一天日期, 时分秒均为0
	 */
	public static Date getFirstDayOfMonth(LocalDate date) {
		return localDateToDate(
				(date == null ? LocalDate.now() : date).with(TemporalAdjusters.firstDayOfMonth()));
	}

	/**
	 * 获取当前月的最后一天日期
	 *
	 * @return Date 所在月的最后一天日期, 时分秒为 00:00:00
	 */
	public static Date getLastDayOfCurrentMonth() {
		return getLastDayOfMonth(dateToLocalDate(null));
	}

	/**
	 * 获取指定日期所在月的最后一天日期
	 *
	 * @param date 目标日期（如果为空，则默认使用当前日期）
	 * @return Date 所在月的最后一天日期, 时分秒为 00:00:00
	 */
	public static Date getLastDayOfMonth(Date date) {
		return getLastDayOfMonth(dateToLocalDate(date));
	}

	/**
	 * 获取指定日期所在月的最后一天日期
	 *
	 * @param date 目标日期（如果为空，则默认使用当前日期）
	 * @return Date 所在月的最后一天日期, 时分秒为 00:00:00
	 */
	public static Date getLastDayOfMonth(LocalDate date) {
		return localDateToDate(
				(date == null ? LocalDate.now() : date).with(TemporalAdjusters.lastDayOfMonth()));
	}

	/**
	 * 获取与指定日期间隔指定年份的日期
	 *
	 * @param x 日期间隔（支持负数）
	 * @return Date 指定日期间隔指定年份的日期, 时分秒均为0
	 */
	public static Date getYearsBetweenCurrentDate(int x) {
		return getYearsBetweenDate(LocalDate.now(), x);
	}

	/**
	 * 获取与指定日期间隔指定年份的日期
	 *
	 * @param date 目标日期（如果为空，则默认使用当前日期）
	 * @param x 日期间隔（支持负数）
	 * @return Date 指定日期间隔指定年份的日期, 时分秒均为0
	 */
	public static Date getYearsBetweenDate(Date date, int x) {
		return getYearsBetweenDate(dateToLocalDate(date), x);
	}

	/**
	 * 获取与指定日期间隔指定年份的日期
	 *
	 * @param date 目标日期（如果为空，则默认使用当前日期）
	 * @param x 日期间隔（支持负数）
	 * @return Date 指定日期间隔指定年份的日期, 时分秒均为0
	 */
	public static Date getYearsBetweenDate(LocalDate date, int x) {
		if (null == date) {
			date = LocalDate.now();
		}
		return localDateToDate(date.plus(x, ChronoUnit.YEARS));
	}

	/**
	 * 获取两个日期指定类型的时间间隔，只支持年月日
	 *
	 * @param firstDate 第一个日期
	 * @param secondDate 第二个日期
	 * @param chronoUnit 日期时间类型 {@link ChronoUnit}
	 * @return 两个日期指定类型的时间间隔
	 */
	public static long getDateTimeDuration(Date firstDate, Date secondDate, ChronoUnit chronoUnit) {
		LocalDateTime localDateTime1 = dateToLocalDateTime(firstDate);
		LocalDateTime localDateTime2 = dateToLocalDateTime(secondDate);
		return chronoUnit.between(localDateTime1, localDateTime2);
	}


	/**
	 * 获取当月的总天数
	 *
	 * @return 当月的总天数
	 */
	public static int getDurationOfCurrentMonth() {
		return getDurationOfMonth(null);
	}

	/**
	 * 获取指定日期所在月的总天数
	 *
	 * @return 当月的总天数
	 */
	public static int getDurationOfMonth(Date date) {
		return dateToLocalDate(date).lengthOfMonth();
	}

	/**
	 * Date 转成 LocalDate
	 *
	 * @param date 日期
	 * @return LocalDate 当地日期
	 */
	private static LocalDate dateToLocalDate(Date date) {
		if (date == null) {
			return LocalDate.now();
		}
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	/**
	 * LocalDate 转成 Date
	 *
	 * @param localDate 当地日期
	 * @return Date 日期
	 */
	private static Date localDateToDate(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	/**
	 * Date 转成 LocalDateTime
	 *
	 * @param date 日期
	 * @return LocalDateTime 当地日期时间
	 */
	private static LocalDateTime dateToLocalDateTime(Date date) {
		if (date == null) {
			return LocalDateTime.now();
		}
		return date.toInstant().atZone(ZoneId.systemDefault()).truncatedTo(ChronoUnit.SECONDS)
				.toLocalDateTime();
	}

	/**
	 * LocalDateTime 转成 Date
	 *
	 * @param localDateTime 当地日期时间
	 * @return Date 日期
	 */
	private static Date localDateTimeToDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant().truncatedTo(
				ChronoUnit.SECONDS));
	}


	/**
	 * 获取jvm启动时间
	 * @return 启动时间
	 */
	public static Date getVMStartTime() {
		return new Date(ManagementFactory.getRuntimeMXBean().getStartTime());
	}

	public static Date getAfterDaysToDate(Date before, int days) {
		Calendar c = Calendar.getInstance();
		c.setTime(before);
		c.add(5, days);
		return c.getTime();
	}

	/**
	 * 计算两之间的天数
	 * @param before
	 * @param after
	 * @return
	 */
	public static double getDistanceOfTwoDate(Date before, Date after) {
		long beforeTime = before.getTime();
		long afterTime = after.getTime();
		return (double)((afterTime - beforeTime) / 86400000L);
	}
	private static String[] parsePatterns = new String[]{"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM", "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};
	public static Date parseDate(Object str) {
		if (str == null) {
			return null;
		} else {
			try {
				return DateUtils.parseDate(str.toString(), parsePatterns);
			} catch (ParseException var2) {
				return null;
			}
		}
	}

	/**
	 * 获取本季度的第一天或最后一天
	 * @Param: [today, isFirst: true 表示开始时间，false表示结束时间]
	 * @return: java.lang.String
	 */
	public static String getStartOrEndDayOfQuarter(LocalDate today, Boolean isFirst){
		LocalDate resDate = LocalDate.now();
		if (today == null) {
			today = resDate;
		}
		Month month = today.getMonth();
		Month firstMonthOfQuarter = month.firstMonthOfQuarter();
		Month endMonthOfQuarter = Month.of(firstMonthOfQuarter.getValue() + 2);
		if (isFirst) {
			resDate = LocalDate.of(today.getYear(), firstMonthOfQuarter, 1);
		} else {
			resDate = LocalDate.of(today.getYear(), endMonthOfQuarter, endMonthOfQuarter.length(today.isLeapYear()));
		}
		return resDate.toString();
	}

	/**
	 * 获取本年的第一天或最后一天
	 * @Date: 2019/7/23 13:48
	 * @Param: [today, isFirst: true 表示开始时间，false表示结束时间]
	 * @return: java.lang.String
	 */
	public static String getStartOrEndDayOfYear(LocalDate today, Boolean isFirst){
		LocalDate resDate = LocalDate.now();
		if (today == null) {
			today = resDate;
		}
		if (isFirst) {
			resDate = LocalDate.of(today.getYear(), Month.JANUARY, 1);
		} else {
			resDate = LocalDate.of(today.getYear(), Month.DECEMBER, Month.DECEMBER.length(today.isLeapYear()));
		}
		return resDate.toString();
	}

	/**
	 * 获取两个日期之间的所有月(字符串格式, 按月计算)
	 *
	 * @param start
	 * @param end
	 * @return
	 */
	public static List<String> getBetweenMonths(Date start, Date end) {
		List<String> result = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

		Calendar tempStart = Calendar.getInstance();
		tempStart.setTime(start);
		// 加了一个月
		tempStart.add(Calendar.MONTH, 1);
		Calendar tempEnd = Calendar.getInstance();
		tempEnd.setTime(end);
		result.add(sdf.format(start));
		while (tempStart.before(tempEnd)) {
			result.add(sdf.format(tempStart.getTime()));
			tempStart.add(Calendar.MONTH, 1);
		}
		return result;
	}

	/**
	 * 获取两个日期间的所有日
	 *
	 * @param start
	 * @param end
	 * @return
	 */
	public static List<String> getDays(Date start, Date end) {
		// 返回的日期集合
		List<String> days = new ArrayList<>();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar tempStart = Calendar.getInstance();
		tempStart.setTime(start);
		Calendar tempEnd = Calendar.getInstance();
		tempEnd.setTime(end);
		// 日期加1(包含结束)
		tempEnd.add(Calendar.DATE, +1);
		while (tempStart.before(tempEnd)) {
			days.add(dateFormat.format(tempStart.getTime()));
			tempStart.add(Calendar.DAY_OF_YEAR, 1);
		}
		return days;
	}

	/**
	 * 获取两个日期间的所有年
	 *
	 * @param start
	 * @param end  end > start
	 * @return
	 */
	public static List<String> getYears(Date start, Date end) {
		List<String> years = new ArrayList<>();
		DateFormat dateFormat = new SimpleDateFormat("yyyy");
		Integer startTime = Integer.parseInt(dateFormat.format(start));
		Integer endTime = Integer.parseInt(dateFormat.format(end));
		while (endTime - startTime >= 0) {
			years.add(startTime.toString());
			startTime++;
		}
		return years;
	}
}
