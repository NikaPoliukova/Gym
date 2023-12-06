package upskill.service;

import upskill.dto.UserDto;
import upskill.dto.UserUpdatePass;
import upskill.entity.User;

import java.util.List;

public interface UserService {

  User findById(long userId);

  List<User> findAll();

  void updatePassword(UserUpdatePass userUpdatePass);

  User findByUsernameAndPassword(String username, String password);

  void updateLogin(UserDto userDto);

  User findByUsername(String username);

  void delete(long userId);


}
