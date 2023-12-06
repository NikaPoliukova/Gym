package upskill.dto;

import java.time.LocalDate;

public record TrainingTrainerRequest(
    String username,
    LocalDate periodFrom,
    LocalDate periodTo,
    String traineeName
) {
}
