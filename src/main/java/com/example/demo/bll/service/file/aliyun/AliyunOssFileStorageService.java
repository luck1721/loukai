package com.example.demo.bll.service.file.aliyun;

import com.aliyun.oss.model.ObjectMetadata;
import com.example.demo.bll.enums.FileStorageType;
import com.example.demo.bll.service.file.FileStorage;
import com.example.demo.bll.service.file.FileStorageHandler;
import com.example.demo.bll.service.file.FileStorageService;
import com.example.demo.bll.service.file.seaweedfs.SimpleFileStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * 阿里云存储实现
 *
 * @author lk
 * @date 2020/8/14
 */

public class AliyunOssFileStorageService implements FileStorageService {
	private static Logger log = LoggerFactory.getLogger(AliyunOssFileStorageService.class);

    private AliyunOssOperations aliyunOssOperations;

	public AliyunOssOperations getAliyunOssOperations() {
		return aliyunOssOperations;
	}

	public void setAliyunOssOperations(AliyunOssOperations aliyunOssOperations) {
		this.aliyunOssOperations = aliyunOssOperations;
	}

	@Override
	public FileStorage getFileStorageInfo(FileStorage fileStorage) throws IOException {
		ObjectMetadata objectMetadata = aliyunOssOperations.getObjectMetadata(fileStorage.getPathId());
		if(null != objectMetadata){
			Map<String,String> userMetadata	= objectMetadata.getUserMetadata();
			return new SimpleFileStorage(userMetadata.get("storageType") ==null ? null : FileStorageType.valueOf(userMetadata.get("storageType")), objectMetadata.getContentType(),
					userMetadata.get("path"), userMetadata.get("filename"), objectMetadata.getContentLength());
		}
		return fileStorage;
	}

	@Override
	public void downloadFile(FileStorage fileStorage, OutputStream outputStream) throws IOException {
		aliyunOssOperations.downloadFile(fileStorage.getPathId(), outputStream);
	}

	@Override
	public InputStream downloadFileStream(FileStorage fileStorage) throws IOException {
		return aliyunOssOperations.downloadFileStream(fileStorage.getPathId());
	}

	@Override
	public FileStorage uploadFile(FileStorage fileStorage,  InputStream inputStream) throws IOException {
		String pathId =	aliyunOssOperations.uploadFile(fileStorage, inputStream);
		return new SimpleFileStorage(fileStorage.getStorageType(), fileStorage.getContentType(),
				pathId, fileStorage.getName(), fileStorage.getSize());
	}

	@Override
	public OutputStream uploadFileStream(FileStorage fileStorage, FileStorageHandler handler) throws IOException {
		return aliyunOssOperations.uploadFileStream(fileStorage, handler);
	}

	@Override
	public void updateFile(FileStorage fileStorage, InputStream inputStream) throws IOException {

	}

	@Override
	public OutputStream updateFileStream(FileStorage fileStorage) throws IOException {
		return null;
	}

	@Override
	public void deleteFile(FileStorage fileStorage) throws IOException {
		aliyunOssOperations.deleteFile(fileStorage.getPathId());
	}
}
