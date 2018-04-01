package com.epam.esm.repository;

import com.epam.esm.config.DataConfig;
import com.epam.esm.domain.GiftCertificate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DataConfig.class)
@ActiveProfiles("test")
public class GiftCertificateRepositoryImplTest {
    @Autowired
    private GiftCertificateRepository certificateRepository;

    private GiftCertificate certificate;

    @Before
    public void setUp() {
        certificate = new GiftCertificate.Builder().name("certificate").id(42L)
                .price(BigDecimal.TEN).build();
    }

    @Test
    public void update() {
        GiftCertificate updatedCertificate = certificateRepository.read(2L);
        updatedCertificate.setName("updated");
        certificateRepository.update(updatedCertificate);
        assertEquals("updated", certificateRepository.read(2L).getName());
    }

    @Test
    public void create() {
        certificateRepository.create(certificate);
        assertNotNull(certificateRepository.read(certificate.getId()));
    }

    @Test
    public void read() {
        assertNotNull(certificateRepository.read(3L));
    }

    @Test
    public void delete() {
        certificateRepository.delete(1L);
        assertNull(certificateRepository.read(1L));
    }
}