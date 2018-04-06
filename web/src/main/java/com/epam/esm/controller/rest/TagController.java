package com.epam.esm.controller.rest;

import com.epam.esm.controller.rest.util.RestValidator;
import com.epam.esm.domain.Tag;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    @Autowired
    private TagService tagService;

    /**
     * Method to save Tag in the system
     *
     * @param tag tag entity to save
     * @return Response entity with CREATED http status code and location of the new resource
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Tag> create(@RequestBody Tag tag, UriComponentsBuilder ucb) {
        RestValidator.checkNotNull(tag);
        URI locationUri = ucb.path("/api/tags/").path(String.valueOf(tag.getId())).build().toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(locationUri);
        return new ResponseEntity<>(RestValidator.checkFound(tagService.add(tag)), headers, HttpStatus.CREATED);
    }

    /**
     * Method to find Tag by id
     *
     * @param tagId id of a tag to find
     * @return found Tag or null if nothing found
     */
    @GetMapping(value = "/{tagId}")
    @ResponseStatus(HttpStatus.OK)
    public Tag get(@PathVariable long tagId) {
        return RestValidator.checkFound(tagService.get(tagId));
    }

    /**
     * Method to delete Tag from the system
     *
     * @param tagId id of Tag to delete
     */
    @DeleteMapping(value = "/{tagId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long tagId) {
        tagService.delete(tagId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Tag> getAll() {
        return tagService.getAll();
    }
}
