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