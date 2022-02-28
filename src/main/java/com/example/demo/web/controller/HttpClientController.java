package com.example.demo.web.controller;

import com.example.demo.bll.service.impl.HttpAPIService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

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

	@GetMapping("/httpclient/json")
	public String httpclient() throws Exception {
		Runnable runnable = () -> {
			while (true) {
				try {
					httpAPIService.sendPostJson("http://10.162.12.234/media/oauth/api/transcriptions/61cd6f75c68b326f47e669c8", "{\n" +
							"  \"transcriptionFiles\": [\n" +
							"    {\n" +
							"      \"type\": \"original\",\n" +
							"      \"name\": \"string\",\n" +
							"      \"sentences\": [\n" +
							"        {\n" +
							"          \"id\": \"string\",\n" +
							"          \"words\": [\n" +
							"            {\n" +
							"              \"beginTime\": 0,\n" +
							"              \"endTime\": 0,\n" +
							"              \"text\": \"string\"\n" +
							"            }\n" +
							"          ]\n" +
							"        }\n" +
							"      ],\n" +
							"      \"contents\": [\n" +
							"        {\n" +
							"          \"contentId\": \"string\",\n" +
							"          \"beginTime\": 0,\n" +
							"          \"endTime\": 0,\n" +
							"          \"tagName\": \"string\",\n" +
							"          \"tagTime\": 0,\n" +
							"          \"text\": \"string\"\n" +
							"        }\n" +
							"      ]\n" +
							"    }\n" +
							"  ]\n" +
							"}", new String[]{}, new String[]{});
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}


			}
		};
		Thread thread = new Thread(runnable);

		thread.start();
		return "hello";
	}
}
