package upskill.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upskill.converter.TraineeConverter;
import upskill.dao.TrainerRepository;
import upskill.dao.TrainingRepository;
import upskill.dto.Principal;
import upskill.dto.TraineeDtoForTrainer;
import upskill.dto.TrainerRegistration;
import upskill.dto.TrainerUpdateRequest;
import upskill.entity.Trainer;
import upskill.entity.TrainingType;
import upskill.exception.UserNotFoundException;
import upskill.service.HashPassService;
import upskill.service.TrainerService;
import upskill.service.UserService;
import upskill.util.UserUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class TrainerServiceImpl implements TrainerService {
  private final HashPassService hashPassService;
  private final TrainerRepository trainerRepository;
  private final UserService userService;
  private final TraineeConverter traineeConverter;
  private final TrainingRepository trainingRepository;

  @Override
  @Transactional
  public Trainer findById(long trainerId) {
    return trainerRepository.findById(trainerId).orElseThrow(()
        -> new UserNotFoundException("trainer with id = " + trainerId));
  }

  @Override
  @Transactional(readOnly = true)
  public Trainer findByUsername(String username) {
    return trainerRepository.findByUsername(username).orElseThrow(()
        -> new UserNotFoundException(username));
  }

  @Override
  @Transactional
  public Trainer findByUsernameAndPassword(String username, String password) {
    var hashedPassword = hashPassService.hashPass(password);
    return trainerRepository.findByUsernameAndPassword(username, hashedPassword).orElseThrow(()
        -> new UserNotFoundException(username));
  }

  @Override
  @Transactional
  public List<Trainer> findAll() {
    var trainerMap = trainerRepository.findAll();
    return trainerMap != null ? trainerMap : Collections.emptyList();
  }

  @Override
  @Transactional
  public Principal saveTrainer(TrainerRegistration trainerRegistration) {
    var username = UserUtils.createUsername(trainerRegistration.getFirstName(), trainerRegistration.getLastName(),
        userService.findAll());
    var password = UserUtils.generateRandomPassword();
    var hashedPassword = hashPassService.hashPass(password);
    Trainer trainer = new Trainer();
    TrainingType trainingType = trainingRepository.findTrainingTypeByName(trainerRegistration.getSpecialization());
    fillInTheTrainer(trainerRegistration, username, hashedPassword, trainer, trainingType);
    var savedTrainer = trainerRepository.save(trainer);
    return new Principal(savedTrainer.getUsername(), password);
  }

  @Override
  @Transactional
  public Trainer update(TrainerUpdateRequest request) {
    var trainer = findByUsername(request.username());
    Optional.ofNullable(request.firstName()).filter(firstName -> !firstName.isEmpty())
        .ifPresent(trainer::setFirstName);
    if (request.isActive() != trainer.isActive()) {
      trainer.setActive(request.isActive());
    }
    return trainerRepository.update(trainer).get();
  }

  @Override
  @Transactional
  public List<Trainer> findByIsActive() {
    List<Trainer> activeTrainers = trainerRepository.findByIsActive();
    return activeTrainers.isEmpty() ? Collections.emptyList() : activeTrainers;

  }

  @Override
  @Transactional
  public void toggleProfileActivation(long trainerId, boolean isActive) {
    var trainer = findById(trainerId);
    trainer.setActive(isActive);
    trainerRepository.toggleProfileActivation(trainer);

  }

  @Override
  @Transactional
  public List<TraineeDtoForTrainer> findTraineesForTrainer(long id) {
    var listTrainees = trainerRepository.findTraineesForTrainer(id);
    if (listTrainees.isEmpty()) {
      return Collections.emptyList();
    } else {
      return traineeConverter.toTraineeDtoForTrainer(listTrainees);
    }
  }

  private static void fillInTheTrainer(TrainerRegistration trainerRegistration, String username, String password,
                                       Trainer trainer, TrainingType trainingType) {
    trainer.setFirstName(trainerRegistration.getFirstName());
    trainer.setLastName(trainerRegistration.getLastName());
    trainer.setUsername(username);
    trainer.setPassword(password);
    trainer.setActive(true);
    trainer.setSpecialization(trainingType);
  }
}

