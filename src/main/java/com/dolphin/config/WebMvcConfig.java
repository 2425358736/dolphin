package com.dolphin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 开发公司：青岛海豚数据技术有限公司
 * 版权：青岛海豚数据技术有限公司
 * <p>
 * WebMvcConfig
 *
 * @author 刘志强
 * @created Create Time: 2019/2/16
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    //静态资源放过
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }
}
