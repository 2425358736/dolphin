package com.dolphin.config.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 开发公司：青岛海豚数据技术有限公司
 * 版权：青岛海豚数据技术有限公司
 * <p>
 * BindExchangeQueue
 *
 * @author 刘志强
 * @created Create Time: 2019/2/16
 */
@Component
public class BindExchangeQueue {

    // 将queueOne队列绑定到exchangeOne交换机上路由建为exchangeOne.queueOne
    @Bean(name = "BindingExchangeOneQueueOne")
    Binding bindingExchangeMessage(@Qualifier("queueOne") Queue queueOne, @Qualifier("exchangeOne")TopicExchange exchangeOne) {
        return BindingBuilder.bind(queueOne).to(exchangeOne).with("exchangeOne.queueOne");
    }
    // 将queueTwo队列绑定到exchangeOne交换机上路由建为exchangeOne.#
    @Bean(name = "BindingExchangeOneQueueTwo")
    Binding BindingExchangeOneQueueTwo(@Qualifier("queueTwo") Queue queueTwo, @Qualifier("exchangeOne")TopicExchange exchangeOne) {
        return BindingBuilder.bind(queueTwo).to(exchangeOne).with("exchangeOne.#"); //*表示一个词,#表示零个或多个词
    }
    // 将queueThree队列绑定到exchangeOne交换机上路由建为exchangeOne.queueThree
    @Bean(name = "BindingExchangeOneQueueThree")
    Binding BindingExchangeOneQueueThree(@Qualifier("queueThree") Queue queueThree, @Qualifier("exchangeOne")TopicExchange exchangeOne) {
        return BindingBuilder.bind(queueThree).to(exchangeOne).with("exchangeOne.queueThree");
    }
    // 将queueThree队列绑定到exchangeTwo交换机上路由建为exchangeTwo.queueThree
    @Bean(name = "BindingExchangeOneQueueThree")
    Binding BindingExchangeTwoQueueThree(@Qualifier("queueThree") Queue queueThree, @Qualifier("exchangeTwo")TopicExchange exchangeTwo) {
        return BindingBuilder.bind(queueThree).to(exchangeTwo).with("exchangeTwo.queueThree");
    }

    // 将queueFour队列绑定到exchangeTwo交换机上路由建为exchangeTwo.queueFour
    @Bean(name = "BindingExchangeTwoQueueFour")
    Binding BindingExchangeTwoQueueFour(@Qualifier("queueFour") Queue queueFour, @Qualifier("exchangeTwo")TopicExchange exchangeTwo) {
        return BindingBuilder.bind(queueFour).to(exchangeTwo).with("exchangeTwo.queueFour");
    }
    // 将queueFive队列绑定到exchangeTwo交换机上路由建为exchangeTwo.queueFive
    @Bean(name = "BindingExchangeTwoQueuequeueFive")
    Binding BindingExchangeTwoQueuequeueFive(@Qualifier("queueFive") Queue queueFive, @Qualifier("exchangeTwo")TopicExchange exchangeTwo) {
        return BindingBuilder.bind(queueFive).to(exchangeTwo).with("exchangeTwo.queueFive");
    }


    // 将queueOne队列绑定到FanoutExchange交换机上
    @Bean(name = "BindingFanoutExchangeQueueOne")
    Binding BindingFanoutExchangeQueueOne(@Qualifier("queueOne") Queue queueOne, @Qualifier("fanoutExchange")FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(queueOne).to(fanoutExchange);
    }
    // 将queueTwo队列绑定到FanoutExchange交换机上
    @Bean(name = "BindingFanoutExchangeQueueTwo")
    Binding BindingFanoutExchangeQueueTwo(@Qualifier("queueTwo") Queue queueTwo, @Qualifier("fanoutExchange")FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(queueTwo).to(fanoutExchange);
    }

}