package upskill.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableRabbit
public class RabbitMQConfig {
  private static final String exchangeName = "my_exchange";
  private static final String deleteQueue = "delete_queue";
  private static final String routingKeyForDelete = "delete_key";

  private static final String saveQueue = "save_queue";
  private static final String routingKeyForSave = "save_key";

  public static final String deadLetterQueue = "dead_letter_queue";
  private static final String routingKeyForDeadLetter = "dead_letter_key";


  @Bean
  public TopicExchange exchange() {
    return new TopicExchange(exchangeName);
  }

  //for save
  @Bean
  public Queue saveQueue() {
    return new Queue(saveQueue);
  }

  @Bean
  public Binding bindingForSave() {
    return BindingBuilder
        .bind(saveQueue())
        .to(exchange())
        .with(routingKeyForSave);
  }

    //for delete
  @Bean
  public Queue deleteQueue() {
    return new Queue(deleteQueue);
  }

  @Bean
  public Binding bindingForDelete() {
    return BindingBuilder
        .bind(deleteQueue())
        .to(exchange())
        .with(routingKeyForDelete);
  }


  // Dead Letter
  @Bean
  public Queue deadLetterQueue() {
    return new Queue(deadLetterQueue);
  }

  @Bean
  public Declarables deadLetterBindings() {
    Binding binding = BindingBuilder
        .bind(deadLetterQueue())
        .to(exchange())
        .with(routingKeyForDeadLetter);
    return new Declarables(deadLetterQueue(), exchange(), binding);
  }

  @Bean
  public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
    RabbitTemplate template = new RabbitTemplate(connectionFactory);
    template.setMessageConverter(messageConverter());
    return template;
  }

  @Bean
  public MessageConverter messageConverter() {
    ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
    return new Jackson2JsonMessageConverter(mapper);
  }
}

