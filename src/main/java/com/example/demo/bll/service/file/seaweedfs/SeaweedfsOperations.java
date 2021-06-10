package com.example.demo.bll.service.file.seaweedfs;

import net.anumbrella.seaweedfs.core.file.FileHandleStatus;
import net.anumbrella.seaweedfs.exception.SeaweedfsException;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Seaweed文件操作接口
 *
 * @date 2020年5月15日
 * @author huanglj
 */
public interface SeaweedfsOperations {

	/**
	 * 获取Seaweed配置对象
	 *
	 * @return
	 * @date 2020年5月15日
	 * @author huanglj
	 */
	public SeaweedfsOptions getSeaweedfsOptions();

	/**
	 * 获取文件状态信息
	 *
	 * @param fileId
	 * @return
	 * @throws SeaweedfsException
	 * @date 2020年5月15日
	 * @author huanglj
	 */
	public FileHandleStatus getFileStatus(String fileId) throws SeaweedfsException;

	/**
	 * 文件下载
	 *
	 * @param fileId
	 * @param outputStream
	 * @throws SeaweedfsException
	 * @date 2020年5月15日
	 * @author huanglj
	 */
	public void downloadFile(String fileId, OutputStream outputStream) throws SeaweedfsException;

	/**
	 * 文件下载
	 *
	 * @param fileId
	 * @return
	 * @throws SeaweedfsException
	 * @date 2020年5月15日
	 * @author huanglj
	 */
	public InputStream downloadFileStream(String fileId) throws SeaweedfsException;

	/**
	 * 文件上传
	 *
	 * @param fileName
	 * @param inputStream
	 * @return
	 * @throws SeaweedfsException
	 * @date 2020年5月15日
	 * @author huanglj
	 */
	public FileHandleStatus uploadFile(String fileName, InputStream inputStream) throws SeaweedfsException;

	/**
	 * 文件上传
	 *
	 * @param fileName
	 * @param handler
	 * @return
	 * @throws SeaweedfsException
	 * @date 2020年5月15日
	 * @author huanglj
	 */
	public OutputStream uploadFileStream(String fileName, SeaweedfsStatusHandler handler) throws SeaweedfsException;

	/**
	 * 更新文件
	 *
	 * @param fileId
	 * @param fileName
	 * @param inputStream
	 * @return
	 * @throws SeaweedfsException
	 * @date 2020年5月15日
	 * @author huanglj
	 */
	public FileHandleStatus updateFile(String fileId, String fileName, InputStream inputStream) throws SeaweedfsException;

	/**
	 * 更新文件
	 *
	 * @param fileId
	 * @param fileName
	 * @return
	 * @throws SeaweedfsException
	 * @date 2020年5月15日
	 * @author huanglj
	 */
	default OutputStream updateFileStream(String fileId, String fileName) throws SeaweedfsException {
		return updateFileStream(fileId, fileName, null);
	}

	/**
	 * 更新文件
	 *
	 * @param fileId
	 * @param fileName
	 * @param handler
	 * @return
	 * @throws SeaweedfsException
	 * @date 2020年5月15日
	 * @author huanglj
	 */
	public OutputStream updateFileStream(String fileId, String fileName, SeaweedfsStatusHandler handler) throws SeaweedfsException;

	/**
	 * 删除文件
	 *
	 * @param fileId
	 * @throws SeaweedfsException
	 * @date 2020年5月15日
	 * @author huanglj
	 */
	public void deleteFile(String fileId) throws SeaweedfsException;

}
