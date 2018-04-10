package com.epam.esm.repository;

import com.epam.esm.domain.GiftCertificate;

import java.util.List;
import java.util.Optional;

/**
 * Interface that expands default CRD functionality
 * for working with gift certificates
 */
public interface GiftCertificateRepository extends CrdRepository<GiftCertificate> {
    /**
     * Method that updates existing entity in a database
     *
     * @param certificate entity to update
     */
    void update(GiftCertificate certificate);

    /**
     * Method that searches gift certificates based on
     * a bunch of optional parameters
     *
     * @param tag         optional tag id connected to a gift certificate
     * @param name        optional part of gift certificate's name
     * @param description optional part of gift certificate's description
     * @param sortBy      optional column name by which to sort (name or creation_date)
     * @return list of gift certificates if anything is found or empty list otherwise
     */
    List<GiftCertificate> search(Optional<Long> tag, Optional<String> name, Optional<String> description,
                                 Optional<String> sortBy);
}
