package com.example.demo.bll.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.bll.entity.VerificationLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * (VerificationLog)表数据库访问层
 *
 * @author loukai
 * @since 2021-07-16 15:28:42
 */
@Mapper
public interface VerificationLogMapper extends BaseMapper<VerificationLog> {
}
