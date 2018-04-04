package com.epam.esm.service.impl;

import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.exception.ValidationException;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {
    @Autowired
    private GiftCertificateRepository certificateRepository;

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
        return certificateRepository.search(tag, name, description, sortBy);
    }

    @Override
    public GiftCertificate add(GiftCertificate certificate) {
        if (certificate == null) {
            throw new ValidationException("GiftCertificate you've provided is null!");
        }
        addNewTags(certificate);
        return certificateRepository.create(certificate);
    }

    @Override
    public GiftCertificate get(long id) {
            GiftCertificate certificate = certificateRepository.read(id);
            if (certificate != null) {
                List<Tag> certificateTags = tagRepository.getCertificateTags(certificate.getId());
                certificate.setTags(certificateTags);
            }
            return certificate;
    }

    @Override
    public void delete(long id) {
        certificateRepository.delete(id);
    }

    private void addNewTags(GiftCertificate certificate) {
        List<Tag> tags = certificate.getTags();
        if (!CollectionUtils.isEmpty(tags)) {
            List<Tag> newTags = tags.stream()
                    .filter(tag -> tagRepository.read(tag.getId()) == null).collect(Collectors.toList());

            if (!newTags.isEmpty()) {
                newTags.forEach(tag -> tagRepository.create(tag));
                newTags.forEach(tag -> tagRepository.createCertificateTag(certificate.getId(), tag.getId()));
            }
        }
    }
}
