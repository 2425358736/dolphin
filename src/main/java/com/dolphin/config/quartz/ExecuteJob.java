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