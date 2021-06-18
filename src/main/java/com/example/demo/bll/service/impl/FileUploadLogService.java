package com.example.demo.bll.service.impl;

import cn.com.citycloud.hcs.common.data.EntityServiceSupport;
import cn.com.citycloud.hcs.common.domain.exception.ExceptionParser;
import cn.com.citycloud.hcs.common.task.log.expiredclean.ExpiredCleanService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.bll.config.FileConfig;
import com.example.demo.bll.entity.FileUploadLog;
import com.example.demo.bll.entity.SysBizLog;
import com.example.demo.bll.mapper.FileUploadLogMapper;
import com.example.demo.bll.service.file.FileStorage;
import com.example.demo.bll.service.file.FileStorageService;
import com.example.demo.web.domain.FileUpload;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class FileUploadLogService  implements ExpiredCleanService<FileUploadLog, Long> {
	private static Logger log = LoggerFactory.getLogger(FileUploadLogService.class);
	private static String encoding = "ISO-8859-1";

	@Autowired
	private FileUploadLogMapper fileUploadLogMapper;
	@Autowired
	private FileStorageService fileStorageService;
	@Autowired
	private FileConfig fileConfig;

	@Override
	public List<FileUploadLog> getExpireds(Date expiredTimeLimit) {
		QueryWrapper<FileUploadLog> queryWrapper = new QueryWrapper<FileUploadLog>();
		queryWrapper.lt("upload_time",expiredTimeLimit);
		return fileUploadLogMapper.selectList(queryWrapper);
	}

	@Override
	public void clearExpire(FileUploadLog fileUploadLog) {
		try {
			fileStorageService.deleteFile(fileUploadLog);
			fileUploadLogMapper.deleteById(fileUploadLog.getId());
		} catch (Exception e) {
			log.warn("清理文件【" + fileUploadLog.getId() + "】失败！", e);
		}
	}
	public void deleteFile(FileUploadLog fileUploadLog) {
		try {
			fileStorageService.deleteFile(fileUploadLog);
			fileUploadLogMapper.deleteById(fileUploadLog.getId());
		} catch (Exception e) {
			log.warn("删除文件【" + fileUploadLog.getId() + "】失败！", e);
		}
	}

	public void modifyValid(String path, boolean valid) {
		QueryWrapper<SysBizLog> queryWrapper = new QueryWrapper<SysBizLog>();
		queryWrapper.eq("valid",valid);
		FileUploadLog log = fileUploadLogMapper.selectById(path);
		log.setValid(valid);
		fileUploadLogMapper.updateById(log);
	}

	public FileUploadLog findByPath(String path){
		return fileUploadLogMapper.selectById(path);
	}

	public void downloadFile(String path, OutputStream outputStream) throws IOException {
		downloadFile(findByPath(path), outputStream);
	}

	public void downloadFile(FileUploadLog fileUploadLog, OutputStream outputStream) throws IOException {
		Assert.notNull(fileUploadLog, "文件不存在！");
		fileStorageService.downloadFile(fileUploadLog, outputStream);
	}

	public InputStream downloadFileStream(String path) throws IOException {
		FileUploadLog fileUploadLog = findByPath(path);
		Assert.notNull(fileUploadLog, "文件不存在！");
		return fileStorageService.downloadFileStream(fileUploadLog);
	}

	public FileUploadLog uploadFile(FileUpload fileUpload, boolean valid){
    	List<FileUploadLog> logs = uploadFiles(Arrays.asList(fileUpload), valid);
    	return CollectionUtils.isEmpty(logs) ? null : logs.get(0);
    }

	public List<FileUploadLog> uploadFiles(List<FileUpload> fileUploads, boolean valid){
        List<FileUploadLog> fileUploadLogs = new ArrayList<>();
        if (CollectionUtils.isEmpty(fileUploads)) {
            return new ArrayList<>();
        }
        for (FileUpload fileUpload : fileUploads) {
        	FileUploadLog fileUploadLog = new FileUploadLog();
        	try {
        		if(uploadFile(fileUpload, fileUploadLog)) {
        			fileUploadLog.setSuccess(true);
        			fileUploadLog.setValid(valid);
        		}
			} catch (Exception e) {
				if(fileUploadLog.getStorageType() != null && StringUtils.isNotBlank(fileUploadLog.getPathId())) {
					try {
						fileStorageService.deleteFile(fileUploadLog);
					} catch (Exception ex) {
					}
				}
				if(fileUploadLogs.isEmpty()) {
					ReflectionUtils.rethrowRuntimeException(e);
				}
				log.error("文件" + fileUploadLog.getName() + "上传失败!", e);
				fileUploadLog.setFailMessage(ExceptionParser.parseAllErrorMessage(e));
			}
        	fileUploadLog.setUploadTime(new Date());
        	fileUploadLogs.add(fileUploadLog);
        }
		fileUploadLogs.forEach(log -> {
			fileUploadLogMapper.insert(log);
		});
        return fileUploadLogs;
    }

	private boolean uploadFile(FileUpload fileUpload, FileUploadLog fileUploadLog) throws Exception {
		fileUploadLog.setName(fileUpload.getName());
		fileUploadLog.setContentType(fileUpload.getContentType());
		fileUploadLog.setSize(fileUpload.getSize());
		fileUploadLog.setUploaderId(fileUpload.getUploaderId());
		Assert.hasText(fileUploadLog.getName(), "文件名不能为空！");
		Assert.isTrue(fileUploadLog.getName().length() <= fileConfig.getMaxNameLength(), "文件名过长！");
    	int dotIndex = fileUploadLog.getName().lastIndexOf(".");
    	Assert.isTrue(dotIndex > 0 && dotIndex < fileUploadLog.getName().length() - 1, "文件名格式不正确！");
    	fileUploadLog.setFormat(fileUploadLog.getName().substring(dotIndex + 1));
    	if(CollectionUtils.isNotEmpty(fileConfig.getAllowedFormat())) {
    		Assert.isTrue(fileConfig.getAllowedFormat().contains(fileUploadLog.getFormat().toLowerCase()), "文件名格式不正确！");
    	}
    	if(fileUploadLog.getSize() != null) {
    		Assert.isTrue(fileUploadLog.getSize() <= fileConfig.getMaxSize(), "文件过大！");
    	}
    	fileUploadLog.setStorageType(fileConfig.getStorageType());
    	FileStorage[] fileStorage = {null};
        try (InputStream inputStream = fileUpload.getSource().getInputStream();
        		OutputStream outputStream = fileStorageService.uploadFileStream(fileUploadLog, (handle, exception) -> {
        	if(handle == null || StringUtils.isBlank(handle.getPathId())) {
        		return;
        	}
        	fileUploadLog.setPathId(handle.getPathId());
        	fileStorage[0] = handle;
		})) {
        	saveFileContent(inputStream, outputStream);
        }
        if(fileStorage[0] == null) {
            return false;
        }
        fillFileContentTypeAndSize(fileStorage[0], fileUploadLog);
    	return true;
	}

	private void saveFileContent(InputStream inputStream, OutputStream outputStream) throws Exception {
		boolean filter = CollectionUtils.isNotEmpty(fileConfig.getContentExcludes());
    	byte[] last = new byte[fileConfig.getBufferSize() > 10 ? 10 : fileConfig.getBufferSize()];
    	byte[] buffer = new byte[fileConfig.getBufferSize()];
    	int length = 0;
    	while (-1 != (length = inputStream.read(buffer))) {
    		if(filter) {
    			byte[] content = new byte[last.length + length];
    			System.arraycopy(last, 0, content, 0, last.length);
    			System.arraycopy(buffer, 0, content, last.length, length);
    			String contentText = new String(content, encoding);
    			for (String exclude : fileConfig.getContentExcludes()) {
    				try {
    					Assert.isTrue(!contentText.contains(exclude), "文件内容不正确！");
    				} catch (Exception e) {
    					if(log.isDebugEnabled()) {
    						try {
    							log.debug("文件内容[" + contentText + "]含有非法内容[" + exclude + "]！");
    						} catch (Exception ex) {
    						}
    					}
    					throw e;
    				}
    			}
    			System.arraycopy(buffer, buffer.length - last.length, last, 0, last.length);
    		}
    		outputStream.write(buffer, 0, length);
        }
	}

	private void fillFileContentTypeAndSize(FileStorage fileStorage, FileUploadLog fileUploadLog) {
		FileStorage fileStorageInfo = null;
        String contentType = fileUploadLog.getContentType();
        if(StringUtils.isBlank(contentType)) {
        	contentType = fileStorage.getContentType();
        	if(StringUtils.isBlank(contentType)) {
        		try {
        			fileStorageInfo = fileStorageService.getFileStorageInfo(fileUploadLog);
                    if(fileStorageInfo != null) {
                    	contentType = fileStorageInfo.getContentType();
                    }
                } catch (Exception e) {
                }
        	}
        	fileUploadLog.setContentType(contentType);
        }
        Long size = fileStorage.getSize();
        if(size == null) {
        	if(fileStorageInfo == null) {
        		try {
        			fileStorageInfo = fileStorageService.getFileStorageInfo(fileUploadLog);
                } catch (Exception e) {
                }
        	}
        	if(fileStorageInfo != null) {
        		size = fileStorageInfo.getSize();
        	}
        }
        if(size != null) {
        	Assert.isTrue(size <= fileConfig.getMaxSize(), "文件过大！");
        	fileUploadLog.setSize(size);
        }
	}

}
