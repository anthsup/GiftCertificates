package com.epam.esm.service;

import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.impl.GiftCertificateRepositoryImpl;
import com.epam.esm.service.impl.GiftCertificateServiceImpl;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GiftCertificateServiceImplTest {
    private GiftCertificateService certificateService;
    private GiftCertificateRepository certificateRepository;
    private GiftCertificate certificate;

    @Before
    public void setUp() {
        certificateRepository = mock(GiftCertificateRepositoryImpl.class);
        certificateService = new GiftCertificateServiceImpl(certificateRepository);
        certificate = new GiftCertificate();
    }

    @Test
    public void update() {
        certificateService.update(certificate);
        verify(certificateRepository, times(1)).update(certificate);
    }

    @Test
    public void add() {
        certificateService.add(certificate);
        verify(certificateRepository, times(1)).create(certificate);
    }

    @Test
    public void get() {
        when(certificateRepository.read(certificate.getId())).thenReturn(certificate);
        GiftCertificate actualCertificate = certificateService.get(certificate.getId());

        verify(certificateRepository, times(1)).read(certificate.getId());
        assertEquals(certificate, actualCertificate);
    }

    @Test
    public void delete() {
        certificateService.delete(certificate.getId());
        verify(certificateRepository, times(1)).delete(certificate.getId());
    }
}