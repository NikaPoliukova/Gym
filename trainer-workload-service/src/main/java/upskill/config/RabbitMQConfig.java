package upskill.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
@EnableRabbit
public class RabbitMQConfig {
  private static final String EXCHANGE_NAME = "my_exchange";

  private static final String DELETE_QUEUE = "delete_queue";
  private static final String ROUTING_KEY_FOR_DELETE = "delete_key";

  private static final String SAVE_QUEUE = "save_queue";
  private static final String ROUTING_KEY_FOR_SAVE = "save_key";

  public static final String DEAD_LETTER_QUEUE = "dead_letter_queue";
  private static final String ROUTING_KEY_FOR_DEAD_LETTER = "dead_letter_key";


  @Bean
  public TopicExchange exchange() {
    return new TopicExchange(EXCHANGE_NAME);
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

  //for save
  @Bean
  public Queue saveQueue() {
    return new Queue(SAVE_QUEUE);
  }

  @Bean
  public Binding bindingForSave() {
    return BindingBuilder
        .bind(saveQueue())
        .to(exchange())
        .with(ROUTING_KEY_FOR_SAVE);
  }

  //for delete
  @Bean
  public Queue deleteQueue() {
    return new Queue(DELETE_QUEUE);
  }

  @Bean
  public Binding bindingForDelete() {
    return BindingBuilder
        .bind(deleteQueue())
        .to(exchange())
        .with(ROUTING_KEY_FOR_DELETE);
  }


  // for dead Letter
  @Bean
  public Queue deadLetterQueue() {
    return new Queue(DEAD_LETTER_QUEUE);
  }

  @Bean
  public Declarables deadLetterBindings() {
    Binding binding = BindingBuilder
        .bind(deadLetterQueue())
        .to(exchange())
        .with(ROUTING_KEY_FOR_DEAD_LETTER);
    return new Declarables(deadLetterQueue(), exchange(), binding);
  }
}

