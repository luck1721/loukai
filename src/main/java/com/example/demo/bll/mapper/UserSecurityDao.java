package com.example.demo.bll.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.bll.entity.UserSecurity;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * @author lk
 * @date 2021/7/22
 */
@Mapper
public interface UserSecurityDao extends BaseMapper<UserSecurity> {

	UserSecurity getByUserId(String userId);

	boolean updateByUserId(String userId, Map<String, Object> fieldParamMapping);
}
