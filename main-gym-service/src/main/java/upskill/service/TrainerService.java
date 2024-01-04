package upskill.service;

import upskill.dto.Principal;
import upskill.dto.TraineeDtoForTrainer;
import upskill.dto.TrainerRegistration;
import upskill.dto.TrainerUpdateRequest;
import upskill.entity.Trainer;

import java.util.List;

public interface TrainerService {
  Trainer findById(long trainerId);

  Trainer findByUsername(String username);

  Trainer findByUsernameAndPassword(String username, String password);

  List<Trainer> findAll();

  Principal saveTrainer(TrainerRegistration trainerDto);

  Trainer update(TrainerUpdateRequest request);

  List<Trainer> findByIsActive();

  void toggleProfileActivation(long trainerId, boolean isActive);

  List<TraineeDtoForTrainer> findTraineesForTrainer(long id);
}
