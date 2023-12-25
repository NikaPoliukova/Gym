package upskill.service.messageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upskill.dto.TrainerTrainingDtoForSave;

@Service
@RequiredArgsConstructor
@Slf4j
public class SenderMessagesForSaveService {
  private static final String exchangeName = "my_exchange";
  private static final String routingKeyForSave = "save_key";


  @Autowired
  private RabbitTemplate rabbitTemplate;

  public void sendJsonMessage(TrainerTrainingDtoForSave trainingDto) {
    log.info(String.format("Json message sent -> %s", trainingDto.toString()));
    rabbitTemplate.convertAndSend(exchangeName, routingKeyForSave, trainingDto);
  }
}
