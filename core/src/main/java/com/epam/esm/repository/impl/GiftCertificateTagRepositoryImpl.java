package com.epam.esm.repository.impl;

import com.epam.esm.repository.GiftCertificateTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GiftCertificateTagRepositoryImpl implements GiftCertificateTagRepository {
    private static final String INSERT_CERTIFICATE_TAG = "INSERT INTO certificate_tag " +
            "(certificate_id, tag_id) VALUES (?, ?)";
    private static final String GET_TAGS_IDS_BY_CERTIFICATE_ID = "SELECT * FROM  certificate_tag WHERE certificate_id = ?";

    @Autowired
    private JdbcOperations jdbcOperations;

    @Override
    public void create(long certificateId, long tagId) {
        jdbcOperations.update(INSERT_CERTIFICATE_TAG, certificateId, tagId);
    }

    @Override
    public List<Long> getCertificateTagsId(long certificateId) {
        try {
            return jdbcOperations.query(GET_TAGS_IDS_BY_CERTIFICATE_ID,
                    (resultSet, i) -> resultSet.getLong("tag_id"), certificateId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
