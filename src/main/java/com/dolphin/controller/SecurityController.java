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