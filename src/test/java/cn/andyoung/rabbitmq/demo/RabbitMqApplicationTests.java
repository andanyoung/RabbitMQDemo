package cn.andyoung.rabbitmq.demo;

import cn.andyoung.rabbitmq.RabbitMqApplication;
import cn.andyoung.rabbitmq.spring.Consumer;
import cn.andyoung.rabbitmq.spring.Producer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = RabbitMqApplication.class)
class RabbitMqApplicationTests {

  @Autowired Producer producer;

  @Autowired Consumer consumer;

  @Test
  void produce() throws InterruptedException {

    producer.produce();
  }
}
