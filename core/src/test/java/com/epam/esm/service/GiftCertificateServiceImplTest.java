package com.epam.esm.service;

import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.exception.ValidationException;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.GiftCertificateTagRepository;
import com.epam.esm.service.impl.GiftCertificateServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GiftCertificateServiceImplTest {
    @InjectMocks
    private GiftCertificateService certificateService = new GiftCertificateServiceImpl();

    @Mock
    private GiftCertificateRepository certificateRepository;

    @Mock
    private GiftCertificateTagRepository certificateTagRepository;

    private GiftCertificate certificate;
    private String expectedQuery;
    private List<Object> testList;
    private Optional<Long> tag;
    private Optional<String> name;
    private Optional<String> description;
    private Optional<String> sortBy;

    @Before
    public void setUp() {
        certificate = new GiftCertificate.Builder().id(42L).name("certif").price(BigDecimal.TEN).build();
        expectedQuery = "SELECT * FROM certificate WHERE id IN (SELECT certificate_id FROM certificate_tag WHERE tag_id = ?)" +
                " AND id IN (SELECT * FROM searchlikeid(?, ?)) ORDER BY name;";
        testList = new LinkedList<>();
        tag = Optional.of(1L);
        name = Optional.of("cert");
        description = Optional.empty();
        sortBy = Optional.of("name");
    }

    @Test
    public void update() {
        certificateService.update(certificate);
        verify(certificateRepository, times(1)).update(certificate);
    }

    @Test
    public void addReturnsGiftCertificateWhenGivenValidArg() {
        when(certificateRepository.create(certificate)).thenReturn(certificate);
        GiftCertificate actualCertificate = certificateService.add(certificate);

        verify(certificateRepository, times(1)).create(certificate);
        assertEquals(certificate, actualCertificate);
    }

    @Test(expected = ValidationException.class)
    public void addThrowsExceptionWhenGivenNull() {
        certificateService.add(null);
    }

    @Test
    public void getReturnsGiftCertificateWhenGivenValidId() {
        when(certificateTagRepository.getCertificateTagsId(certificate.getId())).thenReturn(Collections.emptyList());
        when(certificateRepository.read(certificate.getId())).thenReturn(certificate);
        GiftCertificate actualCertificate = certificateService.get(certificate.getId());

        verify(certificateRepository, times(1)).read(certificate.getId());
        assertEquals(certificate, actualCertificate);
    }

    @Test
    public void getReturnsNullWhenGivenInvalidId() {
        when(certificateRepository.read(13)).thenReturn(null);
        GiftCertificate actualCertificate = certificateService.get(13);

        verify(certificateRepository, times(1)).read(13);
        assertNull(actualCertificate);
    }

    @Test
    public void delete() {
        certificateService.delete(certificate.getId());
        verify(certificateRepository, times(1)).delete(certificate.getId());
    }

    @Test
    public void search() {
        testList.add(1L);
        testList.add("name");
        testList.add("cert");
        when(certificateRepository.search(expectedQuery, testList.toArray())).thenReturn(Collections.EMPTY_LIST);
        List<GiftCertificate> certificates = certificateService.search(tag, name, description, sortBy);

        verify(certificateRepository, times(1)).search(expectedQuery, testList.toArray());
        assertEquals(Collections.EMPTY_LIST, certificates);
    }

    @Test
    public void searchWithDescription() {
        name = Optional.empty();
        description = Optional.of("cool");
        testList.add(1L);
        testList.add("description");
        testList.add("cool");
        expectedQuery = "SELECT * FROM certificate WHERE id IN (SELECT certificate_id FROM certificate_tag WHERE tag_id = ?)" +
                " AND id IN (SELECT * FROM searchlikeid(?, ?)) ORDER BY name;";
        when(certificateRepository.search(expectedQuery, testList.toArray())).thenReturn(Collections.EMPTY_LIST);
        List<GiftCertificate> certificates = certificateService.search(tag, name, description, sortBy);

        verify(certificateRepository, times(1)).search(expectedQuery, testList.toArray());
        assertEquals(Collections.EMPTY_LIST, certificates);
    }
}