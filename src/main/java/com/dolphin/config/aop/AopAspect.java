package com.dolphin.config.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 开发公司：青岛海豚数据技术有限公司
 * 版权：青岛海豚数据技术有限公司
 * <p>
 * Aspect
 *
 * @author 刘志强
 * @created Create Time: 2019/2/12
 */
@Aspect   //定义一个切面
@Component
public class AopAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // 定义切点Pointcut
    @Pointcut("execution(* com.dolphin.controller.*.*(..))")
    public void excudeService() {
    }

    // 环绕通知
    @Around("excudeService()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        logger.info("----环绕通知----");
        // pjp.proceed()获取原方法的返回值
        Object o = pjp.proceed();
        logger.info("----环绕通知----");
        // 可以修改值
        if (o.getClass().toString().indexOf("Map") > 0){
            Map map = (Map) o;
            map.put("作者","刘志强");
            return map;
        }
        return o;
    }

    // 后置通知
    @After("excudeService() && @annotation(monitor)")
    public void afterMethod(JoinPoint joinPoint, Monitor monitor) {
        logger.info("----后置通知----" + monitor.text() + ":" + monitor.title() + ":" + monitor.value());
    }

    // 前置通知
    @Before("excudeService()")
    public void beforeMethod(JoinPoint joinPoint) {
        logger.info("----前置通知----");
    }

    /**
     *
     * @param joinpoint
     * @param rvt 返回值
     */
    @AfterReturning(pointcut = "excudeService()", returning = "rvt")
    public void afterReturningMethod(JoinPoint joinpoint, Object rvt) {
        logger.info("----返回通知----");
    }

    // 异常通知
    @AfterThrowing(pointcut = "excudeService()")
    public void afterThrowingMethod(JoinPoint joinPoint) {
        logger.info("----异常通知----");
    }

}