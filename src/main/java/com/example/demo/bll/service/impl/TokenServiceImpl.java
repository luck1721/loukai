package com.example.demo.bll.service.impl;

/*import com.example.demo.bll.service.RedisService;
import com.example.demo.bll.service.TokenService;
import com.example.demo.bll.util.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

*//**
 * @author lk
 * @date 2020/5/5
 *//*
@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private RedisService redisService;


    *//**
     * 创建token
     *
     * @return
     *//*
    @Override
    public String createToken() {
        String str = UUIDUtils.getUUID();
        StringBuilder token = new StringBuilder();
        try {
            token.append("redis_").append(str);
            redisService.setEx(token.toString(), token.toString(),10000L);
            boolean notEmpty = StringUtils.isEmpty(token.toString());
            if (!notEmpty) {
                return token.toString();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }


    *//**
     * 检验token
     *
     * @param request
     * @return
     *//*
    @Override
    public boolean checkToken(HttpServletRequest request) throws Exception {

        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)) {// header中不存在token
            token = request.getParameter("token");
            if (StringUtils.isEmpty(token)) {// parameter中也不存在token
                throw new Exception("异常");
            }
        }

        if (!redisService.exists(token)) {
            throw new Exception("异常");
        }

        boolean remove = redisService.remove(token);
        if (!remove) {
            throw new Exception("异常");
        }
        return true;
    }
}*/
