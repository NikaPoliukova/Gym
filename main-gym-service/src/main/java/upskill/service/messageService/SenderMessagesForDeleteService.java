package upskill.service.messageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upskill.dto.TrainerWorkloadRequestForDelete;

@Service
@RequiredArgsConstructor
@Slf4j
public class SenderMessagesForDeleteService {

  private static final String EXCHANGE_NAME = "my_exchange";
  private static final String ROUTING_KEY_FOR_DELETE = "delete_key";

  @Autowired
  private RabbitTemplate rabbitTemplate;

  public void sendJsonMessage(TrainerWorkloadRequestForDelete trainingDto) {
    log.info(String.format("Json message sent -> %s", trainingDto.toString()));
    rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY_FOR_DELETE, trainingDto);
  }
}
