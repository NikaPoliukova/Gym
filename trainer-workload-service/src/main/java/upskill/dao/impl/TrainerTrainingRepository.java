//package upskill.dao.impl;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.data.mongodb.core.query.Update;
//import org.springframework.stereotype.Repository;
//import upskill.dao.TrainerTrainingRepository;
//import upskill.entity.MonthData;
//import upskill.entity.TrainingTrainerSummary;
//
//import java.util.List;
//import java.util.Optional;
//
//@Slf4j
//@Repository
//@RequiredArgsConstructor
//public class TrainerTrainingRepositoryImpl implements TrainerTrainingRepository {
//  private final MongoTemplate mongoTemplate;
//
//  @Override
//  public void updateMonthsList(String username, int year, List<MonthData> monthsList) {
//    Query query = new Query(Criteria.where("username").is(username).and("yearsList.year").is(year));
//    Update update = new Update().set("yearsList.$.monthsList", monthsList);
//    mongoTemplate.updateFirst(query, update, TrainingTrainerSummary.class);
//  }
//}
