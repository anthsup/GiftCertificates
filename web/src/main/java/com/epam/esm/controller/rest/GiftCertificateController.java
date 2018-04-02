package com.epam.esm.controller.rest;

import com.epam.esm.controller.rest.util.RestPreConditions;
import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.exception.ValidationException;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/certificates")
public class GiftCertificateController {
    @Autowired
    private GiftCertificateService certificateService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<GiftCertificate> create(@RequestBody GiftCertificate certificate, UriComponentsBuilder ucb) {
        RestPreConditions.checkNotNull(certificate);
        URI locationUri = ucb.path("/api/certificates").buildAndExpand(certificate.getId()).toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(locationUri);
        return new ResponseEntity<>(RestPreConditions.checkFound(certificateService.add(certificate)), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{certificateId}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public GiftCertificate getById(@PathVariable long certificateId) {
        return RestPreConditions.checkFound(certificateService.get(certificateId));
    }

    @RequestMapping(value = "/{certificateId}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> update(@PathVariable long certificateId, @RequestBody GiftCertificate certificate,
                                       UriComponentsBuilder ucb) {
        RestPreConditions.checkNotNull(certificate);
        if (certificateId != certificate.getId()) {
            throw new ValidationException("Path and request body id should match.");
        }
        RestPreConditions.checkFound(certificateService.get(certificateId));
        URI locationUri = ucb.path("/api/certificates").buildAndExpand(certificateId).toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(locationUri);
        certificateService.update(certificate);
        return new ResponseEntity<>( headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/{certificateId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long certificateId) {
        certificateService.delete(certificateId);
    }
}
