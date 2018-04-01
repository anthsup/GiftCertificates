package com.epam.esm.controller.rest;

import com.epam.esm.domain.Tag;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    @Autowired
    private TagService tagService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Tag create(@RequestBody Tag tag) {
        return tagService.add(tag);
    }

    @RequestMapping(value = "/{tagId}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Tag get(@PathVariable long tagId) {
        return tagService.get(tagId);
    }

    @RequestMapping(value = "/{tagId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long tagId) {
        tagService.delete(tagId);
    }
}
