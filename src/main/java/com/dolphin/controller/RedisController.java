package com.dolphin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 开发公司：青岛海豚数据技术有限公司
 * 版权：青岛海豚数据技术有限公司
 * <p>
 * RedisController
 *
 * @author 刘志强
 * @created Create Time: 2019/2/15
 */
@RestController
@RequestMapping("/redis")
public class RedisController {
    @Autowired
    public RedisTemplate redisTemplate;

    @GetMapping("index")
    @ResponseBody
    public String index(){
        redisTemplate.opsForValue().set("id","22222");
        return (String) redisTemplate.opsForValue().get("id");
    }

    @GetMapping("index2")
    @ResponseBody
    public Map index2(){
        Map<String,Object> map = new HashMap<>();
        map.put("id",1);
        map.put("userName", "刘志强");
        ValueOperations<String,Map> valueOperations = redisTemplate.opsForValue();
        valueOperations.set("map",map);
        return valueOperations.get("map");
    }

    @GetMapping("index3")
    @ResponseBody
    public List index3() throws InterruptedException {
        List<Map> list = new ArrayList();
        Map<String,Object> map = new HashMap<>();
        map.put("id",1);
        map.put("userName", "刘志强");
        list.add(map);
        redisTemplate.opsForValue().set("list",list,1, TimeUnit.SECONDS);
        List list1 = (List) redisTemplate.opsForValue().get("list");
        redisTemplate.delete("d");
        return list1;
    }
}