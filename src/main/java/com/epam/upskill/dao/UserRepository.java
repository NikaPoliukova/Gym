package com.epam.upskill.dao;

import com.epam.upskill.entity.User;

public interface UserRepository extends AbstractRepository<User> {
  void update(User user);

  void delete(long userId);

  User findByUsername(String username);

  void toggleProfileActivation(User user);
}
