package com.example.demo.bll.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.demo.bll.entity.SysBizLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author lk
 * @date 2021/1/21
 */
@Mapper
public interface SysBizLogMapper extends BaseMapper<SysBizLog> {

	void insertLog(SysBizLog log);

	IPage<SysBizLog> selectByActionTime(IPage<SysBizLog> page, @Param("log") SysBizLog sysBizLog);

}
