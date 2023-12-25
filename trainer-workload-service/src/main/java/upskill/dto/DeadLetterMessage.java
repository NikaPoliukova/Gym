package upskill.dto;

import lombok.Data;

@Data
public class DeadLetterMessage {
  String exception;
  TrainingRequestDto training;
}
