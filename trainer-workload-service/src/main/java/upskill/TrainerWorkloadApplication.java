package upskill;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.mongodb.core.MongoTemplate;
import upskill.entity.MonthData;
import upskill.entity.TrainingTrainerSummary;
import upskill.entity.YearData;

import javax.annotation.PostConstruct;
import java.time.Month;
import java.util.List;

@SpringBootApplication
@EnableEurekaClient
public class TrainerWorkloadApplication {
  @Autowired
  private MongoTemplate mongoTemplate;

  public static void main(String[] args) {
    SpringApplication.run(TrainerWorkloadApplication.class, args);
  }
//
//  @PostConstruct
//  public void insertTestDocument() {
//    TrainingTrainerSummary trainer = new TrainingTrainerSummary();
//    boolean status = true;
//    trainer.setFirstName("Nika");
//    trainer.setLastName("Doe");
//    trainer.setUsername("Nika.Doe");
//    trainer.setStatus(status);
//
//    MonthData monthData = new MonthData();
//    monthData.setMonth(Month.JANUARY);
//    monthData.setTrainingsSummaryDuration(120);
//
//    YearData year1 = new YearData();
//    year1.setYear(2023);
//    year1.setMonthsList(List.of(monthData));
//    YearData year2 = new YearData();
//    year2.setYear(2024);
//    year2.setMonthsList(List.of(monthData));
//
//
//    trainer.setYearsList(List.of(year1, year2));
//    mongoTemplate.insert(trainer, "TrainingsSummary");
 // }
}