package com.epam.upskill.service;

import com.epam.upskill.dto.UserDto;
import com.epam.upskill.dto.UserUpdatePass;
import com.epam.upskill.entity.User;

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
