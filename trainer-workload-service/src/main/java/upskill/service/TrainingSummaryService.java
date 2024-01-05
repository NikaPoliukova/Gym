package upskill.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import upskill.dao.TrainerTrainingRepository;
import upskill.dto.TrainerTrainingDtoForSave;
import upskill.dto.TrainerWorkloadRequestForDelete;
import upskill.entity.MonthData;
import upskill.entity.TrainingTrainerSummary;
import upskill.entity.YearData;
import upskill.exception.OperationFailedException;
import upskill.exception.UpdateDurationException;
import upskill.exception.UpdateMonthException;
import upskill.exception.UpdateYearException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainingSummaryService {

  private final TrainerTrainingRepository trainingRepository;

  public void saveTraining(TrainerTrainingDtoForSave dto) {
    try {
      var username = dto.getTrainerUsername();
      var year = dto.getTrainingDate().getYear();
      var month = dto.getTrainingDate().getMonth().getValue();
      var duration = dto.getDuration();
      var yearData = getYearData(dto, year, month);
      var trainingTrainerSummary = convertToTrainingSummary(dto, List.of(yearData));

      getTrainingTrainerSummary(username)
          .ifPresentOrElse(
              trainer -> updateExistingTraining(trainer, year, month, duration),
              () -> saveTraining(trainingTrainerSummary)
          );
    } catch (Exception e) {
      log.error("Error while saving training.", e);
      throw new OperationFailedException(dto.getTrainerUsername(), "save training");
    }
  }

  public void deleteTraining(TrainerWorkloadRequestForDelete dto) {
    try {
      getTrainingTrainerSummary(dto.getTrainerUsername())
          .ifPresent(trainer -> {
            var yearData = getYearData(dto, dto.getTrainingDate().getYear(), dto.getTrainingDate().getMonth().getValue());
            yearData.getMonthsList()
                .stream()
                .filter(monthData -> monthData.getMonthValue() == dto.getTrainingDate().getMonth().getValue())
                .findFirst()
                .ifPresent(monthData -> {
                  var newDuration = monthData.getTrainingsSummaryDuration() - dto.getDuration();
                  if (newDuration < 0) {
                    throw new UpdateDurationException();
                  }

                  if (newDuration == 0) {
                    deleteMonth(trainer, yearData, monthData);
                    if (yearData.getMonthsList().isEmpty()) {
                      deleteYear(trainer, yearData);
                    }
                  } else {
                    monthData.setTrainingsSummaryDuration(newDuration);
                    trainingRepository.save(trainer);
                  }
                });
          });
    } catch (Exception e) {
      log.error("Error while deleting training.", e);
      throw new OperationFailedException(dto.getTrainerUsername(), "delete training");
    }
  }

  protected void updateExistingMonth(TrainingTrainerSummary trainer, YearData yearData, MonthData monthData, int duration) {
    try {
      monthData.setTrainingsSummaryDuration(monthData.getTrainingsSummaryDuration() + duration);
      var existingSummaryOptional = trainingRepository.findByUsernameAndYearAndMonth(
          trainer.getUsername(),
          yearData.getYear(),
          monthData.getMonthValue()
      );
      existingSummaryOptional.ifPresent(training -> {
        training.getYearsList().forEach(existingYear -> {
          if (existingYear.getYear() == yearData.getYear()) {
            existingYear.getMonthsList().forEach(existingMonth -> {
              if (existingMonth.getMonthValue() == monthData.getMonthValue()) {
                existingMonth.setTrainingsSummaryDuration(monthData.getTrainingsSummaryDuration());
              }
            });
          }
        });
        trainingRepository.save(training);
      });
    } catch (Exception e) {
      log.error("Error while updating existing month.", e);
      throw new UpdateMonthException();
    }
  }


  protected void deleteYear(TrainingTrainerSummary trainer, YearData yearData) {
    try {
      List<YearData> updatedYearList = new ArrayList<>(trainer.getYearsList());
      updatedYearList.remove(yearData);
      trainer.setYearsList(updatedYearList);
      trainingRepository.save(trainer);
    } catch (Exception e) {
      log.error("Error while deleting year.", e);
      throw new UpdateYearException();
    }
  }


  protected void deleteMonth(TrainingTrainerSummary trainer, YearData yearData, MonthData monthData) {
    try {
      List<MonthData> updatedMonthsList = new ArrayList<>(yearData.getMonthsList());
      updatedMonthsList.remove(monthData);
      yearData.setMonthsList(updatedMonthsList);
      trainer.setYearsList(List.of(yearData));
      trainingRepository.save(trainer);
    } catch (Exception e) {
      log.error("Error while deleting month.", e);
      throw new UpdateMonthException();
    }
  }

  protected void updateExistingTraining(TrainingTrainerSummary trainer, int year, int month, int duration) {
    var yearDataOptional = trainer.getYearsList()
        .stream()
        .filter(yearData -> yearData.getYear() == year)
        .findFirst();
    yearDataOptional.ifPresentOrElse(
        yearData -> updateExistingYear(trainer, yearData, month, duration),
        () -> createNewYear(trainer, year, month, duration)
    );
  }

  protected void updateExistingYear(TrainingTrainerSummary trainer, YearData yearData, int month, int duration) {
    try {
      var monthDataOptional = yearData.getMonthsList()
          .stream()
          .filter(monthData -> monthData.getMonthValue() == (month))
          .findFirst();
      monthDataOptional.ifPresentOrElse(
          monthData -> updateExistingMonth(trainer, yearData, monthData, duration),
          () -> createNewMonth(trainer, yearData, month, duration)
      );
    } catch (Exception e) {
      log.error("Error while updating existing year.", e);
      throw new UpdateYearException();
    }
  }


  protected Optional<TrainingTrainerSummary> getTrainingTrainerSummary(String username) {
    return trainingRepository.findByUsername(username);
  }


  protected void createNewMonth(TrainingTrainerSummary trainer, YearData yearData, int month, int duration) {
    try {
      var newMonth = MonthData.builder().monthValue(month).trainingsSummaryDuration(duration).build();
      yearData.getMonthsList().add(newMonth);
      trainingRepository.save(trainer);
    } catch (Exception e) {
      log.error("Error while creating new month.", e);
      throw new UpdateMonthException();
    }
  }

  protected void createNewYear(TrainingTrainerSummary training, int year, int month, int duration) {
    try {
      var newYear = YearData.builder().year(year).build();
      var newMonth = MonthData.builder()
          .monthValue(month)
          .trainingsSummaryDuration(duration)
          .build();
      if (newYear.getMonthsList() == null) {
        newYear.setMonthsList(new ArrayList<>());
      }
      newYear.getMonthsList().add(newMonth);
      training.getYearsList().add(newYear);
      trainingRepository.save(training);
    } catch (
        Exception e) {
      log.error("Error while creating new year.", e);
      throw new UpdateYearException();
    }
  }


  public TrainingTrainerSummary saveTraining(TrainingTrainerSummary dto) {
    try {
      return trainingRepository.save(dto);
    } catch (Exception e) {
      log.error("Error while saving training.", e);
      throw new OperationFailedException(dto.getUsername(), "Save training");
    }
  }

  private static YearData getYearData(TrainerTrainingDtoForSave dto, int year, int month) {
    return new YearData(year, List.of(new MonthData(month, dto.getDuration())));
  }

  private static YearData getYearData(TrainerWorkloadRequestForDelete dto, int year, int month) {
    return new YearData(year, List.of(new MonthData(month, dto.getDuration())));
  }

  private static TrainingTrainerSummary convertToTrainingSummary(TrainerTrainingDtoForSave dto,
                                                                 List<YearData> yearData) {
    return TrainingTrainerSummary.builder()
        .firstName(dto.getFirstName())
        .lastName(dto.getLastName())
        .username(dto.getTrainerUsername())
        .status(dto.isStatus())
        .yearsList(yearData)
        .build();
  }


  public Integer getTrainerWorkload(String trainerUsername, int year, int month) {
    var duration = trainingRepository.getTrainerWorkload(trainerUsername, year, month);
    return duration.orElse(0);
  }
}
