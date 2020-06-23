package cn.andyoung.rabbitmq.demo;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Map;

public class MyConsumer {

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

    // 4. 声明一个队列
    /* durable 持久化
     * exclusive 排他队列
     * 如果你想创建一个只有自己可见的队列，即不允许其它用户访问，RabbitMQ允许你将一个Queue声明成为排他性的（Exclusive Queue）。
     * 该队列的特点是：
     ** 只对首次声明它的连接（Connection）可见
     ** 会在其连接断开的时候自动删除。
     ** 对于第一点，首先是强调首次声明，因为另外一个连接无法声明一个同样的排他性队列；其次是只区别连接（Connection）而不是通道（Channel），从同一个连接创建的不同的通道可以同时访问某一个排他性的队列。这里说的连接是指一个AMQPConnection，以RabbitMQ的Java客户端为例：
     ** 如果试图在一个不同的连接中重新声明或访问（如publish，consume）该排他性队列，会得到资源被锁定的错误：
     **   `ESOURCE_LOCKED - cannot obtain exclusive access to locked queue 'UserLogin2'`
     ** 对于第二点，RabbitMQ会自动删除这个队列，而不管这个队列是否被声明成持久性的（Durable =true)。 也就是说即使客户端程序将一个排他性的队列声明成了Durable的，只要调用了连接的Close方法或者客户端程序退出了，RabbitMQ都会删除这个队列。注意这里是连接断开的时候，而不是通道断开。这个其实前一点保持一致，只区别连接而非通道。
     *  */
    channel.queueDeclare(QUEUE_NAME, true, false, false, null);
    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

    /*
       true:表示自动确认，只要消息从队列中获取，无论消费者获取到消息后是否成功消费，都会认为消息已经成功消费
       false:表示手动确认，消费者获取消息后，服务器会将该消息标记为不可用状态，等待消费者的反馈，如果消费者一
       直没有反馈，那么该消息将一直处于不可用状态，并且服务器会认为该消费者已经挂掉，不会再给其发送消息，
       直到该消费者反馈。
    */

    // 5. 创建消费者并接收消息
    Consumer consumer =
        new DefaultConsumer(channel) {
          @Override
          public void handleDelivery(
              String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
              throws IOException {
            String message = new String(body, "UTF-8");
            Map<String, Object> headers = properties.getHeaders();
            System.out.println("head: " + headers.get("myHead1"));
            System.out.println(" [x] Received '" + message + "'");
            System.out.println("expiration : " + properties.getExpiration());
          }
        };

    // 6. 设置 Channel 消费者绑定队列
    channel.basicConsume(QUEUE_NAME, true, consumer);
  }
}
