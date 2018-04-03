package com.epam.esm.repository;

import com.epam.esm.domain.GiftCertificate;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateRepository extends CrdRepository<GiftCertificate> {
    void update(GiftCertificate certificate);
    List<GiftCertificate> search(Optional<Long> tag, Optional<String> name, Optional<String> description,
                                 Optional<String> sortBy);
}
