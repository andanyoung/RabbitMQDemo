package cn.andyoung.rabbitmq.demo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

/** Return 消息示例 */
public class ReturnListeningProducer {
  public static void main(String[] args) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    factory.setVirtualHost("/");
    factory.setUsername("guest");
    factory.setPassword("guest");

    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    String exchangeName = "test_return_exchange";
    String routingKey = "item.update";
    String errRoutingKey = "error12.update";

    // 指定消息的投递模式：confirm 确认模式
    channel.confirmSelect();

    // 发送
    for (int i = 0; i < 10; i++) {
      String msg = "this is return——listening msg " + i;
      // @param mandatory 设置为 true 路由不可达的消息会被监听到，不会被自动删除
      if (i == 0) {
        channel.basicPublish(exchangeName, errRoutingKey, true, null, msg.getBytes());
      } else {
        channel.basicPublish(exchangeName, routingKey, true, null, msg.getBytes());
      }
      System.out.println("Send message : " + msg);
    }

    // 添加一个确认监听， 这里就不关闭连接了，为了能保证能收到监听消息
    channel.addConfirmListener(
        new ConfirmListener() {
          /** 返回成功的回调函数 */
          public void handleAck(long deliveryTag, boolean multiple) throws IOException {
            System.out.println("succuss ack" + deliveryTag);
          }
          /** 返回失败的回调函数 */
          public void handleNack(long deliveryTag, boolean multiple) throws IOException {
            System.out.println("defeat ack");
          }
        });

    // 添加一个 return 监听
    // 这个可能不会生效：受到 rabbitmq 配置的内存和磁盘的限制 {@link http://www.rabbitmq.com/alarms.html}
    channel.addReturnListener(
        (replyCode, replyText, exchange, routingKey1, properties, body) -> {
          System.out.println("return relyCode: " + replyCode);
          System.out.println("return replyText: " + replyText);
          System.out.println("return exchange: " + exchange);
          System.out.println("return routingKey: " + routingKey1);
          System.out.println("return properties: " + properties);
          System.out.println("return body: " + new String(body));
        });
  }
}
