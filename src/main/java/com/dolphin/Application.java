package com.dolphin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 开发公司：青岛海豚数据技术有限公司
 * 版权：青岛海豚数据技术有限公司
 * <p>
 * Application
 *
 * @author 刘志强
 * @created Create Time: 2019/2/12
 */
@SpringBootApplication(scanBasePackages = "com.dolphin")
@RestController
@EnableAutoConfiguration
public class Application implements CommandLineRunner {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        logger.info("服务已启动");
    }

    // 跟目录重定向
    @RequestMapping("/")
    @ResponseBody
    Map<String,Object> home() {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("作者：","刘志强");
        return map;
    }
}