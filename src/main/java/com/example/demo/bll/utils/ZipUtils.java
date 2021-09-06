package com.example.demo.bll.utils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author lk
 * @date 2021/2/1
 */
public class ZipUtils {
	public static final int EOF = -1;
	public static final int BUFFER_SIZE = 1024;

	/**
	 * @param files:
	 * @param outputStream:
	 * @Author: wangting
	 * @Description: 多文件压缩下载
	 * @Date: Created in 11:08 2020/6/1
	 * @return: void
	 */
	public static void zipFiles(List<File> files, OutputStream outputStream) throws IOException {
		try (ZipOutputStream zipOutStream = new ZipOutputStream(new BufferedOutputStream(outputStream))) {
			// -- 设置压缩方法
			zipOutStream.setMethod(ZipOutputStream.DEFLATED);
			// 将多文件循环写入压缩包
			for (int i = 0; i < files.size(); i++) {
				File file = files.get(i);
				// 添加ZipEntry，并ZipEntry中写入文件流，这里，加上i是防止要下载的文件有重名的导致下载失败
				zipOutStream.putNextEntry(new ZipEntry((i + 1) + "_" + file.getName()));
				FileInputStream fileInputStream = new FileInputStream(file);
				BufferedInputStream bi = new BufferedInputStream(fileInputStream);
				byte[] bytes = new byte[BUFFER_SIZE];
				while (bi.read(bytes, 0, BUFFER_SIZE) != EOF) {
					zipOutStream.write(bytes);
				}
				fileInputStream.close();
				zipOutStream.closeEntry();
			}
		} finally {
			try {
				if (Objects.nonNull(outputStream)) {
					outputStream.close();
				}
			} catch (IOException e) {

			}
		}
	}


	/**
	 * @Author: wangting
	 * @Description: 压缩目录
	 * @Date: Created in 17:14 2020/6/5
	 * @param directoryName:
	 * @param outputStream:
	 * @return: void
	 */
	public static void zipDir(String directoryName, OutputStream outputStream) throws IOException {
		try (ZipOutputStream zipOutStream = new ZipOutputStream(new BufferedOutputStream(outputStream))) {
			zipOutStream.setMethod(ZipOutputStream.DEFLATED);
			File dir = new File(directoryName);
			if(!dir.exists()){
				return;
			}
			List<File> files = (List<File>) FileUtils.listFiles(dir,null,true);
			if(CollectionUtils.isEmpty(files)){
				return;
			}
			// 针对window 系统处理
			directoryName = directoryName.replaceAll("\\\\","/");
			if(!directoryName.startsWith("/")){
				directoryName = "/" + directoryName;
			}
			for (File file :files) {
				// 针对window 系统处理
				String filePath = file.getAbsolutePath().replaceAll("\\\\","/");
				if(!filePath.startsWith("/")){
					filePath = "/" + filePath;
				}
				// 压缩文件分目录
				String name  =filePath.replace(directoryName + "/","");
				zipOutStream.putNextEntry(new ZipEntry(name));
				FileInputStream fileInputStream = new FileInputStream(file);
				BufferedInputStream bi = new BufferedInputStream(fileInputStream);
				byte[] bytes = new byte[BUFFER_SIZE];
				while (bi.read(bytes, 0, BUFFER_SIZE) != EOF) {
					zipOutStream.write(bytes);
				}
				fileInputStream.close();
				zipOutStream.closeEntry();
			}
		} finally {
			try {
				if (Objects.nonNull(outputStream)) {
					outputStream.close();
				}
			} catch (IOException e) {
			}
		}

	}
}
