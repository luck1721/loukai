package com.example.demo.bll.dao;

import com.example.demo.bll.entity.User;

/**
 * @author lk
 * @date 2021/1/19
 */
public interface UserDao extends BaseDao<User, Long> {

	User findByName(String name);

}
