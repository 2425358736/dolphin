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