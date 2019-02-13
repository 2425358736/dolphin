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
    int addSchedulingPlan(SchedulingPlan schedulingPlan);

    /**
     * 运行指定计划
     *
     * @param schedulingPlan 计划信息
     */
    int runSchedulingPlan(SchedulingPlan schedulingPlan);

    /**
     * 修改调度计划
     *
     * @param schedulingPlan 计划信息
     */
    int upSchedulingPlan(SchedulingPlan schedulingPlan);


    /**
     * 暂停指定调度计划
     *
     * @param schedulingPlan 计划信息
     */
    int suspendSchedulingPlan(SchedulingPlan schedulingPlan);


    /**
     * 恢复调度计划
     *
     * @param schedulingPlan 计划信息
     */
    int recoverySchedulingPlan(SchedulingPlan schedulingPlan);

    /**
     * 删除指定调度计划
     *
     * @param schedulingPlan 计划信息
     */
    int deleteSchedulingPlan(SchedulingPlan schedulingPlan);
}