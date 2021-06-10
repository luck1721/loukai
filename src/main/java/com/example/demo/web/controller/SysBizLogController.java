package com.example.demo.web.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.demo.bll.config.JSONResult;
import com.example.demo.bll.config.PageJSONResult;
import com.example.demo.bll.entity.SysBizLog;
import com.example.demo.bll.service.impl.SysBizLogServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author lk
 * @date 2021/6/2
 */
@RestController
public class SysBizLogController {
	@Autowired
	SysBizLogServiceImpl sysBizLogService;

	@GetMapping("/log")
	public JSONResult<IPage<SysBizLog>> getExpiredsByDate() {
		return PageJSONResult.ok(sysBizLogService.getExpiredsByDate(new Date()));
	}
}
