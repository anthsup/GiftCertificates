package com.epam.esm.service;

/**
 * {@link com.epam.esm.repository.CrdRepository}
 */
public interface CrdService<T> {
    /**
     * {@link com.epam.esm.repository.CrdRepository#create(Object)}
     *
     * @throws com.epam.esm.exception.ValidationException when provided entity is null
     */
    T create(T t);

    /**
     * {@link com.epam.esm.repository.CrdRepository#get(long)}
     */
    T get(long id);

    /**
     * {@link com.epam.esm.repository.CrdRepository#delete(long)}
     */
    void delete(long id);
}
