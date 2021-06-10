package com.example.demo.bll.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.bll.entity.SysBizLog;

/**
 * 业务日志(SysBizLog)表服务接口
 *
 * @author lk
 * @date 2020-08-18 13:44:56
 */
public interface SysBizLogService extends IService<SysBizLog> {

	void saveLog(SysBizLog log);

}
