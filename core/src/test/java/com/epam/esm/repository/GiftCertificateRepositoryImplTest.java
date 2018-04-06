package com.epam.esm.repository;

import com.epam.esm.config.DataConfig;
import com.epam.esm.domain.GiftCertificate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
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
        tag = Optional.empty();
        name = Optional.empty();
        description = Optional.empty();
        sortBy = Optional.of("creation_date");
    }

    @Test
    public void update() {
        GiftCertificate updatedCertificate = new GiftCertificate.Builder()
                .id(2L).name("updated").price(BigDecimal.ONE).build();
        certificateRepository.update(updatedCertificate);
        assertEquals("updated", certificateRepository.read(updatedCertificate.getId()).getName());
    }

    @Test
    public void createReturnsEntityWithIdWhenGivenValidEntity() {
        certificate = new GiftCertificate.Builder().name("certificate").price(BigDecimal.TEN).build();
        GiftCertificate actualCertificate = certificateRepository.create(certificate);
        assertTrue(actualCertificate.getId() != 0);
    }

    @Test(expected = DataAccessException.class)
    public void createThrowsExceptionWhenGivenInvalidEntity() {
        certificate = new GiftCertificate.Builder().name("certificate").build();
        certificateRepository.create(certificate);
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
    public void searchSortsCertificates() {
        List<GiftCertificate> found = certificateRepository.search(tag, name, description, sortBy);
        assertEquals(LocalDate.of(2018, 3, 30), found.get(0).getCreationDate());
    }

    @Test
    public void searchFindsByNamePart() {
        name = Optional.of("2");
        List<GiftCertificate> found = certificateRepository.search(tag, name, description, sortBy);
        assertTrue(found.stream().anyMatch(cert -> cert.getName().endsWith("2")));
    }

    @Test
    public void searchFindsByDescriptionPart() {
        description = Optional.of("cool");
        List<GiftCertificate> found = certificateRepository.search(tag, name, description, sortBy);
        assertTrue(found.stream().anyMatch(cert -> cert.getDescription().contains("cool")));
    }

    @Test
    public void searchDoesntFindInvalidCertificates() {
        tag = Optional.of(42L);
        List<GiftCertificate> found = certificateRepository.search(tag, name, description, sortBy);
        assertTrue(found.isEmpty());
    }
}