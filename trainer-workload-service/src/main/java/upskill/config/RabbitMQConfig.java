package upskill.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@RequiredArgsConstructor
@Configuration
public class RabbitMQConfig {
  private static final String exchangeName = "my_exchange";

  private static final String deleteQueue = "queue_for_delete";
  private static final String routingKeyForDelete = "delete_key";

  private static final String saveQueue = "queue_for_save";
  private static final String routingKeyForSave = "save_key";

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

