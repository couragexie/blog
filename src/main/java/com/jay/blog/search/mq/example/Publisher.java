package com.jay.blog.search.mq.example;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author: xiejie
 * @Date: 2020/11/10 15:51
 */
public class Publisher {
    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUsername("root");
        connectionFactory.setPassword("xieJIE123...");
        connectionFactory.setHost("120.78.158.142");
        // 创建连接器
        Connection connection = connectionFactory.newConnection();
        // 创建 channel
        Channel channel = connection.createChannel();

        String exchangeName = "es-exchange";
        // 声明交换器
        channel.exchangeDeclare(exchangeName,"direct", true);

        String routingKey = "blog";
        byte[] message = "this is a new blog".getBytes();
        // producer 绑定交换器
        channel.basicPublish(exchangeName, routingKey, null, message);

        channel.close();
        connection.close();
    }

}
