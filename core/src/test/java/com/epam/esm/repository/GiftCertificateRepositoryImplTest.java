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
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DataConfig.class)
@ActiveProfiles("test")
public class GiftCertificateRepositoryImplTest {
    @Autowired
    private GiftCertificateRepository certificateRepository;

    private GiftCertificate certificate;
    private Optional<Long> tag;
    private Optional<String> name;
    private Optional<String> description;
    private Optional<String> sortBy;

    @Before
    public void setUp() {
        certificate = new GiftCertificate.Builder().name("certificate").id(42L)
                .price(BigDecimal.TEN).build();
        tag = Optional.of(1L);
        name = Optional.empty();
        description = Optional.empty();
        sortBy = Optional.of("name");
    }

    @Test
    public void update() {
        GiftCertificate updatedCertificate = new GiftCertificate.Builder()
                .id(3L).name("updated").price(BigDecimal.ONE).build();
        certificateRepository.update(updatedCertificate);
        assertEquals("updated", certificateRepository.read(updatedCertificate.getId()).getName());
    }

    @Test
    public void create() {
        GiftCertificate actualCertificate = certificateRepository.create(certificate);
        assertEquals(certificate.getId(), actualCertificate.getId());
    }

    @Test
    public void readReturnsGiftCertificateWhenGivenValidId() {
        assertNotNull(certificateRepository.read(3L));
    }

    @Test
    public void readReturnsNullWhenGivenInvalidId() {
        assertNull(certificateRepository.read(45L));
    }

    @Test
    public void delete() {
        certificateRepository.delete(1L);
        assertNull(certificateRepository.read(1L));
    }

    @Test
    public void searchFindsValidCertificates() {
        List<GiftCertificate> found = certificateRepository.search(tag, name, description, sortBy);
        assertFalse(found.isEmpty());
    }

    @Test
    public void searchDoesntFindInvalidCertificates() {
        tag = Optional.of(42L);
        List<GiftCertificate> found = certificateRepository.search(tag, name, description, sortBy);
        assertTrue(found.isEmpty());
    }
}