package upskill.service;

import lombok.AllArgsConstructor;
import lombok.Data;
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
              trainer -> updateExistingTraining(new UpdateParamsDto(trainer, year, month, duration)),
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
        var yearData = getYearData(dto);
        var monthDataOptional = getMonthData(yearData, dto.getTrainingDate().getMonth().getValue());
        monthDataOptional.ifPresent(monthData -> {
          int newDuration = monthData.getTrainingsSummaryDuration() - dto.getDuration();
          if (newDuration < 0) {
            throw new UpdateDurationException();
          }
          if (newDuration == 0) {
            deleteMonthAndYearIfEmpty(new DtoForDelete(trainer, yearData, monthData));
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


  private void deleteMonthAndYearIfEmpty(DtoForDelete dto) {
    deleteMonth(dto);
    if (dto.training.getYearsList().size() == 1) {
      deleteYear(dto.training, dto.yearData);
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

  protected void updateExistingMonth(UpdateDataDto dto) {
    try {
      dto.monthData.setTrainingsSummaryDuration(dto.monthData.getTrainingsSummaryDuration() + dto.duration);
      trainingRepository.findByUsernameAndYearAndMonth(
          dto.training.getUsername(),
          dto.yearData.getYear(),
          dto.monthData.getMonthValue()
      ).ifPresent(existingSummary -> {
        updateMonthDuration(new DtoForDelete(existingSummary, dto.yearData, dto.monthData));
        trainingRepository.save(existingSummary);
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

  protected void deleteMonth(DtoForDelete dto) {
    try {
      var updatedMonthsList = new ArrayList<>(dto.yearData().getMonthsList());
      updatedMonthsList.remove(dto.monthData);
      var yearDate = dto.yearData;
      dto.training.getYearsList().remove(dto.yearData);
      yearDate.setMonthsList(updatedMonthsList);
      dto.training.getYearsList().add(yearDate);
      trainingRepository.save(dto.training);
    } catch (Exception e) {
      log.error("Error while deleting month.", e);
      throw new UpdateMonthException();
    }
  }

  protected void updateExistingYear(DtoForCreateNewMonth dto) {
    try {
      var monthDataOptional = dto.yearData.getMonthsList()
          .stream()
          .filter(monthData -> monthData.getMonthValue() == (dto.month))
          .findFirst();
      monthDataOptional.ifPresentOrElse(
          monthData -> updateExistingMonth(new UpdateDataDto(dto.training, dto.yearData, monthData, dto.duration)),
          () -> createNewMonth(new DtoForCreateNewMonth(dto.training, dto.yearData, dto.month, dto.duration))
      );
    } catch (Exception e) {
      log.error("Error while updating existing year.", e);
      throw new UpdateYearException();
    }
  }

  protected Optional<TrainingTrainerSummary> getTrainingTrainerSummary(String username) {
    return trainingRepository.findByUsername(username);
  }

  protected void createNewMonth(DtoForCreateNewMonth dto) {
    try {
      var newMonth = MonthData.builder().monthValue(dto.month).trainingsSummaryDuration(dto.duration).build();
      dto.yearData.getMonthsList().add(newMonth);
      trainingRepository.save(dto.training);
    } catch (Exception e) {
      log.error("Error while creating new month.", e);
      throw new UpdateMonthException();
    }
  }

  protected void createNewYear(UpdateParamsDto dto) {
    try {
      var newYear = YearData.builder().year(dto.year).build();
      var newMonth = MonthData.builder()
          .monthValue(dto.month)
          .trainingsSummaryDuration(dto.duration)
          .build();
      if (newYear.getMonthsList() == null) {
        newYear.setMonthsList(new ArrayList<>());
      }
      newYear.getMonthsList().add(newMonth);
      dto.training.getYearsList().add(newYear);
      trainingRepository.save(dto.training);
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

  private Optional<MonthData> getMonthData(YearData yearData, int monthValue) {
    return yearData.getMonthsList().stream()
        .filter(monthData -> monthData.getMonthValue() == monthValue)
        .findFirst();
  }

  protected void updateExistingTraining(UpdateParamsDto dto) {
    var yearDataOptional = dto.training.getYearsList()
        .stream()
        .filter(yearData -> yearData.getYear() == dto.year)
        .findFirst();
    yearDataOptional.ifPresentOrElse(
        yearData -> updateExistingYear(new DtoForCreateNewMonth(dto.training, yearData, dto.month, dto.duration)),
        () -> createNewYear(new UpdateParamsDto(dto.training, dto.year, dto.month, dto.duration))
    );
  }

  private void updateMonthDuration(DtoForDelete dto) {
    dto.training.getYearsList().stream()
        .filter(existingYear -> existingYear.getYear() == dto.yearData.getYear())
        .findFirst().flatMap(existingYear -> existingYear.getMonthsList().stream()
            .filter(existingMonth -> existingMonth.getMonthValue() == dto.monthData.getMonthValue())
            .findFirst()).ifPresent(existingMonth ->
            existingMonth.setTrainingsSummaryDuration(dto.monthData.getTrainingsSummaryDuration()));
  }

  private static YearData getYearData(TrainerTrainingDtoForSave dto, int year, int month) {
    return new YearData(year, List.of(new MonthData(month, dto.getDuration())));
  }

  private static YearData getYearData(TrainerWorkloadRequestForDelete dto) {
    return new YearData(dto.getTrainingDate().getYear(),
        List.of(new MonthData(dto.getTrainingDate().getMonth().getValue(), dto.getDuration())));
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

  private record UpdateDataDto(
      TrainingTrainerSummary training,
      YearData yearData,
      MonthData monthData,
      int duration
  ) {
  }

  private record UpdateParamsDto(
      TrainingTrainerSummary training,
      int year,
      int month,
      int duration) {
  }

  private record DtoForDelete(
      TrainingTrainerSummary training,
      YearData yearData,
      MonthData monthData) {
  }

  @Data
  @AllArgsConstructor
  private class DtoForDeleteMonth {
    TrainingTrainerSummary training;
    List<MonthData> monthList;
    MonthData monthData;

  }

  private record DtoForCreateNewMonth(
      TrainingTrainerSummary training,
      YearData yearData,
      int month,
      int duration) {
  }


}
