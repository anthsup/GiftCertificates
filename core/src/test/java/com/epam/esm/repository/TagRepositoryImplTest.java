package com.epam.esm.repository;

import com.epam.esm.config.DataConfig;
import com.epam.esm.domain.Tag;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DataConfig.class)
@ActiveProfiles("test")
public class TagRepositoryImplTest {
    @Autowired
    private TagRepository tagRepository;

    private Tag tag;

    @Before
    public void setUp() {
        tag = new Tag("tag");
    }

    @Test
    public void create() {
        Tag actualTag = tagRepository.create(tag);
        assertTrue(actualTag.getId() != 0);
    }

    @Test
    public void read() {
        assertNotNull(tagRepository.read(3L));
    }

    @Test
    public void delete() {
        tagRepository.delete(1L);
        assertNull(tagRepository.read(1L));
    }

    @Test
    public void createCertificateTag() {
        long certificateId = 3L;
        long tagId = 2L;
        tagRepository.createCertificateTag(certificateId, tagId);
        assertTrue(tagRepository.getCertificateTags(certificateId)
                .stream().anyMatch(tag1 -> tag1.getId() == tagId));
    }

    @Test
    public void getCertificateTags() {
        assertTrue(tagRepository.getCertificateTags(3L)
                .stream().anyMatch(tag1 -> tag1.getName().equals("gift")));
    }

    @Test
    public void createNewTags() {
        List<Tag> newTags = new ArrayList<>();
        newTags.add(new Tag("newtag"));
        newTags.add(new Tag("even newer tag"));
        tagRepository.createNewTags(2L, newTags);
        assertTrue(tagRepository.getCertificateTags(2L)
                .stream().anyMatch(tag1 -> tag1.getName().equals("newtag")));
        assertTrue(tagRepository.getCertificateTags(2L)
                .stream().anyMatch(tag1 -> tag1.getName().equals("even newer tag")));
    }
}