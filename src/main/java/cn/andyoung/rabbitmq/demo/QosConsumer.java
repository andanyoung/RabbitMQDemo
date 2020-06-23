package cn.andyoung.rabbitmq.demo;

import com.rabbitmq.client.*;

import java.io.IOException;

public class QosConsumer {
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

    // 4. 声明
    String exchangeName = "test_qos_exchange";
    String queueName = "test_qos_queue";
    String routingKey = "item.#";
    channel.exchangeDeclare(exchangeName, "topic", true, false, null);
    channel.queueDeclare(queueName, true, false, false, null);

    channel.basicQos(0, 3, false);

    // 一般不用代码绑定，在管理界面手动绑定
    channel.queueBind(queueName, exchangeName, routingKey);

    // 5. 创建消费者并接收消息
    Consumer consumer =
        new DefaultConsumer(channel) {
          @Override
          public void handleDelivery(
              String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
              throws IOException {

            String message = new String(body, "UTF-8");
            System.out.println("[x] Received '" + message + "'");
            try {
              Thread.sleep(5000);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }

            channel.basicAck(envelope.getDeliveryTag(), true);
          }
        };
    // 6. 设置 Channel 消费者绑定队列
    channel.basicConsume(queueName, false, consumer);
  }
}
