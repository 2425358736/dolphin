### [mybatis学习手册](https://www.w3cschool.cn/mybatis/)
### [mysql学习手册](http://www.runoob.com/mysql/mysql-tutorial.html)
### [Druid介绍](https://github.com/alibaba/druid/wiki/%E5%B8%B8%E8%A7%81%E9%97%AE%E9%A2%98) Druid是Java语言中最好的数据库连接池。Druid能够提供强大的监控和扩展功能。，
### [mybatis-generator官方文档](http://www.mybatis.org/generator/index.html) 自动生成mapper(dao层),domain(实体类),xml(sql)

## 集成

1. pom.xml引入mybatis,mysql,Druid开发包

```
        <!--mybatis-->
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
        </dependency>
        <!--mysql-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.21</version>
        </dependency>
        <!--druid-->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>1.0.27</version>
        </dependency>
        <!--generator-->
        <dependency>
            <groupId>org.mybatis.generator</groupId>
            <artifactId>mybatis-generator-maven-plugin</artifactId>
            <version>1.3.7</version>
        </dependency>
```

2. pom.xml 添加mybatis-generator插件

```
<!--plugin 插件-->
            <plugin>
                <groupId>org.mybatis.generator</groupId>
                <artifactId>mybatis-generator-maven-plugin</artifactId>
                <version>${mybatis.generator.version}</version>
                <dependencies>
                    <dependency>
                        <groupId> mysql</groupId>
                        <artifactId>mysql-connector-java</artifactId>
                        <version>${mysql.version}</version>
                    </dependency>
                    <dependency>
                        <groupId>org.mybatis.generator</groupId>
                        <artifactId>mybatis-generator-core</artifactId>
                        <version>${mybatis.generator.version}</version>
                    </dependency>
                </dependencies>
                <configuration>
                    <!--允许移动生成的文件 -->
                    <verbose>true</verbose>
                    <!-- 是否覆盖 -->
                    <overwrite>true</overwrite>
                    <!-- 自动生成的配置 -->
                    <configurationFile>
                        src/main/resources/mybatis/mybatis-generator.xml</configurationFile>
                </configuration>
            </plugin>
```
3. application.yml 增加mysq及mybatis配置信息

```
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/dolphin?useUnicode=true&amp;characterEncoding=UTF-8&amp;tinyInt1isBit=false
    username: liuzhiqiang
    password: lzq199528
    driver-class-name: com.mysql.jdbc.Driver
    # DruidDataSource 阿里数据库连接池
    type: com.alibaba.druid.pool.DruidDataSource
    #连接池的配置信息
    initialSize: 5
    minIdle: 5
    maxActive: 20
    maxWait: 60000
    timeBetweenEvictionRunsMillis: 60000
    minEvictableIdleTimeMillis: 300000
    validationQuery: SELECT 1 FROM DUAL
    testWhileIdle: true
    testOnBorrow: false
    testOnReturn: false
    poolPreparedStatements: true
    maxPoolPreparedStatementPerConnectionSize: 20
    filters: stat,wall,log4j
    connectionProperties: druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000
    useGlobalDataSourceStat: true
    
#mybatis 配置信息
mybatis:
  type-aliases-package: com.dolphin.domain
  config-location: classpath:/mybatis/mybatis-config.xml
  mapperLocations: classpath:/mappers/**/*.xml
```

4. 新建mybatis-config.xml 路径和config-location统一

```
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN" "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>

    <!-- 配置mybatis的缓存，延迟加载等等一系列属性 -->
    <settings>
        <setting name="autoMappingBehavior" value="FULL"/>
        <!--下换线和驼峰映射，如实体类为userName 数据库是user_name.查询时resultType可以转换 -->
        <setting name="mapUnderscoreToCamelCase" value="true"/>
        <!--  开启二级缓存  	-->
        <setting name="cacheEnabled" value="true"/>
        <!--  懒加载  -->
        <setting name="lazyLoadingEnabled" value="true"/>
        <setting name="aggressiveLazyLoading" value="false"/>

    </settings>

</configuration>
```
5. 新建mybatis-generator.xm 路径和 步骤2里的configurationFile标签统一

```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
<generatorConfiguration>

    <context id="DB2Tables" targetRuntime="MyBatis3">

        <!--当取消自动生成注释时，重新生成xml不会自动覆盖，启用UnmergeableXmlMappersPlugin覆盖生成XML文件-->
        <plugin type="org.mybatis.generator.plugins.UnmergeableXmlMappersPlugin" />

        <commentGenerator>
            <property name="javaFileEncoding" value="UTF-8"/>
            <!-- 是否去除自动生成的注释 true：是 ： false:否 -->
            <property name="suppressAllComments" value="true"/>
            <property name="suppressDate" value="true"/>
        </commentGenerator>

        <!--数据库链接地址账号密码-->
        <jdbcConnection driverClass="com.mysql.jdbc.Driver"
                        connectionURL="jdbc:mysql://127.0.0.1:3306/dolphin" userId="liuzhiqiang"
                        password="lzq199528">
        </jdbcConnection>

        <!--生成Model类存放位置-->
        <javaModelGenerator targetPackage="com.dolphin.domain.sys" targetProject="src/main/java">
            <property name="enableSubPackages" value="true"/>
            <property name="trimStrings" value="true"/>
        </javaModelGenerator>

        <!--生成映射文件存放位置-->
        <sqlMapGenerator targetPackage="mappers.sys" targetProject="src/main/resources">
            <property name="enableSubPackages" value="true"/>
        </sqlMapGenerator>

        <!--生成Dao类存放位置-->
        <!-- 客户端代码，生成易于使用的针对Model对象和XML配置文件 的代码
                type="ANNOTATEDMAPPER",生成Java Model 和基于注解的Mapper对象
                type="MIXEDMAPPER",生成基于注解的Java Model 和相应的Mapper对象
                type="XMLMAPPER",生成SQLMap XML文件和独立的Mapper接口
        -->
        <javaClientGenerator type="XMLMAPPER" targetPackage="com.dolphin.mapper.sys"
                             targetProject="src/main/java">
            <property name="enableSubPackages" value="true"/>
        </javaClientGenerator>
        <!--生成对应表及类名
            tableName表名
            domainObjectName 实体类名称 mapper 和 xml 也是跟就这个名称来生成的
            其余的false是不生成的一些东西，详细信息参考mybatis-generator官网
        -->
        <table tableName="admin_user"
               domainObjectName="AdminUser"
               enableCountByExample="false"
               enableUpdateByExample="false"
               enableDeleteByExample="false"
               enableSelectByExample="false"
               selectByExampleQueryId="false">
            <!--类型转换-->
            <columnOverride column="del_flag" jdbcType="TINYINT" javaType="java.lang.Integer"/>
            <columnOverride column="user_state" jdbcType="TINYINT" javaType="java.lang.Integer"/>
        </table>
    </context>
</generatorConfiguration>
```
6. mysql新建表 admin_user

```
CREATE TABLE `NewTable` (
`id`  bigint(16) NOT NULL AUTO_INCREMENT ,
`user_name`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户名' ,
`password`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '密码' ,
`user_state`  tinyint(2) NOT NULL DEFAULT 0 COMMENT '用户状态 0 可登陆 1不可登录' ,
`phone`  varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '联系方式（手机号）' ,
`real_name`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '真实姓名' ,
`user_img`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户头像' ,
`create_by`  bigint(16) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建者' ,
`create_date`  datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' ,
`update_by`  bigint(16) UNSIGNED NOT NULL DEFAULT 0 COMMENT '更新者' ,
`update_date`  datetime NULL DEFAULT NULL COMMENT '更新时间' ,
`del_flag`  tinyint(2) NOT NULL DEFAULT 0 COMMENT '删除标示 0正常 1删除 ' ,
`remarks`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '备注' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci
AUTO_INCREMENT=1
ROW_FORMAT=DYNAMIC
;


```


7.使用maven里的plugins 来生成 文件

![image](https://raw.githubusercontent.com/2425358736/mybatis-generator-demo/master/image/P%5B1OV_LAW%252TZD1JN5%40_N\(F.png)

8. 如果不出错的话，此时我们就能在想对应的目录下看到我们生成的mapper,sql,model文件了

9. 编写测试controller MybatisController.java

```
package com.dolphin.controller;

import com.dolphin.domain.sys.AdminUser;
import com.dolphin.mapper.sys.AdminUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 开发公司：青岛海豚数据技术有限公司
 * 版权：青岛海豚数据技术有限公司
 * <p>
 * MybatisController
 *
 * @author 刘志强
 * @created Create Time: 2019/2/15
 */
@RestController
@RequestMapping("/mybatis")
public class MybatisController {
    @Autowired
    public AdminUserMapper adminUserMapper;

    /**
     * insert
     * @return
     */
    @GetMapping("add")
    @ResponseBody
    public String add(){
        AdminUser adminUser = new AdminUser();
        adminUser.setUserName("刘志强");
        adminUser.setRealName("刘志强");
        adminUser.setPassword("123456");
        int i = adminUserMapper.insertSelective(adminUser);
        if (i > 0) {
            return "添加成功";
        } else {
            return "添加失败";
        }
    }
    /**
     * update
     * @return
     */
    @GetMapping("up")
    @ResponseBody
    public String up(){
        AdminUser adminUser = new AdminUser();
        adminUser.setId(new Long("1"));
        adminUser.setUserName("刘志强");
        adminUser.setRealName("王妍");
        adminUser.setPassword("123456");
        int i = adminUserMapper.updateByPrimaryKeySelective(adminUser);
        if (i > 0) {
            return "修改成功";
        } else {
            return "修改失败";
        }
    }
    /**
     * select
     * @return
     */
    @GetMapping("get")
    @ResponseBody
    public AdminUser get(Long id) {
        AdminUser adminUser = adminUserMapper.selectByPrimaryKey(id);
        return adminUser;
    }
}
```
10. 访问接口观察

11.  Druid 配置

12. 新建Druid 配置 DruidConfig.java


```
package com.dolphin.config.druid;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * 开发公司：青岛海豚数据技术有限公司
 * 版权：青岛海豚数据技术有限公司
 * <p>
 * DruidConfig
 *
 * @author 刘志强
 * @created Create Time: 2019/2/15
 */
@Configuration
@ServletComponentScan //用于扫描所有的Servlet、filter、listener
public class DruidConfig {
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource druidDataSource() {
        return new DruidDataSource();
    }
}
```

13. 新建Druid过滤器 DruidStatFilter.java


```
package com.dolphin.config.druid;

import com.alibaba.druid.support.http.WebStatFilter;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

/**
 * 开发公司：青岛海豚数据技术有限公司
 * 版权：青岛海豚数据技术有限公司
 * <p>
 * DruidStatFilter
 *
 * @author 刘志强
 * @created Create Time: 2019/2/15
 */
@WebFilter(
        filterName = "druidWebStatFilter",
        urlPatterns = {"/*"},
        initParams = {
                @WebInitParam(name = "exclusions", value = "*.js,*.jpg,*.png,*.gif,*.ico,*.css,/druid/*")//配置本过滤器放行的请求后缀
        }
)
public class DruidStatFilter extends WebStatFilter {
}
```

14. 新建Druid服务 DruidStatViewServlet.java


```
package com.dolphin.config.druid;

import com.alibaba.druid.support.http.StatViewServlet;

import javax.servlet.Servlet;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

/**
 * 开发公司：青岛海豚数据技术有限公司
 * 版权：青岛海豚数据技术有限公司
 * <p>
 * DruidStatViewServlet
 *
 * @author 刘志强
 * @created Create Time: 2019/2/15
 */
@WebServlet(
        urlPatterns = {"/druid/*"},
        initParams = {
                @WebInitParam(name = "allow", value = "127.0.0.1"),
                @WebInitParam(name = "loginUsername", value = "root"),
                @WebInitParam(name = "loginPassword", value = "123"),
                @WebInitParam(name = "resetEnable", value = "true")// 允许HTML页面上的“Reset All”功能

        }
)
public class DruidStatViewServlet extends StatViewServlet implements Servlet {
    private static final long serialVersionUID = 1L;


}
```
15. 启动服务访问 http://localhost:6533/druid/login.html 账号：root 密码：123


