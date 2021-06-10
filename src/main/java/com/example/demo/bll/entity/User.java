package com.example.demo.bll.entity;

import javax.persistence.*;
import java.util.List;

/**
 * @author lk
 * @date 2021/1/19
 */
@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(unique = true)
	private String name;
	private String password;
	@OneToMany(cascade = CascadeType.ALL,fetch=FetchType.EAGER,mappedBy = "user")
	private List<Role> roles;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
