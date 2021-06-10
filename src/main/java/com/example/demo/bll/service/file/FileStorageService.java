package com.example.demo.bll.service.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 文件存储服务接口
 *
 * @date 2019年8月22日
 * @author huanglj
 */
public interface FileStorageService {

	/**
	 * 查询文件存储基本信息
	 *
	 * @param fileStorage
	 * @return
	 * @date 2019年8月22日
	 * @author huanglj
	 */
	public FileStorage getFileStorageInfo(FileStorage fileStorage) throws IOException;

	/**
	 * 文件下载
	 *
	 * @param fileStorage
	 * @param outputStream
	 * @throws IOException
	 * @date 2019年8月22日
	 * @author huanglj
	 */
	public void downloadFile(FileStorage fileStorage, OutputStream outputStream) throws IOException;

	/**
	 * 文件下载
	 *
	 * @param fileStorage
	 * @return
	 * @throws IOException
	 * @date 2020年4月12日
	 * @author huanglj
	 */
	public InputStream downloadFileStream(FileStorage fileStorage) throws IOException;

	/**
	 * 文件上传
	 *
	 * @param fileStorage
	 * @param inputStream
	 * @return
	 * @throws IOException
	 * @date 2019年8月22日
	 * @author huanglj
	 */
	public FileStorage uploadFile(FileStorage fileStorage, InputStream inputStream) throws IOException;

	/**
	 * 文件上传
	 *
	 * @param fileStorage
	 * @param handler
	 * @return
	 * @throws IOException
	 * @date 2020年5月15日
	 * @author huanglj
	 */
	public OutputStream uploadFileStream(FileStorage fileStorage, FileStorageHandler handler) throws IOException;

	/**
	 * 更新文件
	 *
	 * @param fileStorage
	 * @param inputStream
	 * @return
	 * @throws IOException
	 * @date 2019年8月22日
	 * @author huanglj
	 */
	public void updateFile(FileStorage fileStorage, InputStream inputStream) throws IOException;

	/**
	 * 更新文件
	 *
	 * @param fileStorage
	 * @return
	 * @throws IOException
	 * @date 2020年5月15日
	 * @author huanglj
	 */
	public OutputStream updateFileStream(FileStorage fileStorage) throws IOException;

	/**
	 * 删除文件
	 *
	 * @param fileStorage
	 * @throws IOException
	 * @date 2019年8月22日
	 * @author huanglj
	 */
	public void deleteFile(FileStorage fileStorage) throws IOException;

}
