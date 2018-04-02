package com.epam.esm.repository;

/**
 * Interface to perform CRD operations on entities
 *
 * @param <T>
 *            entity type on which to perform operations
 */
public interface CrdRepository<T> {
    T create(T t);
    T read(long id);
    void delete(long id);
}
