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
      getTrainingTrainerSummary(dto.getTrainerUsername()).ifPresent(trainer -> {
        var yearData = getYearData(dto, dto.getTrainingDate().getYear(), dto.getTrainingDate().getMonth().getValue());
        var monthDataOptional = getMonthData(yearData, dto.getTrainingDate().getMonth().getValue());

        monthDataOptional.ifPresent(monthData -> {
          int newDuration = monthData.getTrainingsSummaryDuration() - dto.getDuration();
          if (newDuration < 0) {
            throw new UpdateDurationException();
          }

          if (newDuration == 0) {
            deleteMonthAndYearIfEmpty(trainer, yearData, monthData);
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

  private Optional<MonthData> getMonthData(YearData yearData, int monthValue) {
    return yearData.getMonthsList().stream()
        .filter(monthData -> monthData.getMonthValue() == monthValue)
        .findFirst();
  }

  private void deleteMonthAndYearIfEmpty(TrainingTrainerSummary trainer, YearData yearData, MonthData monthData) {
    deleteMonth(trainer, yearData, monthData);
    if (yearData.getMonthsList().isEmpty()) {
      deleteYear(trainer, yearData);
    }
  }

  public Integer getTrainerWorkload(String trainerUsername, int year, int month) {
    var trainingSummary = trainingRepository.findByUsernameAndYearAndMonth(trainerUsername, year, month);
    return trainingSummary.map(training ->
        training.getYearsList().stream()
            .filter(yearData -> yearData.getYear() == year)
            .findFirst()
            .flatMap(yearData -> yearData.getMonthsList().stream()
                .filter(monthData -> monthData.getMonthValue() == month)
                .findFirst()
                .map(MonthData::getTrainingsSummaryDuration)
            )
            .orElse(0)
    ).orElse(0);
  }

  protected void updateExistingMonth(TrainingTrainerSummary trainer, YearData yearData, MonthData monthData,
                                     int duration) {
    try {
      monthData.setTrainingsSummaryDuration(monthData.getTrainingsSummaryDuration() + duration);
      trainingRepository.findByUsernameAndYearAndMonth(
          trainer.getUsername(),
          yearData.getYear(),
          monthData.getMonthValue()
      ).ifPresent(existingSummary -> {
        updateMonthDuration(existingSummary, yearData, monthData);
        trainingRepository.save(existingSummary);
      });
    } catch (Exception e) {
      log.error("Error while updating existing month.", e);
      throw new UpdateMonthException();
    }
  }

  private void updateMonthDuration(TrainingTrainerSummary existingSummary, YearData yearData, MonthData monthData) {
    existingSummary.getYearsList().stream()
        .filter(existingYear -> existingYear.getYear() == yearData.getYear())
        .findFirst().flatMap(existingYear -> existingYear.getMonthsList().stream()
            .filter(existingMonth -> existingMonth.getMonthValue() == monthData.getMonthValue())
            .findFirst()).ifPresent(existingMonth ->
            existingMonth.setTrainingsSummaryDuration(monthData.getTrainingsSummaryDuration()));
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
      var updatedMonthsList = new ArrayList<>(yearData.getMonthsList());
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

  private static TrainingTrainerSummary convertToTrainingSummary(TrainerTrainingDtoForSave dto, List<YearData> yearData) {
    return TrainingTrainerSummary.builder()
        .firstName(dto.getFirstName())
        .lastName(dto.getLastName())
        .username(dto.getTrainerUsername())
        .status(dto.isStatus())
        .yearsList(yearData)
        .build();
  }
}
