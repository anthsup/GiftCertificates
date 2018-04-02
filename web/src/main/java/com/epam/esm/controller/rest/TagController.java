package com.epam.esm.controller.rest;

import com.epam.esm.controller.rest.util.RestPreConditions;
import com.epam.esm.domain.Tag;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    @Autowired
    private TagService tagService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Tag> create(@RequestBody Tag tag, UriComponentsBuilder ucb) {
        RestPreConditions.checkNotNull(tag);
        URI locationUri = ucb.path("/api/tags").buildAndExpand(tag.getId()).toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(locationUri);
        return new ResponseEntity<>(RestPreConditions.checkFound(tagService.add(tag)), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{tagId}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Tag get(@PathVariable long tagId) {
        return RestPreConditions.checkFound(tagService.get(tagId));
    }

    @RequestMapping(value = "/{tagId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long tagId) {
        tagService.delete(tagId);
    }
}
