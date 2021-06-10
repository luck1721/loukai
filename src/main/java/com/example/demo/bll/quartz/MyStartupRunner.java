package com.example.demo.bll.quartz;

import com.example.demo.bll.anon.CacheLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author lk
 * @date 2021/1/28
 */
@Component
public class MyStartupRunner implements CommandLineRunner {

	@Autowired
	public CronSchedulerJob scheduleJobs;

	@Override
	public void run(String... args) throws Exception {
		//scheduleJobs.scheduleJobs();
		//System.out.println(">>>>>>>>>>>>>>>定时任务开始执行<<<<<<<<<<<<<");
	}
}
