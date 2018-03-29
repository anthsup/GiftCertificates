package com.epam.esm.repository;

import com.epam.esm.domain.GiftCertificate;

public interface GiftCertificateRepository extends CrdRepository<GiftCertificate> {
    void update(GiftCertificate certificate);
}
