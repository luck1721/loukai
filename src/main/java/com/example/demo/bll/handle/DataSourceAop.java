package com.example.demo.bll.handle;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author lk
 * @date 2020/5/17
 */
@Aspect
@Component
public class DataSourceAop {
	@Pointcut("!@annotation(com.example.demo.bll.anon.Master) " +
			"&& (execution(* com.example.demo.bll.service..*.select*(..)) " +
			"|| execution(* com.example.demo.bll.service..*.get*(..)))")
	public void readPointcut() {

	}

	@Pointcut("@annotation(com.example.demo.bll.anon.Master) " +
			"|| execution(* com.example.demo.bll.service..*.insert*(..)) " +
			"|| execution(* com.example.demo.bll.service..*.save*(..)) " +
			"|| execution(* com.example.demo.bll.service..*.add*(..)) " +
			"|| execution(* com.example.demo.bll.service..*.update*(..)) " +
			"|| execution(* com.example.demo.bll.service..*.edit*(..)) " +
			"|| execution(* com.example.demo.bll.service..*.delete*(..)) " +
			"|| execution(* com.example.demo.bll.service..*.remove*(..))")
	public void writePointcut() {

	}

	@Before("readPointcut()")
	public void read() {
		DBContextHolder.slave();
	}

	@Before("writePointcut()")
	public void write() {
		DBContextHolder.master();
	}

	@After("readPointcut()")
	public void remove() {
		DBContextHolder.remove();
	}

	@After("writePointcut()")
	public void remove2() {
		DBContextHolder.remove();
	}
}
