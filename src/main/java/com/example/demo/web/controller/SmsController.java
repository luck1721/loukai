package com.example.demo.web.controller;

import com.example.demo.bll.service.notice.Notice;
import com.example.demo.bll.service.notice.NoticeService;
import com.example.demo.bll.service.notice.SimpleMultiNoticeType;
import com.example.demo.bll.service.notice.SimpleNotice;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lk
 * @date 2021/2/4
 */
@RestController
@RequestMapping("/api")
public class SmsController {
	private static final Logger logger = LoggerFactory.getLogger(EmailController.class);

	@Autowired
	private NoticeService noticeService;

	@GetMapping("/sendSms")
	public void sendSms() throws JsonProcessingException {
		List receivers = new ArrayList();
		receivers.add("18358507511");
		receivers.add("13282801229");
		Notice notice = new SimpleNotice("神奇速记","神奇速记",receivers,null,null,"董老师,今天浇水了吗",null,null);
		noticeService.send(new SimpleMultiNoticeType(),notice);
	}

}
