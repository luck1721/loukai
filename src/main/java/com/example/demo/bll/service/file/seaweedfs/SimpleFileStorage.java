package com.example.demo.bll.service.file.seaweedfs;

import cn.com.citycloud.hcs.common.domain.BaseBean;
import com.example.demo.bll.enums.FileStorageType;
import com.example.demo.bll.service.file.FileStorage;

/**
 * 文件存储接口简单实现类
 *
 * @date 2020年5月16日
 * @author huanglj
 */
public class SimpleFileStorage extends BaseBean implements FileStorage {
	private static final long serialVersionUID = 5462584920340070793L;

	/** 存储类型 */
	private FileStorageType storageType;
	/** 内容类型 */
	private String contentType;
	/** 文件路径 */
	private String pathId;
	/** 文X件名称 */
	private String name;
	/** 文件大小 */
	private Long size;
	/** 文件目录 */
	private String dir;

	public SimpleFileStorage() {
	}

	public SimpleFileStorage(FileStorageType storageType, String contentType, String pathId, String name, Long size) {
		this.storageType = storageType;
		this.contentType = contentType;
		this.pathId = pathId;
		this.name = name;
		this.size = size;
	}

	public SimpleFileStorage(FileStorageType storageType, String contentType, String pathId, String name, Long size ,String  dir) {
		this.storageType = storageType;
		this.contentType = contentType;
		this.pathId = pathId;
		this.name = name;
		this.size = size;
		this.dir = dir;
	}

	public FileStorageType getStorageType() {
		return storageType;
	}

	public void setStorageType(FileStorageType storageType) {
		this.storageType = storageType;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getPathId() {
		return pathId;
	}

	public void setPathId(String pathId) {
		this.pathId = pathId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	@Override
	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}
}
