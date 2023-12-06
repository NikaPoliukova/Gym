package upskill.dto;

import java.util.List;

public record TrainerResponse(
    String firstName,
    String lastName,
    String specialization,
    boolean isActive,
    List<TraineeDtoForTrainer> traineesList
) {
}
