package com.epam.esm.repository.impl;

import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.service.GiftCertificateTagService;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
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

    @Autowired
    private JdbcOperations jdbcOperations;

    @Autowired
    private TagService tagService;

    @Autowired
    private GiftCertificateTagService certificateTagService;

    @Override
    public void update(GiftCertificate certificate) {
        addNewTags(certificate);
        jdbcOperations.update(UPDATE_BY_ID, certificate.getName(), certificate.getDescription(),
                certificate.getPrice(), certificate.getCreationDate(), certificate.getLastModificationDate(),
                certificate.getDurationInDays(), certificate.getId());
    }

    @Override
    public GiftCertificate create(GiftCertificate certificate) {
        addNewTags(certificate);
        if (jdbcOperations.update(INSERT_CERTIFICATE, certificate.getId(), certificate.getName(),
                certificate.getDescription(), certificate.getPrice(), certificate.getCreationDate(),
                certificate.getLastModificationDate(), certificate.getDurationInDays()) > 0) {
            return certificate;
        }
        return null;
    }

    // TODO populate tags field?
    @Override
    public GiftCertificate read(long id) {
        try {
            return jdbcOperations.queryForObject(SELECT_BY_ID, (rs, i) -> new GiftCertificate.Builder()
                    .id(rs.getLong("id"))
                    .name(rs.getString("name"))
                    .description(rs.getString("description"))
                    .price(rs.getBigDecimal("price"))
                    .creationDate(rs.getObject("creation_date", LocalDate.class))
                    .lastModificationDate(rs.getObject("modification_date", LocalDate.class))
                    .durationInDays(rs.getLong("duration_days"))
                    .build(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void delete(long id) {
        jdbcOperations.update(DELETE_BY_ID, id);
    }

    private void addNewTags(GiftCertificate certificate) {
        List<Tag> tags = certificate.getTags();
        if (!Objects.isNull(tags) && !tags.isEmpty()) {
            List<Tag> newTags = tags.stream()
                    .filter(tag -> tagService.get(tag.getId()) == null).collect(Collectors.toList());

            if (!newTags.isEmpty()) {
                newTags.forEach(tag -> tagService.add(tag));
                newTags.forEach(tag -> certificateTagService.add(certificate.getId(), tag.getId()));
            }
        }
    }
}
