package com.example.demo.bll.service;

import com.example.demo.bll.entity.Role;
import com.example.demo.bll.entity.User;

/**
 * @author lk
 * @date 2021/1/19
 */
public interface LoginService {

	User addUser(User user);

	Role addRole(Role role);

	User findByName(String name);

	User getUser();

}
