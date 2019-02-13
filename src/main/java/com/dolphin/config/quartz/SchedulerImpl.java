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