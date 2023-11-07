package com.epam.upskill.service.impl;


import com.epam.upskill.dao.UserRepository;
import com.epam.upskill.dto.UserDto;
import com.epam.upskill.dto.UserUpdatePass;
import com.epam.upskill.entity.User;
import com.epam.upskill.exception.UserNotFoundException;
import com.epam.upskill.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;

  @Override
  public User findById(long userId) {
    String transactionId = UUID.randomUUID().toString();
    MDC.put("transactionId", transactionId);

    log.info("Transaction ID: {} | Fetching User by ID: {}", transactionId, userId);
    try {
      return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("userId = " + userId));
    } finally {
      MDC.remove("transactionId");
    }
  }

  @Override
  @Transactional(readOnly = true)
  public List<User> findAll() {
    String transactionId = UUID.randomUUID().toString();
    MDC.put("transactionId", transactionId);

    log.info("Transaction ID: {} | Fetching all Users", transactionId);
    try {
      var users = userRepository.findAll();
      return users != null ? users : Collections.emptyList();
    } finally {
      MDC.remove("transactionId");
    }
  }

  @Override
  @Transactional(readOnly = true)
  public User findByUsername(@NotBlank String username) {
    String transactionId = UUID.randomUUID().toString();
    MDC.put("transactionId", transactionId);

    log.info("Transaction ID: {} | Fetching User by username: {}", transactionId, username);
    try {
      return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
    } finally {
      MDC.remove("transactionId");
    }
  }

  @Override
  @Transactional
  public void updatePassword(@Valid UserUpdatePass userUpdatePass) {
    String transactionId = UUID.randomUUID().toString();
    MDC.put("transactionId", transactionId);

    log.info("Transaction ID: {} | Updating password for User with username: {}", transactionId, userUpdatePass.username());
    try {
      var user = findByUsernameAndPassword(userUpdatePass.username(), userUpdatePass.oldPassword());
      user.setPassword(userUpdatePass.newPassword());
      userRepository.update(user);
    } finally {
      MDC.remove("transactionId");
    }
  }

  @Override
  public User findByUsernameAndPassword(@NotBlank String username, @NotBlank String password) {
    String transactionId = UUID.randomUUID().toString();
    MDC.put("transactionId", transactionId);

    log.info("Transaction ID: {} | Fetching User by username: {} and password", transactionId, username);
    try {
      return userRepository.findByUsernameAndPassword(username, password)
          .orElseThrow(() -> new UserNotFoundException("User not found"));
    } finally {
      MDC.remove("transactionId");
    }
  }

  @Override
  public void updateLogin(@Valid UserDto userDto) {
    String transactionId = UUID.randomUUID().toString();
    MDC.put("transactionId", transactionId);

    log.info("Transaction ID: {} | Updating login for User with ID: {}", transactionId, userDto.id());
    try {
      var user = findById(userDto.id());
      user.setUsername(userDto.username());
      userRepository.update(user);
    } catch (Exception ex) {
      log.warn("Transaction ID: {} | User not found with ID: {}", transactionId, userDto.id());
      throw new UserNotFoundException("User not found with ID: " + userDto.id());
    } finally {
      MDC.remove("transactionId");
    }
  }

  @Override
  @Transactional
  public void delete(@NotNull long userId) {
    String transactionId = UUID.randomUUID().toString();
    MDC.put("transactionId", transactionId);
    log.info("Transaction ID: {} | Deleting User with ID: {}", transactionId, userId);
    try {
      findById(userId);
      userRepository.delete(userId);
    } catch (Exception ex) {
      log.warn("Transaction ID: {} | User not found with ID: {}", transactionId, userId);
      throw new UserNotFoundException("User not found with ID: " + userId);
    } finally {
      MDC.remove("transactionId");
    }
  }
}