package upskill.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record TrainingDto(
    String trainingName,
    LocalDate trainingDate,
    int trainingDuration,
    int trainingTypeId,
    long traineeId,
    long trainerId
) {
}
