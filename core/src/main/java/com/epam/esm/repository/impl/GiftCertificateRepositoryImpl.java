package com.epam.esm.repository.impl;

import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.repository.GiftCertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {
    private static final String INSERT_CERTIFICATE = "INSERT INTO certificate " +
            "(name, description, price, creation_date, modification_date, duration_days) " +
            "VALUES (:name, :description, :price, :creationDate, :lastModificationDate, :durationInDays)";
    private static final String UPDATE_BY_ID = "UPDATE certificate " +
            "SET name = :name, description = :description, price = :price, " +
            "creation_date = :creationDate, modification_date = :lastModificationDate, duration_days = :durationInDays " +
            "WHERE id = :id";
    private static final String SELECT_BY_ID = "SELECT " +
            "id, name, description, price, creation_date, modification_date, duration_days " +
            "FROM certificate WHERE id = :id";
    private static final String DELETE_BY_ID = "DELETE FROM certificate WHERE id = :id";

    @Autowired
    private NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Override
    public void update(GiftCertificate certificate) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(certificate);
        namedParameterJdbcOperations.update(UPDATE_BY_ID, parameterSource);
    }

    @Override
    public List<GiftCertificate> search(Optional<Long> tag, Optional<String> name, Optional<String> description,
                                        Optional<String> sortBy) {
        Map<String, Object> params = constructSearchQuery(tag, name, description, sortBy);

        return namedParameterJdbcOperations.query((String) params.remove("query"), params, (rs, rowNum) -> mapRow(rs));
    }

    @Override
    public GiftCertificate create(GiftCertificate certificate) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcOperations
                .update(INSERT_CERTIFICATE, new BeanPropertySqlParameterSource(certificate), keyHolder, new String[]{"id"});
        certificate.setId(keyHolder.getKey().longValue());
        return certificate;
    }

    @Override
    public GiftCertificate get(long id) {
        try {
            return namedParameterJdbcOperations.queryForObject(SELECT_BY_ID,
                    new MapSqlParameterSource("id", id), (rs, rowNum) -> mapRow(rs));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void delete(long id) {
        namedParameterJdbcOperations.update(DELETE_BY_ID, new MapSqlParameterSource("id", id));
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

    private Map<String, Object> constructSearchQuery(Optional<Long> tag, Optional<String> name, Optional<String> description,
                                                     Optional<String> sortBy) {
        StringBuilder query = new StringBuilder("SELECT * FROM certificate WHERE ");
        String likeQuery = "id IN (SELECT * FROM searchLike(:column, :like))";
        Map<String, Object> namedParameters = new HashMap<>();

        if (tag.isPresent()) {
            query.append("id IN (SELECT certificate_id FROM certificate_tag WHERE tag_id = :tagId)");
            namedParameters.put("tagId", tag.get());
            if (name.isPresent() || description.isPresent()) {
                query.append(" AND ");
            }
        }

        if (name.isPresent()) {
            query.append(likeQuery);
            namedParameters.put("column", "name");
            namedParameters.put("like", name.get());
        } else if (description.isPresent()) {
            query.append(likeQuery);
            namedParameters.put("column", "description");
            namedParameters.put("like", description.get());
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
        namedParameters.put("query", query.append(";").toString());
        return namedParameters;
    }
}
