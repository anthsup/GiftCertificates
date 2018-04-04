package com.epam.esm.repository.impl;

import com.epam.esm.domain.Tag;
import com.epam.esm.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Repository
public class TagRepositoryImpl implements TagRepository {
    private static final String INSERT_TAG = "INSERT INTO tag (name) VALUES (:name)";
    private static final String SELECT_TAG_BY_ID = "SELECT id, name FROM tag WHERE id = :id";
    private static final String DELETE_TAG_BY_ID = "DELETE FROM tag WHERE id = :id";
    private static final String INSERT_CERTIFICATE_TAG = "INSERT INTO certificate_tag " +
            "(certificate_id, tag_id) VALUES (:certificateId, :tagId)";
    private static final String GET_TAGS_IDS_BY_CERTIFICATE_ID = "SELECT certificate_id, tag_id FROM certificate_tag " +
            "WHERE certificate_id = :certificateId";
    private static final String GET_TAGS_BY_IDS = "SELECT id, name FROM tag WHERE id IN (:ids)";

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
    public Tag read(long id) {
        try {
            return namedParameterJdbcOperations
                    .queryForObject(SELECT_TAG_BY_ID, new MapSqlParameterSource("id", id), (resultSet, i) -> {
                Tag tag = new Tag();
                tag.setName(resultSet.getString("name"));
                tag.setId(resultSet.getLong("id"));
                return tag;
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
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
                .queryForList(GET_TAGS_BY_IDS, new MapSqlParameterSource("ids", certificateTagIds), Tag.class);
    }
}
