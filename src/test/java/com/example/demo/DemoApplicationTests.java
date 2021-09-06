package com.example.demo;

import cn.com.citycloud.hcs.common.utils.ConvertUtils;
import com.example.demo.bll.cache.RedisOperate;
import com.example.demo.bll.entity.GridEventCreate;
import com.example.demo.bll.entity.SysBizLog;
import com.example.demo.bll.exception.ApplicationException;
import com.example.demo.bll.service.MapperProxyFactory;
import com.example.demo.bll.service.impl.HttpAPIService;
import com.example.demo.bll.utils.ColumnUtil;
import com.example.demo.bll.utils.MapperUtils;
import com.example.demo.web.domain.vo.EventTypeVO;
import com.example.demo.web.domain.vo.EventVO;
import com.example.demo.web.domain.vo.GridEventCreateVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
        ConvertUtils.convert(eventCreateVO, GridEventCreate.class);
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

    @Test
    public void lambda() {
        ColumnUtil.getName(SysBizLog::getActionTime);
    }

    @Test
    public void sortMap() {
        List<Map<String, Object>> messageList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("date","2021-08-07 09:00:00");
        Map<String, Object> map2 = new HashMap<>();
        map2.put("date","2021-08-08 09:00:00");
        Map<String, Object> map3 = new HashMap<>();
        map3.put("date","2021-08-06 09:00:00");
        messageList.add(map);
        messageList.add(map2);
        messageList.add(map3);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Collections.sort(messageList,(o1,o2)->{
            Date  date1 = new Date();
            Date  date2 = new Date();
            try {
                date1 = simpleDateFormat.parse((String) o1.get("date"));
                date2 = simpleDateFormat.parse((String) o2.get("date"));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return date2.compareTo(date1);
        });
        System.out.println(messageList);
    }

}
