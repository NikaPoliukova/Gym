package upskill.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import upskill.dao.TrainerTrainingRepository;
import upskill.dao.TrainerTrainingRepositoryCustom;
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
  private final TrainerTrainingRepositoryCustom trainingRepositoryImpl;

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
              training -> updateExistingTraining(new UpdateParamsDto(training, year, month, duration)),
              () -> saveTraining(trainingTrainerSummary)
          );
    } catch (Exception e) {
      log.error("Error while saving training.", e);
      throw new OperationFailedException(dto.getTrainerUsername(), "save training");
    }
  }

  public void deleteTraining(TrainerWorkloadRequestForDelete dto) {
    try {
      getTrainingTrainerSummary(dto.getTrainerUsername()).ifPresent(training -> {
        var yearData = getYearData(dto);
        var monthDataOptional = getMonthData(yearData, dto.getTrainingDate().getMonth().getValue());
        monthDataOptional.ifPresent(monthData -> {
          int newDuration = monthData.getTrainingsSummaryDuration() - dto.getDuration();
          if (newDuration < 0) {
            throw new UpdateDurationException();
          }
          if (newDuration == 0) {
            deleteMonthAndYearIfEmpty(new DtoForDelete(training, yearData, monthData));
          } else {
            updateDuration(new UpdateParamsDto(training, yearData.getYear(), monthData.getMonthValue(), newDuration));
          }
        });
      });
    } catch (Exception e) {
      log.error("Error while deleting training.", e);
      throw new OperationFailedException(dto.getTrainerUsername(), "delete training");
    }
  }


  public void updateDuration(UpdateParamsDto dto) {
    trainingRepositoryImpl.updateDuration(dto);
  }


  public Optional<TrainingTrainerSummary> findByUsername(String username) {
    return trainingRepository.findByUsername(username);
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
    var newDuration = dto.monthData.getTrainingsSummaryDuration() + dto.duration;
    updateDuration(new UpdateParamsDto(dto.training, dto.yearData.getYear(), dto.monthData.getMonthValue(),
        newDuration));
  }

  protected void deleteYear(TrainingTrainerSummary trainer, YearData yearData) {
    try {
      trainingRepositoryImpl.deleteYear(trainer.getId(), yearData.getYear());
    } catch (Exception e) {
      log.error("Error while deleting year.", e);
      throw new UpdateYearException();
    }
  }

  protected void deleteMonth(DtoForDelete dto) {
    try {
      trainingRepositoryImpl.deleteMonth(dto.training.getId(),
          dto.yearData.getYear(), dto.monthData.getMonthValue());
    } catch (Exception e) {
      log.error("Error while deleting month.", e);
      throw new UpdateMonthException();
    }
  }


  protected Optional<TrainingTrainerSummary> getTrainingTrainerSummary(String username) {
    return trainingRepository.findByUsername(username);
  }


  protected void createNewMonth(DtoForCreateNewMonth dto) {
    try {
      var newMonth = MonthData.builder().monthValue(dto.month).trainingsSummaryDuration(dto.duration).build();
      dto.yearData.getMonthsList().add(newMonth);
      trainingRepositoryImpl.createNewMonth(dto.training.getUsername(), dto.yearData);
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
      trainingRepositoryImpl.createYear(dto.training.getUsername(), newYear);
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

  private void deleteMonthAndYearIfEmpty(DtoForDelete dto) {
    deleteMonth(dto);
    if (dto.training.getYearsList().size() == 1) {
      deleteYear(dto.training, dto.yearData);
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

  public record UpdateDataDto(
      TrainingTrainerSummary training,
      YearData yearData,
      MonthData monthData,
      int duration
  ) {
  }

  public record UpdateParamsDto(
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

  public record DtoForCreateNewMonth(
      TrainingTrainerSummary training,
      YearData yearData,
      int month,
      int duration) {
  }
}
