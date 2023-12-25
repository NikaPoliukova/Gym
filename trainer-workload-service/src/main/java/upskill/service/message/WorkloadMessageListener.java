package upskill.service.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import upskill.controller.TrainerWorkloadController;
import upskill.dto.DeadLetterMessage;
import upskill.dto.TrainerTrainingDtoForSave;
import upskill.dto.TrainingRequestDto;

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
  private final TrainerWorkloadController controller;

  @RabbitListener(queues = {saveQueue})
  public void handleMessage(TrainerTrainingDtoForSave trainingDto) {
    try {
      log.info(String.format("Received message -> %s", trainingDto));
      controller.saveTraining(trainingDto);
    } catch (Exception e) {
      sendToDeadLetterQueue(trainingDto);
    }
  }

  @RabbitListener(queues = {deleteQueue})
  public void handleMessage(TrainingRequestDto trainingDto) {
    try {
      log.info(String.format("Received message -> %s", trainingDto));
      controller.deleteTraining(trainingDto);
    } catch (Exception e) {
      var message = new DeadLetterMessage();
      message.setTraining(trainingDto);
      message.setException(e.getCause().getMessage());
      sendToDeadLetterQueue(message);
    }
  }

  private void sendToDeadLetterQueue(DeadLetterMessage message ) {
    rabbitTemplate.convertAndSend(exchangeName, routingKeyForDeadLetter, message);
  }

  private void sendToDeadLetterQueue(TrainerTrainingDtoForSave request) {
    rabbitTemplate.convertAndSend(exchangeName, routingKeyForDeadLetter, request);
  }
}