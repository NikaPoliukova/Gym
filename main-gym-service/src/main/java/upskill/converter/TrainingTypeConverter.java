package upskill.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import upskill.dto.TrainingTypeResponse;
import upskill.entity.TrainingType;

import java.util.List;

@Mapper
public interface TrainingTypeConverter {

  List<TrainingTypeResponse> toTrainingTypeResponse(List<TrainingType> trainingTypes);

  @Mapping(source = "id", target = "trainingTypeId")
  @Mapping(source = "trainingTypeName", target = "trainingType")
  TrainingTypeResponse toTrainingTypeResponse(TrainingType trainingType);

}
