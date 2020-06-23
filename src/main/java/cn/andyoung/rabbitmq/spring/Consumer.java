package cn.andyoung.rabbitmq.spring;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {
  @RabbitHandler
  @RabbitListener(queuesToDeclare = @Queue("rabbitmq_queue"))
  public void process(String message) {
    System.out.println("消费者消费消息=====" + message);
  }
}
