package com.example.demo.bll.service.impl;

import com.example.demo.bll.dao.RoleDao;
import com.example.demo.bll.dao.UserDao;
import com.example.demo.bll.entity.Permission;
import com.example.demo.bll.entity.Role;
import com.example.demo.bll.entity.User;
import com.example.demo.bll.event.MyEvent;
import com.example.demo.bll.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lk
 * @date 2021/1/19
 */
@Service
@Transactional
public class LoginServiceImpl implements LoginService {

	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;
	@Resource
	private ApplicationContext applicationContext;

	//添加用户
	@Override
	public User addUser(User user) {
		userDao.save(user);
		return user;
	}

	//添加角色
	@Override
	public Role addRole(Role role) {
		User user = userDao.findByName(role.getUser().getName());
		role.setUser(user);
		Permission permission1 = new Permission();
		permission1.setPermission("create");
		permission1.setRole(role);
		Permission permission2 = new Permission();
		permission2.setPermission("update");
		permission2.setRole(role);
		List<Permission> permissions = new ArrayList<Permission>();
		permissions.add(permission1);
		permissions.add(permission2);
		role.setPermissions(permissions);
		roleDao.save(role);
		return role;
	}

	//查询用户通过用户名
	@Override
	public User findByName(String name) {
		return userDao.findByName(name);
	}

	public String getNumber(String name,String age) {
		System.out.println("------:"+name + age);
		return name + age;
	}

	/**
	 * 发布事件
	 * @return
	 */
	public User getUser() {
		User user = new User();
		user.setId(new Long(5));
		user.setName("345");
		user.setPassword("788");
		// 发布事件
		MyEvent event = new MyEvent(this, user);
		applicationContext.publishEvent(event);
		return user;
	}
}
