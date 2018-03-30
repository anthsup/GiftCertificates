package com.epam.esm.service.impl;

import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {
    @Autowired
    private GiftCertificateRepository certificateRepository;

    @Override
    public void update(GiftCertificate certificate) {
        certificateRepository.update(certificate);
    }

    @Override
    public GiftCertificate add(GiftCertificate certificate) {
        return certificateRepository.create(certificate);
    }

    @Override
    public GiftCertificate get(long id) {
        return certificateRepository.read(id);
    }

    @Override
    public void delete(long id) {
        certificateRepository.delete(id);
    }
}
