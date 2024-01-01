package upskill.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import upskill.dto.TrainerWorkloadRequestForDelete;
import upskill.dto.TrainingRequestDto;

@Mapper
public interface TrainerWorkloadRequestForDeleteConverter {
  @Mapping(source = "trainerUsername", target = "trainerUsername")
  @Mapping(source = "trainingDate", target = "trainingDate")
  @Mapping(source = "duration", target = "duration")
  TrainerWorkloadRequestForDelete toTrainerWorkloadRequestForDelete(TrainingRequestDto trainingDto);
}
