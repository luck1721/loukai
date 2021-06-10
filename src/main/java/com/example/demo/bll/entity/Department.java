package com.example.demo.bll.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author lk
 * @date 2021/3/25
 */
@Data
@NoArgsConstructor
public class Department implements Serializable,Comparable<Department> {

	private String id;
	private String depId;
	private String depName;
	private String pid;
	private String code;

	@Override
	public int compareTo(Department o) {
		return code.compareTo(o.getCode());
	}
}
