package upskill.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import upskill.dto.*;
import upskill.entity.Trainee;
import upskill.entity.Trainer;
import upskill.entity.Training;
import upskill.entity.TrainingType;

import java.util.List;

@Mapper(uses = TrainingTypeConverter.class)
public interface TrainingConverter {
  @Mapping(target = "trainingType", ignore = true)
  @Mapping(target = "trainee", ignore = true)
  @Mapping(target = "trainer", ignore = true)
  Training toTraining(TrainingDto dto, @MappingTarget Training entity);

  @Mapping(target = "trainingType", ignore = true)
  Training toTraining(TrainingRequest trainingRequest, @MappingTarget Training entity);

  @Mapping(source = "trainingName", target = "trainingName")
  @Mapping(source = "trainingDate", target = "trainingDate", dateFormat = "yyyy-MM-dd")
  @Mapping(source = "trainingDuration", target = "trainingDuration")
  @Mapping(source = "trainer", target = "trainerName", qualifiedByName = "trainerName")
  @Mapping(target = "trainingType", expression = "java(trainingTypeToString(training.getTrainingType().getTrainingTypeName()))")
  TrainingTraineeResponse toTraineeTrainingResponse(Training training);

  @Mapping(source = "trainingName", target = "trainingName")
  @Mapping(source = "trainingDate", target = "trainingDate", dateFormat = "yyyy-MM-dd")
  @Mapping(source = "trainingDuration", target = "trainingDuration")
  @Mapping(source = "trainee", target = "traineeName", qualifiedByName = "traineeName")
  @Mapping(target = "trainingType", expression = "java(trainingTypeToString(training.getTrainingType().getTrainingTypeName()))")
  TrainingTrainerResponse toTrainerTrainingResponse(Training training);

  @Mapping(source = "trainer.username", target = "trainerUsername")
  @Mapping(source = "trainer.firstName", target = "firstName")
  @Mapping(source = "trainer.lastName", target = "lastName")
  @Mapping(source = "trainer", target = "status", qualifiedByName = "extractTrainerStatus")
  @Mapping(source = "trainingDate", target = "trainingDate")
  @Mapping(source = "trainingDuration", target = "duration")
  TrainerTrainingDtoForSave toTrainerTrainingDtoForSave(Training training);

  List<TrainingTrainerResponse> toTrainerTrainingResponse(List<Training> trainingsList);

  List<TrainingTraineeResponse> toTrainingResponse(List<Training> trainingsList);

  @Named("extractTrainerStatus")
  default boolean extractTrainerStatus(Trainer trainer) {
    return trainer.isActive();
  }

  default String mapTrainingType(TrainingType trainingType) {
    return trainingType.getTrainingTypeName().name();
  }


  @Named("traineeName")
  default String mapTraineeName(Trainee trainee) {
    return trainee.getUsername();
  }

  @Named("trainerName")
  default String mapTraineeName(Trainer trainer) {
    return trainer.getUsername();
  }

  @Named("trainingTypeToString")
  default String trainingTypeToString(TrainingTypeEnum trainingType) {
    return trainingType.toString();
  }

}
