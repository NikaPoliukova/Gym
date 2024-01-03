package upskill.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import upskill.dao.TrainerTrainingRepository;
import upskill.dto.TrainerTrainingDtoForSave;
import upskill.dto.TrainerWorkloadRequestForDelete;
import upskill.entity.MonthData;
import upskill.entity.TrainingTrainerSummary;
import upskill.entity.YearData;
import upskill.exception.OperationFailedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainingSummaryService {
  public static final String USERNAME = "username";
  public static final String YEARS_LIST_YEAR = "yearsList.year";
  public static final String YEARS_AND_MONTH_LISTS = "yearsList.$.monthsList";
  private final MongoTemplate mongoTemplate;
  private final TrainerTrainingRepository trainingRepository;

  public void saveTraining(TrainerTrainingDtoForSave dto) {
    try {
      var username = dto.getTrainerUsername();
      var year = dto.getTrainingDate().getYear();
      var month = dto.getTrainingDate().getMonth().getValue();
      var duration = dto.getDuration();
      var yearData = getYearData(dto, year, month);
      var trainingTrainerSummary = convertToTrainingSummary(dto, List.of(yearData));
      var trainerOptional = getTrainingTrainerSummary(username);
      trainerOptional.ifPresentOrElse(
          trainer -> updateExistingTraining(trainer, year, month, duration),
          () -> saveTraining(trainingTrainerSummary));
    } catch (Exception e) {
      throw new OperationFailedException(dto.getTrainerUsername(), "save training");
    }
  }

  public void deleteTraining(TrainerWorkloadRequestForDelete dto) {
    try {
      var trainerOptional = getTrainingTrainerSummary(dto.getTrainerUsername());
      var year = dto.getTrainingDate().getYear();
      var month = dto.getTrainingDate().getMonth().getValue();
      var date = getYearData(dto, year, month);
      var duration = dto.getDuration();

      trainerOptional.ifPresent(trainer -> {
        var foundYear = trainer.getYearsList()
            .stream()
            .filter(y -> y.getYear() == date.getYear())
            .findFirst();
        foundYear.ifPresent(yearData -> {
          var foundMonth = yearData.getMonthsList()
              .stream()
              .filter(m -> m.getMonthValue() == month)
              .findFirst();
          foundMonth.ifPresent(monthData -> {
            var newDuration = monthData.getTrainingsSummaryDuration() - duration;
            monthData.setTrainingsSummaryDuration(newDuration);
            mongoTemplate.save(trainer);
          });
        });
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  private void updateExistingTraining(TrainingTrainerSummary trainer, int year, int month, int duration) {
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
      e.printStackTrace();
    }
  }

  protected void updateExistingMonth(TrainingTrainerSummary trainer, YearData yearData, MonthData monthData,
                                     int duration) {
    try {
      monthData.setTrainingsSummaryDuration(monthData.getTrainingsSummaryDuration() + duration);
      var query = Query.query(Criteria.where(USERNAME).is(trainer.getUsername())
          .and(YEARS_LIST_YEAR).is(yearData.getYear())
          .and("yearsList.monthsList.month").is(monthData.getMonthValue()));
      var update = new Update().set(YEARS_AND_MONTH_LISTS, trainer.getYearsList().get(0).getMonthsList());
      mongoTemplate.updateFirst(query, update, TrainingTrainerSummary.class);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  private Optional<TrainingTrainerSummary> getTrainingTrainerSummary(String username) {
    var query = Query.query(Criteria.where(USERNAME).is(username));
    return Optional.ofNullable(mongoTemplate.findOne(query, TrainingTrainerSummary.class));
  }

  private void createNewMonth(TrainingTrainerSummary trainer, YearData yearData, int month, int duration) {
    try {
      var newMonth = MonthData.builder().monthValue(month).trainingsSummaryDuration(duration).build();
      yearData.getMonthsList().add(newMonth);
      var query = Query.query(Criteria.where(USERNAME).is(trainer.getUsername())
          .and(YEARS_LIST_YEAR).is(yearData.getYear()));
      var update = new Update().set(YEARS_AND_MONTH_LISTS, yearData.getMonthsList());
      mongoTemplate.updateFirst(query, update, TrainingTrainerSummary.class);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  protected void createNewYear(TrainingTrainerSummary trainer, int year, int month, int duration) {
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
      trainer.getYearsList().add(newYear);
      var query = Query.query(Criteria.where(USERNAME).is(trainer.getUsername()));
      var update = new Update().addToSet("yearsList", newYear);
      mongoTemplate.updateFirst(query, update, TrainingTrainerSummary.class);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public TrainingTrainerSummary saveTraining(TrainingTrainerSummary dto) {
    try {
      return trainingRepository.save(dto);
    } catch (Exception e) {
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
}
