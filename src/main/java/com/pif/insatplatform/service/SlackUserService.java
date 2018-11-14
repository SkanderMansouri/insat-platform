package com.pif.insatplatform.service;

import com.pif.insatplatform.domain.SlackUser;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing SlackUser.
 */
public interface SlackUserService {

    /**
     * Save a slackUser.
     *
     * @param slackUser the entity to save
     * @return the persisted entity
     */
    SlackUser save(SlackUser slackUser);

    /**
     * Get all the slackUsers.
     *
     * @return the list of entities
     */
    List<SlackUser> findAll();


    /**
     * Get the "id" slackUser.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<SlackUser> findOne(Long id);

    /**
     * Delete the "id" slackUser.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
