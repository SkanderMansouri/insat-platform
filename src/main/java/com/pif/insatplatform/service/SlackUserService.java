package com.pif.insatplatform.service;

import com.pif.insatplatform.service.dto.SlackUserDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing SlackUser.
 */
public interface SlackUserService {

    /**
     * Save a slackUser.
     *
     * @param slackUserDTO the entity to save
     * @return the persisted entity
     */
    SlackUserDTO save(SlackUserDTO slackUserDTO);

    /**
     * Get all the slackUsers.
     *
     * @return the list of entities
     */
    List<SlackUserDTO> findAll();


    /**
     * Get the "id" slackUser.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<SlackUserDTO> findOne(Long id);

    /**
     * Delete the "id" slackUser.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
