package com.example.demo.web.controller;

import com.example.demo.bll.utils.PicUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @author lk
 * @date 2021/9/3
 */
@RestController
public class PicController {

	@PostMapping("file/img")
	@ApiOperation(value = "图片上传接口(广告图片专用入口)")
	public void imgUploads() throws IOException {
		File localFile = new File("D:\\old_pic/");
		compressPicForScale(localFile);

	}

	private void compressPicForScale(File localFile) throws IOException {
		if (localFile.exists()) {
			for (File file : localFile.listFiles()) {
				if (file.isFile()) {
					byte[] allBytes = Files.readAllBytes(file.toPath());
					//压缩图片到指定120K以内,不管你的图片有多少兆,都不会超过120kb,精度还算可以,不会模糊
					byte[] bytes = PicUtils.compressPicForScale(allBytes, 100);
					ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
					//生成保存在服务器的图片名称，统一修改原后缀名为:jpg
					String rootPath = "D:\\new_pic/";
					String newFileName = file.getName() + ".jpg";
					PicUtils.uploadFileStream(rootPath,newFileName,inputStream);
				}else if (file.isDirectory()){
					compressPicForScale(file);
				}
			}

		}
	}
}
