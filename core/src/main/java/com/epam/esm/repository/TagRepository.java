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
     * @param certificateId ID of a certificate whose tags are being requested
     *
     * @return list of tags if anything is found, empty collection otherwise
     */
    List<Tag> getCertificateTags(long certificateId);

    /**
     * Method that retains only provided tags for
     * a particular gift certificate
     *
     * @param certificateId ID of a certificate whose tags are provided
     * @param retainedTagIds ids of tags to be retained
     */
    void retainCertificateTags(long certificateId, List<Long> retainedTagIds);

    /**
     * Method that creates new tags and binds them to
     * a particular gift certificate
     *
     * @param certificateId gift certificate whose tags are being created
     * @param newTags       list of tags which are being created
     *
     * @return list of created tags with generated IDs
     */
    List<Tag> createNewTags(long certificateId, List<Tag> newTags);

    /**
     * Method that returns all tags from a database
     *
     * @return all the tags from a database or empty collection if nothing found
     */
    List<Tag> getAll();

    /**
     * Method that finds a tag in a database by its name
     *
     * @param tagName name of a tag being searched
     * @return a tag if it exists or null otherwise
     */
    Tag getByName(String tagName);
}
