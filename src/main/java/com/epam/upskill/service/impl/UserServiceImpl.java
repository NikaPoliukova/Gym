package com.epam.upskill.service.impl;


import com.epam.upskill.dao.UserRepository;
import com.epam.upskill.dto.UserDto;
import com.epam.upskill.dto.UserUpdatePass;
import com.epam.upskill.entity.User;
import com.epam.upskill.exception.OperationFailedException;
import com.epam.upskill.exception.UserNotFoundException;
import com.epam.upskill.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service

public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;

  @Override
  @Transactional
  public User findById(long userId) {
    return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(" witt userId = " + userId));

  }

  @Override
  @Transactional(readOnly = true)
  public List<User> findAll() {
    var users = userRepository.findAll();
    return users != null ? users : Collections.emptyList();
  }

  @Override
  @Transactional(readOnly = true)
  public User findByUsername(String username) {
    return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));

  }

  @Override
  @Transactional
  public void updatePassword(UserUpdatePass userUpdatePass) {
    var user = findByUsernameAndPassword(userUpdatePass.username(), userUpdatePass.oldPassword());
    user.setPassword(userUpdatePass.newPassword());
    userRepository.update(user);
  }

  @Override
  @Transactional
  public User findByUsernameAndPassword(String username, String password) {
    return userRepository.findByUsernameAndPassword(username, password)
        .orElseThrow(() -> new UserNotFoundException(username));
  }

  @Override
  @Transactional
  public void updateLogin(UserDto userDto) {
    try {
      var user = findById(userDto.id());
      user.setUsername(userDto.username());
      userRepository.update(user);
    } catch (OperationFailedException ex) {
      throw new OperationFailedException(userDto.username(), "update login");
    }
  }

  @Override
  @Transactional
  public void delete(long userId) {
    try {
      findById(userId);
      userRepository.delete(userId);
    } catch (OperationFailedException ex) {
      throw new OperationFailedException(" user with id=  " + userId, "delete user");
    }
  }
}