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
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

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
  public static final String TRANSACTION_ID = "transactionId";
  private final UserRepository userRepository;

  @Override
  @Transactional
  public User findById(long userId) {
    String transactionId = UUID.randomUUID().toString();
    MDC.put(TRANSACTION_ID, transactionId);
    return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(" witt userId = " + userId));

  }

  @Override
  @Transactional(readOnly = true)
  public List<User> findAll() {
    String transactionId = UUID.randomUUID().toString();
    MDC.put(TRANSACTION_ID, transactionId);
    var users = userRepository.findAll();
    return users != null ? users : Collections.emptyList();
  }

  @Override
  @Transactional(readOnly = true)
  public User findByUsername(String username) {
    String transactionId = UUID.randomUUID().toString();
    MDC.put(TRANSACTION_ID, transactionId);
    return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));

  }

  @Override
  @Transactional
  public void updatePassword(@Valid UserUpdatePass userUpdatePass) {
    String transactionId = UUID.randomUUID().toString();
    MDC.put(TRANSACTION_ID, transactionId);
    var user = findByUsernameAndPassword(userUpdatePass.username(), userUpdatePass.oldPassword());
    user.setPassword(userUpdatePass.newPassword());
    userRepository.update(user);
  }

  @Override
  @Transactional
  public User findByUsernameAndPassword(@NotBlank String username, @NotBlank String password) {
    String transactionId = UUID.randomUUID().toString();
    MDC.put(TRANSACTION_ID, transactionId);
    return userRepository.findByUsernameAndPassword(username, password)
        .orElseThrow(() -> new UserNotFoundException(username));
  }

  @Override
  @Transactional
  public void updateLogin(@Valid UserDto userDto) {
    String transactionId = UUID.randomUUID().toString();
    MDC.put(TRANSACTION_ID, transactionId);
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
  public void delete(@NotNull long userId) {
    String transactionId = UUID.randomUUID().toString();
    MDC.put(TRANSACTION_ID, transactionId);
    try {
      findById(userId);
      userRepository.delete(userId);
    } catch (OperationFailedException ex) {
      throw new OperationFailedException(" user with id=  " + userId, "delete user");
    }
  }
}