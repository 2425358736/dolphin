# [springboot技术栈](https://github.com/2425358736/dolphin/blob/master/README.md)

## [thymeleaf官网](https://www.thymeleaf.org/)

### Thymeleaf是一个适用于Web和独立环境的现代服务器端Java模板引擎。
### Thymeleaf的主要目标是为您的开发工作流程带来优雅的自然模板 - 可以在浏览器中正确显示的HTML，也可以用作静态原型，从而在开发团队中实现更强大的协作。

### 通过Spring Framework模块，与您喜欢的工具的大量集成，以及插入您自己的功能的能力，Thymeleaf是现代HTML5 JVM Web开发的理想选择 - 尽管它可以做得更多。

1. pom.xml增加thymeleaf开发包
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```
2. application.yml 中增加thymeleaf配置

```
spring:
  thymeleaf:
    cache: false
    encoding: UTF-8
    check-template-location: true
    mode: HTML5
    prefix: classpath:/templates
    suffix: .html
    servlet:
      content-type: text/html
```
3. 新建ThymeleafController

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
 * ThymeleafController
 *
 * @author 刘志强
 * @created Create Time: 2019/2/12
 */
@RestController
@RequestMapping("/thymeleaf")
public class ThymeleafController {
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
        return new ModelAndView("/thymeleaf/index", modelMap);
    }
}
```
4. 新建thymeleaf模板index.html,在配置文件中的prefix下新建目录或文件,跟controller返回的目录对应起来

```
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<body>
<h1>字符串属性</h1>
<p th:text="${userName}" />
<h1>日期属性</h1>
<p th:text="${#dates.format(date, 'yyyy-MM-dd hh:mm:ss')}" />
<h1>循环</h1>
<div th:each="user,userStat :${list}">
    <p th:text="'第' + ${userStat.count} + '个用户'">
    <p>ID:<span th:text="${user.id}"></span></p>
    <p>名字:<span th:text="${user.name}"></span></p>
</div>
<h1>判断1</h1>
<div th:switch="${userName}">
    <p th:case="'刘志强'">存在刘志强</p>
    <p th:case="'王妍'">存在刘志强王妍</p>
    <p th:case="*">不存在任何人</p>
</div>
<h1>判断2</h1>
<span th:if="${userName} == '刘志强'" th:text="${userName}">  </span><br />
</body>
</html>
```
5. 访问 http://localhost:6533/thymeleaf/index



