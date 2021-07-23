package com.example.demo.bll.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

/**
 * @author lk
 * @date 2021/1/19
 */
@Entity
public class Permission {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String permission;
	@ManyToOne(fetch = FetchType.EAGER)
	private Role role;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	@JsonBackReference
	public Role getRole() {
		return role;
	}

	@JsonBackReference
	public void setRole(Role role) {
		this.role = role;
	}
}
