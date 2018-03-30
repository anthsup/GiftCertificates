package com.epam.esm.service;

import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.impl.GiftCertificateRepositoryImpl;
import com.epam.esm.service.impl.GiftCertificateServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GiftCertificateServiceImplTest {
    @InjectMocks
    private GiftCertificateService certificateService = new GiftCertificateServiceImpl();

    @Mock
    private GiftCertificateRepository certificateRepository;

    private GiftCertificate certificate;

    @Before
    public void setUp() {
        certificate = new GiftCertificate();
    }

    @Test
    public void update() {
        certificateService.update(certificate);
        verify(certificateRepository, times(1)).update(certificate);
    }

    @Test
    public void add() {
        when(certificateRepository.create(certificate)).thenReturn(certificate);
        GiftCertificate actualCertificate = certificateService.add(certificate);

        verify(certificateRepository, times(1)).create(certificate);
        assertEquals(certificate, actualCertificate);
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