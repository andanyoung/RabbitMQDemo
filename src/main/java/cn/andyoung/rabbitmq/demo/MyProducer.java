package cn.andyoung.rabbitmq.demo;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.HashMap;
import java.util.Map;

public class MyProducer {
  private static final String QUEUE_NAME = "ITEM_QUEUE";

  public static void main(String[] args) throws Exception {
    // 1. 创建一个 ConnectionFactory 并进行设置
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    factory.setVirtualHost("/");
    factory.setUsername("guest");
    factory.setPassword("guest");

    // 2. 通过连接工厂来创建连接
    Connection connection = factory.newConnection();

    // 3. 通过 Connection 来创建 Channel
    Channel channel = connection.createChannel();

    /* Message 消息 Properties
     * deliverMode 设置为 2 的时候代表持久化消息
     * expiration 意思是设置消息的有效期，超过10秒没有被消费者接收后会被自动删除
     * headers 自定义的一些属性
     */
    Map<String, Object> headers = new HashMap<>();
    headers.put("myHead1", "111");
    headers.put("myHead2", "222");

    AMQP.BasicProperties properties =
        new AMQP.BasicProperties()
            .builder()
            .deliveryMode(2)
            .contentEncoding("UTF-8")
            .expiration("100000")
            .headers(headers)
            .build();

    // 实际场景中，消息多为json格式的对象 Message：消息的 Body
    String msg = "hello";
    // 4. 发送三条数据
    for (int i = 1; i <= 3; i++) {
      channel.basicPublish("", QUEUE_NAME, properties, msg.getBytes());
      System.out.println("Send message" + i + " : " + msg);
    }

    // 5. 关闭连接
    channel.close();
    connection.close();
  }
}
