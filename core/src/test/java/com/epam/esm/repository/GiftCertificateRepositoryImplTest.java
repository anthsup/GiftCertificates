package com.epam.esm.repository;

import com.epam.esm.config.AppConfig;
import com.epam.esm.domain.GiftCertificate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
@ActiveProfiles("test")
public class GiftCertificateRepositoryImplTest {
    @Autowired
    private GiftCertificateRepository certificateRepository;

    @Mock
    private GiftCertificate certificate;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(certificate.getTags()).thenReturn(Collections.emptyList());
        when(certificate.getCreationDate()).thenReturn(LocalDate.now());
        when(certificate.getLastModificationDate()).thenReturn(LocalDate.now());
        when(certificate.getDescription()).thenReturn("description");
        when(certificate.getName()).thenReturn("name");
        when(certificate.getPrice()).thenReturn(BigDecimal.TEN);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void update() {

    }

    @Test
    public void create() {
        certificateRepository.create(certificate);
        assertNotNull(certificateRepository.read(certificate.getId()));
    }

    @Test
    public void read() {
    }

    @Test
    public void delete() {
    }
}