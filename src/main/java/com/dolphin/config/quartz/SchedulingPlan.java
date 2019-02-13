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