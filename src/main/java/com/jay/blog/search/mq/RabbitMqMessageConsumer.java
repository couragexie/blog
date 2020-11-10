package com.jay.blog.search.mq;

import com.jay.blog.config.RabbitmqConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * @Author: xiejie
 * @Date: 2020/11/10 17:06
 */
@RabbitListener(queues = RabbitmqConfig.QUEUE_NAME)
public class RabbitMqMessageConsumer {
    private final static Logger logger = LoggerFactory.getLogger(RabbitMqMessageConsumer.class);

    @RabbitHandler
    public void esMessageConcumer(MqEsIndexMessage message){
        logger.info(">>>>> mq 收到 es 同步消息：{}" + message);
        System.out.println(message);
        logger.info(">>>>> es 同步成功");
    }


}
