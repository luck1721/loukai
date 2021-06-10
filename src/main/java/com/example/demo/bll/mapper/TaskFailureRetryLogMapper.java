package com.example.demo.bll.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.bll.entity.TaskFailureRetryLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TaskFailureRetryLogMapper extends BaseMapper<TaskFailureRetryLog> {
    int deleteByPrimaryKey(Integer id);

    int insert(TaskFailureRetryLog record);

    int insertSelective(TaskFailureRetryLog record);

    TaskFailureRetryLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TaskFailureRetryLog record);

    int updateByPrimaryKey(TaskFailureRetryLog record);
}