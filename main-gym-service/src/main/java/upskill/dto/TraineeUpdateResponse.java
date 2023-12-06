package upskill.dto;

import java.util.List;

public record TraineeUpdateResponse(
    String username,
    String firstName,
    String lastName,
    String dateOfBirth,
    String address,
    boolean isActive,
    List<TrainerDtoForTrainee> trainersList
) {
}
