package com.example.demo.web.controller;

/*import com.example.demo.bll.anon.AutoIdempotent;
import com.example.demo.bll.service.TokenService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

*//**
 * @author lk
 * @date 2020/5/5
 *//*
@RestController
public class BusinessController {
    @Resource
    private TokenService tokenService;

    @PostMapping("/get/token")
    public String  getToken(){
        String token = tokenService.createToken();
        if (!StringUtils.isEmpty(token)) {
            return token;
        }
        return "";
    }

    @AutoIdempotent
    @PostMapping("/test/Idempotence")
    public String testIdempotence() {

        return "12345";
    }
}*/
