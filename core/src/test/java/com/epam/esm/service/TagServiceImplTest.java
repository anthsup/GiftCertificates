package com.epam.esm.service;

import com.epam.esm.domain.Tag;
import com.epam.esm.exception.ValidationException;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.impl.TagServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TagServiceImplTest {
    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagService tagService = new TagServiceImpl();

    private Tag tag;

    @Before
    public void setUp() {
        tag = new Tag();
        tag.setId(42);
    }

    @Test
    public void addReturnsTagWhenGivenValidArg() {
        when(tagRepository.create(tag)).thenReturn(tag);
        Tag actualTag = tagService.create(tag);

        verify(tagRepository, times(1)).create(tag);
        assertEquals(tag, actualTag);
    }

    @Test(expected = ValidationException.class)
    public void addThrowsExceptionWhenGivenNull() {
        tagService.create(null);
    }

    @Test
    public void getReturnsTagWhenGivenValidId() {
        when(tagRepository.get(tag.getId())).thenReturn(tag);
        Tag actualTag = tagService.get(tag.getId());

        verify(tagRepository, times(1)).get(tag.getId());
        assertEquals(tag, actualTag);
    }

    @Test
    public void getReturnsNullWhenGivenInvalidId() {
        when(tagRepository.get(13)).thenReturn(null);
        Tag actualTag = tagService.get(13);

        verify(tagRepository, times(1)).get(13);
        assertNull(actualTag);
    }

    @Test
    public void delete() {
        tagService.delete(tag.getId());
        verify(tagRepository, times(1)).delete(tag.getId());
    }
}