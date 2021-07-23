package com.example.demo.bll.service.notice;

import cn.com.citycloud.hcs.common.domain.BaseBean;
import org.springframework.core.io.InputStreamSource;

import java.io.IOException;
import java.io.InputStream;

/**
 * 通知附件对象
 *
 * @date 2020年5月16日
 * @author huanglj
 */
public class NoticeMultipart extends BaseBean implements InputStreamSource {
	private static final long serialVersionUID = -1962868093603918037L;

	private String path;

	public NoticeMultipart() {
	}

	public NoticeMultipart(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return null;
	}

}
