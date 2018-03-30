package com.epam.esm.repository;

public interface CrdRepository<T> {
    T create(T t);
    T read(long id);
    void delete(long id);
}
