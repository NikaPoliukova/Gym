//package upskill.entity;
//
//import com.fasterxml.jackson.annotation.JsonBackReference;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//import java.time.LocalDate;
//
//@Entity
//@Getter
//@Table(name = "training")
//@AllArgsConstructor
//@NoArgsConstructor
//public class Training {
//  @Id
//  @GeneratedValue(strategy = GenerationType.IDENTITY)
//  private long id;
//
//  @Column(name = "training_name")
//  private String trainingName;
//
//  @Column(name = "training_date")
//  private LocalDate trainingDate;
//
//  @Column(name = "training_duration")
//  private int trainingDuration;
//
//  @JsonBackReference
//  @ManyToOne
//  @JoinColumn(name = "trainee_id")
//  private Trainee trainee;
//
//  @ManyToOne
//  @JoinColumn(name = "trainer_id")
//  private Trainer trainer;
//
//  @ManyToOne
//  @JoinColumn(name = "training_type_id")
//  private TrainingType trainingType;
//}
