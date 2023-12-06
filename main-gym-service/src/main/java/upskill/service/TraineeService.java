package upskill.service;

import upskill.dto.TraineeRegistration;
import upskill.dto.TraineeUpdateRequest;
import upskill.dto.TrainerDtoForTrainee;
import upskill.entity.Trainee;
import upskill.dto.Principal;

import java.util.List;

public interface TraineeService {
  Trainee findById(long traineeId);

  Trainee findByUsername(String username);

  Trainee findByUsernameAndPassword(String username, String password);

  Principal saveTrainee(TraineeRegistration trainee);

  List<Trainee> findAll();

  Trainee updateTrainee(TraineeUpdateRequest traineeUpdateRequest);

  void toggleProfileActivation(long userId, boolean isActive);

  List<TrainerDtoForTrainee> findTrainersForTrainee(long id);
}
