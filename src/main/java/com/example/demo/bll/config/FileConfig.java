package com.example.demo.bll.config;

import com.aliyun.oss.OSSClient;
import com.example.demo.bll.enums.FileStorageType;
import com.example.demo.bll.service.file.aliyun.AliyunOssOptions;
import com.example.demo.bll.service.file.seaweedfs.SeaweedfsOptions;
import net.anumbrella.seaweedfs.core.FileSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * 文件存储服务配置
 *
 * @date 2019年12月24日
 * @author huanglj
 */
@Configuration
@ConfigurationProperties("file")
public class FileConfig {

	private FileStorageType storageType = FileStorageType.local;
	private String basePath;
	private String nameSuffix;
	private int maxNameLength = 255;
	private int bufferSize = 1024 * 4;
	private long maxSize = 524288000L;
	private List<String> allowedFormat;
	private List<String> contentExcludes = Arrays.asList(".sh'", ".sh\"", ".exe'", ".exe\"", ".js'", ".js\"", "<script", "</script");

	public FileStorageType getStorageType() {
		return storageType;
	}

	public void setStorageType(FileStorageType storageType) {
		this.storageType = storageType;
	}

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public String getNameSuffix() {
		return nameSuffix;
	}

	public void setNameSuffix(String nameSuffix) {
		this.nameSuffix = nameSuffix;
	}

	public int getMaxNameLength() {
		return maxNameLength;
	}

	public void setMaxNameLength(int maxNameLength) {
		this.maxNameLength = maxNameLength;
	}

	public int getBufferSize() {
		return bufferSize;
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	public long getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(long maxSize) {
		this.maxSize = maxSize;
	}

	public List<String> getAllowedFormat() {
		return allowedFormat;
	}

	public void setAllowedFormat(List<String> allowedFormat) {
		this.allowedFormat = allowedFormat;
	}

	public List<String> getContentExcludes() {
		return contentExcludes;
	}

	public void setContentExcludes(List<String> contentExcludes) {
		this.contentExcludes = contentExcludes;
	}

	@Configuration("seaweedfsConfig")
	@ConditionalOnClass(FileSource.class)
	@ConfigurationProperties("file.seaweedfs")
	public static class SeaweedfsConfig extends SeaweedfsOptions {
		private static final long serialVersionUID = -8013812445672341267L;

		private boolean enabled;

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}
	}

	@Configuration("aliyunOssConfig")
	@ConditionalOnClass(OSSClient.class)
	@ConfigurationProperties("file.aliyunoss")
	public static class AliyunOssConfig extends AliyunOssOptions {
		private static final long serialVersionUID = -8013812445567341267L;

		private boolean enabled;

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

	}


}
