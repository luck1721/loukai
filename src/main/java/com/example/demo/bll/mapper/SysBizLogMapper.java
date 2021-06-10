package com.example.demo.bll.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.bll.entity.SysBizLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lk
 * @date 2021/1/21
 */
@Mapper
public interface SysBizLogMapper extends BaseMapper<SysBizLog> {

	void insertLog(SysBizLog log);

}
