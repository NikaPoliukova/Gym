package upskill.dao;

import java.util.List;
import java.util.Optional;

public interface AbstractRepository<T> {
  T save(T t);

  Optional<T> findById(long id);

  List<T> findAll();


}