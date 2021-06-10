package com.example.demo.bll.config;

import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

/**
 * @author lk
 * @date 2021/2/20
 */
@Data
public class HttpResult {

	private int code;

	private String body;

	public HttpResult(int code, String body) {
		this.code = code;
		this.body = body;
	}
}
