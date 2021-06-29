package com.example.demo.web.controller;

import com.example.demo.bll.service.impl.HttpAPIService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author lk
 * @date 2021/2/20
 */

@RestController
@RequestMapping("/api")
public class HttpClientController {

	@Resource
	private HttpAPIService httpAPIService;

	@GetMapping("/httpclient")
	public String test() throws Exception {
		String str = httpAPIService.doGet("http://www.baidu.com");
		System.out.println(str);
		return "hello";
	}
}
