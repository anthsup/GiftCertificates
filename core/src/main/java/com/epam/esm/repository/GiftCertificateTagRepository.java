package com.epam.esm.repository;

import java.util.List;

public interface GiftCertificateTagRepository {
    void create(long certificateId, long tagId);
    List<Long> getCertificateTagsId(long certificateId);
}
