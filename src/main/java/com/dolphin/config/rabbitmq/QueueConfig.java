package com.dolphin.config.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 开发公司：青岛海豚数据技术有限公司
 * 版权：青岛海豚数据技术有限公司
 * <p>
 * QueueConfig
 *
 * @author 刘志强
 * @created Create Time: 2019/2/16
 */
@Configuration
public class QueueConfig {

    @Bean(name="queueOne")
    public Queue queueOne() {
        return new Queue("queue.one");
    }

    @Bean(name="queueTwo")
    public Queue queueTwo() {
        return new Queue("queue.two");
    }

    @Bean(name="queueThree")
    public Queue queueThree() {
        return new Queue("queue.three");
    }

    @Bean(name="queueFour")
    public Queue queueFour() {
        return new Queue("queue.four");
    }

    @Bean(name="queueFive")
    public Queue queueFive() {
        return new Queue("queue.five");
    }
}