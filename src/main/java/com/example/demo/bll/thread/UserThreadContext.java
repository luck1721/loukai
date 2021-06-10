package com.example.demo.bll.thread;

import com.example.demo.web.domain.DetailUserInfo;

/**
 * @author lk
 * @date 2021/1/20
 */
public class UserThreadContext {

	private static final ThreadLocal<DetailUserInfo> USER_THREADS = new ThreadLocal<>();

	/**
	 * 将当前用户设置到线程上下文中
	 *
	 * @param currentUser 当前登录用户
	 */
	public static void addCurrentThreadUser(DetailUserInfo currentUser) {
		USER_THREADS.set(currentUser);
	}

	/**
	 * 返回当前线程用户
	 *
	 * @return 当前线程用户
	 */
	public static DetailUserInfo getCurrentThreadUser() {
		return USER_THREADS.get();
	}

	/**
	 * 将当前线程变量中的用户清空
	 */
	public static void removeCurrentThreadUser() {
		USER_THREADS.remove();
	}

	/**
	 * 返回当前线程用户的租户id
	 * @return 当前线程用户的租户id
	 */
	public static Integer getCurrentProjectId() {
		DetailUserInfo currentThreadUser = getCurrentThreadUser();
		return currentThreadUser.getProjectId();
	}

	/**
	 * 返回当前线程用户的行政区划id
	 * @return 当前线程用户的行政区划id
	 */
	public static Integer getCurrentGroupId() {
		DetailUserInfo currentThreadUser = getCurrentThreadUser();
		return currentThreadUser.getGroupId();
	}

}
