# [springboot官方文档](http://spring.io/projects/spring-boot)

### Spring Boot可以轻松创建独立的，生产级的基于Spring的应用程序。Spring Boot应用程序只需要很少的Spring配置。springboot极大程度上简化了web服务的搭建

## 特征

创建独立的Spring应用程序

直接嵌入Tomcat，Jetty或Undertow（无需部署WAR文件）

提供入门依赖项以简化构建配置

尽可能自动配置Spring和第三方库

提供生产就绪功能，例如指标，运行状况检查和外部化配置

绝对没有代码生成，也不需要XML配置


## 快速开始

使用 [Spring Initializr](https://start.spring.io/) 引导您的应用程序。

## 自己构建
1. idea新建maven项目 项目名及工程名为dolphin
2. 修改pom.xml文件

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.2.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>

    <groupId>dolphin</groupId>
    <artifactId>dolphin</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <java.version>1.8</java.version>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <!-- 设定主仓库，按设定顺序进行查找。 -->
    <repositories>
        <repository>
            <id>aliyun-releases</id>
            <url>http://maven.aliyun.com/nexus/content/groups/public</url>
        </repository>
    </repositories>

    <!-- 设定插件仓库 -->
    <pluginRepositories>
        <pluginRepository>
            <id>aliyun-repos</id>
            <name>aliyun Repository</name>
            <url>http://maven.aliyun.com/nexus/content/groups/public</url>
        </pluginRepository>
    </pluginRepositories>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!--测试类包-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>
```
3. 新建配置文件application.yml(resources目录下)

```
spring:
  application:
    name: dolphin # 服务名
server:
  port: 6533 # 服务端口

```
4. 新建服务启动类(Application.java) ==启动类不能直接在Java目录下，Java目录下新建com.dolphin目录==

```
package com.dolphin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 开发公司：青岛海豚数据技术有限公司
 * 版权：青岛海豚数据技术有限公司
 * <p>
 * Application
 *
 * @author 刘志强
 * @created Create Time: 2019/2/12
 */
@SpringBootApplication(scanBasePackages = "com.dolphin")
@RestController
public class Application implements CommandLineRunner {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        logger.info("服务已启动");
    }

    // 跟目录重定向
    @RequestMapping("/")
    @ResponseBody
    Map<String,Object> home() {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("作者：","刘志强");
        return map;
    }
}
```
5. 新建WebMvcConfig.java 配置静态资源

```
package com.dolphin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 开发公司：青岛海豚数据技术有限公司
 * 版权：青岛海豚数据技术有限公司
 * <p>
 * WebMvcConfig
 *
 * @author 刘志强
 * @created Create Time: 2019/2/16
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    //静态资源放过
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }
}

```

6. 运行main方法,服务启动



