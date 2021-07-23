package com.example.demo.bll.service.file.aliyun;

import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ObjectMetadata;
import com.example.demo.bll.service.file.FileStorage;
import com.example.demo.bll.service.file.FileStorageHandler;
import net.anumbrella.seaweedfs.exception.SeaweedfsException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/** 阿里云文件操作接口
 * @Author: wangting
 * @Description:
 * @Date: Created in 10:11 2020/10/9
 */

public interface AliyunOssOperations {

	/**
	 * 阿里云oss配置对象
	 * @Author: wangting
	 * @Description:
	 * @Date: Created in 10:31 2020/10/9
	 */
	public AliyunOssOptions getAliyunOssOptions();


	/** 获取文件元数据
	 * 阿里云oss配置对象
	 * @Author: wangting
	 * @Description:
	 * @Date: Created in 10:31 2020/10/9
	 */
	public ObjectMetadata getObjectMetadata(String fileId);


	/**
	 * 文件上传
	 *
	 * @param fileStorage
	 * @param inputStream
	 * @throws OSSException
	 * @date 2020年5月15日
	 * @author huanglj
	 */
	public String uploadFile(FileStorage fileStorage, InputStream inputStream) throws OSSException;


	/**
	 * 文件上传
	 *
	 * @param fileStorage
	 * @param handler
	 * @throws SeaweedfsException
	 * @date 2020年5月15日
	 * @author huanglj
	 */
	public OutputStream uploadFileStream(FileStorage fileStorage, FileStorageHandler handler) throws IOException;


	/**
	 * 文件下载
	 *
	 * @param fileId
	 * @param outputStream
	 * @throws SeaweedfsException
	 * @date 2020年5月15日
	 * @author huanglj
	 */
	public void downloadFile(String fileId, OutputStream outputStream) throws OSSException;

	/**
	 * 文件下载
	 *
	 * @param fileId
	 * @return
	 * @throws OSSException
	 * @date 2020年5月15日
	 * @author huanglj
	 */
	public InputStream downloadFileStream(String fileId) throws OSSException;


	/**
	 * 删除文件
	 *
	 * @param fileId
	 * @throws OSSException
	 * @date 2020年5月15日
	 * @author huanglj
	 */
	public void deleteFile(String fileId) throws OSSException;

}
