package com.epam.upskill.service;

import com.epam.upskill.dto.PrepareUserDto;
import com.epam.upskill.dto.UserDto;
import com.epam.upskill.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

  Optional<User> findById(long userId);

  List<User> findAll();

  void save(PrepareUserDto prepareUserDto);

  void updateUser(UserDto userDto);

  Optional<User> findByUsername(String username);

  void delete(long userId);


}
