package com.epam.upskill.service.impl;


import com.epam.upskill.dao.UserRepository;
import com.epam.upskill.dto.PrepareUserDto;
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
    return userRepository.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<User> findByUsername(String username) {
    var user = userRepository.findByUsername(username);
    if (user == null) {
      throw new UserNotFoundException(username);
    }
    return user;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public void save(@Valid PrepareUserDto prepareUserDto) {
    User user = new User(prepareUserDto.firstName(), prepareUserDto.lastName(), prepareUserDto.username(),
        prepareUserDto.password());
    userRepository.save(user);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public void updateUser(@Valid UserDto userDto) {
    var user = userRepository.findById(userDto.id());
    user.get().setPassword(userDto.password());
    userRepository.update(user.get());
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public void delete(long userId) {
    userRepository.delete(userId);
  }


}