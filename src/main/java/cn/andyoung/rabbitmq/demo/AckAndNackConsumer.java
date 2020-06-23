package cn.andyoung.rabbitmq.demo;

import com.rabbitmq.client.*;

import java.io.IOException;

public class AckAndNackConsumer {
  public static void main(String[] args) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    factory.setVirtualHost("/");
    factory.setUsername("guest");
    factory.setPassword("guest");
    factory.setAutomaticRecoveryEnabled(true);
    factory.setNetworkRecoveryInterval(3000);

    Connection connection = factory.newConnection();

    final Channel channel = connection.createChannel();

    String exchangeName = "test_ack_exchange";
    String queueName = "test_ack_queue";
    String routingKey = "item.#";
    channel.exchangeDeclare(exchangeName, "topic", true, false, null);
    channel.queueDeclare(queueName, false, false, false, null);

    // 一般不用代码绑定，在管理界面手动绑定
    channel.queueBind(queueName, exchangeName, routingKey);

    Consumer consumer =
        new DefaultConsumer(channel) {
          @Override
          public void handleDelivery(
              String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
              throws IOException {

            System.out.println(consumerTag);
            String message = new String(body, "UTF-8");
            System.out.println(" [x] Received '" + message + "'");

            try {
              Thread.sleep(2000);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }

            if ((Integer) properties.getHeaders().get("num") == 0) {
              channel.basicNack(envelope.getDeliveryTag(), false, true);
            } else {
              channel.basicAck(envelope.getDeliveryTag(), false);
            }
          }
        };

    // 6. 设置 Channel 消费者绑定队列 关闭自动ack
    channel.basicConsume(queueName, false, consumer);
  }
}
