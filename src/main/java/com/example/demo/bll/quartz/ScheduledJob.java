package com.example.demo.bll.quartz;

import com.example.demo.bll.anon.CacheLock;
import com.example.demo.bll.service.impl.LoginServiceImpl;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;

/**
 * @author lk
 * @date 2021/1/28
 */
public class ScheduledJob implements Job {

	private String name;

	public void setName(String name) {
		this.name = name;
	}
	private String age;

	public void setAge(String age) {
		this.age = age;
	}
	@Autowired
	private LoginServiceImpl loginServiceImpl;


	@Override
	@CacheLock(lockedPrefix = "TimeTaskService",expireTime=30)
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		System.out.println("CRON ----> schedule job1 is running ... + " + name + "  ---->  " + loginServiceImpl.getNumber(name,age));
	}
}
