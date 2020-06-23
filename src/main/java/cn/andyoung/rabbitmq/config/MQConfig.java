package cn.andyoung.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {

  public static final String LAZY_EXCHANGE = "Ex.LazyExchange";
  public static final String LAZY_QUEUE = "MQ.LazyQueue";
  public static final String LAZY_KEY = "lazy.#";

  @Bean
  public TopicExchange lazyExchange() {
    // Map<String, Object> pros = new HashMap<>();
    // 设置交换机支持延迟消息推送
    // pros.put("x-delayed-message", "topic");
    TopicExchange exchange = new TopicExchange(LAZY_EXCHANGE, true, false, null);
    exchange.setDelayed(true);
    return exchange;
  }

  @Bean
  public Queue lazyQueue() {
    return new Queue(LAZY_QUEUE, true);
  }

  @Bean
  public Binding lazyBinding() {
    return BindingBuilder.bind(lazyQueue()).to(lazyExchange()).with(LAZY_KEY);
  }

  @Bean
  public Exchange bootEXChange() {
    return new TopicExchange("BOOT-EXCHANGE-1", true, false);
  }

  @Bean
  public Queue bootQueue() {
    return new Queue("boot.queue1", true);
  }
}
