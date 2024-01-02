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

import java.time.Month;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainingSummaryService {
  public static final String USERNAME = "username";
  public static final String YEARS_LIST_YEAR = "yearsList.year";
  private final MongoTemplate mongoTemplate;
  private final TrainerTrainingRepository trainingRepository;

  public void deleteTraining(TrainerWorkloadRequestForDelete dto) {
    var trainerOptional = getTrainingTrainerSummary(dto.getTrainerUsername());
    var year = dto.getTrainingDate().getYear();
    var month = dto.getTrainingDate().getMonth();
    var date = getYearData(dto, year, month);
    var duration = dto.getDuration();

    trainerOptional.ifPresent(trainer -> {
      Optional<YearData> foundYear = trainer.getYearsList().stream()
          .filter(y -> y.getYear() == date.getYear())
          .findFirst();
      foundYear.ifPresent(yearData -> {
        Optional<MonthData> foundMonth = yearData.getMonthsList().stream()
            .filter(m -> m.getMonth() == month)
            .findFirst();
        foundMonth.ifPresent(monthData -> {
          int newDuration = monthData.getTrainingsSummaryDuration() - duration;
          monthData.setTrainingsSummaryDuration(newDuration);
          mongoTemplate.save(trainer);
        });
      });
    });
  }

  public void saveTraining(TrainerTrainingDtoForSave dto) {
    var username = dto.getTrainerUsername();
    var year = dto.getTrainingDate().getYear();
    var month = dto.getTrainingDate().getMonth();
    var duration = dto.getDuration();
    var yearData = getYearData(dto, year, month);
    var trainingTrainerSummary = convertToTrainingSummary(dto, yearData);
    var trainerOptional = getTrainingTrainerSummary(username);

    trainerOptional.ifPresentOrElse(
        trainer -> updateExistingTrainer(trainer, year, month, duration),
        () -> saveTraining(trainingTrainerSummary));
  }

  private void updateExistingTrainer(TrainingTrainerSummary trainer, int year, Month month, int duration) {
    var yearDataOptional = trainer.getYearsList()
        .stream()
        .filter(yearData -> yearData.getYear() == year)
        .findFirst();
    yearDataOptional.ifPresentOrElse(
        yearData -> updateExistingYear(trainer, yearData, month, duration),
        () -> createNewYear(trainer, year, month, duration)
    );
  }

  private void updateExistingYear(TrainingTrainerSummary trainer, YearData yearData, Month month, int duration) {
    var monthDataOptional = yearData.getMonthsList().stream()
        .filter(monthData -> monthData.getMonth().equals(month))
        .findFirst();

    monthDataOptional.ifPresentOrElse(
        monthData -> updateExistingMonth(trainer, yearData, monthData, duration),
        () -> createNewMonth(trainer, yearData, month, duration)
    );
  }

  private Optional<TrainingTrainerSummary> getTrainingTrainerSummary(String username) {
    var query = Query.query(Criteria.where(USERNAME).is(username));
    return Optional.ofNullable(mongoTemplate.findOne(query, TrainingTrainerSummary.class));
  }

  private void updateExistingMonth(TrainingTrainerSummary trainer, YearData yearData, MonthData monthData,
                                   int duration) {
    monthData.setTrainingsSummaryDuration(monthData.getTrainingsSummaryDuration() + duration);
    var query = Query.query(Criteria.where(USERNAME).is(trainer.getUsername())
        .and(YEARS_LIST_YEAR).is(yearData.getYear())
        .and("yearsList.monthsList.month").is(monthData.getMonth()));
    var update = new Update().set("yearsList.$.monthsList", trainer.getYearsList().get(0).getMonthsList());
    mongoTemplate.updateFirst(query, update, TrainingTrainerSummary.class);
  }

  private void createNewMonth(TrainingTrainerSummary trainer, YearData yearData, Month month, int duration) {
    var newMonth = MonthData.builder().month(month).trainingsSummaryDuration(duration).build();
    yearData.getMonthsList().add(newMonth);
    var query = Query.query(Criteria.where(USERNAME).is(trainer.getUsername())
        .and(YEARS_LIST_YEAR).is(yearData.getYear()));
    var update = new Update().set("yearsList.$.monthsList", yearData.getMonthsList());
    mongoTemplate.updateFirst(query, update, TrainingTrainerSummary.class);
  }

  private void createNewYear(TrainingTrainerSummary trainer, int year, Month month, int duration) {
    var newYear = YearData.builder().year(year).build();
    var newMonth = MonthData.builder().month(month).trainingsSummaryDuration(duration).build();
    newYear.getMonthsList().add(newMonth);
    trainer.getYearsList().add(newYear);
    var query = Query.query(Criteria.where(USERNAME).is(trainer.getUsername()));
    var update = new Update().addToSet("yearsList", newYear);
    mongoTemplate.updateFirst(query, update, TrainingTrainerSummary.class);
  }

  public void saveTraining(TrainingTrainerSummary dto) {
    trainingRepository.save(dto);
  }

  private static YearData getYearData(TrainerTrainingDtoForSave dto, int year, Month month) {
    return new YearData(year, List.of(new MonthData(month, dto.getDuration())));
  }

  private static YearData getYearData(TrainerWorkloadRequestForDelete dto, int year, Month month) {
    return new YearData(year, List.of(new MonthData(month, dto.getDuration())));
  }

  private static TrainingTrainerSummary convertToTrainingSummary(TrainerTrainingDtoForSave dto, YearData yearData) {
    return TrainingTrainerSummary.builder()
        .firstName(dto.getFirstName())
        .lastName(dto.getLastName())
        .username(dto.getTrainerUsername())
        .status(dto.isStatus())
        .yearsList(List.of(yearData))
        .build();
  }
}
