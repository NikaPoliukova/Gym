package upskill.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class User {
  long id;
  String firstName;
  String lastName;
  String username;
  String password;
  boolean isActive;
}
