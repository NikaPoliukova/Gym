package upskill.dto;

import java.time.LocalDate;

public record TrainingDtoRequest(
    String username,
    LocalDate periodFrom,
    LocalDate periodTo,
    String trainerName,
    TrainingTypeEnum trainingType
) {
}
