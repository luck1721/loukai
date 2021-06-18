package com.example.demo.bll.service.file.local;

import com.example.demo.bll.config.FileConfig;
import com.example.demo.bll.service.file.FileStorage;
import com.example.demo.bll.service.file.FileStorageHandler;
import com.example.demo.bll.service.file.FileStorageService;
import com.example.demo.bll.service.file.seaweedfs.SimpleFileStorage;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.*;
import java.util.UUID;

/**
 * 文件存储服务接口的本地存储实现类
 *
 * @date 2019年12月24日
 * @author huanglj
 */
@Service
public class LocalFileStorageService implements FileStorageService {

	@Autowired
	private FileConfig fileConfig;

	@Override
	public FileStorage getFileStorageInfo(FileStorage fileStorage) throws IOException {
		try {
			File file = new File(fileStorage.getPathId());
			return new SimpleFileStorage(fileStorage.getStorageType(), null, fileStorage.getPathId(), file.getName(), file.length());
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	@Override
	public void downloadFile(FileStorage fileStorage, OutputStream outputStream) throws IOException {
		InputStream inputStream = null;
		try {
			inputStream = downloadFileStream(fileStorage);
			IOUtils.copy(inputStream, outputStream);
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw new IOException(e);
		} finally {
			if(outputStream != null) {
				try {
					outputStream.close();
				} catch (Exception e) {
				}
			}
			if(inputStream != null) {
				try {
					inputStream.close();
				} catch (Exception e) {
				}
			}
		}
	}

	@Override
	public InputStream downloadFileStream(FileStorage fileStorage) throws IOException {
		return new FileInputStream(fileStorage.getPathId());
	}

	@Override
	public FileStorage uploadFile(FileStorage fileStorage, InputStream inputStream) throws IOException {
		OutputStream outputStream = null;
		try {
			FileStorage[] ret = {null};
			outputStream = uploadFileStream(fileStorage, (handle, exception) -> {
				ret[0] = handle;
			});
			IOUtils.copy(inputStream, outputStream);
			return ret[0];
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw new IOException(e);
		} finally {
			if(outputStream != null) {
				try {
					outputStream.close();
				} catch (Exception e) {
				}
			}
			if(inputStream != null) {
				try {
					inputStream.close();
				} catch (Exception e) {
				}
			}
		}
	}

	@Override
	public OutputStream uploadFileStream(FileStorage fileStorage, FileStorageHandler handler) throws IOException {
		String path = fileConfig.getBasePath();
		Assert.hasText(path, "file storage base path con't be null!");
		if(!path.endsWith("/")) {
			path += "/";
		}
		path += UUID.randomUUID().toString().replaceAll("\\-", "");
		String original = fileStorage.getName();
		int idx = original.lastIndexOf('.');
		if(idx >= 0 && idx < original.length() - 1) {
			path += original.substring(idx);
		}
		if(StringUtils.isNotBlank(fileConfig.getNameSuffix())) {
			path += fileConfig.getNameSuffix();
		}
		OutputStream outputStream = null;
		FileStorage handle = null;
		IOException exception = null;
		try {
			File file = new File(path);
			File parent = file.getParentFile();
			if (parent != null && !parent.exists()) {
				parent.mkdirs();
			}
			if(!file.exists()) {
				file.createNewFile();
			}
			outputStream = new FileOutputStream(file);
			handle = new SimpleFileStorage(fileStorage.getStorageType(), null, path, original, null);
		} catch (IOException e) {
			exception = e;
		} catch (Exception e) {
			exception = new IOException(e);
		}
		if(handler != null) {
			handler.handle(handle, exception);
		}
		if(exception != null) {
			throw exception;
		}
		return outputStream;
	}

	@Override
	public void updateFile(FileStorage fileStorage, InputStream inputStream) throws IOException {
		OutputStream outputStream = null;
		try {
			outputStream = updateFileStream(fileStorage);
			IOUtils.copy(inputStream, outputStream);
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw new IOException(e);
		} finally {
			if(outputStream != null) {
				try {
					outputStream.close();
				} catch (Exception e) {
				}
			}
			if(inputStream != null) {
				try {
					inputStream.close();
				} catch (Exception e) {
				}
			}
		}
	}

	@Override
	public OutputStream updateFileStream(FileStorage fileStorage) throws IOException {
		try {
			File file = new File(fileStorage.getPathId());
			return new FileOutputStream(file);
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	@Override
	public void deleteFile(FileStorage fileStorage) throws IOException {
		try {
			File file = new File(fileStorage.getPathId());
			if(file.exists()) {
				file.delete();
			}
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

}
