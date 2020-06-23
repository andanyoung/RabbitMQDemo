package cn.andyoung.rabbitmq.spring;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Producer {
  @Autowired RabbitTemplate restTemplate;

  public void produce() throws InterruptedException {
    String message = new Date() + "Beijing";
    System.out.println("生产者生产消息=====" + message);
    restTemplate.convertAndSend("rabbitmq_queue", message);
    Thread.sleep(10000);
  }
}
