package com.epam.upskill.service;

import com.epam.upskill.dto.UserDto;
import com.epam.upskill.entity.User;

import java.util.List;

public interface UserService {

  User findById(long userId);

  List<User> findAll();

  void updateUserPassword(UserDto userDto);

  void updateLogin(UserDto userDto);

  User findByUsername(String username);

  void delete(long userId);


}
