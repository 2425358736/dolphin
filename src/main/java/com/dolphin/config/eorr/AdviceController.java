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