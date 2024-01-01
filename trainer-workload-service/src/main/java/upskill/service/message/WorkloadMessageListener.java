package upskill.service.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import upskill.dto.DeadLetterMessage;
import upskill.dto.TrainerTrainingDtoForSave;
import upskill.dto.TrainerWorkloadRequestForDelete;
import upskill.service.TrainingSummaryService;

@Slf4j
@Component
@RequiredArgsConstructor
public class WorkloadMessageListener {
  private static final String exchangeName = "my_exchange";
  private static final String saveQueue = "save_queue";

  private static final String deleteQueue = "delete_queue";
  private static final String routingKeyForDeadLetter = "dead_letter_key";

  @Autowired
  private RabbitTemplate rabbitTemplate;
  private final TrainingSummaryService service;

  @RabbitListener(queues = {saveQueue})
  public void handleMessage(TrainerTrainingDtoForSave trainingDto) {
    try {
      log.info(String.format("Received message -> %s", trainingDto));
      service.saveTraining(trainingDto);
    } catch (Exception e) {
      sendToDeadLetterQueue(trainingDto);
    }
  }

  @RabbitListener(queues = {deleteQueue})
  public void handleMessage(TrainerWorkloadRequestForDelete trainingDto) {
    try {
      log.info(String.format("Received message -> %s", trainingDto));
      service.deleteTraining(trainingDto);
    } catch (Exception e) {
      var message = DeadLetterMessage.builder().training(trainingDto).exception(e.getCause().getMessage()).build();
      sendToDeadLetterQueue(message);
    }
  }

  private void sendToDeadLetterQueue(DeadLetterMessage message) {
    rabbitTemplate.convertAndSend(exchangeName, routingKeyForDeadLetter, message);
  }

  private void sendToDeadLetterQueue(TrainerTrainingDtoForSave request) {
    rabbitTemplate.convertAndSend(exchangeName, routingKeyForDeadLetter, request);
  }
}