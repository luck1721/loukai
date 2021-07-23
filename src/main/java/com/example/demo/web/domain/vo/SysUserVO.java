package com.example.demo.web.domain.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * (SysUser)VO
 *
 * @author makejava
 * @since 2021-07-15 16:27:25
 */
@Data
public class SysUserVO implements Serializable {

	private static final long serialVersionUID = 644582699883803040L;

	private Integer id;

	private String name;

	private Integer age;

}

