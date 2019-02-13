package com.dolphin.controller;

import com.dolphin.config.aop.Monitor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 开发公司：青岛海豚数据技术有限公司
 * 版权：青岛海豚数据技术有限公司
 * <p>
 * AopController
 *
 * @author 刘志强
 * @created Create Time: 2019/2/13
 */
@RestController
@RequestMapping("/aop")
public class AopController {

    @GetMapping("index")
    @ResponseBody
    public Map<String,Object> index(String userName){
        Map<String,Object> map = new HashMap<>();
        map.put("userName",userName);
        map.put("id", 1);
        return map;
    }

    @GetMapping("test")
    @ResponseBody
    @Monitor(text = "接口描述", value = "接口描述", title = "接口标题")
    public Map<String,Object> test(String userName){
        Map<String,Object> map = new HashMap<>();
        map.put("userName",userName);
        map.put("id", 1);
        return map;
    }
}