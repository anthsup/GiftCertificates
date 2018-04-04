package com.epam.esm.repository;

import com.epam.esm.domain.Tag;

import java.util.List;

public interface TagRepository extends CrdRepository<Tag> {
    void createCertificateTag(long certificateId, long tagId);
    List<Tag> getCertificateTags(long certificateId);
}
