package com.example.demo.bll.entity;

import cn.com.citycloud.hcs.common.data.jdbc.Entity;
import cn.com.citycloud.hcs.common.task.log.expiredclean.ExpiredClean;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.demo.bll.anon.SensitiveData;
import com.example.demo.bll.enums.FileStorageType;
import com.example.demo.bll.service.file.FileStorage;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lk
 * @date 2021/6/17
 */
@Data
@NoArgsConstructor
@SensitiveData
@TableName("file_upload_log")
public class FileUploadLog extends Entity<Long> implements FileStorage, Serializable, ExpiredClean<Long> {

	/** 返回提示信息 **/
	private boolean success;
	/** 上传失败消息 */
	private String failMessage;
	/** 文件名称 */
	private String name;
	/** 存储类型 */
	private FileStorageType storageType;
	/** 内容类型 */
	private String contentType;
	/** 文件路径 */
	private String pathId;
	/** 文件格式 */
	private String format;
	/** 文件大小 */
	private Long size;
	/** 是否有效 **/
	private boolean valid;
	/** 上传人 */
	private String uploaderId;
	/** 上传时间 */
	private Date uploadTime;
	/** 文件目录*/
	private String dir;

	@Override
	public Date getExpiredTime() {
		return uploadTime;
	}

	@Override
	public String getDir() {
		return dir;
	}

	public Long getPath() {
		return getId();
	}
}
