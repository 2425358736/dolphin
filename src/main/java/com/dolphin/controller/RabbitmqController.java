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