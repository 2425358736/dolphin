package com.dolphin.controller;

import com.dolphin.config.quartz.SchedulerService;
import com.dolphin.config.quartz.SchedulingPlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 开发公司：青岛海豚数据技术有限公司
 * 版权：青岛海豚数据技术有限公司
 * <p>
 * QuartzController
 *调度计划测试
 * @author 刘志强
 * @created Create Time: 2019/1/31
 */
@RestController
@RequestMapping("/quartz")
@CrossOrigin
public class QuartzController {
    @Autowired
    public SchedulerService schedulerService;

    @GetMapping("/addSchedulingPlan")
    public Map<String,Object> addSchedulingPlan(SchedulingPlan schedulingPlan) {
        int i = schedulerService.addSchedulingPlan(schedulingPlan);
        Map<String,Object> map = new HashMap<>();
        map.put("code",i);
        if (i == 200) {
            map.put("message","操作成功");
        } else {
            map.put("message","操作失败");
        }
        return map;
    }

    @GetMapping("/runSchedulingPlan")
    public Map<String,Object> runSchedulingPlan(SchedulingPlan schedulingPlan) {
        int i = schedulerService.runSchedulingPlan(schedulingPlan);
        Map<String,Object> map = new HashMap<>();
        map.put("code",i);
        if (i == 200) {
            map.put("message","操作成功");
        } else {
            map.put("message","操作失败");
        }
        return map;
    }


    @GetMapping("/upSchedulingPlan")
    public Map<String,Object> upSchedulingPlan(SchedulingPlan schedulingPlan) {
        int i = schedulerService.upSchedulingPlan(schedulingPlan);
        Map<String,Object> map = new HashMap<>();
        map.put("code",i);
        if (i == 200) {
            map.put("message","操作成功");
        } else {
            map.put("message","操作失败");
        }
        return map;
    }

    @GetMapping("/suspendSchedulingPlan")
    public Map<String,Object> suspendSchedulingPlan(SchedulingPlan schedulingPlan) {
        int i = schedulerService.suspendSchedulingPlan(schedulingPlan);
        Map<String,Object> map = new HashMap<>();
        map.put("code",i);
        if (i == 200) {
            map.put("message","操作成功");
        } else {
            map.put("message","操作失败");
        }
        return map;
    }

    @GetMapping("/recoverySchedulingPlan")
    public Map<String,Object> recoverySchedulingPlan(SchedulingPlan schedulingPlan) {
        int i = schedulerService.recoverySchedulingPlan(schedulingPlan);
        Map<String,Object> map = new HashMap<>();
        map.put("code",i);
        if (i == 200) {
            map.put("message","操作成功");
        } else {
            map.put("message","操作失败");
        }
        return map;
    }

    @GetMapping("/deleteSchedulingPlan")
    public Map<String,Object> deleteSchedulingPlan(SchedulingPlan schedulingPlan) {
        int i = schedulerService.deleteSchedulingPlan(schedulingPlan);
        Map<String,Object> map = new HashMap<>();
        map.put("code",i);
        if (i == 200) {
            map.put("message","操作成功");
        } else {
            map.put("message","操作失败");
        }
        return map;
    }

}