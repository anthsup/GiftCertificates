package com.epam.esm.repository.impl;

import com.epam.esm.domain.Tag;
import com.epam.esm.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class TagRepositoryImpl implements TagRepository {
    private static final String INSERT_TAG = "INSERT INTO tag (name) VALUES (:name)";
    private static final String SELECT_TAG_BY_ID = "SELECT id, name FROM tag WHERE id = :id";
    private static final String SELECT_TAG_BY_NAME = "SELECT id, name FROM tag WHERE name = :name";
    private static final String DELETE_TAG_BY_ID = "DELETE FROM tag WHERE id = :id";
    private static final String INSERT_CERTIFICATE_TAG = "INSERT INTO certificate_tag " +
            "(certificate_id, tag_id) VALUES (:certificateId, :tagId)";
    private static final String GET_TAGS_IDS_BY_CERTIFICATE_ID = "SELECT certificate_id, tag_id FROM certificate_tag " +
            "WHERE certificate_id = :certificateId";
    private static final String GET_TAGS_BY_IDS = "SELECT id, name FROM tag WHERE id IN (:ids)";
    private static final String SELECT_ALL = "SELECT id, name FROM tag";
    private static final String DELETE_ALL_EXCEPT_PROVIDED = "DELETE from certificate_tag " +
            "WHERE certificate_id = :certificateId AND tag_id NOT IN (:tagIds)";

    @Autowired
    private NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Override
    public Tag create(Tag tag) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcOperations
                .update(INSERT_TAG, new BeanPropertySqlParameterSource(tag), keyHolder, new String[]{"id"});
        tag.setId(keyHolder.getKey().longValue());

        return tag;
    }

    @Override
    public Tag get(long id) {
        return getBy(SELECT_TAG_BY_ID, new MapSqlParameterSource("id", id));
    }

    @Override
    public void delete(long id) {
        namedParameterJdbcOperations.update(DELETE_TAG_BY_ID, new MapSqlParameterSource("id", id));
    }

    @Override
    public void createCertificateTag(long certificateId, long tagId) {
        namedParameterJdbcOperations.update(INSERT_CERTIFICATE_TAG,
                new MapSqlParameterSource("certificateId", certificateId).addValue("tagId", tagId));
    }

    @Override
    public List<Tag> getCertificateTags(long certificateId) {
        List<Long> certificateTagIds = namedParameterJdbcOperations.query(GET_TAGS_IDS_BY_CERTIFICATE_ID,
                new MapSqlParameterSource("certificateId", certificateId),
                (resultSet, i) -> resultSet.getLong("tag_id"));

        if (CollectionUtils.isEmpty(certificateTagIds)) {
            return Collections.emptyList();
        }
        return namedParameterJdbcOperations
                .query(GET_TAGS_BY_IDS, new MapSqlParameterSource("ids", certificateTagIds),
                        BeanPropertyRowMapper.newInstance(Tag.class));
    }

    @Override
    public void retainCertificateTags(long certificateId, List<Long> tagIds) {
        SqlParameterSource parameterSource = new MapSqlParameterSource("certificateId", certificateId)
                .addValue("tagIds", tagIds);
        namedParameterJdbcOperations
                .update(DELETE_ALL_EXCEPT_PROVIDED, parameterSource);
    }

    @Override
    public List<Tag> createNewTags(long certificateId, List<Tag> newTags) {
        List<Tag> createdTags = newTags.stream().map(this::create).collect(Collectors.toList());
        createdTags.forEach(tag -> createCertificateTag(certificateId, tag.getId()));
        return createdTags;
    }

    @Override
    public List<Tag> getAll() {
        try {
            return namedParameterJdbcOperations.query(SELECT_ALL, BeanPropertyRowMapper.newInstance(Tag.class));
        } catch (DataAccessException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public Tag getByName(String tagName) {
        return getBy(SELECT_TAG_BY_NAME, new MapSqlParameterSource("name", tagName));
    }

    private Tag getBy(String query, MapSqlParameterSource parameterSource) {
        try {
            return namedParameterJdbcOperations
                    .queryForObject(query, parameterSource,
                            BeanPropertyRowMapper.newInstance(Tag.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
