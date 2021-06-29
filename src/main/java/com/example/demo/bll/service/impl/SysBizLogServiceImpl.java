package com.example.demo.bll.service.impl;

import cn.com.citycloud.hcs.common.task.log.expiredclean.ExpiredCleanService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.bll.entity.SysBizLog;
import com.example.demo.bll.mapper.SysBizLogMapper;
import com.example.demo.bll.service.SysBizLogService;
import com.example.demo.bll.util.CommonQueryPageUtils;
import com.example.demo.web.domain.param.PageParam;
import com.example.demo.web.domain.param.QueryPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author lk
 * @date 2021/1/20
 */
@Service("sysBizLogService")
public class SysBizLogServiceImpl extends ServiceImpl<SysBizLogMapper, SysBizLog> implements SysBizLogService, ExpiredCleanService<SysBizLog, String> {

	@Autowired
	private SysBizLogMapper sysBizLogMapper;

	@Override
	public void saveLog(SysBizLog log) {
		 save(log);
	}

	@Override
	public List<SysBizLog> getExpireds(Date date) {
		QueryWrapper<SysBizLog> queryWrapper = new QueryWrapper<SysBizLog>();
		queryWrapper.lt("action_time",date);
		return list(queryWrapper);
	}

	@Override
	public void clearExpire(SysBizLog sysBizLog) {
		removeById(sysBizLog.getLogId());
	}

	public IPage<SysBizLog> getExpiresByDate(PageParam pageParam) {
		QueryWrapper<SysBizLog> queryWrapper = new QueryWrapper<SysBizLog>();
		queryWrapper.lt("action_time",new Date());
		QueryPage queryPage = CommonQueryPageUtils.commonQueryPage(pageParam, queryWrapper);
		return sysBizLogMapper.selectPage(queryPage.getPage(), queryPage.getQueryWrapper());
	}
}
