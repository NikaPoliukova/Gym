package upskill.dto;

public record TrainingTraineeResponse(
    String trainingName,
    String trainingDate,
    String trainingType,
    int trainingDuration,
    String trainerName
) {
}