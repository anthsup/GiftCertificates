package com.epam.esm.service;

import com.epam.esm.domain.Tag;
import com.epam.esm.repository.TagRepository;

import java.util.List;

/**
 * Interface that expands default CRD functionality
 * for working with tags
 */
public interface TagService extends CrdService<Tag> {
    /**
     *
     * {@link TagRepository#getAll()}
     */
    List<Tag> getAll();
}
