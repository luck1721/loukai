package com.example.demo.bll.lambda;

/**
 * @author lk
 * @date 2020/5/31
 */
public class Test {
	public static void main(String[] args) {
		IConvert<String, String> convert = Something::startsWith;
		String converted = convert.convert("123");
		System.out.println(converted);
	}
}
