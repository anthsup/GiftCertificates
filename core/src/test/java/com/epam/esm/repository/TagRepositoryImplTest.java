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
        tag = new Tag(42L, "tag");
    }

    @Test
    public void create() {
        Tag actualTag = tagRepository.create(tag);
        assertEquals(tag.getId(), actualTag.getId());
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
}