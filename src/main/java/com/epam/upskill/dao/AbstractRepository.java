package com.epam.upskill.dao;

import java.util.Map;

public interface AbstractRepository<T> {
  void create(T t);

  T findById(long id);

  Map<Long, T> findAll();
}
