package com.jay.blog.search.mq.example;

import com.jay.blog.config.RabbitmqConfig;
import com.jay.blog.search.mq.MqEsIndexMessage;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author: xiejie
 * @Date: 2020/11/10 15:51
 */
public class Producer {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        // 创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUsername("root");
        connectionFactory.setPassword("xieJIE123...");
        connectionFactory.setHost("120.78.158.142");
        // 创建连接器
        Connection connection = connectionFactory.newConnection();
        // 创建 channel
        Channel channel = connection.createChannel();

//        String exchangeName = "es-exchange";
        String exchangeName = RabbitmqConfig.EXCHANGE_NAME;
        // 声明交换器
        channel.exchangeDeclare(exchangeName,"direct", true);

//        String routingKey = "es-blog";
        String routingKey = RabbitmqConfig.ROUTING_KEY;


        // producer 绑定交换器
        for (int i = 0; i < 100; i ++) {
            MqEsIndexMessage message = new MqEsIndexMessage(10L, MqEsIndexMessage.CREATE_OR_UPDATE);
            //byte[] message = new String("this is a new blog" + i).getBytes();
            //channel.basicPublish(exchangeName, routingKey, null, message);
            //Thread.sleep(1000);
        }

        channel.close();
        connection.close();
    }

    public static void publish(){


    }
}
