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
