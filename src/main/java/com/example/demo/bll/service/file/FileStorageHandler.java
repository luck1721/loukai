package com.example.demo.bll.service.file;

import java.io.IOException;

/**
 * 文件存储结果处理器
 *
 * @date 2020年5月15日
 * @author huanglj
 */
@FunctionalInterface
public interface FileStorageHandler {

	public void handle(FileStorage fileStorage, IOException exception);

}
