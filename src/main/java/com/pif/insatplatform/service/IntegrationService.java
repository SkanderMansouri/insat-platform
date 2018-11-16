package com.pif.insatplatform.service;

import com.pif.insatplatform.domain.Integration;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Integration.
 */
public interface IntegrationService {

    /**
     * Save a integration.
     *
     * @param integration the entity to save
     * @return the persisted entity
     */
    Integration save(Integration integration);

    /**
     * Get all the integrations.
     *
     * @return the list of entities
     */
    List<Integration> findAll();


    /**
     * Get the "id" integration.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Integration> findOne(Long id);

    /**
     * Delete the "id" integration.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
