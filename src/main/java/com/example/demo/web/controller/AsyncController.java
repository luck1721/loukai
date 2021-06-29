package com.example.demo.web.controller;

import cn.com.citycloud.hcs.common.web.DataType;
import cn.com.citycloud.hcs.common.web.ParamType;
import com.example.demo.bll.service.AsyncService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 多线程异步处理
 * @author lk
 * @date 2021/6/1
 */
@RestController
public class AsyncController {

	@Autowired
	private AsyncService asyncService;

	@ApiOperation("根据通知来源获取对象")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "token", dataType = DataType.string, paramType = ParamType.query,required = true),
			@ApiImplicitParam(name = "createDate", value = "createDate", dataType = DataType.string,paramType = ParamType.query)
	})
	@GetMapping("/api/async")
	public void async(String token, Date createDate){
		System.out.println(createDate);
		asyncService.executeAsync(token);
	}
}
