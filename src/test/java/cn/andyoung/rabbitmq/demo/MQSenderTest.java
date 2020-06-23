package cn.andyoung.rabbitmq.demo;

import cn.andyoung.rabbitmq.RabbitMqApplication;
import cn.andyoung.rabbitmq.entiy.User;
import cn.andyoung.rabbitmq.spring.MQSender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = RabbitMqApplication.class)
public class MQSenderTest {

  @Autowired private MQSender mqSender;

  @Test
  public void send() {
    String msg = "hello spring boot";
    try {
      for (int i = 0; i < 15; i++) {
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        // mqSender.send(msg + ":" + i, null);
        mqSender.sendUser(new User("anQi", "pwd*wd"));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void sendLazy() {

    String msg = "hello spring boot";

    mqSender.sendLazy(msg + ":");
  }
}
