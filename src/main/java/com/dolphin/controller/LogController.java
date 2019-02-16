package com.dolphin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 开发公司：青岛海豚数据技术有限公司
 * 版权：青岛海豚数据技术有限公司
 * <p>
 * LogController
 *
 * @author 刘志强
 * @created Create Time: 2019/2/16
 */
@Controller
@RequestMapping("/log")
public class LogController {

    @GetMapping("error")
    @ResponseBody
    public String error() throws Exception{
        int[] i = {1,2,3,4};
        int j = i[5];
        return "测试系统错误日志";
    }
}