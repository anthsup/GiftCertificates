package com.epam.esm.controller.rest;

import com.epam.esm.controller.rest.util.RestValidator;
import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/certificates")
public class GiftCertificateController {
    @Autowired
    private GiftCertificateService certificateService;

    /**
     * Method to save Gift Certificates in the system
     *
     * @param certificate gift certificate entity to save
     * @return Response entity with CREATED http status code and location of the new resource
     */
    @PostMapping
    public ResponseEntity<GiftCertificate> create(@RequestBody GiftCertificate certificate) {
        RestValidator.checkNotNull(certificate);
        certificate = RestValidator.checkFound(certificateService.create(certificate));
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{certificateId}").buildAndExpand(certificate.getId()).toUri();
        return ResponseEntity.created(location).body(certificate);
    }

    /**
     * Method to find gift certificates by id
     *
     * @param certificateId id of gift certificate to find
     * @return found gift certificate or null if nothing found
     */
    @GetMapping(value = "/{certificateId}")
    @ResponseStatus(HttpStatus.OK)
    public GiftCertificate getById(@PathVariable long certificateId) {
        return RestValidator.checkFound(certificateService.get(certificateId));
    }

    /**
     * Method to edit Gift Certificates. certificateId param and id of Gift Certificate entity
     * should be the same
     *
     * @param certificateId id of Gift Certificate to edit
     * @param certificate   Certificate entity to be updated
     * @return Response entity with OK http status code and location of a new resource
     */
    @PutMapping(value = "/{certificateId}")
    public ResponseEntity<Void> update(@PathVariable long certificateId, @RequestBody GiftCertificate certificate) {
        RestValidator.checkNotNull(certificate);
        RestValidator.checkFound(certificateService.get(certificateId));
        certificate.setId(certificateId);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().buildAndExpand(certificateId).toUri();
        certificateService.update(certificate);
        return ResponseEntity.ok().location(location).build();
    }

    /**
     * Method to delete Gift Certificates from the system
     *
     * @param certificateId id of Gift Certificate to delete
     */
    @DeleteMapping(value = "/{certificateId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long certificateId) {
        certificateService.delete(certificateId);
    }

    /**
     * Method which allows to find gift certificates in the system
     *
     * @param description search by part of certificate's description
     * @param name        search by part of certificate's name
     * @param tagId       provide tagId which certificate contains
     * @param sortBy      provide column to sort by — either name or creation_date
     * @return List of Gift Certificates
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<GiftCertificate> search(@RequestParam Optional<Long> tagId, @RequestParam Optional<String> name,
                                        @RequestParam Optional<String> description,
                                        @RequestParam Optional<String> sortBy) {
        return certificateService.search(tagId, name, description, sortBy);
    }
}
