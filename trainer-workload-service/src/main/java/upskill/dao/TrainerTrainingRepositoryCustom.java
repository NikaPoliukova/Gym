package upskill.dao;

import upskill.entity.YearData;
import upskill.service.TrainingSummaryService;

public interface TrainerTrainingRepositoryCustom {
  void createYear(String username, YearData newYear);

  void createNewMonth(String username, YearData yearData);

  void deleteMonth(String trainingId, int year, int month);

  void deleteYear(String trainingId, int year);

  void updateDuration(TrainingSummaryService.UpdateParamsDto dto);
}
