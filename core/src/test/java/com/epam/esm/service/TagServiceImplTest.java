package com.epam.esm.service;

import com.epam.esm.domain.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.impl.TagRepositoryImpl;
import com.epam.esm.service.impl.TagServiceImpl;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class TagServiceImplTest {
    private TagService tagService;
    private TagRepository tagRepository;
    private Tag tag;

    @Before
    public void setUp() {
        tagRepository = mock(TagRepositoryImpl.class);
        tagService = new TagServiceImpl(tagRepository);
        tag = new Tag();
    }

    @Test
    public void add() {
        tagService.add(tag);
        verify(tagRepository, times(1)).create(tag);
    }

    @Test
    public void get() {
        when(tagRepository.read(tag.getId())).thenReturn(tag);
        Tag actualTag = tagService.get(tag.getId());

        verify(tagRepository, times(1)).read(tag.getId());
        assertEquals(tag, actualTag);
    }

    @Test
    public void delete() {
        tagService.delete(tag.getId());
        verify(tagRepository, times(1)).delete(tag.getId());
    }
}