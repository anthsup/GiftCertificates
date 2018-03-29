package com.epam.esm.service;

import com.epam.esm.domain.GiftCertificate;

public interface GiftCertificateService extends CrdService<GiftCertificate> {
    void update(GiftCertificate certificate);
}
