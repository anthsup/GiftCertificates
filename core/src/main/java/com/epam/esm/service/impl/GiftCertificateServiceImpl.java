package com.epam.esm.service.impl;

import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.exception.ValidationException;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class GiftCertificateServiceImpl implements GiftCertificateService {
    @Autowired
    private GiftCertificateRepository certificateRepository;

    @Autowired
    private TagRepository tagRepository;

    @Override
    public void update(GiftCertificate certificate) {
        parseCertificateTags(certificate, true);
        certificateRepository.update(certificate);
    }

    @Override
    public List<GiftCertificate> search(Optional<Long> tag, Optional<String> name, Optional<String> description,
                                        Optional<String> sortBy) {
        List<GiftCertificate> giftCertificates = certificateRepository.search(tag, name, description, sortBy);
        giftCertificates.forEach(this::setTags);
        return giftCertificates;
    }

    /**
     * {@inheritDoc}
     *
     * Method also adds new tags if they were found in provided entity
     */
    @Override
    public GiftCertificate create(GiftCertificate certificate) {
        if (certificate == null) {
            throw new ValidationException("GiftCertificate you've provided is null!");
        }
        certificate = certificateRepository.create(certificate);
        parseCertificateTags(certificate, false);

        return certificate;
    }

    /**
     * {@inheritDoc}
     *
     * Method also sets certificates' tags if they're found
     */
    @Override
    public GiftCertificate get(long id) {
        GiftCertificate certificate = certificateRepository.get(id);
        if (certificate != null) {
            setTags(certificate);
        }
        return certificate;
    }

    @Override
    public void delete(long id) {
        certificateRepository.delete(id);
    }

    private void parseCertificateTags(GiftCertificate certificate, boolean certificateExists) {
        List<Tag> tags = certificate.getTags();
        if (!CollectionUtils.isEmpty(tags)) {
            List<Tag> newTags = new ArrayList<>();
            List<Tag> existingTags = new ArrayList<>();
            tags.forEach(receivedTag -> {
                Tag tag = tagRepository.getByName(receivedTag.getName());
                if (tag == null) {
                    newTags.add(receivedTag);
                } else {
                    existingTags.add(tag);
                }
            });

            parseExistingTags(existingTags, certificate.getId(), certificateExists);
            existingTags.addAll(parseNewTags(newTags, certificate.getId()));
            certificate.setTags(existingTags);
        }
    }

    private void parseExistingTags(List<Tag> existingTags, long certificateId, boolean certificateExists) {
        if (!certificateExists && !existingTags.isEmpty()) {
            existingTags.forEach(tag -> tagRepository.createCertificateTag(certificateId, tag.getId()));
        } else if (certificateExists) {
            tagRepository.retainCertificateTags(certificateId,
                    existingTags.stream().map(tag -> tag.getId()).collect(Collectors.toList()));
        }
    }

    private List<Tag> parseNewTags(List<Tag> newTags, long certificateId) {
        if (!newTags.isEmpty()) {
            return tagRepository.createNewTags(certificateId, newTags);
        }
        return Collections.emptyList();
    }

    private void setTags(GiftCertificate certificate) {
        List<Tag> certificateTags = tagRepository.getCertificateTags(certificate.getId());
        certificate.setTags(certificateTags);
    }
}
