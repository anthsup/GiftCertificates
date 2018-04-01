package com.epam.esm.controller.rest;

import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/certificates")
public class GiftCertificateController {
    @Autowired
    private GiftCertificateService certificateService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public GiftCertificate create(@RequestBody GiftCertificate certificate) {
        return certificateService.add(certificate);
    }

    @RequestMapping(value = "/{certificateId}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public GiftCertificate getById(@PathVariable long certificateId) {
        return certificateService.get(certificateId);
    }

    @RequestMapping(value = "/{certificateId}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable long certificateId, @RequestBody GiftCertificate certificate) {
        certificateService.update(certificate);
    }

    @RequestMapping(value = "/{certificateId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long certificateId) {
        certificateService.delete(certificateId);
    }
}
