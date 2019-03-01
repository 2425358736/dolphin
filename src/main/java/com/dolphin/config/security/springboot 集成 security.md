1.  pxm.xml 引用开发包

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

2.  新建权限实体类 实现 GrantedAuthority 接口


```
package com.dolphin.config.security;

import org.springframework.security.core.GrantedAuthority;


/**
 * 开发公司：青岛海豚数据技术有限公司
 * 版权：青岛海豚数据技术有限公司
 * <p>
 * SysPermission
 *
 * @author 刘志强
 * @created Create Time: 2019/2/28
 */
public class SysPermission implements GrantedAuthority {

    private String perCode;

    public String getPerCode() {
        return perCode;
    }

    public void setPerCode(String perCode) {
        this.perCode = perCode;
    }

    @Override
    public String getAuthority() {
        return this.perCode;
    }
}
```

3. 新建用户实体类 实现 UserDetails 接口

```
package com.dolphin.config.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * 开发公司：青岛海豚数据技术有限公司
 * 版权：青岛海豚数据技术有限公司
 * <p>
 * User
 *
 * @author 刘志强
 * @created Create Time: 2019/2/28
 */
public class User implements UserDetails {
    private Long id;

    private String userName;

    private String password;

    private List<SysPermission> list;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<SysPermission> getList() {
        return list;
    }

    public void setList(List<SysPermission> list) {
        this.list = list;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.getList();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
```
4. 新建查询用户集成权限类 集成 UserDetailsServiceImpl接口


```
package com.dolphin.config.security;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 开发公司：青岛海豚数据技术有限公司
 * 版权：青岛海豚数据技术有限公司
 * <p>
 * UserDetailsServiceImpl
 *
 * @author 刘志强
 * @created Create Time: 2019/2/15
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    public User loadUserByUsername(String userName) throws UsernameNotFoundException {
        // 根据userName 查询用户信息，此处写死用户信息
        User user = new User();
        user.setUserName(userName);
        user.setPassword("123456");
        List<SysPermission> list = new ArrayList<>();
        SysPermission sysPermission = new SysPermission();
        sysPermission.setPerCode("security:list");
        list.add(sysPermission);
        SysPermission sysPermission1 = new SysPermission();
        sysPermission1.setPerCode("security:test");
        list.add(sysPermission1);
        user.setList(list);
        return user;
    }
}

```

5. 新建 SecurityConfig 配置类


```
package com.dolphin.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;


/**
 * 开发公司：青岛海豚数据技术有限公司
 * 版权：青岛海豚数据技术有限公司
 * <p>
 * SecurityConfig
 *
 * @author 刘志强
 * @created Create Time: 2019/2/15
 */
@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable();
        httpSecurity
                .authorizeRequests()
                .antMatchers("/static/**").permitAll()
                /**
                 * fullyAuthenticated 需要登录才可以访问
                 * denyAll 拒绝所有的访问
                 * anonymous 可以匿名访问
                 * permitAll 不需要验证
                 */
                .antMatchers("/security/notPer").fullyAuthenticated()
                /**
                 * /security/notPer 异常处理
                 */
                .and().exceptionHandling().accessDeniedPage("/security/notPer")
                /**
                 * /security/notLogin 登录接口 当访问需要登录才可以访问的接口时，未登录会跳转至登录页。
                 * /security/loginError 登录异常接口
                 *
                 * failureUrl 异常处理地址
                 * loginPage 登录页面
                 * 下面这行代码在前后端分离的项目中可以不写 .未登录等操作可以在拦截器进行处理
                 */
                .and().formLogin().loginPage("/security/notLogin").failureUrl("/security/loginError");
    }
}

```
6. 新建测试 controller


```
package com.dolphin.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


/**
 * 开发公司：青岛海豚数据技术有限公司
 * 版权：青岛海豚数据技术有限公司
 * <p>
 * SecurityController
 * 前后端分离目前除了基于jwt，还没有找到其他方案
 * @author 刘志强
 * @created Create Time: 2019/2/28
 */
@RestController
@RequestMapping("/security")
@CrossOrigin
public class SecurityController {

    @GetMapping("login")
    public String login2(User user) {
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authRequest);
        return "登陆成功";
    }

    @GetMapping("securityList")
    @PreAuthorize("hasAnyAuthority('security:list')")
    @ResponseBody
    public String securityList(){
        return "测试有权限";
    }

    @GetMapping("securityList2")
    @PreAuthorize("hasAnyAuthority('security:list222')")
    @ResponseBody
    public String securityList2(){
        return "测试无权限";
    }

    @GetMapping("/notPer")
    @ResponseBody
    public String notPer(){
        return "无权限/测试未登录";
    }

    @GetMapping("/notLogin")
    @ResponseBody
    public String notLogin(){
        return "未登录/跳转此页";
    }

    @GetMapping("/loginError")
    @ResponseBody
    public String loginError(){
        return "登录异常";
    }

}
```






