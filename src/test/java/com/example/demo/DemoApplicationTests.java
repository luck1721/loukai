package com.example.demo;

import com.example.demo.bll.cache.RedisOperate;
import com.example.demo.bll.exception.ApplicationException;
import com.example.demo.bll.service.MapperProxyFactory;
import com.example.demo.bll.service.impl.HttpAPIService;
import com.example.demo.bll.util.MapperUtils;
import com.example.demo.bll.vo.EventTypeVO;
import com.example.demo.bll.vo.EventVO;
import com.example.demo.bll.vo.GridEventCreateVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

    @Resource
    private RedisOperate redisOperate;

    @Resource
    private MapperProxyFactory invoker;

    @Resource
    private HttpAPIService httpAPIService;

    @Test
    public void contextLoads() {
        redisOperate.set("11", "888888");
        System.out.println(redisOperate.get("11"));
    }

    @Test
    public void testException() {
        if (2 > 1)  {
            throw new ApplicationException("异常啊啊啊");
        }

    }

    @Test
    public void testPost() throws Exception {
        GridEventCreateVO eventCreateVO = new GridEventCreateVO();
        eventCreateVO.setThirtyPartyEventId("111");
        eventCreateVO.setThirtyPartyEventType("");
        eventCreateVO.setTitle("9999");
        eventCreateVO.setDescription("567");
        eventCreateVO.setStatus(0);
        eventCreateVO.setCollector("1");
        eventCreateVO.setCollectTime(new Date());
        eventCreateVO.setLimitingTime(new Date());
        eventCreateVO.setProcessor("333");
        EventTypeVO typeVO = new EventTypeVO();
        typeVO.setFirstLevel("1");
        EventVO eventVO = new EventVO();
        eventVO.setEvent(eventCreateVO);
        eventVO.setEventType(typeVO);
        String[] herderName = new String[]{"Access-Token"};
        String[] headerValue = new String[]{"17950699-c29a-4a1c-b24b-6ce88ee3006d"};
        httpAPIService.sendPostJson("http://10.12.107.156:8000/api/v1/client/gridEvent/addEvent", MapperUtils.obj2json(eventVO),herderName,headerValue);

    }

    @Test
    public void proxy() {
        invoker.invoke();
    }

}
