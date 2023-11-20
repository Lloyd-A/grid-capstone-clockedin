package com.clockedIn.shiftservice.repositories;

import java.util.Optional;

public interface Repository<T, R> {

    T save(T entity);

    Optional<T> findById(R id);

    Iterable<T> findAll();

    void delete(R id);
}
