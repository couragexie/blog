package com.jay.blog.config;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: xiejie
 * @Date: 2020/11/10 16:57
 */
@Configuration
public class RabbitmqConfig {
    public final static  String EXCHANGE_NAME = "es-exchange";
    public final static  String QUEUE_NAME = "es-queue";
    public final static  String ROUTING_KEY = "es-blog";

    @Value("${spring.rabbitmq.username}")
    private String username;
    @Value("${spring.rabbitmq.password}")
    private String password;
    @Value("${spring.rabbitmq.host}")
    private String host;
//    @Value("${spring.rabbitmq.port}")
//    private Integer port;


    @Bean
    public Queue esQueue(){
        return new Queue(QUEUE_NAME);
    }

    @Bean
    public DirectExchange esExchange(){
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding esBinding(Queue esQueue, DirectExchange esExchange){
        return BindingBuilder.bind(esQueue).to(esExchange).with(ROUTING_KEY);
    }


    public ConnectionFactory connection(){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setHost(host);
//        connectionFactory.setPort(port);
        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connection());
        return rabbitTemplate;
    }

}
