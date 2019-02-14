## 注解说明

注解 | 说明
---|---
@Aspect | aop类注解
@Pointcut | 定义切面点注解
@Around | 环绕通知，围绕方法执行，此通知可以修改返回值，ProceedingJoinPoint.proceed() 
@After | 后置通知，方法执行后执行
@Before | 前置通知，方法执行前执行
@AfterReturning | 返回通知，在方法返回结果之后执行
@AfterThrowing | 异常通知, 在方法抛出异常之后执行

### 执行顺序



```
环绕通知开始 
    -》前置通知
    -》目标方法执行
环绕通知结束    
后置通知
返回通知
```

##### 环绕通知最先执行，当环绕通知执行ProceedingJoinPoint.proceed()方法时,前置通知执行，然后开始执行目标方法，ProceedingJoinPoint.proceed()返回目标方法的返回值，然后环绕通知继续执行，接着执行后置通知，再然后执行返回通知

### 集成
1. pom.xml增加aop开发包

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```
2. 新建aop配置类AopAspect.java

```
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
    @After("excudeService()")
    public void afterMethod(JoinPoint joinPoint) {
        logger.info("----后置通知----");
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
```
3. 新建aop测试controller AopController.java

```
package com.dolphin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 开发公司：青岛海豚数据技术有限公司
 * 版权：青岛海豚数据技术有限公司
 * <p>
 * AopController
 *
 * @author 刘志强
 * @created Create Time: 2019/2/13
 */
@RestController
@RequestMapping("/aop")
public class AopController {

    @GetMapping("index")
    @ResponseBody
    public Map<String,Object> index(String userName){
        Map<String,Object> map = new HashMap<>();
        map.put("userName",userName);
        map.put("id", 1);
        return map;
    }
}
```
4. 浏览器访问 http://localhost:6533/aop/index?userName=刘志强
5. 观察控制台打印

### 扩展自定义注解

1. 新建自定义注解Monitor.java

```
package com.dolphin.config.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开发公司：青岛海豚数据技术有限公司
 * 版权：青岛海豚数据技术有限公司
 * <p>
 * Monitor
 *
 * @author 刘志强
 * @created Create Time: 2019/2/12
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Monitor {
    String title() default "";

    String value() default "";

    String text() default "";
}

```

2. 以后置通知为例，切点条件增加@Monitor注解

```
    // 后置通知
    @After("excudeService() && @annotation(monitor)")
    public void afterMethod(JoinPoint joinPoint, Monitor monitor) {
        logger.info("----后置通知----" + monitor.text() + ":" + monitor.title() + ":" + monitor.value());
    }
```
这样后置通知只有在目标方法满足切面点并拥有@Monitor注解时才会执行

3. AopController增加测试方法 test

```
    @GetMapping("test")
    @ResponseBody
    @Monitor(text = "接口描述", value = "接口描述", title = "接口标题")
    public Map<String,Object> test(String userName){
        Map<String,Object> map = new HashMap<>();
        map.put("userName",userName);
        map.put("id", 1);
        return map;
    }
```
4. 浏览器访问 http://localhost:6533/aop/test?userName=刘志强

5.观察控制台打印。增加自定义注解可以做日志使用 











