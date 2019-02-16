# [springboot技术栈](https://github.com/2425358736/dolphin/blob/master/README.md)

### log配置详解

springboot集成了log开发包,pom.xml引入 spring-boot-starter 或包含spring-boot-starter的包spring-boot-starter-**如 spring-boot-starter-web


```
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter</artifactId>
</dependency>
```




1. application.yml 里增加日志配置

```
logging:
    config: classpath:log/logback.xml
```
classpath 指resources目录, ==千万不要写/log/logback.xml== ,那样会找不到配置文件


2. 新建logback.xml 路径和application配置里一致


```
<configuration>
    <!--本文主要输出日志为控制台日志，系统日志，sql日志，异常日志-->
    <!-- %m输出的信息,%p日志级别,%t线程名,%d日期,%c类的全名,,,, -->
    <!--控制台-->
    <appender name="console" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d %p (%file:%line\)- %m%n</pattern>
            <charset>UTF-8</charset>
        </encoder>
    </appender>

    <!--系统info级别日志-->
    <!--<File> 日志目录，没有会自动创建-->
    <!--<rollingPolicy>日志策略，每天简历一个日志文件，或者当天日志文件超过64MB时-->
    <!--encoder 日志编码及输出格式-->
    <appender name="fileLog"
              class="ch.qos.logback.core.rolling.RollingFileAppender">
        <File>log/file/fileLog.log</File>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>log/file/fileLog.log.%d.%i</fileNamePattern>
            <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                <!-- or whenever the file size reaches 64 MB -->
                <maxFileSize>64 MB</maxFileSize>
            </timeBasedFileNamingAndTriggeringPolicy>
        </rollingPolicy>
        <encoder>
            <pattern>
                %d %p (%file:%line\)- %m%n
            </pattern>
            <charset>UTF-8</charset>
            <!-- 此处设置字符集 -->
        </encoder>
    </appender>

    <!--sql日志-->
    <appender name="sqlFile"
              class="ch.qos.logback.core.rolling.RollingFileAppender">
        <File>log/sql/sqlFile.log</File>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>log/sql/sqlFile.log.%d.%i</fileNamePattern>
            <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                <!-- or whenever the file size reaches 64 MB -->
                <maxFileSize>64 MB</maxFileSize>
            </timeBasedFileNamingAndTriggeringPolicy>
        </rollingPolicy>
        <!--对记录事件进行格式化。负责两件事，一是把日志信息转换成字节数组，二是把字节数组写入到输出流。-->
        <encoder>
            <!--用来设置日志的输入格式-->
            <pattern>
                %d %p (%file:%line\)- %m%n
            </pattern>
            <charset>UTF-8</charset>
            <!-- 此处设置字符集 -->
        </encoder>
    </appender>


    <!--异常日志-->
    <appender name="errorFile"
              class="ch.qos.logback.core.rolling.RollingFileAppender">
        <File>log/error/errorFile.log</File>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>log/error/errorFile.%d.log.%i</fileNamePattern>
            <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                <!-- or whenever the file size reaches 64 MB -->
                <maxFileSize>64 MB</maxFileSize>
            </timeBasedFileNamingAndTriggeringPolicy>
        </rollingPolicy>
        <!--对记录事件进行格式化。负责两件事，一是把日志信息转换成字节数组，二是把字节数组写入到输出流。-->
        <encoder>
            <!--用来设置日志的输入格式-->
            <pattern>
                %d %p (%file:%line\)- %m%n
            </pattern>
            <charset>UTF-8</charset>
            <!-- 此处设置字符集 -->
        </encoder>
        <!--
            日志都在这里 过滤出 error
            使用 try {}catch (Exception e){} 的话异常无法写入日志，可以在catch里用logger.error()方法手动写入日志
            -->
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>ERROR</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
    </appender>
    <!--  日志输出级别 -->
    <!--All\DEBUG\INFO\WARN\ERROR\FATAL\OFF-->
    <!--打印info级别日志，分别在控制台，fileLog，errorFile输出
        异常日志在上面由过滤器过滤出ERROR日志打印
    -->
    <root level="INFO">
        <appender-ref ref="fileLog" />
        <appender-ref ref="console" />
        <appender-ref ref="errorFile" />
    </root>

    <!--打印sql至sqlFile文件日志-->
    <logger name="com.dolphin.mapper" level="DEBUG" additivity="false">
        <appender-ref ref="console" />
        <appender-ref ref="sqlFile" />
    </logger>
</configuration>
```
项目启动时将会在项目跟目录常见log文件夹及log文件



### 日志级别
    ALL: 最低等级的，用于打开所有日志记录。 
    DEBUG :详细的显示信息
    INFO :一般显示信息
        WARN :一般警告
        ERROR:严重错误
        FATAL：崩溃，整个程序终止运行
    OFF: 最高级，关闭所有日志

3. 新建测试controller LogController.java

```
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
```
只测试异常日志，和系统日志,sql日志请先集成mybatis。
##### 集成参考： [springboot 集成 mybatis,mysql,Druid(数据库连接池), mybatis-generator(MBG自动生成)](https://github.com/2425358736/dolphin/blob/master/src/main/resources/mybatis/springboot%20%E9%9B%86%E6%88%90%20mybatis%2Cmysql%2CDruid(%E6%95%B0%E6%8D%AE%E5%BA%93%E8%BF%9E%E6%8E%A5%E6%B1%A0)%2C%20mybatis-generator(MBG%E8%87%AA%E5%8A%A8%E7%94%9F%E6%88%90).md)

## 浏览器访问 http://localhost:6533/log/error 观察日志文件


