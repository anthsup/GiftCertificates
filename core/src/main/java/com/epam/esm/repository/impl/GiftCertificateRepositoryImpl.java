package com.epam.esm.repository.impl;

import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.repository.GiftCertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

@Repository
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {
    private static final String INSERT_CERTIFICATE = "INSERT INTO certificate " +
            "(name, description, price, creation_date, modification_date, duration_days) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_BY_ID = "UPDATE certificate SET name = ?, description = ?, price = ?, " +
            "creation_date = ?, modification_date = ?, duration_days = ? WHERE id = ?";
    private static final String SELECT_BY_ID = "SELECT * FROM certificate WHERE id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM certificate WHERE id = ?";

    @Autowired
    private JdbcOperations jdbcOperations;

    @Override
    public void update(GiftCertificate certificate) {
        jdbcOperations.update(UPDATE_BY_ID, certificate.getName(), certificate.getDescription(),
                certificate.getPrice(), certificate.getCreationDate(), certificate.getLastModificationDate(),
                certificate.getDurationInDays(), certificate.getId());
    }

    @Override
    public List<GiftCertificate> search(String query, Object... params) {
        return jdbcOperations.query(query, params, this::mapRow);
    }

    @Override
    public GiftCertificate create(GiftCertificate certificate) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(INSERT_CERTIFICATE, new String[]{"id"});
            stmt.setString(1, certificate.getName());
            stmt.setString(2, certificate.getDescription());
            stmt.setBigDecimal(3, certificate.getPrice());
            stmt.setDate(4, Date.valueOf(certificate.getCreationDate()));
            stmt.setDate(5, Date.valueOf(certificate.getLastModificationDate()));
            stmt.setLong(6, certificate.getDurationInDays());
            return stmt;
        }, keyHolder);
        certificate.setId(keyHolder.getKey().longValue());
        return certificate;
    }

    @Override
    public GiftCertificate read(long id) {
        return jdbcOperations.queryForObject(SELECT_BY_ID, this::mapRow, id);
    }

    @Override
    public void delete(long id) {
        jdbcOperations.update(DELETE_BY_ID, id);
    }

    private GiftCertificate mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new GiftCertificate.Builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .price(rs.getBigDecimal("price"))
                .creationDate(rs.getObject("creation_date", LocalDate.class))
                .lastModificationDate(rs.getObject("modification_date", LocalDate.class))
                .durationInDays(rs.getLong("duration_days"))
                .build();
    }
}
