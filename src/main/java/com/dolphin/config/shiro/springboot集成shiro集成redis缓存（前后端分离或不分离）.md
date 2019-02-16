### Apache Shiro是一个强大且易用的Java安全框架,执行身份验证、授权、密码和会话管理。使用Shiro的易于理解的API,您可以快速、轻松地获得任何应用程序,从最小的移动应用程序到最大的网络和企业应用程序


### 在springboot中集成

1. pom.xml引入shiro及shiro-redis开发包

```
       <!--shiro-->
        <dependency>
            <groupId>org.apache.shiro</groupId>
            <artifactId>shiro-spring</artifactId>
            <version>1.4.0</version>
        </dependency>
        <!--shiro-redis-->
        <dependency>
            <groupId>org.crazycake</groupId>
            <artifactId>shiro-redis</artifactId>
            <version>2.4.2.1-RELEASE</version>
        </dependency>
```

2. application.yml 中配置redis信息

```
spring:
  redis:
    hostName: 123.206.19.217
    port: 6379
    password:
    timeout: 5000
```

3. 新建Shiro配置文件 ShiroConfig.java

```
package com.dolphin.config.shiro;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 开发公司：青岛海豚数据技术有限公司
 * 版权：青岛海豚数据技术有限公司
 * <p>
 * ShiroConfig
 *
 * @author 刘志强
 * @created Create Time: 2019/2/16
 */
@Configuration
public class ShiroConfig {

    @Value("${spring.redis.hostName}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.timeout}")
    private int timeout;
    @Value("${spring.redis.password}")
    private String password;

    /**
     * shirFilter过滤器
     * @param securityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        //配置shiro默认登录界面地址，前后端分离中登录界面跳转应由前端路由控制，后台仅返回json数据
        //注意过滤器配置顺序 不能颠倒
        //配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了，登出后跳转配置的loginUrl
        filterChainDefinitionMap.put("/shiro/logout2", "logout");
        // 配置不会被拦截的链接 顺序判断
        filterChainDefinitionMap.put("/static/**", "anon");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public MyShiroRealm myShiroRealm() {
        MyShiroRealm myShiroRealm = new MyShiroRealm();
        return myShiroRealm;
    }


    @Bean(name = "securityManager")
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myShiroRealm());
        // 自定义session管理 使用redis
        securityManager.setSessionManager(sessionManager());
        // 自定义缓存实现 使用redis
        securityManager.setCacheManager(cacheManager());
        return securityManager;
    }

    //自定义sessionManager
    @Bean
    public SessionManager sessionManager() {
        SessionConfig mySessionManager = new SessionConfig();
        mySessionManager.setSessionDAO(redisSessionDAO());
        return mySessionManager;
    }

    /**
     * 配置shiro redisManager
     * <p>
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(host);
        redisManager.setPort(port);
        // redisManager.setExpire(18000);// 配置缓存过期时间
        redisManager.setTimeout(timeout);
        redisManager.setPassword(password);
        return redisManager;
    }

    /**
     * cacheManager 缓存 redis实现
     * <p>
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    @Bean
    public RedisCacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }

    /**
     * RedisSessionDAO shiro sessionDao层的实现 通过redis
     * <p>
     * 使用的是shiro-redis开源插件
     */
    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        return redisSessionDAO;
    }

    /**
     * 开启shiro aop注解支持.
     * 使用代理方式;所以需要开启代码支持;
     *
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}
```
用redis实现缓存管理

4. 新建DefaultWebSessionManager继承类，自定义getSessionId方法，以实现基于token登录。

```
package com.dolphin.config.shiro;

import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
 * 开发公司：青岛海豚数据技术有限公司
 * 版权：青岛海豚数据技术有限公司
 * <p>
 * SessionConfig
 *
 * @author 刘志强
 * @created Create Time: 2019/2/16
 */
public class SessionConfig extends DefaultWebSessionManager {

    public SessionConfig() {
        super();
    }

    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        String id = WebUtils.toHttp(request).getHeader("token");
        if (!StringUtils.isEmpty(id) && !"null".equals(id)) {
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, "Stateless request");
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            return id;
        } else {
            //否则按默认规则从cookie取sessionId
            return super.getSessionId(request, response);
        }
    }
}
```

5. 新建验证及授权类 MyShiroRealm 集成 AuthorizingRealm


```
package com.dolphin.config.shiro;

import com.dolphin.domain.sys.AdminUser;
import com.dolphin.mapper.sys.AdminUserMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
 * 开发公司：青岛海豚数据技术有限公司
 * 版权：青岛海豚数据技术有限公司
 * <p>
 * MyShiroRealm
 *
 * @author 刘志强
 * @created Create Time: 2019/2/16
 */
public class MyShiroRealm extends AuthorizingRealm {

    @Autowired
    AdminUserMapper adminUserMapper;

    public MyShiroRealm() {
        super();
    }

    /**
     * 认证信息，主要针对用户登录，
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken authcToken) throws AuthenticationException {
        UsernamePasswordToken utoken = (UsernamePasswordToken) authcToken;
        AdminUser adminUser = new AdminUser();
        adminUser.setUserName(utoken.getUsername());
        adminUser.setPassword(new String(utoken.getPassword()));
        //根据账号密码查用户信息
        AdminUser user = adminUserMapper.verificationUser(adminUser);
        if (null == user) {
            throw new AccountException("账号不存在！");
        } else if (!StringUtils.equals(user.getPassword(), adminUser.getPassword())) {
            throw new AccountException("密码不正确！");
        }
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(user.getUserName(),// 用户名
                user.getPassword(), // 密码
                getName());
        return simpleAuthenticationInfo;
    }

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        String userName = principals.getPrimaryPrincipal().toString().split(":")[0];
        //根据用户userName查询权限（permission) 此处省略sql写固定权限
        Set<String> permissions = new HashSet<>();
        permissions.add("shiro:all");
        info.setStringPermissions(permissions);
        return info;
    }
}
```

6. 定义一个全局异常处理类来处理shiro异常。。AdviceController


```
package com.dolphin.config.eorr;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * 开发公司：青岛海豚数据技术有限公司
 * 版权：青岛海豚数据技术有限公司
 * <p>
 * AdviceController
 *
 * @author 刘志强
 * @created Create Time: 2019/2/16
 */
@RestController
@ControllerAdvice
public class AdviceController {
    @Autowired
    public HttpServletResponse httpServletResponse;
    /**
     * shiro权限错误
     * @param ex
     * @return
     */
    @ExceptionHandler(AuthorizationException.class)
    @CrossOrigin
    public String authorizationException(AuthorizationException ex) {
        if (ex instanceof UnauthenticatedException) {
//            try {
//                httpServletResponse.sendRedirect("/shiro/login");
//            } catch (IOException e) {
//                e.printStackTrace();
//                return "token错误或未登录";
//            }
            return "token错误或未登录";
        } else if (ex instanceof UnauthorizedException) {
            return "用户无权限";
        } else {
            return ex.getMessage();
        }
    }
}
```

7. 建立shiro测试controller

```
package com.dolphin.controller;

import com.dolphin.domain.sys.AdminUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 开发公司：青岛海豚数据技术有限公司
 * 版权：青岛海豚数据技术有限公司
 * <p>
 * ShiroController
 *
 * @author 刘志强
 * @created Create Time: 2019/2/16
 */

@RestController
@RequestMapping("/shiro")
public class ShiroController {


    @GetMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("/freemarker/login");
    }


    /**
     * 验证用户信息，用于登陆
     *
     * @param adminUser
     * @return
     */
    @PostMapping("/verificationUser")
    public String verificationUser(AdminUser adminUser) {
        //验证用户信息
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(adminUser.getUserName(), adminUser.getPassword());
        Subject subject = SecurityUtils.getSubject();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            //完成登录
            subject.login(usernamePasswordToken);
            return subject.getSession().getId().toString();
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping("shiroAll")
    @RequiresPermissions("shiro:all")
    @ResponseBody
    public Map<String,Object> shiroAll(){
        Subject subject = SecurityUtils.getSubject();
        String UserName = subject.getPrincipal().toString().split(":")[0];
        Map<String,Object> map = new HashMap<>();
        map.put("userName", UserName);
        map.put("value", "有权限");
        return map;
    }

    @GetMapping("noAuthority")
    @RequiresPermissions("noAuthority")
    @ResponseBody
    public Map<String,Object> noAuthority(){
        Subject subject = SecurityUtils.getSubject();
        String UserName = subject.getPrincipal().toString().split(":")[0];
        Map<String,Object> map = new HashMap<>();
        map.put("value", "无权限");
        map.put("userName", UserName);
        return map;
    }

    /**
     * 手动退出
     * @param httpServletRequest
     * @return
     */
    @GetMapping("logout")
    public String logout(HttpServletRequest httpServletRequest) {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return  "退出成功";
    }

    /**
     * 自动退出 退出后会重定向到跟目录
     * @return
     */
    @GetMapping("logout2")
    public String logout2() {
        return  "退出成功";
    }
}
```

8. 访问接口测试权限

9. 如果前后端分离, headers里携带token。。token值是verificationUser接口返回的值







