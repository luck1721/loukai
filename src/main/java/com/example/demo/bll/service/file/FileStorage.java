package com.example.demo.bll.service.file;

import com.example.demo.bll.enums.FileStorageType;

/**
 * 文件存储基本信息
 *
 * @date 2019年8月22日
 * @author huanglj
 */
public interface FileStorage {

	/**
	 * 存储类型
	 *
	 * @return
	 * @date 2019年8月22日
	 * @author huanglj
	 */
	public FileStorageType getStorageType();

	/**
	 * 文件内容类型
	 *
	 * @return
	 * @date 2019年8月22日
	 * @author huanglj
	 */
	public String getContentType();

	/**
	 * 文件路径ID
	 *
	 * @return
	 * @date 2019年8月22日
	 * @author huanglj
	 */
	public String getPathId();

	/**
	 * 文件名称
	 *
	 * @return
	 * @date 2019年8月22日
	 * @author huanglj
	 */
	public String getName();

	/**
	 * 文件大小
	 *
	 * @return
	 * @date 2019年8月22日
	 * @author huanglj
	 */
	public Long getSize();

	/**
	 * 文件目录
	 * @Author: wangting
	 * @Description:
	 * @Date: Created in 11:34 2020/10/9
	 */

	public String getDir();

}
