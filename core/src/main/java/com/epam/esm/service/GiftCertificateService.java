package com.epam.esm.service;

import com.epam.esm.domain.GiftCertificate;

import java.util.List;
import java.util.Optional;

/**
 * {@link com.epam.esm.repository.GiftCertificateRepository}
 */
public interface GiftCertificateService extends CrdService<GiftCertificate> {
    /**
     * {@link com.epam.esm.repository.GiftCertificateRepository#update(GiftCertificate)}
     *
     * Method also adds new tags if they were found in certificate entity
     */
    void update(GiftCertificate certificate);

    /**
     * {@link com.epam.esm.repository.GiftCertificateRepository#search(Optional, Optional, Optional, Optional)}
     *
     * Method also sets certificates' tags if they're found
     */
    List<GiftCertificate> search(Optional<Long> tag, Optional<String> name, Optional<String> description,
                                 Optional<String> sortBy);
}
