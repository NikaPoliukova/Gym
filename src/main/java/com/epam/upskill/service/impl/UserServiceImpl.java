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

import static com.epam.upskill.util.UserUtils.createUsername;
import static com.epam.upskill.util.UserUtils.generateRandomPassword;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;

  @Override
  public User getUserById(long userId) {
    return userRepository.findById(userId);
  }

  @Override
  @Transactional(readOnly = true)
  public List<User> findAll() {
    return userRepository.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public User findByUsername(String username) {
    var user = userRepository.findByUsername(username);
    if (user == null) {
      throw new UserNotFoundException(username);
    }
    return user;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public void save(@Valid PrepareUserDto prepareUserDto) {
    userRepository.save(User.builder()
        .firstName(prepareUserDto.firstName())
        .lastName(prepareUserDto.lastName())
        .username(createUsername(prepareUserDto.firstName(), prepareUserDto.lastName(), findAll()))
        .password(generateRandomPassword()).build());
  }

  @Override
  @Transactional
  public void updatePassword(@Valid UserDto userDto) {
    log.info("Updating Trainee's password: ");
    var user = userRepository.getUserById(userDto.id());
    user.setPassword(userDto.password());
    updateUser(new UserDto(userDto.id(), userDto.password()));
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public void updateUser(@Valid UserDto userDto) {
    var user = userRepository.findById(userDto.id());
    user.setPassword(userDto.password());
    userRepository.update(user);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public void delete(long userId) {
    userRepository.delete(userId);
  }

  public void toggleProfileActivation(long userId) {
    var user = getUserById(userId);
    if (user != null) {
      boolean currentStatus = user.isActive();
      user.setActive(!currentStatus);
      userRepository.toggleProfileActivation(user);
    }
  }
}
