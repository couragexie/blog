package com.jay.blog.search.mq.example;


import com.jay.blog.config.RabbitmqConfig;
import com.jay.blog.search.mq.MqEsIndexMessage;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * @Author: xiejie
 * @Date: 2020/11/16 14:49
 */
public class Producer2 {

    public static void main(String[] args) {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setUsername("root");
        connectionFactory.setPassword("xieJIE123...");
        connectionFactory.setHost("120.78.158.142");

        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);

        for (int i = 0; i < 100; i ++) {
            MqEsIndexMessage message = new MqEsIndexMessage(10L, MqEsIndexMessage.CREATE_OR_UPDATE);
            rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_NAME, RabbitmqConfig.ROUTING_KEY, message);
        }
    }
}
