package com.jay.blog.search.mq;

import com.jay.blog.config.RabbitmqConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @Author: xiejie
 * @Date: 2020/11/10 17:06
 */
@Component
@RabbitListener(queues = RabbitmqConfig.QUEUE_NAME)
public class RabbitMqMessageConsumer {
    private final static Logger logger = LoggerFactory.getLogger("MQ-SYN");

    @RabbitHandler
    public void esMessageConcumer(MqEsIndexMessage message){
        logger.info(">>>>> mq consumer message：{}", message);
        System.out.println(message);

    }

    @RabbitHandler
    public void esMessageConcumer(byte[] message){
        String message1 = new String(message);
        logger.info(">>>>> mq 收到 es2 同步消息：{}" + message1);
        System.out.println(new String(message1));
        logger.info(">>>>> es 同步成功");
    }

}
