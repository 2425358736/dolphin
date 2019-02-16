# [springboot技术栈](https://github.com/2425358736/dolphin/blob/master/README.md)

## [freemarke在线文档](http://freemarker.foofun.cn/index.html)

### FreeMarker是一款模板引擎： 即一种基于模板和要改变的数据，	并用来生成输出文本（HTML网页、电子邮件、配置文件、源代码等）的通用工具。	它不是面向最终用户的，而是一个Java类库，是一款程序员可以嵌入他们所开发产品的组件

1. pom.xml增加freemarker开发包

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-freemarker</artifactId>
</dependency>
```
2. application.yml 中增加freemarker配置

```
spring:
  #freemarker配置
  freemarker:
    cache: false
    charset: UTF-8
    check-template-location: true
    content-type: text/html
    expose-request-attributes: true
    expose-session-attributes: true
    request-context-attribute: request
    template-loader-path: classpath:/templates
```
3. 新建FreemarkerController

```
package com.dolphin.controller;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * 开发公司：青岛海豚数据技术有限公司
 * 版权：青岛海豚数据技术有限公司
 * <p>
 * FreemarkerController
 *
 * @author 刘志强
 * @created Create Time: 2019/2/12
 */
@RestController
@RequestMapping("/freemarker")
public class FreemarkerController {

    @GetMapping("index")
    public ModelAndView index(ModelMap modelMap){
        modelMap.put("userName","刘志强");
        modelMap.put("date",new Date());
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map1 = new HashMap<>();
        map1.put("id",1);
        map1.put("name","张三");
        list.add(map1);
        Map<String,Object> map2 = new HashMap<>();
        map2.put("id",2);
        map2.put("name","李四");
        list.add(map2);
        modelMap.put("list", list);
        return new ModelAndView("/freemarker/index", modelMap);
    }
}
```
##### 在这里介绍下@RestController和@Controller的区别
注解 | 说明
---|---
@RestController | Controller中的方法无法直接返回视图 如 return "index" 这样是返回字符串"index"到前端, 而不是将index.ftl视图返回,ModelAndView不受影响
@Controller | Controller可以配合视图解析器返回视图 如 return "index"是将index.ftl视图返回

4. 新建freemarker模板index.ftl,在配置文件中的template-loader-path下新建目录或文件，跟controller返回的目录对应起来

```
<html>
<body>
<h1>字符串属性</h1>
<p>${userName!""}</p>
<h1>日期属性</h1>
<p>${(date?string('yyyy-MM-dd hh:mm:ss'))!'日期为null'}</p>
<h1>循环</h1>
<div>
<#list list as item>
    <p>第${item_index+1}个用户</p>
    <p>用户名：${item.name}</p>
    <p>id：${item.id}</p>
</#list>
</div>
<h1>判断1</h1>
<#if userName =='刘志强'>
    <P>存在刘志强</p>
<#elseif userName =='王妍'>
    <P>存在王妍</P>
<#else>
    <P>不存在刘志强和王妍</P>
</#if>
<h1>判断2</h1>
<#assign foo = (userName =='王妍')>
${foo?then('存在王妍', '不存在王妍')}
</html>
```
5. 访问 http://localhost:6533/freemarker/index


## freemarker基础语法介绍
```
<p>${userName!""}</p>
```
freemarker语法中不允许出现null值,!号是属性不存在时展示！号后面的内容



