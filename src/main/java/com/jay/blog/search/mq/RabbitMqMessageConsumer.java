package com.jay.blog.search.mq;

import com.jay.blog.config.RabbitmqConfig;
import com.jay.blog.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author: xiejie
 * @Date: 2020/11/10 17:06
 */
@Component
@RabbitListener(queues = RabbitmqConfig.QUEUE_NAME)
public class RabbitMqMessageConsumer {
    private final static Logger logger = LoggerFactory.getLogger("MQ-SYN");

    @Autowired
    private SearchService searchService;

    @RabbitHandler
    public void esMessageConcumer(MqEsIndexMessage message)  {
        logger.info(">>>>> mq 同步更新 es ：{}", message);
        try {
            if (message.getType().equals(MqEsIndexMessage.CREATE_OR_UPDATE)) {
                searchService.createDoc(message.getPostId());
            } else if (message.getType().equals(MqEsIndexMessage.REMOVE)) {
                searchService.deleteDoc(message.getPostId());
            }
        }catch (Exception exception){
            logger.error(" mq 同步 es 失败，postId:{}",message.getPostId());
        }
    }

    @RabbitHandler
    public void esMessageConcumer(byte[] message){
        String message1 = new String(message);
        logger.info(">>>>> mq 收到 es2 同步消息：{}" + message1);
        System.out.println(new String(message1));
        logger.info(">>>>> es 同步成功");
    }

}
