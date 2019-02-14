### [Quartz学习文档W3Cschool](https://www.w3cschool.cn/quartz_doc/quartz_doc-2put2clm.html)
#### 调度计划由作业（JobDetail）和触发器（Trigger）构成。 Scheduler的scheduleJob方法可以向调度程序中添加新的作业和触发器。

## Quartz API说明

### Quartz API

属性 | 描述 | 使用
---|---|---
Scheduler | 与调度程序交互的主要API| 
Job | 调度程序执行的组件实现的接口| 
JobBuilder | 用于定义/构建JobDetail实例，用于定义作业的实例。
TriggerBuilder | 用于定义/构建触发器实例。
JobDetail | 用于定义作业的实例
Trigger/CronTrigger | 即触发器） - 定义执行给定作业的计划的组件。


### Scheduler Api


```
 // StdSchedulerFactory是org.quartz.SchedulerFactory接口的一个实现。它使用一组属性（java.util.Properties）来创建和初始化Quartz Scheduler
 SchedulerFactory schedulerFactory = new StdSchedulerFactory();
 // 创建Scheduler实例
 Scheduler scheduler = schedulerFactory.getScheduler();

 scheduler.start(); //启动
           standby() //暂停
           shutdown() //停止
           scheduleJob(JobDetail var1, Trigger var2) //添加调度计划
           triggerJob(JobKey var1) //立即运行一次作业
           rescheduleJob(TriggerKey var1, Trigger var2) // 修改触发器
           pauseJob(JobKey var1) //暂停作业
           resumeJob(JobKey var1) //恢复作业
           deleteJob(JobKey var1) //删除作业
           .
           .
           .
           .
 
```


## 工作流程
1. 创建调度程序Scheduler

```
SchedulerFactory schedulerFactory = new StdSchedulerFactory();
// 创建Scheduler实例
Scheduler scheduler = schedulerFactory.getScheduler();
```
2. 启动调度程序

```
scheduler.start();
```
3. 创建调度程序执行类 继承 Job接口

```
public class ExecuteJob implements Job {
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("定时任务开始执行,作业名=" + jobExecutionContext.getMergedJobDataMap().get("key"););
    }
}
```

4. 创建作业和触发器

```
// 创建作业
JobDetail jobDetail = JobBuilder.newJob(ExecuteJob.class)
                    .withIdentity(作业名, 作业组).build();
jobDetail.getJobDataMap().put("key", 作业名);
// 创建计划
CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron表达式).withMisfireHandlingInstructionDoNothing();
// 创建触发器绑定计划
CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(触发器名, 触发器组)
                    .withSchedule(scheduleBuilder).build();
```
5. 添加调度作业

```
scheduler.scheduleJob(jobDetail, trigger);
```
6. 修改触发器

```
// 获取触发器key  TriggerKey 
TriggerKey triggerKey = TriggerKey.triggerKey(触发器名, 触发器组);
// 获取触发器
CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
// 创建新的时间计划
CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron表达式).withMisfireHandlingInstructionDoNothing();
// 修改触发器计划
trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
// 修改触发器
scheduler.rescheduleJob(triggerKey, trigger); 
```
7. 立即运行指定作业

```
jobKey jobKey = JobKey.jobKey(作业名, 作业组);
scheduler.triggerJob(jobKey);
```
8. 暂停指定作业

```
jobKey jobKey = JobKey.jobKey(作业名, 作业组);
scheduler.pauseJob(jobKey);
```
9. 恢复指定作业

```
jobKey jobKey = JobKey.jobKey(作业名, 作业组);
scheduler.resumeJob(jobKey);
```

10. 删除指定作业

```
jobKey jobKey = JobKey.jobKey(作业名, 作业组);
scheduler.deleteJob(jobKey);
```





## 集成

1. ### pom.xml 引入maven包
```
<dependency>
    <groupId>org.quartz-scheduler</groupId>
    <artifactId>quartz</artifactId>
    <version>2.3.0</version>
</dependency>
<dependency>
    <groupId>org.quartz-scheduler</groupId>
    <artifactId>quartz-jobs</artifactId>
    <version>2.3.0</version>
</dependency>
```

2. ### 创建quartz.properties（在resources目录下）
```
#============================================================================
# 配置 Scheduler Properties
#org.quartz.scheduler.instanceName  程序名
#org.quartz.scheduler.instanceId 程序id 必须唯一
#org.quartz.threadPool.class 是要使用的ThreadPool实现的名称。Quartz附带的线程池是“org.quartz.simpl.SimpleThreadPool
#org.quartz.threadPool.threadCount 线程数 可用于并发执行作业的线程数
#org.quartz.threadPool.threadPriority  现成优先级 1-10
#org.quartz.jobStore.class RAMJobStore用于存储内存中的调度信息
#============================================================================

org.quartz.scheduler.instanceName = DOLPHIN
org.quartz.scheduler.instanceId = AUTO

#============================================================================
# Configure ThreadPool
#============================================================================

org.quartz.threadPool.class = org.quartz.simpl.SimpleThreadPool
org.quartz.threadPool.threadCount = 25
org.quartz.threadPool.threadPriority = 5

#============================================================================
# Configure RAMJobStore
#============================================================================
org.quartz.jobStore.class = org.quartz.simpl.RAMJobStore
```

3. ### 创建Scheduler配置类 构建Scheduler实例
```
package com.dolphin.config.quartz;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Properties;

/**
 * 开发公司：青岛海豚数据技术有限公司
 * 版权：青岛海豚数据技术有限公司
 * <p>
 * SchedulerConfig
 *
 * @author 刘志强
 * @created Create Time: 2019/1/31
 */
@Configuration
public class SchedulerConfig {

    @Bean(name = "properties")
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }

    @Bean(name = "schedulerFactory")
    public SchedulerFactory SchedulerFactory(Properties properties) throws IOException, SchedulerException {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory(properties);
        return schedulerFactory;
    }

    /**
     * 通过SchedulerFactory获取Scheduler的实例
     */
    @Bean(name = "scheduler")
    public Scheduler scheduler(SchedulerFactory schedulerFactory) throws IOException, SchedulerException {
        Scheduler scheduler = schedulerFactory.getScheduler();
        scheduler.start();
        return scheduler;
    }
}

```
4. ### 创建调度实体类

```
package com.dolphin.config.quartz;

/**
 * 开发公司：青岛海豚数据技术有限公司
 * 版权：青岛海豚数据技术有限公司
 * <p>
 * SchedulingPlan
 * 调度计划实体类
 *
 * @author 刘志强
 * @created Create Time: 2019/1/31
 */
public class SchedulingPlan {
    /**
     * 计划id
     */
    private Long id;

    /**
     * 计划名称
     */
    private String name;

    /**
     * 计划组
     */
    private String group;

    /**
     * Cron表达式
     */
    private String CronExpressions;

    /**
     * 开启关闭状态 0开启 1暂停 2删除
     */
    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getCronExpressions() {
        return CronExpressions;
    }

    public void setCronExpressions(String cronExpressions) {
        CronExpressions = cronExpressions;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
```

5. ### 创建调度程序执行类

```
package com.dolphin.config.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 开发公司：青岛海豚数据技术有限公司
 * 版权：青岛海豚数据技术有限公司
 * <p>
 * ExecuteJob
 *
 * @author 刘志强
 * @created Create Time: 2019/1/31
 */
@Component
public class ExecuteJob implements Job {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        SchedulingPlan schedulingPlan = (SchedulingPlan) jobExecutionContext.getMergedJobDataMap().get("key");
        logger.info("定时任务开始执行，任务名称[" + schedulingPlan.getName() + "]::::::执行时间" + new Date());
    }
}
```
6. 新建 调度对象类 JobInformation.java

```
package com.dolphin.config.quartz;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.TriggerKey;

/**
 * 开发公司：青岛海豚数据技术有限公司
 * 版权：青岛海豚数据技术有限公司
 * <p>
 * JobInformation
 * 任务的触发器，等信息
 * @author 刘志强
 * @created Create Time: 2019/1/31
 */
public class JobInformation {
    // 作业key
    private JobKey jobKey;
    // 作业
    private JobDetail jobDetail;
    // 触发器key
    private TriggerKey triggerKey;
    // Cron触发器
    private CronTrigger trigger;
    // code 用于验证是否缺失
    private Integer code;
    // message 对象缺失信息
    private String message;

    public JobKey getJobKey() {
        return jobKey;
    }

    public void setJobKey(JobKey jobKey) {
        this.jobKey = jobKey;
    }

    public JobDetail getJobDetail() {
        return jobDetail;
    }

    public void setJobDetail(JobDetail jobDetail) {
        this.jobDetail = jobDetail;
    }

    public TriggerKey getTriggerKey() {
        return triggerKey;
    }

    public void setTriggerKey(TriggerKey triggerKey) {
        this.triggerKey = triggerKey;
    }

    public CronTrigger getTrigger() {
        return trigger;
    }

    public void setTrigger(CronTrigger trigger) {
        this.trigger = trigger;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
```



6. ### 创建调度处理接口和实现类

```
package com.dolphin.config.quartz;

/**
 * 开发公司：青岛海豚数据技术有限公司
 * 版权：青岛海豚数据技术有限公司
 * <p>
 * SchedulerService
 *
 * @author 刘志强
 * @created Create Time: 2019/1/31
 */
public interface SchedulerService {
    /**
     * 添加调度计划
     * @param schedulingPlan 计划信息
     */
    public int addSchedulingPlan(SchedulingPlan schedulingPlan);

    /**
     * 运行指定计划
     *
     * @param schedulingPlan 计划信息
     */
    public int runSchedulingPlan(SchedulingPlan schedulingPlan);

    /**
     * 修改调度计划
     *
     * @param schedulingPlan 计划信息
     */
    public int upSchedulingPlan(SchedulingPlan schedulingPlan);


    /**
     * 暂停指定调度计划
     *
     * @param schedulingPlan 计划信息
     */
    public int suspendSchedulingPlan(SchedulingPlan schedulingPlan);


    /**
     * 恢复调度计划
     *
     * @param schedulingPlan 计划信息
     */
    public int recoverySchedulingPlan(SchedulingPlan schedulingPlan);

    /**
     * 删除指定调度计划
     *
     * @param schedulingPlan 计划信息
     */
    public int deleteSchedulingPlan(SchedulingPlan schedulingPlan);
}
```


```
package com.dolphin.config.quartz;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 *withMisfireHandlingInstructionIgnoreMisfires（所有misfire的任务会马上执行）
 * 如 原调度计划是1秒执行一次，，暂停10秒，当调度计划恢复时 会先执行10次
 *
 *withMisfireHandlingInstructionDoNothing(所有的misfire不管，执行下一个周期的任务)
 * 如 原调度计划是1秒执行一次，，暂停10秒，当调度计划恢复时 会在下个周期在执行（中间暂停的10次会放弃）
 *
 *withMisfireHandlingInstructionFireAndProceed（会合并部分的misfire,正常执行下一个周期的任务）
 * 当修改计划时，会先执行一次计划
 * 恢复计划时，会执行一次
 */


/**
 * 开发公司：青岛海豚数据技术有限公司
 * 版权：青岛海豚数据技术有限公司
 * <p>
 * QuartzHandle
 *
 * @author 刘志强
 * @created Create Time: 2019/1/31
 */
@Service
public class SchedulerImpl implements SchedulerService {
    @Autowired
    public Scheduler scheduler;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 添加调度计划
     * @param schedulingPlan 计划信息
     */
    public int addSchedulingPlan(SchedulingPlan schedulingPlan) {
        logger.info("开始向任务调度中添加定时任务---" + schedulingPlan.getName());
        try {
            JobDetail jobDetail = JobBuilder.newJob(ExecuteJob.class)
                    .withIdentity(schedulingPlan.getName(), schedulingPlan.getGroup()).build();
            jobDetail.getJobDataMap().put("key", schedulingPlan);
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(schedulingPlan.getCronExpressions()).withMisfireHandlingInstructionDoNothing();
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(schedulingPlan.getName(), schedulingPlan.getGroup())
                    .withSchedule(scheduleBuilder).build();
            scheduler.scheduleJob(jobDetail, trigger);
            logger.info("成功向任务调度中添加定时任务---" + schedulingPlan.getName());
            return 200;
        } catch (Exception e) {
            logger.error("向任务调度中添加定时任务异常！---" + schedulingPlan.getName(), e);
            return 500;
        }
    }


    /**
     * 运行指定计划
     *
     * @param schedulingPlan 计划信息
     */
    public int runSchedulingPlan(SchedulingPlan schedulingPlan) {
        logger.info("运行调度计划---" + schedulingPlan.getName());
        try {
            JobInformation jobInformation = check(schedulingPlan);
            if (jobInformation.getCode() == 500) {
                logger.error(jobInformation.getMessage());
                return jobInformation.getCode();
            }
            scheduler.triggerJob(jobInformation.getJobKey());
            return 200;
        } catch (Exception e) {
            logger.error("运行调度计划异常！" + e.getMessage(), e);
            return 500;
        }
    }


    /**
     * 修改调度计划
     *
     * @param schedulingPlan 计划信息
     */
    public int upSchedulingPlan(SchedulingPlan schedulingPlan) {
        logger.info("修改调度计划----" + schedulingPlan.getName());
        TriggerKey triggerKey = null;
        CronTrigger trigger = null;

        try {
            JobInformation jobInformation = check(schedulingPlan);
            if (jobInformation.getCode() == 500) {
                logger.error(jobInformation.getMessage());
                return jobInformation.getCode();
            }
            triggerKey = jobInformation.getTriggerKey();
            trigger = jobInformation.getTrigger();
            logger.info("原始任务表达式:" + trigger.getCronExpression()
                    + "，现在任务表达式:" + schedulingPlan.getCronExpressions());
            if (trigger.getCronExpression().equals(schedulingPlan.getCronExpressions())) {
                logger.info("任务调度表达式一致，不予进行修改！----" + schedulingPlan.getName());
                return 501;
            }
            logger.info("任务调度表达式不一致，进行修改----" + schedulingPlan.getName());
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(schedulingPlan.getCronExpressions()).withMisfireHandlingInstructionDoNothing();
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            scheduler.rescheduleJob(triggerKey, trigger);
            return 200;
        } catch (Exception e) {
            logger.error("修改任务调度中的定时任务异常！----" + schedulingPlan.getName() + e.getMessage(), e);
            return 500;
        }
    }


    /**
     * 暂停指定调度计划
     *
     * @param schedulingPlan 计划信息
     */
    public int suspendSchedulingPlan(SchedulingPlan schedulingPlan) {
        logger.info("暂停调度计划---" + schedulingPlan.getName());
        JobKey jobKey = null;
        try {
            JobInformation jobInformation = check(schedulingPlan);
            if (jobInformation.getCode() == 500) {
                logger.error(jobInformation.getMessage());
                return jobInformation.getCode();
            }
            jobKey = jobInformation.getJobKey();
            scheduler.pauseJob(jobKey);
            return 200;
        } catch (Exception e) {
            logger.error("暂停调度计划异常！" + e.getMessage(), e);
            return 500;
        }
    }


    /**
     * 恢复调度计划
     *
     * @param schedulingPlan 计划信息
     */
    public int recoverySchedulingPlan(SchedulingPlan schedulingPlan) {
        logger.info("恢复调度计划");
        JobKey jobKey = null;
        try {
            JobInformation jobInformation = check(schedulingPlan);
            if (jobInformation.getCode() == 500) {
                logger.error(jobInformation.getMessage());
                return jobInformation.getCode();
            }
            jobKey = jobInformation.getJobKey();
            scheduler.resumeJob(jobKey);
            return 200;
        } catch (Exception e) {
            logger.error("恢复调度计划异常！" + e.getMessage(), e);
            return 500;
        }
    }

    /**
     * 删除指定调度计划
     *
     * @param schedulingPlan 计划信息
     */
    public int deleteSchedulingPlan(SchedulingPlan schedulingPlan) {
        logger.info("删除指定调度计划");
        JobKey jobKey = null;
        try {
            JobInformation jobInformation = check(schedulingPlan);
            if (jobInformation.getCode() == 500) {
                logger.error(jobInformation.getMessage());
                return jobInformation.getCode();
            }
            jobKey = jobInformation.getJobKey();
            scheduler.deleteJob(jobKey);
            return 200;
        } catch (Exception e) {
            logger.error("删除指定调度计划异常！" + e.getMessage(), e);
            return 500;
        }
    }

    public JobInformation check(SchedulingPlan schedulingPlan) {
        JobInformation jobInformation = new JobInformation();
        jobInformation.setMessage("校验通过");
        jobInformation.setCode(200);
        JobKey jobKey = null;
        JobDetail jobDetail = null;
        TriggerKey triggerKey = null;
        CronTrigger trigger = null;
        try {
            if (null == schedulingPlan) {
                jobInformation.setMessage("调度计划信息为空");
                jobInformation.setCode(500);
            }
            jobKey = JobKey.jobKey(schedulingPlan.getName(), schedulingPlan.getGroup());
            jobDetail = scheduler.getJobDetail(jobKey);
            if (null == jobDetail) {
                jobInformation.setMessage("任务调度中不存在[" + schedulingPlan.getName() + "]任务");
                jobInformation.setCode(500);
            }
            triggerKey = TriggerKey.triggerKey(schedulingPlan.getName(), schedulingPlan.getGroup());
            trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (null == trigger) {
                jobInformation.setMessage("任务调度中不存在[" + schedulingPlan.getName() + "]触发器");
                jobInformation.setCode(500);
            }
        } catch (Exception e) {
            jobInformation.setMessage(e.getMessage());
            jobInformation.setCode(500);
        }
        jobInformation.setJobKey(jobKey);
        jobInformation.setJobDetail(jobDetail);
        jobInformation.setTriggerKey(triggerKey);
        jobInformation.setTrigger(trigger);
        return jobInformation;
    }
}
```
7. 新建测试controller QuartzController.java
```
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
```

8. [cron生成器](http://cron.qqe2.com/)

9. 添加定时任务 访问 [http://localhost:6533/quartz/addSchedulingPlan?id=1&name=ceshi&group=ceshizu&CronExpressions=0,10,20,30,40,50 * * * * ?](http://localhost:6533/quartz/addSchedulingPlan?id=1&name=ceshi&group=ceshizu&CronExpressions=0,10,20,30,40,50%20*%20*%20*%20*%20?) 

10. 修改定时任务 访问 [http://localhost:6533/quartz/upSchedulingPlan?id=1&name=ceshi&group=ceshizu&CronExpressions=15 * * * * ?](http://localhost:6533/quartz/upSchedulingPlan?id=1&name=ceshi&group=ceshizu&CronExpressions=15%20*%20*%20*%20*%20?)