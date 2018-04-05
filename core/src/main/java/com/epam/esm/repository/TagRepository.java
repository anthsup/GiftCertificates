package com.epam.esm.repository;

import com.epam.esm.domain.Tag;

import java.util.List;

/**
 * Interface that expands default CRD functionality
 * for working with tags
 */
public interface TagRepository extends CrdRepository<Tag> {
    /**
     * Method that creates a row in a table used to
     * reflect N-N connection of tags and gift certificates
     *
     * @param certificateId
     * @param tagId
     */
    void createCertificateTag(long certificateId, long tagId);

    /**
     * Method that finds all the tags belonging to
     * a particular gift certificate
     *
     * @param certificateId ID of the certificate whose tags are being requested
     * @return list of tags if anything is found, empty collection otherwise
     */
    List<Tag> getCertificateTags(long certificateId);

    /**
     * Method that creates new tags and binds them to
     * a particular gift certificate
     *
     * @param certificateId gift certificate whose tags are being created
     * @param newTags       list of tags which are being created
     */
    void createNewTags(long certificateId, List<Tag> newTags);
}
