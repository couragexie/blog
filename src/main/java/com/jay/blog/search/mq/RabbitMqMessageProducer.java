package com.jay.blog.search.mq;

import com.jay.blog.config.RabbitmqConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @Author: xiejie
 * @Date: 2020/11/16 14:33
 */
@Component
public class RabbitMqMessageProducer {
    private final static Logger logger = LoggerFactory.getLogger("MQ-SYN");

    @Autowired
    RabbitTemplate rabbitTemplate;

    public void publishMessage(MqEsIndexMessage mqEsIndexMessage){
        logger.info("MQ publish message : message={}", mqEsIndexMessage);
        rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_NAME, RabbitmqConfig.ROUTING_KEY, mqEsIndexMessage);

    }


}
