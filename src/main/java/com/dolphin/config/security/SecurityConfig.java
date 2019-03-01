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
