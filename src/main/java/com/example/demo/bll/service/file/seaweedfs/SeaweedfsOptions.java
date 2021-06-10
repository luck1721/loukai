package com.example.demo.bll.service.file.seaweedfs;

import cn.com.citycloud.hcs.common.domain.BaseBean;

/**
 * Seaweed配置对象
 *
 * @date 2020年5月15日
 * @author huanglj
 */
public class SeaweedfsOptions extends BaseBean {
	private static final long serialVersionUID = 3982835003535289958L;

	private String host = "localhost";
	private int port = 9333;
	private int connectionTimeout = 10000;
	private int statusExpiry = 60;
	private int maxConnection = 100;
	private int idleConnectionExpiry = 60;
	private int maxConnectionsPreRoute = 1000;
	private boolean enableLookupVolumeCache = true;
	private int lookupVolumeCacheExpiry = 120;
	private int lookupVolumeCacheEntries = 100;
	private boolean enableFileStreamCache = true;
	private int fileStreamCacheEntries = 1000;
	private long fileStreamCacheSize = 8192;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public int getStatusExpiry() {
		return statusExpiry;
	}

	public void setStatusExpiry(int statusExpiry) {
		this.statusExpiry = statusExpiry;
	}

	public int getMaxConnection() {
		return maxConnection;
	}

	public void setMaxConnection(int maxConnection) {
		this.maxConnection = maxConnection;
	}

	public int getIdleConnectionExpiry() {
		return idleConnectionExpiry;
	}

	public void setIdleConnectionExpiry(int idleConnectionExpiry) {
		this.idleConnectionExpiry = idleConnectionExpiry;
	}

	public int getMaxConnectionsPreRoute() {
		return maxConnectionsPreRoute;
	}

	public void setMaxConnectionsPreRoute(int maxConnectionsPreRoute) {
		this.maxConnectionsPreRoute = maxConnectionsPreRoute;
	}

	public boolean isEnableLookupVolumeCache() {
		return enableLookupVolumeCache;
	}

	public void setEnableLookupVolumeCache(boolean enableLookupVolumeCache) {
		this.enableLookupVolumeCache = enableLookupVolumeCache;
	}

	public int getLookupVolumeCacheExpiry() {
		return lookupVolumeCacheExpiry;
	}

	public void setLookupVolumeCacheExpiry(int lookupVolumeCacheExpiry) {
		this.lookupVolumeCacheExpiry = lookupVolumeCacheExpiry;
	}

	public int getLookupVolumeCacheEntries() {
		return lookupVolumeCacheEntries;
	}

	public void setLookupVolumeCacheEntries(int lookupVolumeCacheEntries) {
		this.lookupVolumeCacheEntries = lookupVolumeCacheEntries;
	}

	public boolean isEnableFileStreamCache() {
		return enableFileStreamCache;
	}

	public void setEnableFileStreamCache(boolean enableFileStreamCache) {
		this.enableFileStreamCache = enableFileStreamCache;
	}

	public int getFileStreamCacheEntries() {
		return fileStreamCacheEntries;
	}

	public void setFileStreamCacheEntries(int fileStreamCacheEntries) {
		this.fileStreamCacheEntries = fileStreamCacheEntries;
	}

	public long getFileStreamCacheSize() {
		return fileStreamCacheSize;
	}

	public void setFileStreamCacheSize(long fileStreamCacheSize) {
		this.fileStreamCacheSize = fileStreamCacheSize;
	}

}
