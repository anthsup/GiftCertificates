package com.epam.esm.repository.impl;

import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.repository.GiftCertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Repository
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {
    private static final String INSERT_CERTIFICATE = "INSERT INTO certificate " +
            "(name, description, price, creation_date, modification_date, duration_days) " +
            "VALUES (?, ?, ?, ?, ?, ?)";
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
    public List<GiftCertificate> search(Optional<Long> tag, Optional<String> name, Optional<String> description,
                                        Optional<String> sortBy) {
        List<Object> params = constructSearchQuery(tag, name, description, sortBy);

        return jdbcOperations.query((String) params.remove(params.size() - 1), params.toArray(), (rs, rowNum) -> mapRow(rs));
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
        try {
            return jdbcOperations.queryForObject(SELECT_BY_ID, (rs, rowNum) -> mapRow(rs), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void delete(long id) {
        jdbcOperations.update(DELETE_BY_ID, id);
    }

    private GiftCertificate mapRow(ResultSet rs) throws SQLException {
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

    private List<Object> constructSearchQuery(Optional<Long> tag, Optional<String> name, Optional<String> description,
                                              Optional<String> sortBy) {
        StringBuilder query = new StringBuilder("SELECT * FROM certificate WHERE ");
        String likeQuery = "id IN (SELECT * FROM searchlikeid(?, ?))";
        List<Object> queryAndParams = new LinkedList<>();

        if (tag.isPresent()) {
            query.append("id IN (SELECT certificate_id FROM certificate_tag WHERE tag_id = ?)");
            queryAndParams.add(tag.get());
            if (name.isPresent() || description.isPresent()) {
                query.append(" AND ");
            }
        }

        if (name.isPresent()) {
            query.append(likeQuery);
            queryAndParams.add("name");
            queryAndParams.add(name.get());
        } else if (description.isPresent()) {
            query.append(likeQuery);
            queryAndParams.add("description");
            queryAndParams.add(description.get());
        }

        if (!tag.isPresent() && !name.isPresent() && !description.isPresent()) {
            query.append("TRUE");
        }

        if (sortBy.isPresent()) {
            if (sortBy.get().startsWith("-")) {
                query.append(String.format(" ORDER BY %s", sortBy.get().substring(1)));
                query.append(" DESC");
            } else {
                query.append(String.format(" ORDER BY %s", sortBy.get()));
            }
        }
        queryAndParams.add(query.append(";").toString());
        return queryAndParams;
    }
}
