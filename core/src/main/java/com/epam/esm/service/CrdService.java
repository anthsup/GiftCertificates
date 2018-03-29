package com.epam.esm.service;

public interface CrdService<T> {
    void add(T t);
    T get(long id);
    void delete(long id);
}
