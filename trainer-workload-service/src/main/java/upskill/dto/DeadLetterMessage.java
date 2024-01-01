package upskill.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeadLetterMessage {
  String exception;
  TrainerWorkloadRequestForDelete training;
}
