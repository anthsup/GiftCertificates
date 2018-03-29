package com.epam.esm.repository;

public interface CrdRepository<T> {
    void create(T t);
    T read(long id);
    void delete(long id);
}
