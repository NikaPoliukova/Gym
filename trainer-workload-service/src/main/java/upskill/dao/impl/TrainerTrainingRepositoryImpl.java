package upskill.dao.impl;

import com.mongodb.BasicDBObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import upskill.dao.TrainerTrainingRepositoryCustom;
import upskill.entity.TrainingTrainerSummary;
import upskill.entity.YearData;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TrainerTrainingRepositoryImpl implements TrainerTrainingRepositoryCustom {
  public static final String YEARS_LIST = "yearsList";
  private final MongoTemplate mongoTemplate;
  public static final String USERNAME = "username";
  public static final String YEARS_LIST_YEAR = "yearsList.year";
  public static final String YEARS_AND_MONTH_LISTS = "yearsList.$.monthsList";

  @Override
  public void createYear(String username, YearData newYear) {
    var query = Query.query(Criteria.where(USERNAME).is(username));
    var update = new Update().addToSet(YEARS_LIST, newYear);
    mongoTemplate.updateFirst(query, update, TrainingTrainerSummary.class);
  }

  @Override
  public void createNewMonth(String username, YearData yearData) {
    var query = Query.query(Criteria.where(USERNAME).is(username).and(YEARS_LIST_YEAR).is(yearData.getYear()));
    var update = new Update().addToSet(YEARS_AND_MONTH_LISTS, new Document("$each", yearData.getMonthsList()));
    mongoTemplate.updateFirst(query, update, TrainingTrainerSummary.class);
  }

  @Override
  public void deleteMonth(String trainingId, int year, int month) {
    Query query = new Query(Criteria.where("_id").is(trainingId)
        .and("yearsList.year").is(year)
        .and("yearsList.monthsList.monthValue").is(month));
    Update update = new Update().pull("yearsList.$.monthsList", new BasicDBObject("monthValue", month));
    mongoTemplate.updateFirst(query, update, TrainingTrainerSummary.class);
  }

  @Override
  public void deleteYear(String trainingId, int year) {
    Query query = new Query(Criteria.where("_id").is(trainingId)
        .and("yearsList.year").is(year));
    Update update = new Update().pull("yearsList", new BasicDBObject("year", year));
    mongoTemplate.updateFirst(query, update, TrainingTrainerSummary.class);
  }
}
