package com.example.demo.web.controller;

import com.example.demo.bll.service.OrderService;
import com.example.demo.web.domain.OrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lk
 * @date 2020/4/30
 */
@RestController
@RequestMapping("/api/v1")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/order")
    public String getOrder() {
        OrderDTO dto = new OrderDTO();
        dto.setType("2");
        return orderService.handle(dto);
    }

}
