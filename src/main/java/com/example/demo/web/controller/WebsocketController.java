package com.example.demo.web.controller;

import com.example.demo.bll.websocket.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * @author lk
 * @date 2021/3/9
 */
public class WebsocketController {
	@Autowired
	WebSocket webSocket;

	@ResponseBody
	@GetMapping("/sendTo")
	public String sendTo(@RequestParam("userId") String userId, @RequestParam("msg") String msg) throws IOException {

		webSocket.sendMessageTo(msg,userId);

		return "推送成功";
	}

	@ResponseBody
	@GetMapping("/sendAll")
	public String sendAll(@RequestParam("msg") String msg) throws IOException {

		String fromUserId="system";//其实没用上
		webSocket.sendMessageAll(msg,fromUserId);

		return "推送成功";
	}
}
