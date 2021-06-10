package com.example.demo.bll.service.file.seaweedfs;

import cn.com.citycloud.hcs.common.task.TaskExecutor;
import net.anumbrella.seaweedfs.core.FileSource;
import net.anumbrella.seaweedfs.core.FileTemplate;
import net.anumbrella.seaweedfs.core.file.FileHandleStatus;
import net.anumbrella.seaweedfs.core.http.StreamResponse;
import net.anumbrella.seaweedfs.exception.SeaweedfsException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ReflectionUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * Seaweed文件操作模版类
 *
 * @date 2020年5月15日
 * @author huanglj
 */
public class SeaweedfsTemplate implements SeaweedfsOperations {
	private static Logger log = LoggerFactory.getLogger(SeaweedfsTemplate.class);

	/** Seaweed配置对象 */
	private final SeaweedfsOptions seaweedfsOptions;
	/** 文件源对象 */
	private final FileSource fileSource;
	/** 文件模版 */
	private final FileTemplate fileTemplate;
	/** 任务执行器 */
	private TaskExecutor taskExecutor;

	public SeaweedfsTemplate(SeaweedfsOptions seaweedfsOptions) {
		this.seaweedfsOptions = seaweedfsOptions;
		fileSource = new FileSource();
		try {
			BeanUtils.copyProperties(seaweedfsOptions, fileSource);
			fileSource.startup();
		} catch (Exception e) {
			ReflectionUtils.rethrowRuntimeException(e);
		}
		fileTemplate = new FileTemplate(fileSource.getConnection());
	}

	public TaskExecutor getTaskExecutor() {
		return taskExecutor;
	}

	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	@Override
	public SeaweedfsOptions getSeaweedfsOptions() {
		return seaweedfsOptions;
	}

	@Override
	public FileHandleStatus getFileStatus(String fileId) throws SeaweedfsException {
		try {
			return fileTemplate.getFileStatus(fileId);
		} catch (SeaweedfsException e) {
			throw e;
		} catch (Exception e) {
			throw new SeaweedfsException(e);
		}
	}

	@Override
	public void downloadFile(String fileId, OutputStream outputStream) throws SeaweedfsException {
		InputStream inputStream = downloadFileStream(fileId);
		try {
			IOUtils.copy(inputStream, outputStream);
		} catch (Exception e) {
			throw new SeaweedfsException(e);
		} finally {
			try {
				outputStream.close();
			} catch (Exception e) {
			}
			try {
				inputStream.close();
			} catch (Exception e) {
			}
		}
	}

	@Override
	public InputStream downloadFileStream(String fileId) throws SeaweedfsException {
		try {
			StreamResponse streamResponse = fileTemplate.getFileStream(fileId);
			return streamResponse == null ? null : streamResponse.getInputStream();
		} catch (SeaweedfsException e) {
			throw e;
		} catch (Exception e) {
			throw new SeaweedfsException(e);
		}
	}

	@Override
	public FileHandleStatus uploadFile(String fileName, InputStream inputStream) throws SeaweedfsException {
		try {
			return fileTemplate.saveFileByStream(fileName, inputStream);
		} catch (SeaweedfsException e) {
			throw e;
		} catch (Exception e) {
			throw new SeaweedfsException(e);
		} finally {
			try {
				inputStream.close();
			} catch (Exception e) {
			}
		}
	}

	@Override
	public OutputStream uploadFileStream(String fileName, SeaweedfsStatusHandler handler) throws SeaweedfsException {
		try {
			PipedInputStream inputStream = new PipedInputStream();
			PipedOutputStream outputStream = new PipedOutputStream(inputStream);
			Runnable task = () -> {
				FileHandleStatus status = null;
				SeaweedfsException exception = null;
				try {
					status = uploadFile(fileName, inputStream);
				} catch (SeaweedfsException e) {
					exception = e;
				} catch (Exception e) {
					exception = new SeaweedfsException(e);
				} finally {
					try {
						inputStream.close();
					} catch (Exception e) {
					}
				}
				if(log.isDebugEnabled()) {
					log.debug("File[" + fileName + "] upload status: " + status + ".");
				}
				if(handler != null) {
					handler.handle(status, exception);
				} else if(exception != null) {
					log.error("File[" + fileName + "] upload error!", exception);
				}
			};
			if(taskExecutor != null) {
				taskExecutor.execute(task);
			} else {
				new Thread(task).start();
			}
			return outputStream;
		} catch (Exception e) {
			throw new SeaweedfsException(e);
		}
	}

	@Override
	public FileHandleStatus updateFile(String fileId, String fileName, InputStream inputStream) throws SeaweedfsException {
		try {
			return fileTemplate.updateFileByStream(fileId, fileName, inputStream);
		} catch (SeaweedfsException e) {
			throw e;
		} catch (Exception e) {
			throw new SeaweedfsException(e);
		} finally {
			try {
				inputStream.close();
			} catch (Exception e) {
			}
		}
	}

	@Override
	public OutputStream updateFileStream(String fileId, String fileName, SeaweedfsStatusHandler handler)
			throws SeaweedfsException {
		try {
			PipedInputStream inputStream = new PipedInputStream();
			PipedOutputStream outputStream = new PipedOutputStream(inputStream);
			Runnable task = () -> {
				FileHandleStatus status = null;
				SeaweedfsException exception = null;
				try {
					status = updateFile(fileId, fileName, inputStream);
				} catch (SeaweedfsException e) {
					exception = e;
				} catch (Exception e) {
					exception = new SeaweedfsException(e);
				} finally {
					try {
						inputStream.close();
					} catch (Exception e) {
					}
				}
				if(log.isDebugEnabled()) {
					log.debug("File[" + fileId + "] update status: " + status + ".");
				}
				if(handler != null) {
					handler.handle(status, exception);
				} else if(exception != null) {
					log.error("File[" + fileId + "] update error!", exception);
				}
			};
			if(taskExecutor != null) {
				taskExecutor.execute(task);
			} else {
				new Thread(task).start();
			}
			return outputStream;
		} catch (Exception e) {
			throw new SeaweedfsException(e);
		}
	}

	@Override
	public void deleteFile(String fileId) throws SeaweedfsException {
		try {
			fileTemplate.deleteFile(fileId);
		} catch (SeaweedfsException e) {
			throw e;
		} catch (Exception e) {
			throw new SeaweedfsException(e);
		}
	}

}
