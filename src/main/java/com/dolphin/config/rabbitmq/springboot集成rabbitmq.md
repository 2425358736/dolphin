# [springboot技术栈](https://github.com/2425358736/dolphin/blob/master/README.md)

### 简介
RabbitMQ是实现了高级消息队列协议（AMQP）的开源消息代理软件（亦称面向消息的中间件）。RabbitMQ服务器是用Erlang语言编写的，而群集和故障转移是构建在开放电信平台框架上的。所有主要的编程语言均有与代理接口通讯的客户端库。

### 安装请自行百度

### 概念
  

名称 | 说明
---|---
生产者| 发送消息给队列，或交换机
队列 | 存储消息，并接受订阅
交换机 | 绑定队列，转发消息给旗下的队列
消费者 | 订阅队列，消费消息

### springboot 集成

1. pom.xml 引入开发包

```
        <!--amqp-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-amqp</artifactId>
        </dependency>
```

2. application.yml中配置mq


```
spring:
  rabbitmq:
    host: 123.206.19.217
    port: 5672
    username: guest
    password: guest
```

3. 新建交换机配置类ExchangeConfig.java，创建交换机实例


```
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
```

4. 新建队列配置类QueueConfig.java，创建队列实例


```
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
```

5. 新建交换机队列绑定类BindExchangeQueue.java，将队列绑定至交换机


```
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
```

6. 新建rabbitmq测试Controller


```
package com.dolphin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 开发公司：青岛海豚数据技术有限公司
 * 版权：青岛海豚数据技术有限公司
 * <p>
 * RabbitmqController
 *
 * @author 刘志强
 * @created Create Time: 2019/2/16
 */
@RestController
@RequestMapping("/rabbitmq")
public class RabbitmqController {
    @Autowired
    private AmqpTemplate rabbitTemplate;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     *发送消息给
     * @param exchangeName 队列名
     * @param RoutingKey 路由建
     * @param content 内容
     * @return
     */
    @GetMapping("/pushExchange")
    public String pushExchange(String exchangeName, String RoutingKey, String content) {
        rabbitTemplate.convertAndSend(exchangeName,RoutingKey,content);
        return "已推送";
    }

    /**
     *
     * @param queueName 队列名
     * @param content 消息
     * @return
     */
    @GetMapping("/pushQueue")
    public String pushQueue(String queueName, String content) {
        rabbitTemplate.convertAndSend(queueName,content);
        return "已推送";
    }

    //订阅queue.one的Queue
    @RabbitListener(queues="queue.one")
    public void queueOne(Object o) {
        logger.info("queue=queue.one:::::content:"+ o.toString());
    }
    //订阅queue.two的Queue
    @RabbitListener(queues="queue.two")
    public void queueTwo(Object o) {
        System.out.println("queue=queue.two:::::content:"+ o.toString());
    }
    //订阅queue.three的Queue
    @RabbitListener(queues="queue.three")
    public void queueThree(Object o) {
        System.out.println("queue=queue.three:::::content:"+ o.toString());
    }
    //订阅queue.four的Queue
    @RabbitListener(queues="queue.four")
    public void queueFour(Object o) {
        System.out.println("queue=queue.four:::::content:"+ o.toString());
    }
    //订阅queue.five的Queue
    @RabbitListener(queues="queue.five")
    public void queueFive(Object o) {
        System.out.println("queue=queue.five:::::content:"+ o.toString());
    }

}
```
###### 给指定队列推送消息

访问 http://localhost:6533/rabbitmq/pushQueue?queueName=queue.one&content=dddddddddddddddd


###### 像交换机推送消息

访问 http://localhost:6533/rabbitmq/pushExchange?exchangeName=exchange.one&RoutingKey=exchangeOne.queueOne&content=dddddddddddddddd 
###### 通过路由建来匹配要转发的队列，如果是广播交换机，路由则是null,会给旗下的所有队列转发消息（如请求： http://localhost:6533/rabbitmq/pushExchange?exchangeName=fanoutExchange&content=dddddddddddddddd）

观察订阅队列的接口是否接收到队列







