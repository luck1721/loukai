package com.example.demo.bll.lambda;

/**
 * @author lk
 * @date 2020/5/31
 */
@FunctionalInterface
public interface IConvert<F, T> {
	T convert(F form);
}
