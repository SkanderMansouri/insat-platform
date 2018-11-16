package com.pif.insatplatform.service.impl;

import com.pif.insatplatform.service.SlackUserService;
import com.pif.insatplatform.domain.SlackUser;
import com.pif.insatplatform.repository.SlackUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
/**
 * Service Implementation for managing SlackUser.
 */
@Service
@Transactional
public class SlackUserServiceImpl implements SlackUserService {

    private final Logger log = LoggerFactory.getLogger(SlackUserServiceImpl.class);

    private final SlackUserRepository slackUserRepository;

    public SlackUserServiceImpl(SlackUserRepository slackUserRepository) {
        this.slackUserRepository = slackUserRepository;
    }

    /**
     * Save a slackUser.
     *
     * @param slackUser the entity to save
     * @return the persisted entity
     */
    @Override
    public SlackUser save(SlackUser slackUser) {
        log.debug("Request to save SlackUser : {}", slackUser);        return slackUserRepository.save(slackUser);
    }

    /**
     * Get all the slackUsers.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<SlackUser> findAll() {
        log.debug("Request to get all SlackUsers");
        return slackUserRepository.findAll();
    }


    /**
     * Get one slackUser by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SlackUser> findOne(Long id) {
        log.debug("Request to get SlackUser : {}", id);
        return slackUserRepository.findById(id);
    }

    /**
     * Delete the slackUser by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SlackUser : {}", id);
        slackUserRepository.deleteById(id);
    }
}
