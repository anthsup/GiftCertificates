package com.epam.esm.service;

public interface CrdService<T> {
    T add(T t);
    T get(long id);
    void delete(long id);
}
