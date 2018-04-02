package com.epam.esm.repository;

import com.epam.esm.domain.GiftCertificate;

import java.util.List;

public interface GiftCertificateRepository extends CrdRepository<GiftCertificate> {
    void update(GiftCertificate certificate);
    List<GiftCertificate> search(String query, Object... params);
}
