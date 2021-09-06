package com.example.demo.bll.utils;

import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 短URL生成工具类
 *
 * @date 2020年4月24日
 * @author huanglj
 */
public class TinyUrlUtils {

	/**
	 * 在进制表示中的字符集合，进制数为digits元素个数
	 */
	private static final char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'z', 'g', 'f', 'd', 's', 'a',
			'Q', 'A', 'Z', 'P', 'L', 'I', 'h', 'j', 'k', 'l', 'm', 'W', 'S', 'X', 'M', 'J', 'U', 't', 'r', 'e', 'w', 'q',
			'E', 'D', 'C', 'N', 'H', 'Y', 'y', 'u', 'i', 'o', 'p', 'R', 'F', 'V', 'B', 'G', 'T', 'n', 'b', 'v', 'c', 'x' };

	public static final long TIME_PERIOD = 540L * 24 * 60 * 60 * 1000;
	public static final int COUNTER_SIZE = 49;
	public static final int NUMBER_LENGTH = 7;

	private static final long timePeriodSqrt = 216000L;
	private static final long timePeriodRemainder = 513216000000L;
	private static final List<Integer> counters = new ArrayList<>();

	public static String generateTinyUrlId() {
		synchronized (counters) {
			if (counters.isEmpty()) {
				long time = System.currentTimeMillis();
				for (int i = 0; i < COUNTER_SIZE; i++) {
					counters.add(i);
				}
				Collections.shuffle(counters);
				if(time == System.currentTimeMillis()) {
					try {
						Thread.sleep(1);
					} catch (Exception e) {
					}
				}
			}
			return generateTinyUrlId(counters.remove(counters.size() - 1));
		}
	}

	public static String generateTinyUrlId(int counter) {
		return toNumberString(TIME_PERIOD * counter + getCurrentTimestamp());
	}

	private static long getCurrentTimestamp() {
		long timestamp = System.currentTimeMillis() % TIME_PERIOD;
		return shuffleNumber(timestamp, TIME_PERIOD, timePeriodSqrt);
	}

	private static long shuffleNumber(long number, long size, long sqrt) {
		number = shuffleNumber(number, size, false);
		long m = (number % sqrt) * sqrt;
		long n = number / sqrt;
		long f = parseNumberFactor(sqrt);
		if(sqrt < 2 || f == sqrt) {
			return m + n;
		}
		if(f < 2) {
			return m + shuffleNumber(n, sqrt, (long)Math.pow(sqrt, 0.5));
		}
		long fs = sqrt / f;
		long nx = n / fs;
		long ny = n % fs;
		return m + (fs * nx + shuffleNumber(ny, fs, (long)Math.pow(fs, 0.5)));
	}

	private static long shuffleNumber(long number, long size, boolean half) {
		if(number == 0 || size % 2 > 0) {
			return number;
		}
		if(number % 2 > 0) {
			if(half) {
				size /= 2;
				if(size % 2 > 0) {
					return number;
				}
				return number < size ? size - number : size * 3 - number;
			}
			return size - number;
		}
		return shuffleNumber(number / 2, size / 2, !half) * 2;
	}

	private static long parseNumberFactor(long number) {
		int[] bases = {2, 3, 5};
		for (int base : bases) {
			while (number > 0 && number % (base * base) == 0) {
				number /= (base * base);
			}
		}
		return number;
	}

	private static String toNumberString(long number) {
		number = shuffleNumber(number, TIME_PERIOD * COUNTER_SIZE, timePeriodSqrt * (long)Math.pow(COUNTER_SIZE, 0.5));
		number += timePeriodRemainder;
		int seed = digits.length;
		char[] buf = new char[32];
		int charPos = 32;
		while ((number / seed) > 0) {
			buf[--charPos] = digits[(int) (number % seed)];
			number /= seed;
		}
		buf[--charPos] = digits[(int) (number % seed)];
		String str = new String(buf, charPos, (32 - charPos));
		int len = NUMBER_LENGTH - str.length();
		if (len > 0) {
			for (int i = 0; i < len; i++) {
				str = "0" + str;
			}
		}
		int random = RandomUtils.nextInt(0, 4);
		str = str.replaceAll("0", random / 2 == 0 ? "0" : "O").replaceAll("k", random % 2 == 0 ? "k" : "K");
		return str;
	}

}
