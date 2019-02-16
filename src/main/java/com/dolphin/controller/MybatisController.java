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