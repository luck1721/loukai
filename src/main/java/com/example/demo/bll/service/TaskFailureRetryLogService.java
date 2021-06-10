package com.example.demo.bll.service;

import cn.com.citycloud.hcs.common.task.log.failureretry.FailureRetryLogService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.bll.entity.TaskFailureRetryLog;
import com.example.demo.bll.mapper.TaskFailureRetryLogMapper;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author lk
 * @date 2021/3/19
 */
@Service
public class TaskFailureRetryLogService extends ServiceImpl<TaskFailureRetryLogMapper, TaskFailureRetryLog> implements FailureRetryLogService<TaskFailureRetryLog, Integer> {
	@Override
	public List<TaskFailureRetryLog> gets(Date date, int i) {
		QueryWrapper<TaskFailureRetryLog> queryWrapper = new QueryWrapper<TaskFailureRetryLog>();
		queryWrapper.gt("retry_time",date).le("retry_times",i).eq("retry_success",false);
		return list(queryWrapper);
	}

	@Override
	public void save(String s, List<Serializable> list, String s1) {
		TaskFailureRetryLog log = new TaskFailureRetryLog();
		log.setArg(list.toString());
		log.setFailureMessage(s1);
		log.setName(s);
		log.setRetryTime(new Date());
		log.setRetryTimes(0);
		log.setRetrySuccess(false);
		save(log);
	}

	@Override
	public void update(Integer s, String s2) {
		TaskFailureRetryLog log = getById(s);
		log.setFailureMessage(s2);
		log.setRetryTime(new Date());
		log.setRetryTimes(log.getRetryTimes() + 1);
		updateById(log);
	}

	@Override
	public void remove(Integer s) {
		removeById(s);
	}

	@Override
	public TaskFailureRetryLog get(Integer s) {
		return getById(s);
	}
}
