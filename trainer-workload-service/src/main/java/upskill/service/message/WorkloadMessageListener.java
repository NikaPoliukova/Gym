package upskill.service.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import upskill.dto.DeadLetterMessage;
import upskill.dto.TrainerTrainingDtoForSave;
import upskill.dto.TrainerWorkloadRequestForDelete;
import upskill.service.TrainingSummaryService;

@Slf4j
@Profile("!test")
@Component
@RequiredArgsConstructor
public class WorkloadMessageListener {
  private static final String EXCHANGE_NAME = "my_exchange";
  private static final String SAVE_QUEUE = "save_queue";

  private static final String DELETE_QUEUE = "delete_queue";
  private static final String ROUTING_KEY_FOR_DEAD_LETTER = "dead_letter_key";


  @Autowired
  private RabbitTemplate rabbitTemplate;
  private final TrainingSummaryService service;

  @RabbitListener(queues = {SAVE_QUEUE})
  public void handleMessage(TrainerTrainingDtoForSave trainingDto) {
    try {
      log.info(String.format("Received message -> %s", trainingDto));
      service.saveTraining(trainingDto);
    } catch (Exception e) {
      log.error("training wasn't save");
      sendToDeadLetterQueue(trainingDto);
    }
  }

  @RabbitListener(queues = {DELETE_QUEUE})
  public void handleMessage(TrainerWorkloadRequestForDelete trainingDto) {
    try {
      log.info(String.format("Received message -> %s", trainingDto));
      service.deleteTraining(trainingDto);
    } catch (Exception e) {
      log.error("training wasn't delete");
      var message = DeadLetterMessage.builder().training(trainingDto).exception(e.getCause().getMessage()).build();
      sendToDeadLetterQueue(message);
    }
  }

  private void sendToDeadLetterQueue(DeadLetterMessage message) {
    rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY_FOR_DEAD_LETTER, message);
  }

  private void sendToDeadLetterQueue(TrainerTrainingDtoForSave request) {
    rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY_FOR_DEAD_LETTER, request);
  }
}