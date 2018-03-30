package com.epam.esm.service;

import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.repository.GiftCertificateTagRepository;
import com.epam.esm.service.GiftCertificateTagService;
import com.epam.esm.service.impl.GiftCertificateTagServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GiftCertificateTagServiceImplTest {
    @InjectMocks
    private GiftCertificateTagService certificateTagService = new GiftCertificateTagServiceImpl();

    @Mock
    private GiftCertificateTagRepository certificateTagRepository;

    private GiftCertificate certificate;
    private Tag tag;

    @Before
    public void setUp() {
        certificate = new GiftCertificate();
        tag = new Tag();
    }

    @Test
    public void add() {
        certificateTagService.add(certificate.getId(), tag.getId());
        verify(certificateTagRepository, times(1)).create(certificate.getId(), tag.getId());
    }
}