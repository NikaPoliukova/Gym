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

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;

  @Override
  public User findById(long userId) {
    return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("yserId = " + userId));
  }

  @Override
  @Transactional(readOnly = true)
  public List<User> findAll() {
    var users = userRepository.findAll();
    return users != null ? users : Collections.emptyList();
  }

  //TODO optional.map
  @Override
  @Transactional(readOnly = true)
  public User findByUsername(String username) {
    return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public void updateUserPassword(UserDto userDto) {
    var user = findById(userDto.id());
    user.setPassword(userDto.password());
    userRepository.update(user);
  }
  @Override
  public void updateLogin(UserDto userDto){
    var user = findById(userDto.id());
    user.setUsername(userDto.username());
    userRepository.update(user);
  }

  @Override
  @Transactional
  public void delete(long userId) {
    findById(userId);
    userRepository.delete(userId);
  }
}