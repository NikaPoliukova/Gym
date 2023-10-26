package com.epam.upskill.dao;

import com.epam.upskill.entity.User;

import java.util.Optional;

public interface UserRepository extends AbstractRepository<User> {
  void update(User user);

  void delete(long userId);

  Optional<User> findByUsername(String username);

  void toggleProfileActivation(User user);

  Optional<User> findUserById(long id);
}

