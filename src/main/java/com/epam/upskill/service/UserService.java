package com.epam.upskill.service;

import com.epam.upskill.dto.PrepareUserDto;
import com.epam.upskill.dto.UserDto;
import com.epam.upskill.entity.User;

import java.util.List;

public interface UserService {

  User getUserById(long userId);

  List<User> findAll();

  void save(PrepareUserDto prepareUserDto);

  void updateUser(UserDto userDto);

  User findByUsername(String username);

  void delete(long userId);


}
