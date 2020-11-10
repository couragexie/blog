package com.jay.blog.config;


import org.springframework.amqp.core.*;
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
    public final static  String ROUTING_KEY = "es-exchange";

    // 消息类型
    public final static String CREATE_OR_UPDATE = "create_or_update";
    public final static String REMOVE = "remove";


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

}
