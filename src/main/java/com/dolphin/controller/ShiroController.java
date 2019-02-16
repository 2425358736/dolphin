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