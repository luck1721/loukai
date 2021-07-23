package com.example.demo.web.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.demo.bll.config.JSONResult;
import com.example.demo.bll.config.PageJSONResult;
import com.example.demo.bll.entity.SysBizLog;
import com.example.demo.bll.service.impl.SysBizLogServiceImpl;
import com.example.demo.web.domain.param.PageParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lk
 * @date 2021/6/2
 */
@RestController
@RequestMapping("/api")
public class SysBizLogController {
	@Autowired
	SysBizLogServiceImpl sysBizLogService;

	@GetMapping("/log")
	@Cacheable( value = "getExpiresByDate" , key = "#pageParam.page", sync = true)
	public JSONResult<IPage<SysBizLog>> getExpiresByDate(PageParam pageParam) {
		long begin=System.currentTimeMillis();
		JSONResult<IPage<SysBizLog>> pageJSONResult = PageJSONResult.ok(sysBizLogService.getExpiresByDate(pageParam));
		long timeValue = System.currentTimeMillis() - begin;
		System.out.println("耗时：" + timeValue + "毫秒");
		return pageJSONResult;
	}

	@GetMapping("/log/expires")
	public JSONResult<IPage<SysBizLog>> selectByActionTime(PageParam pageParam) {
	long begin=System.currentTimeMillis();
	JSONResult<IPage<SysBizLog>> pageJSONResult = PageJSONResult.ok(sysBizLogService.selectByActionTime(pageParam));
	long timeValue = System.currentTimeMillis() - begin;
	System.out.println("耗时：" + timeValue + "毫秒");
	return pageJSONResult;
	}
}
