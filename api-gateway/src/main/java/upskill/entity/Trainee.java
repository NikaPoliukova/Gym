package upskill.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder(builderMethodName = "traineeBuilder")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@PrimaryKeyJoinColumn(name = "trainee_id")
@Entity
@Table(name = "trainee")
public class Trainee extends User {

  @Column(name = "date_of_birth")
  private LocalDate dateOfBirth;

  private String address;

  @JsonManagedReference
  @OneToMany(mappedBy = "trainee", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
  private List<Training> trainings = new ArrayList<>();

  @Override
  public String toString() {
    return "Trainee(dateOfBirth=" + this.getDateOfBirth() + ", address=" + this.getAddress()
        + ", trainings=" + this.getTrainings() + ")";
  }


}
