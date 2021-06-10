package com.example.demo.bll.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import cn.com.citycloud.hcs.common.task.log.failureretry.FailureRetryLog;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName("task_failure_retry_log")
public class TaskFailureRetryLog implements Serializable, FailureRetryLog<Integer> {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String project;

    private String name;

    private String arg;

    private String failureMessage;

    private Date retryTime;

    private Integer retryTimes;

    private Boolean retrySuccess;

    @Override
    public List<Serializable> getArgs() {
        return Arrays.asList(arg.split(","));
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArg() {
        return arg;
    }

    public void setArg(String arg) {
        this.arg = arg;
    }

    public String getFailureMessage() {
        return failureMessage;
    }

    public void setFailureMessage(String failureMessage) {
        this.failureMessage = failureMessage;
    }

    public Date getRetryTime() {
        return retryTime;
    }

    public void setRetryTime(Date retryTime) {
        this.retryTime = retryTime;
    }

    public Integer getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(Integer retryTimes) {
        this.retryTimes = retryTimes;
    }

    public Boolean getRetrySuccess() {
        return retrySuccess;
    }

    public void setRetrySuccess(Boolean retrySuccess) {
        this.retrySuccess = retrySuccess;
    }
}