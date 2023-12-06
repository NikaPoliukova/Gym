package upskill.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upskill.dao.UserRepository;
import upskill.dto.UserDto;
import upskill.dto.UserUpdatePass;
import upskill.entity.User;
import upskill.exception.OperationFailedException;
import upskill.exception.UserNotFoundException;
import upskill.service.HashPassService;
import upskill.service.UserService;

import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final HashPassService hashPassService;

  @Override
  @Transactional
  public User findById(long userId) {
    return userRepository.findById(userId).orElseThrow(
        () -> new UserNotFoundException(" witt userId = " + userId));

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
    var hashedPassword = hashPassService.hashPass(userUpdatePass.oldPassword());
    var user = findByUsernameAndPassword(userUpdatePass.username(), hashedPassword);
    user.setPassword(hashPassService.hashPass(userUpdatePass.newPassword()));
    userRepository.update(user);
  }

  @Override
  @Transactional
  public User findByUsernameAndPassword(String username, String password) {
    var hashedPassword = hashPassService.hashPass(password);
    return userRepository.findByUsernameAndPassword(username, hashedPassword)
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