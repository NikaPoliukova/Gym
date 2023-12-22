package upskill.service.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import upskill.controller.TrainerWorkloadController;
import upskill.dto.TrainerTrainingDtoForSave;
import upskill.dto.TrainingRequestDto;

@Slf4j
@Component
@RequiredArgsConstructor
public class WorkloadMessageListener {
  private static final String deleteQueue = "queue_for_delete";
  private static final String saveQueue = "queue_for_save";
  private final TrainerWorkloadController controller;

  @RabbitListener(queues = {saveQueue})
  public void handleMessage(TrainerTrainingDtoForSave trainingDto) {
    log.info(String.format("Received message -> %s", trainingDto));
    controller.saveTraining(trainingDto);
  }

  @RabbitListener(queues = {deleteQueue})
  public void handleMessage(TrainingRequestDto trainingDto) {
    log.info(String.format("Received message -> %s", trainingDto));
    controller.deleteTraining(trainingDto);
  }
}