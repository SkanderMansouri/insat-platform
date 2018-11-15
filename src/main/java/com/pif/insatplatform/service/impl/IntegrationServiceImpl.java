package com.pif.insatplatform.service.impl;

import com.pif.insatplatform.service.IntegrationService;
import com.pif.insatplatform.domain.Integration;
import com.pif.insatplatform.repository.IntegrationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
/**
 * Service Implementation for managing Integration.
 */
@Service
@Transactional
public class IntegrationServiceImpl implements IntegrationService {

    private final Logger log = LoggerFactory.getLogger(IntegrationServiceImpl.class);

    private final IntegrationRepository integrationRepository;

    public IntegrationServiceImpl(IntegrationRepository integrationRepository) {
        this.integrationRepository = integrationRepository;
    }

    /**
     * Save a integration.
     *
     * @param integration the entity to save
     * @return the persisted entity
     */
    @Override
    public Integration save(Integration integration) {
        log.debug("Request to save Integration : {}", integration);        return integrationRepository.save(integration);
    }

    /**
     * Get all the integrations.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Integration> findAll() {
        log.debug("Request to get all Integrations");
        return integrationRepository.findAll();
    }


    /**
     * Get one integration by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Integration> findOne(Long id) {
        log.debug("Request to get Integration : {}", id);
        return integrationRepository.findById(id);
    }

    /**
     * Delete the integration by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Integration : {}", id);
        integrationRepository.deleteById(id);
    }
}
