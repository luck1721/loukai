package com.example.demo.web.domain;

import cn.com.citycloud.hcs.common.domain.BaseBean;
import org.springframework.core.io.InputStreamSource;

/**
 * 文件上传对象
 *
 * @date 2020年6月28日
 * @author huanglj
 */
public class FileUpload extends BaseBean {
	private static final long serialVersionUID = 9076331405858630995L;

	/** 文件名称 */
	private String name;
	/** 内容类型 */
	private String contentType;
	/** 文件输入源 */
	private InputStreamSource source;
	/** 文件大小 */
	private Long size;
	/** 上传人 */
	private String uploaderId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public InputStreamSource getSource() {
		return source;
	}

	public void setSource(InputStreamSource source) {
		this.source = source;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getUploaderId() {
		return uploaderId;
	}

	public void setUploaderId(String uploaderId) {
		this.uploaderId = uploaderId;
	}

}
