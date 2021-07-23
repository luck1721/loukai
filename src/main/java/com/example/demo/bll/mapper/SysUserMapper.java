package com.example.demo.bll.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.bll.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * (SysUser)表数据库访问层
 *
 * @author makejava
 * @since 2021-07-15 16:25:02
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
}
