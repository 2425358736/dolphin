package com.dolphin.config.rabbitmq;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 开发公司：青岛海豚数据技术有限公司
 * 版权：青岛海豚数据技术有限公司
 * <p>
 * ExchangeConfig
 *
 * @author 刘志强
 * @created Create Time: 2019/2/16
 */
@Configuration
public class ExchangeConfig {
    //配置路由交换机 根据路由匹配转发消息给队列
    @Bean(name = "exchangeOne")
    public TopicExchange exchangeOne() {
        return new TopicExchange("exchange.one");
    }

    //配置路由交换机
    @Bean(name = "exchangeTwo")
    public TopicExchange exchangeTwo() {
        return new TopicExchange("exchange.two");
    }

    //配置交换机(广播) 转发消息给旗下所有队列
    @Bean(name = "fanoutExchange")
    FanoutExchange fanoutExchange() {
        return new FanoutExchange("fanoutExchange");
    }
}