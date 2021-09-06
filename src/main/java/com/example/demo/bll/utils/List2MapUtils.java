package com.example.demo.bll.utils;

import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lk
 * @date 2021/2/23
 */
public class List2MapUtils {
	private static final String GET = "get";

	private List2MapUtils() {
		throw new AssertionError("Util禁止反射实例化");
	}


	public static <K, E> Map<K, E> convertOne(List<E> list, String key) {
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}

		Map<K, E> map = null;
		try {
			Method getM = getMethod(list.get(0).getClass(), key);
			map = new HashMap<>();
			for (E en : list) {
				K k = (K) getM.invoke(en);
				map.put(k, en);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}


	public static <K, E> Map<K, List<E>> convertList(List<E> list, String key) {
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		Map<K, List<E>> map = null;
		try {
			Method getM = getMethod(list.get(0).getClass(), key);
			map = new HashMap<>();
			for (E en : list) {
				K k = (K) getM.invoke(en);
				List<E> res = map.get(k);
				if (res != null) {
					res.add(en);
				} else {
					List<E> l1 = new ArrayList<>();
					l1.add(en);
					map.put(k, l1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}


	private static Method getMethod(Class clazz, String key) throws NoSuchMethodException {
		if (key.startsWith(GET)) {
			return clazz.getMethod(key);
		}
		if (Character.isUpperCase(key.charAt(0))) {
			clazz.getMethod(GET + key);
		}
		return clazz.getMethod(GET + Character.toUpperCase(key.charAt(0)) + key.substring(1));
	}
}
