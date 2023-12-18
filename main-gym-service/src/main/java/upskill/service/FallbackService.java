//package upskill.service;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import upskill.client.GatewayClient;
//import upskill.dto.TrainerTraining;
//import upskill.dto.TrainerTrainingDtoForSave;
//import upskill.dto.TrainingRequestDto;
//
//@Slf4j
//@Service
//public class FallbackService implements GatewayClient {
//
//  @Override
//  public TrainerTraining saveTraining(TrainerTrainingDtoForSave trainingDto, String header) {
//    log.error("Fallback method called due to circuit breaker or error");
//    return new TrainerTraining();
//  }
//
//  @Override
//  public void deleteTraining(TrainingRequestDto trainingDto) {
//    log.error("Fallback method called for delete");
//  }
//}
