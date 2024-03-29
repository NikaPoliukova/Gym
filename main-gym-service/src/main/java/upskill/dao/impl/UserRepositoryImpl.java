package upskill.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import upskill.dao.UserRepository;
import upskill.entity.User;
import upskill.exception.UserNotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class UserRepositoryImpl implements UserRepository {

  public static final String USERNAME = "username";
  public static final String PASSWORD = "password";
  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public User save(User user) {
    log.debug("Creating User: " + user);
    entityManager.persist(user);
    return user;
  }

  @Override
  public Optional<User> findById(long id) {
    log.debug("Finding User by ID: " + id);
    return Optional.ofNullable(entityManager.find(User.class, id));
  }

  @Override
  public List<User> findAll() {
    log.debug("Fetching all Trainings");
    return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
  }

  @Override
  public void update(User user) {
    log.debug("Updating User: " + user);
    entityManager.merge(user);
  }

  @Override
  public void delete(long userId) {
    log.debug("Deleting user by ID: " + userId);
    User user = findById(userId).get();
    entityManager.remove(user);
  }

  @Override
  public Optional<User> findByUsername(String username) {
    try {
      var query = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
      query.setParameter(USERNAME, username);
      return Optional.ofNullable(query.getSingleResult());
    } catch (Exception e) {
      throw new UserNotFoundException(username);
    }
  }

  @Override
  public void toggleProfileActivation(User user) {
    entityManager.merge(user);
  }

  @Override
  public Optional<User> findByUsernameAndPassword(String username, String oldPassword) {
    var query = entityManager
        .createQuery("SELECT u FROM User u WHERE u.username = :username AND u.password = : password", User.class);
    query.setParameter(USERNAME, username);
    query.setParameter(PASSWORD, oldPassword);
    try {
      return Optional.ofNullable(query.getSingleResult());
    } catch (NoResultException e) {
      throw new UserNotFoundException(username);
    }
  }
}
