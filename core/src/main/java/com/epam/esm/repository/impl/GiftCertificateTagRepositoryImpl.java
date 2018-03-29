package com.epam.esm.repository.impl;

import com.epam.esm.repository.GiftCertificateTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;

@Repository
public class GiftCertificateTagRepositoryImpl implements GiftCertificateTagRepository {
    private static final String INSERT_CERTIFICATE_TAG = "INSERT INTO certificate_tag " +
            "(certificate_id, tag_id) VALUES (?, ?)";

    private JdbcOperations jdbcOperations;

    @Autowired
    public GiftCertificateTagRepositoryImpl(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public void create(long certificateId, long tagId) {
        jdbcOperations.update(INSERT_CERTIFICATE_TAG, certificateId, tagId);
    }
}
