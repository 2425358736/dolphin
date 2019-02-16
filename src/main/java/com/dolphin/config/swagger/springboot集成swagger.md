# [springboot技术栈](https://github.com/2425358736/dolphin/blob/master/README.md)

## [swagger官网](https://swagger.io/)

swagger注解说明


注解 | 说明
---|---
@Api() | 用于类，表示标识这个类是swagger的资源 
@ApiOperation() | 用于方法,表示一个http请求的操作
@ApiParam() | 用于方法，参数，字段说明，表示对参数的添加元数据（说明或是否必填等） 
@ApiModel() | 用于实体类，@RequestBody接受的参数
@ApiModelProperty() | 实体类属性描述
@ApiImplicitParam() | 用于方法，表示单独的请求参数 
@ApiImplicitParams() | 用于方法，包含多个@ApiImplicitParam

## 集成
1. pom.xml引入swagger开发包及swagger-ui包

```
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
    <version>2.6.1</version>
</dependency>
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger-ui</artifactId>
    <version>2.6.1</version>
</dependency>
```
2. 新建swagger配置类 SwaggerConfig.java

```
package com.dolphin.config.swagger;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * 开发公司：青岛海豚数据技术有限公司
 * 版权：青岛海豚数据技术有限公司
 * <p>
 * SwaggerConfig
 *
 * @author 刘志强
 * @created Create Time: 2019/2/13
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        //添加head参数
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        tokenPar.name("Authorization").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        pars.add(tokenPar.build());

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                // 扫描的路径
                .apis(RequestHandlerSelectors.basePackage("com.dolphin.controller"))
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                // 添加全局操作参数Authorization
                .globalOperationParameters(pars);
    }

    // 配置api信息，版本，邮箱之类
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("server.api")
                .description("server接口文档说明")
                .contact(new Contact("dolphin", "", "2425358736@qq.com"))
                .version("1.0")
                .build();
    }
}
```
3. 新建swagger测试controller SwaggerController.java
```
package com.dolphin.controller;

import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * 开发公司：青岛海豚数据技术有限公司
 * 版权：青岛海豚数据技术有限公司
 * <p>
 * SwaggerController
 *
 * @author 刘志强
 * @created Create Time: 2019/2/13
 */
@RestController
@RequestMapping("/swagger")
@CrossOrigin
@Api(description = "swagger测试api")
public class SwaggerController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping("index")
    @ApiOperation(value = "swagger测试index", notes = "参数如下：{\"userName\": \"刘志强\",\"password\": \"123\"}")
    public User index(@RequestBody  User user){
        return user;
    }

    @PostMapping("index2")
    @ApiOperation(value = "swagger测试index2")
    public User index(@ApiParam(name="id",value="用户id",required=true) @RequestParam Long id){
        logger.info("id参数：" + id);
        User user = new User();
        user.setUserName("刘志强");
        user.setPassword("123456");
        return user;
    }

    @PostMapping("/index3")
    @ApiOperation(value = "swagger测试index3")
    @ApiImplicitParam(name="id",value="用户id",dataType="Long", paramType = "query")
    public User index3(Long id){
        User user = index(id);
        return user;
    }

    @PostMapping("/index4")
    @ApiOperation(value = "swagger测试index4")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userName",value="用户名",dataType="String", paramType = "query",defaultValue="刘志强"),
            @ApiImplicitParam(name="password",value="密码",dataType="String", paramType = "query",defaultValue="123456")})
    public User index4(String userName, String password){
        User user = new User();
        user.setUserName(userName);
        user.setPassword(password);
        return user;
    }
}
@ApiModel
class User{
    @ApiModelProperty(value = "用户名", name = "userName")
    private String userName;
    @ApiModelProperty(value = "密码", name = "password")
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
```

4. 访问 http://localhost:6533/swagger-ui.html 进入swagger-ui的页面，可以点击Try it out!按钮测试接口

5. swagger 的数据来源是/v2/api-docs接口， 访问
http://localhost:6533/v2/api-docs 接口返回json数据。如果想自己实现页面的展示可以拿此数据去自定义页面渲染

