package cn.andyoung.rabbitmq.demo;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DlxConsumer {

  public static void main(String[] args) throws Exception {

    // 1. 创建一个 ConnectionFactory 并进行设置
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    factory.setVirtualHost("/");
    factory.setUsername("guest");
    factory.setPassword("guest");
    factory.setAutomaticRecoveryEnabled(true);
    factory.setNetworkRecoveryInterval(3000);

    // 2. 通过连接工厂来创建连接
    Connection connection = factory.newConnection();

    // 3. 通过 Connection 来创建 Channel
    final Channel channel = connection.createChannel();
    // 创建连接、创建channel忽略 内容可以在上面代码中获取
    String exchangeName = "test_dlx_exchange";
    String queueName = "test_dlx_queue";
    String routingKey = "item.#";

    // 必须设置参数到 arguments 中
    Map<String, Object> arguments = new HashMap<String, Object>();
    arguments.put("x-dead-letter-exchange", "dlx.exchange");

    channel.exchangeDeclare(exchangeName, "topic", true, false, null);
    // 将 arguments 放入队列的声明中
    channel.queueDeclare(queueName, true, false, false, arguments);

    // 一般不用代码绑定，在管理界面手动绑定
    channel.queueBind(queueName, exchangeName, routingKey);

    // 声明死信队列
    channel.exchangeDeclare("dlx.exchange", "topic", true, false, null);
    channel.queueDeclare("dlx.queue", true, false, false, null);
    // 路由键为 # 代表可以路由到所有消息
    channel.queueBind("dlx.queue", "dlx.exchange", "#");

    Consumer consumer =
        new DefaultConsumer(channel) {
          @Override
          public void handleDelivery(
              String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
              throws IOException {

            String message = new String(body, "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
          }
        };

    // 6. 设置 Channel 消费者绑定队列
    channel.basicConsume(queueName, true, consumer);
  }
}
