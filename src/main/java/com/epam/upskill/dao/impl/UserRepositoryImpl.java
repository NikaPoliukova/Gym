package com.epam.upskill.dao.impl;

import com.epam.upskill.dao.UserRepository;
import com.epam.upskill.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Slf4j
@Repository
public class UserRepositoryImpl implements UserRepository {
  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public void save(User user) {
    log.debug("Creating User: " + user);
    entityManager.persist(user);
  }

  @Override
  public User findById(long id) {
    log.debug("Finding User by ID: " + id);
    return entityManager.find(User.class, id);
  }

  @Override
  public List<User> findAll() {
    log.debug("Fetching all Trainings");
    return entityManager.createQuery("SELECT e FROM users e", User.class).getResultList();
  }

  @Override
  public void update(User user) {
    log.debug("Updating User: " + user);
    entityManager.merge(user);
  }

  @Override
  public void delete(long userId) {
    log.debug("Deleting user by ID: " + userId);
    User entity = findById(userId);
    if (entity != null) {
      entityManager.remove(entity);
    }
  }

  @Override
  public User findByUsername(String username) {
    TypedQuery<User> query = entityManager
        .createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
    query.setParameter("username", username);
    return query.getSingleResult();
  }

  public void toggleProfileActivation(User user) {
    entityManager.merge(user);
  }
}
