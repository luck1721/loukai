package com.example.demo.bll.service.impl;

import com.example.demo.bll.config.TinyUrlConfig;
import com.example.demo.bll.utils.TinyUrlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TinyUrlLogService {

	@Autowired
	private TinyUrlConfig tinyUrlConfig;

	public String generateTinyUrl(String url) {
		if(StringUtils.isBlank(url)) {
			return null;
		}
		String tinyId = TinyUrlUtils.generateTinyUrlId();
		String tinyUrl = tinyUrlConfig.getBasePath() + "/" + tinyId;
		if(!tinyUrl.contains("://")) {
			tinyUrl = tinyUrlConfig.getHost() + tinyUrl;
		}
		return tinyUrl;
	}

}
