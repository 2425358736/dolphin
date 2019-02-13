package com.dolphin.controller;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * 开发公司：青岛海豚数据技术有限公司
 * 版权：青岛海豚数据技术有限公司
 * <p>
 * FreemarkerController
 *
 * @author 刘志强
 * @created Create Time: 2019/2/12
 */
@RestController
@RequestMapping("/freemarker")
public class FreemarkerController {

    @GetMapping("index")
    public ModelAndView index(ModelMap modelMap){
        modelMap.put("userName","刘志强");
        modelMap.put("date",new Date());
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map1 = new HashMap<>();
        map1.put("id",1);
        map1.put("name","张三");
        list.add(map1);
        Map<String,Object> map2 = new HashMap<>();
        map2.put("id",2);
        map2.put("name","李四");
        list.add(map2);
        modelMap.put("list", list);
        return new ModelAndView("/freemarker/index", modelMap);
    }
}