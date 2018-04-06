package com.epam.esm.service.impl;

import com.epam.esm.domain.Tag;
import com.epam.esm.exception.ValidationException;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TagServiceImpl implements TagService {
    @Autowired
    private TagRepository tagRepository;

    @Override
    public Tag add(Tag tag) {
        if (tag == null) {
            throw new ValidationException("Tag you've provided is null!");
        }
        return tagRepository.create(tag);
    }

    @Override
    public Tag get(long id) {
        return tagRepository.read(id);
    }

    @Override
    public void delete(long id) {
        tagRepository.delete(id);
    }

    @Override
    public List<Tag> getAll() {
        return tagRepository.getAll();
    }
}
