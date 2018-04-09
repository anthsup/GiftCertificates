package com.epam.esm.repository;

/**
 * Interface that performs CRD operations on entities
 *
 * @param <T> entity type on which to perform operations
 */
public interface CrdRepository<T> {
    /**
     * Method that creates an entity passed by user in a database
     *
     * @param t entity which is being created
     * @return created entity with generated ID
     */
    T create(T t);

    /**
     * Method that finds entity in a database by its ID
     *
     * @param id ID of an entity being searched
     * @return entity if it exists or null otherwise
     */
    T get(long id);

    /**
     * Method that deletes an entity by its ID
     *
     * @param id ID of an entity being deleted
     */
    void delete(long id);
}
