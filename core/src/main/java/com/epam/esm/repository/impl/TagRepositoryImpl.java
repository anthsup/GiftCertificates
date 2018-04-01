package com.epam.esm.repository.impl;

import com.epam.esm.domain.Tag;
import com.epam.esm.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;

@Repository
public class TagRepositoryImpl implements TagRepository {
    private static final String INSERT_TAG = "INSERT INTO tag (id, name) VALUES (?, ?)";
    private static final String SELECT_BY_ID = "SELECT * FROM tag WHERE id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM tag WHERE id = ?";

    @Autowired
    private JdbcOperations jdbcOperations;

    @Override
    public Tag create(Tag tag) {
        if (jdbcOperations.update(INSERT_TAG, tag.getId(), tag.getName()) > 0) {
            return tag;
        }
        return null;
    }

    @Override
    public Tag read(long id) {
        try {
            return jdbcOperations.queryForObject(SELECT_BY_ID, (resultSet, i) -> {
                Tag tag = new Tag();
                tag.setName(resultSet.getString("name"));
                tag.setId(resultSet.getLong("id"));
                return tag;
            }, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void delete(long id) {
        jdbcOperations.update(DELETE_BY_ID, id);
    }
}
