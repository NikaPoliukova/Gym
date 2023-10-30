package com.epam.upskill.service.impl;


import com.epam.upskill.dao.UserRepository;
import com.epam.upskill.dto.UserDto;
import com.epam.upskill.entity.User;
import com.epam.upskill.exception.UserNotFoundException;
import com.epam.upskill.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;

  @Override
  public Optional<User> findById(long userId) {
    return userRepository.findById(userId);
  }

  @Override
  @Transactional(readOnly = true)
  public List<User> findAll() {
    List<User> users = userRepository.findAll();
    if (users != null) {
      return users;
    } else {
      return Collections.emptyList();
    }
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<User> findByUsername(String username) {
    var user = userRepository.findByUsername(username);
    if (user.isEmpty()) {
      throw new UserNotFoundException(username);
    }
    return user;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public void updateUserPassword(@Valid UserDto userDto) {
    var user = userRepository.findById(userDto.id());
    user.get().setPassword(userDto.password());
    userRepository.update(user.get());
  }

  @Override
  @Transactional
  public void delete(long userId) {
    Optional<User> user = findById(userId);
    if (user.isEmpty()) {
      throw new UserNotFoundException("userId =" + userId);
    } else {
      userRepository.delete(userId);
    }
  }
}