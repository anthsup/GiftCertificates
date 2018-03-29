package com.epam.esm.service.impl;

import com.epam.esm.domain.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagServiceImpl implements TagService {
    private TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public void add(Tag tag) {
        tagRepository.create(tag);
    }

    @Override
    public Tag get(long id) {
        return tagRepository.read(id);
    }

    @Override
    public void delete(long id) {
        tagRepository.delete(id);
    }
}
