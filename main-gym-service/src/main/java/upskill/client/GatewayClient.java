package upskill.client;


import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import upskill.dto.TrainerTraining;
import upskill.dto.TrainerTrainingDtoForSave;
import upskill.dto.TrainingRequestDto;

import javax.validation.Valid;

@Deprecated
@Validated
@FeignClient(name = "gateway-service")
public interface GatewayClient {
  Logger log = LoggerFactory.getLogger(GatewayClient.class);

  @PostMapping(value = "/workload-service/api/v1/trainer/workload/new-training")
  @CircuitBreaker(name = "saveTrainingCircuit", fallbackMethod = "saveTrainingFallback")
  TrainerTraining saveTraining(@Valid @RequestBody TrainerTrainingDtoForSave trainingDto,
                               @RequestHeader("Authorization") String header);


  @DeleteMapping("/workload-service/api/v1/trainer/workload")
  @CircuitBreaker(name = "deleteTrainingCircuit", fallbackMethod = "deleteTrainingFallback")
  void deleteTraining(@Valid @RequestBody TrainingRequestDto trainingDto,
                      @RequestHeader("Authorization") String header);


  default TrainerTraining saveTrainingFallback(TrainerTrainingDtoForSave trainingDto, String header, Throwable t) {
    log.error("Fallback method called for operation save Training", t);
    return new TrainerTraining();
  }

  default void deleteTrainingFallback(TrainingRequestDto trainingDto, String header,Throwable t) {
    log.error("Fallback method called for operation delete training", t);
  }
}

