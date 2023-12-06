package upskill.dto;

import java.time.LocalDate;

public record TrainingTrainerDto(
    long trainerId,
    LocalDate periodFrom,
    LocalDate periodTo,
    String traineeName
) {
}
