package com.example.demo.bll.service.impl;

import com.example.demo.bll.service.ExecutorInterface;

/**
 * @author lk
 * @date 2021/3/11
 */
public class Executor implements ExecutorInterface {
	@Override
	public int execute(int x, int y) {
		return x + y;
	}
}
