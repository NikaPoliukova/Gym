package com.epam.upskill.dao;

import java.util.List;

public interface AbstractRepository<T> {
  void save(T t);

  T findById(long id);

  List<T> findAll();
}