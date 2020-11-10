package com.jay.blog.search.mq.example;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author: xiejie
 * @Date: 2020/11/10 15:58
 */
public class Consumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("root");
        factory.setPassword("xieJIE123...");
        factory.setHost("120.78.158.142");
        //建立到代理服务器到连接
        Connection conn = factory.newConnection();
        //获得信道
        final Channel channel = conn.createChannel();
        //声明交换器
        String exchangeName = "es-exchange";
        channel.exchangeDeclare(exchangeName, "direct", true);
        // 获取
        String queueName = channel.queueDeclare().getQueue();
        String routingKey = "blog";
        // comsumer 绑定队列，通过键 blog 将队列和交换器绑定起来
        channel.queueBind(queueName, exchangeName, routingKey);

        while(true){
            boolean autoAck = false;
            String consumerTag = "";
            channel.basicConsume(queueName,autoAck, new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag,
                                           Envelope envelope,
                                           AMQP.BasicProperties properties,
                                           byte[] body) throws IOException {
                    String routingKey = envelope.getRoutingKey();
                    String contenType = properties.getContentType();
                    System.out.println("消费的路由键：" + routingKey);
                    System.out.println("消费的消息类型：" + contenType);

                    long deliveryTag = envelope.getDeliveryTag();

                    channel.basicAck(deliveryTag, false);

                    String message = new String(body, "UTF-8");
                    System.out.println("消费的消息内容：" + message);
                }

            });

        }

    }

}
