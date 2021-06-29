package com.example.demo.web.controller;

import org.apache.commons.io.IOUtils;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author lk
 * @date 2020/5/15
 */
@RestController
@RequestMapping("/api")
public class FastDFSUploadController {

	/**
	 * 上传文件到FastDFS
	 *
	 * @param file
	 */
	@RequestMapping(value = "/fastDFSUpload", method = RequestMethod.POST)
	@ResponseBody
	public void fastDFSUpload(MultipartFile file) {

		String ext_Name = file.getOriginalFilename().split("\\.")[1];
		String file_Name = file.getOriginalFilename().split("\\.")[0];

		byte[] bytes = null;
		try {
			bytes = file.getBytes();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String filePath = uploadFile(bytes, ext_Name, file_Name);
	}

	/**
	 * FastDFS实现文件下载
	 *
	 * @param filePath
	 */
	@RequestMapping(value = "/fastDFSDownload", method = RequestMethod.GET)
	@ResponseBody
	public void fastDFSDownload(String filePath) {
		try {
			ClientGlobal.initByProperties("application.properties");

			// 链接FastDFS服务器，创建tracker和Stroage
			TrackerClient trackerClient = new TrackerClient();
			TrackerServer trackerServer = trackerClient.getTrackerServer();

			String storageServerIp = getStorageServerIp(trackerClient, trackerServer);
			StorageServer storageServer = getStorageServer(storageServerIp);
			StorageClient storageClient = new StorageClient(trackerServer, storageServer);
			byte[] b = storageClient.download_file("group1", filePath);
			if (b == null) {
				throw new IOException("文件" + filePath + "不存在");
			}

			String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
			FileOutputStream fileOutputStream = new FileOutputStream("c://" + fileName);
			IOUtils.write(b, fileOutputStream);
			fileOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * FastDFS获取将上传文件信息
	 */
	@RequestMapping(value = "/fastDFSGetFileInfo", method = RequestMethod.GET)
	@ResponseBody
	public void fastDFSGetFileInfo(String filePath) {
		try {
			// 链接FastDFS服务器，创建tracker和Stroage
			ClientGlobal.initByProperties("application.properties");
			TrackerClient trackerClient = new TrackerClient();
			TrackerServer trackerServer = trackerClient.getTrackerServer();

			String storageServerIp = getStorageServerIp(trackerClient, trackerServer);
			StorageServer storageServer = getStorageServer(storageServerIp);
			StorageClient storageClient = new StorageClient(trackerServer, storageServer);

			FileInfo fi = storageClient.get_file_info("group1", filePath);
			if (fi == null) {
				throw new IOException("文件" + filePath + "不存在");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * FastDFS获取文件名称
	 */
	@RequestMapping(value = "/fastDFSGetFileName", method = RequestMethod.GET)
	@ResponseBody
	public void fastDFSGetFileName(String filePath) {
		try {
			// 链接FastDFS服务器，创建tracker和Stroage
			ClientGlobal.initByProperties("application.properties");
			TrackerClient trackerClient = new TrackerClient();
			TrackerServer trackerServer = trackerClient.getTrackerServer();

			String storageServerIp = getStorageServerIp(trackerClient, trackerServer);
			StorageServer storageServer = getStorageServer(storageServerIp);
			StorageClient storageClient = new StorageClient(trackerServer, storageServer);

			NameValuePair[] nvps = storageClient.get_metadata("group1", filePath);
			if (nvps == null) {
				throw new IOException("文件" + filePath + "不存在");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * FastDFS实现删除文件
	 */
	@RequestMapping(value = "/fastDFSDelete", method = RequestMethod.GET)
	@ResponseBody
	public void fastDFSDelete(String filePath) {
		try {
			// 链接FastDFS服务器，创建tracker和Stroage
			ClientGlobal.initByProperties("application.properties");
			TrackerClient trackerClient = new TrackerClient();
			TrackerServer trackerServer = trackerClient.getTrackerServer();

			String storageServerIp = getStorageServerIp(trackerClient, trackerServer);
			StorageServer storageServer = getStorageServer(storageServerIp);
			StorageClient storageClient = new StorageClient(trackerServer, storageServer);

			int i = storageClient.delete_file("group1", filePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String uploadFile(byte[] byteFile, String ext_file, String file_Name) {
		TrackerServer trackerServer;
		StorageServer storageServer = null;
		String[] strings = null;
		try {
			// 初始化文件资源
			ClientGlobal.initByProperties("application.properties");
			//2.创建TrackerClient对象
			TrackerClient trackerClient = new TrackerClient();
			//3.创建TrackerServer对象
			trackerServer = trackerClient.getTrackerServer();
			//4.创建StorageServler对象
			storageServer = trackerClient.getStoreStorage(trackerServer);
			//5.创建StorageClient对象，这个对象完成对文件的操作
			StorageClient storageClient = new StorageClient(trackerServer, null);
			strings = storageClient.upload_file(byteFile, ext_file, null);
		} catch (IOException | MyException e) {
			e.printStackTrace();
		}
		return strings.toString();
	}

	/**
	 * 得到Storage服务
	 *
	 * @param storageIp
	 * @return 返回Storage服务
	 */
	private static StorageServer getStorageServer(String storageIp) {
		StorageServer storageServer = null;
		if (storageIp != null && !("").equals(storageIp)) {
			try {
				// ip port store_path下标
				storageServer = new StorageServer(storageIp, 23000, 1);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return storageServer;
	}

	/**
	 * 获得可用的storage IP
	 *
	 * @param trackerClient
	 * @param trackerServer
	 * @return 返回storage IP
	 */
	private static String getStorageServerIp(TrackerClient trackerClient, TrackerServer trackerServer) {
		String storageIp = null;
		if (trackerClient != null && trackerServer != null) {
			try {
				StorageServer storageServer = trackerClient.getStoreStorage(trackerServer, "group1");
				storageIp = storageServer.getInetSocketAddress().getAddress().getHostAddress();
			} catch (IOException | MyException e) {
				e.printStackTrace();
			}
		}
		return storageIp;
	}

}
