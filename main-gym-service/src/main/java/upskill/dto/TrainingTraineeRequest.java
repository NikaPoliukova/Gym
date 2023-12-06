package upskill.dto;

import java.time.LocalDate;

public record TrainingTraineeRequest(
    String username,
    LocalDate periodFrom,
    LocalDate periodTo,
    String trainerName,
    String trainingType
) {
}
