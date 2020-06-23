package cn.andyoung.rabbitmq.demo;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class DlxProducer {

  public static void main(String[] args) throws Exception {

    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    factory.setVirtualHost("/");
    factory.setUsername("guest");
    factory.setPassword("guest");

    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    // 设置连接以及创建 channel 湖绿
    String exchangeName = "test_dlx_exchange";
    String routingKey = "item.update";

    String msg = "this is dlx msg";

    // 我们设置消息过期时间，10秒后再消费 让消息进入死信队列
    AMQP.BasicProperties properties =
        new AMQP.BasicProperties().builder().deliveryMode(2).expiration("10000").build();

    channel.basicPublish(exchangeName, routingKey, true, properties, msg.getBytes());
    System.out.println("Send message : " + msg);

    channel.close();
    connection.close();
  }
}
