package com.epam.esm.repository.impl;

import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.GiftCertificateTagRepository;
import com.epam.esm.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {
    private static final String INSERT_CERTIFICATE = "INSERT INTO certificate " +
            "(id, name, description, price, creation_date, modification_date, duration_days) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_BY_ID = "UPDATE certificate SET name = ?, description = ?, price = ?, " +
            "creation_date = ?, modification_date = ?, duration_days = ? WHERE id = ?";
    private static final String SELECT_BY_ID = "SELECT * FROM certificate WHERE id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM certificate WHERE id = ?";

    private JdbcOperations jdbcOperations;
    private TagRepository tagRepository;
    private GiftCertificateTagRepository certificateTagRepository;

    @Autowired
    public GiftCertificateRepositoryImpl(JdbcOperations jdbcOperations, TagRepository tagRepository,
                                         GiftCertificateTagRepository certificateTagRepository) {
        this.jdbcOperations = jdbcOperations;
        this.tagRepository = tagRepository;
        this.certificateTagRepository = certificateTagRepository;
    }

    @Override
    public void update(GiftCertificate certificate) {
        addNewTags(certificate);
        jdbcOperations.update(UPDATE_BY_ID, certificate.getName(), certificate.getDescription(),
                certificate.getPrice(), certificate.getCreationDate(), certificate.getLastModificationDate(),
                certificate.getDurationInDays(), certificate.getId());
    }

    @Override
    public void create(GiftCertificate certificate) {
        addNewTags(certificate);
        jdbcOperations.update(INSERT_CERTIFICATE, certificate.getId(), certificate.getName(),
                certificate.getDescription(), certificate.getPrice(), certificate.getCreationDate(),
                certificate.getLastModificationDate(), certificate.getDurationInDays());
    }

    @Override
    public GiftCertificate read(long id) {
        return jdbcOperations.queryForObject(SELECT_BY_ID, (rs, i) -> {
            GiftCertificate certificate = new GiftCertificate();
            certificate.setId(rs.getLong("id"));
            certificate.setName(rs.getString("name"));
            certificate.setDescription(rs.getString("description"));
            certificate.setPrice(rs.getBigDecimal("price"));
            certificate.setCreationDate(rs.getObject("creation_date", LocalDate.class));
            certificate.setLastModificationDate(rs.getObject("modification_date", LocalDate.class));
            certificate.setDurationInDays(rs.getLong("duration_days"));
            return certificate;
        }, id);
    }

    @Override
    public void delete(long id) {
        jdbcOperations.update(DELETE_BY_ID, id);
    }

    private void addNewTags(GiftCertificate certificate) {
        List<Tag> newTags = certificate.getTags().stream()
                .filter(tag -> tagRepository.read(tag.getId()) == null).collect(Collectors.toList());
        if (!newTags.isEmpty()) {
            newTags.forEach(tag -> tagRepository.create(tag));
            newTags.forEach(tag -> certificateTagRepository.create(certificate.getId(), tag.getId()));
        }
    }
}
