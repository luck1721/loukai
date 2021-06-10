package com.example.demo.bll.service.file.seaweedfs;

import net.anumbrella.seaweedfs.core.file.FileHandleStatus;
import net.anumbrella.seaweedfs.exception.SeaweedfsException;

/**
 * Seaweed文件状态处理器
 *
 * @date 2020年5月15日
 * @author huanglj
 */
@FunctionalInterface
public interface SeaweedfsStatusHandler {

	public void handle(FileHandleStatus status, SeaweedfsException exception);

}
