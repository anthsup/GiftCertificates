package com.epam.esm.service.impl;

import com.epam.esm.repository.GiftCertificateTagRepository;
import com.epam.esm.service.GiftCertificateTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GiftCertificateTagServiceImpl implements GiftCertificateTagService {
    @Autowired
    private GiftCertificateTagRepository certificateTagRepository;

    @Override
    public void add(long certificateId, long tagId) {
        certificateTagRepository.create(certificateId, tagId);
    }
}
