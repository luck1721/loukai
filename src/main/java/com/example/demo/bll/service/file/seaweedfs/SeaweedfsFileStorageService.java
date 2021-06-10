package com.example.demo.bll.service.file.seaweedfs;

import com.example.demo.bll.service.file.FileStorage;
import com.example.demo.bll.service.file.FileStorageHandler;
import com.example.demo.bll.service.file.FileStorageService;
import net.anumbrella.seaweedfs.core.file.FileHandleStatus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 文件存储服务接口的Seaweedfs实现类
 *
 * @date 2019年8月22日
 * @author huanglj
 */
public class SeaweedfsFileStorageService implements FileStorageService {

	private SeaweedfsOperations seaweedfsOperations;

	public SeaweedfsOperations getSeaweedfsOperations() {
		return seaweedfsOperations;
	}

	public void setSeaweedfsOperations(SeaweedfsOperations seaweedfsOperations) {
		this.seaweedfsOperations = seaweedfsOperations;
	}

	@Override
	public FileStorage getFileStorageInfo(FileStorage fileStorage) throws IOException {
		try {
			FileHandleStatus fileHandleStatus = seaweedfsOperations.getFileStatus(fileStorage.getPathId());
			return new SimpleFileStorage(fileStorage.getStorageType(), fileHandleStatus.getContentType(),
					fileHandleStatus.getFileId(), fileHandleStatus.getFileName(), fileHandleStatus.getSize());
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	@Override
	public void downloadFile(FileStorage fileStorage, OutputStream outputStream) throws IOException {
		try {
			seaweedfsOperations.downloadFile(fileStorage.getPathId(), outputStream);
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	@Override
	public InputStream downloadFileStream(FileStorage fileStorage) throws IOException {
		try {
			return seaweedfsOperations.downloadFileStream(fileStorage.getPathId());
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	@Override
	public FileStorage uploadFile(FileStorage fileStorage, InputStream inputStream) throws IOException {
		try {
			FileHandleStatus fileHandleStatus = seaweedfsOperations.uploadFile(fileStorage.getName(), inputStream);
			return new SimpleFileStorage(fileStorage.getStorageType(), fileHandleStatus.getContentType(),
					fileHandleStatus.getFileId(), fileHandleStatus.getFileName(), fileHandleStatus.getSize());
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	@Override
	public OutputStream uploadFileStream(FileStorage fileStorage, FileStorageHandler handler) throws IOException {
		try {
			return seaweedfsOperations.uploadFileStream(fileStorage.getName(), (fileHandleStatus, exception) -> {
				if(handler != null) {
					handler.handle(fileHandleStatus == null ? null : new SimpleFileStorage(fileStorage.getStorageType(), fileHandleStatus.getContentType(),
							fileHandleStatus.getFileId(), fileHandleStatus.getFileName(), fileHandleStatus.getSize()), exception);
				}
			});
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	@Override
	public void updateFile(FileStorage fileStorage, InputStream inputStream) throws IOException {
		try {
			seaweedfsOperations.updateFile(fileStorage.getPathId(), fileStorage.getName(), inputStream);
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	@Override
	public OutputStream updateFileStream(FileStorage fileStorage) throws IOException {
		try {
			return seaweedfsOperations.updateFileStream(fileStorage.getPathId(), fileStorage.getName());
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	@Override
	public void deleteFile(FileStorage fileStorage) throws IOException {
		try {
			seaweedfsOperations.deleteFile(fileStorage.getPathId());
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

}
