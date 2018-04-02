package com.epam.esm.service.impl;

import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.GiftCertificateTagRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {
    @Autowired
    private GiftCertificateRepository certificateRepository;

    @Autowired
    private GiftCertificateTagRepository certificateTagRepository;

    @Autowired
    private TagRepository tagRepository;

    @Override
    public void update(GiftCertificate certificate) {
        addNewTags(certificate);
        certificateRepository.update(certificate);
    }

    @Override
    public List<GiftCertificate> search(Optional<Long> tag, Optional<String> name, Optional<String> description,
                                        Optional<String> sortBy) {
        List<Object> queryAndParams = constructSearchQuery(tag, name, description, sortBy);
        return certificateRepository.search((String) queryAndParams.remove(queryAndParams.size() - 1), queryAndParams.toArray());
    }

    @Override
    public GiftCertificate add(GiftCertificate certificate) {
        addNewTags(certificate);
        return certificateRepository.create(certificate);
    }

    @Override
    public GiftCertificate get(long id) {
        try {
            GiftCertificate certificate = certificateRepository.read(id);
            List<Tag> certificateTags = certificateTagRepository.getCertificateTags(certificate.getId())
                    .stream().map(tagId -> tagRepository.read(tagId))
                    .collect(Collectors.toList());
            certificate.setTags(certificateTags);
            return certificate;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void delete(long id) {
        certificateRepository.delete(id);
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

    private void addNewTags(GiftCertificate certificate) {
        List<Tag> tags = certificate.getTags();
        if (!Objects.isNull(tags) && !tags.isEmpty()) {
            List<Tag> newTags = tags.stream()
                    .filter(tag -> tagRepository.read(tag.getId()) == null).collect(Collectors.toList());

            if (!newTags.isEmpty()) {
                newTags.forEach(tag -> tagRepository.create(tag));
                newTags.forEach(tag -> certificateTagRepository.create(certificate.getId(), tag.getId()));
            }
        }
    }
}
