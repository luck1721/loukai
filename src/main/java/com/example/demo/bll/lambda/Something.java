package com.example.demo.bll.lambda;

/**
 * @author lk
 * @date 2020/5/31
 */
public class Something {
	// constructor methods
	Something() {}

	Something(String something) {
		System.out.println(something);
	}

	// static methods
	static String startsWith(String s) {
		return String.valueOf(s.charAt(0));
	}

	String endWith(String s) {
		return String.valueOf(s.charAt(s.length()-1));
	}

	void endWith() {}

}
