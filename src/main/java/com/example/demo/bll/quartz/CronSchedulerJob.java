package com.example.demo.bll.quartz;

import com.example.demo.bll.anon.CacheLock;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

/**
 * @author lk
 * @date 2021/1/28
 */
@Component
public class CronSchedulerJob {

	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;

	private void scheduleJob1(Scheduler scheduler) throws SchedulerException {
		JobDetail jobDetail = JobBuilder.newJob(ScheduledJob.class) .withIdentity("job1", "group1").build();
		// 6的倍数秒执行 也就是 6 12 18 24 30 36 42 ....
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0/6 * * * * ?");
		CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1")
				.usingJobData("name","王智").usingJobData("age","25").withSchedule(scheduleBuilder).build();
		scheduler.scheduleJob(jobDetail,cronTrigger);
	}

	/**
	 * @Author Smith
	 * @Description 同时启动两个定时任务
	 * @Date 16:31 2019/1/24
	 * @Param
	 * @return void
	 **/
	public void scheduleJobs() throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		scheduleJob1(scheduler);
	}
}
