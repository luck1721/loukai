package com.example.demo.bll.service.file.aliyun;

import cn.com.citycloud.hcs.common.task.TaskExecutor;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.example.demo.bll.config.FileConfig;
import com.example.demo.bll.service.file.FileStorage;
import com.example.demo.bll.service.file.FileStorageHandler;
import com.example.demo.bll.service.file.seaweedfs.SimpleFileStorage;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Seaweed文件操作模版类
 *
 * @date 2020年5月15日
 * @author huanglj
 */
public class AliyunOssTemplate implements AliyunOssOperations {
	private static Logger log = LoggerFactory.getLogger(AliyunOssTemplate.class);

	/** 阿里云配置对象 */
	private AliyunOssOptions aliyunOssOptions;
	/** 阿里云执行客户端 */
	private OSS ossClient;
	/** 任务执行器 */
	private TaskExecutor taskExecutor;

	public AliyunOssTemplate(FileConfig.AliyunOssConfig aliyunOssConfig) {
		this.aliyunOssOptions = aliyunOssConfig;
		ossClient = new OSSClientBuilder().build(aliyunOssOptions.getEndpoint(), aliyunOssOptions.getAccessKeyId(), aliyunOssOptions.getAccessKeySecret());
	}

	public TaskExecutor getTaskExecutor() {
		return taskExecutor;
	}

	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}


	@Override
	public AliyunOssOptions getAliyunOssOptions() {
		return aliyunOssOptions;
	}

	@Override
	public ObjectMetadata getObjectMetadata(String fileId) {
		OSSObject ossObject = ossClient.getObject(aliyunOssOptions.getBucketName(),fileId);
		ObjectMetadata objectMetadata = null;
		if(null != ossObject){
			objectMetadata = ossObject.getObjectMetadata();
		}
		return objectMetadata;
	}

	@Override
	public String uploadFile(FileStorage fileStorage, InputStream inputStream) throws OSSException {
		String ObjectName = fileStorage.getName();
		if(StringUtils.isNotBlank(fileStorage.getDir())){
			ObjectName = ObjectName + File.separator + fileStorage.getDir();
		}
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(fileStorage.getContentType());
		metadata.setContentLength(fileStorage.getSize());
		metadata.setContentEncoding("utf-8");
		try {
			metadata.setContentDisposition("attachment;filename=" +
                    URLEncoder.encode(fileStorage.getName(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage());
		}
		Map<String,String> userMetadata = new HashMap<>();
		userMetadata.put("filename",fileStorage.getName());
		userMetadata.put("path",ObjectName);
		userMetadata.put("storageType",fileStorage.getStorageType().name());
		metadata.setUserMetadata(userMetadata);
		PutObjectResult result = ossClient.putObject(aliyunOssOptions.getBucketName(), ObjectName, inputStream, metadata);
		return ObjectName;
	}

	@Override
	public OutputStream uploadFileStream(FileStorage fileStorage, FileStorageHandler handler) throws IOException {
		try {
			PipedInputStream inputStream = new PipedInputStream();
			PipedOutputStream outputStream = new PipedOutputStream(inputStream);
			Runnable task = () -> {
				FileStorage handle = null;
				IOException exception = null;
				try {
					String path = uploadFile(fileStorage , inputStream);
					handle = new SimpleFileStorage(fileStorage.getStorageType(), fileStorage.getContentType(), path, fileStorage.getName(), fileStorage.getSize());
				}  catch (OSSException e) {
					exception = new IOException(e);
				} finally {
					try {
						inputStream.close();
					} catch (Exception e) {
					}
				}
				if(log.isDebugEnabled()) {
					log.debug("File[" + fileStorage.getName() + "] upload handle: " + handle + ".");
				}
				if(handler != null) {
					handler.handle(handle, exception);
				} else if(exception != null) {
					log.error("File[" + fileStorage.getName() + "] upload error!", exception);
				}
			};
			if(taskExecutor != null) {
				taskExecutor.execute(task);
			} else {
				new Thread(task).start();
			}
			return outputStream;
		}catch (IOException e) {
			throw e;
		}
	}

	@Override
	public void downloadFile(String fileId, OutputStream outputStream) throws OSSException {
		InputStream inputStream = downloadFileStream(fileId);
		// 数据读取完成后，获取的流必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
		try {
			IOUtils.copy(inputStream, outputStream);
		} catch (IOException e) {
			throw new OSSException(e.getMessage());
		}finally {
			try {
				inputStream.close();
			} catch (Exception e) {
			}
		}
	}

	@Override
	public InputStream downloadFileStream(String fileId) throws OSSException {
		OSSObject ossObject = ossClient.getObject(aliyunOssOptions.getBucketName(), fileId);
		return ossObject.getObjectContent();
	}

	@Override
	public void deleteFile(String fileId) throws OSSException {
		// 删除文件。如需删除文件夹，请将ObjectName设置为对应的文件夹名称。如果文件夹非空，则需要将文件夹下的所有object删除后才能删除该文件夹。
		ossClient.deleteObject(aliyunOssOptions.getBucketName(), fileId);
	}
}
