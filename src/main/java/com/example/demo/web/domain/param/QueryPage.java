package com.example.demo.web.domain.param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.io.Serializable;

/**
 * @author lk
 * @date 2021/6/24
 */
public class QueryPage<T> implements Serializable {

	private Page<T> page;

	private QueryWrapper<T> queryWrapper;

	public Page<T> getPage() {
		return page;
	}

	public void setPage(Page<T> page) {
		this.page = page;
	}

	public QueryWrapper<T> getQueryWrapper() {
		return queryWrapper;
	}

	public void setQueryWrapper(QueryWrapper<T> queryWrapper) {
		this.queryWrapper = queryWrapper;
	}
}
