package com.pif.insatplatform.service.impl;

import com.pif.insatplatform.service.SlackUserService;
import com.pif.insatplatform.domain.SlackUser;
import com.pif.insatplatform.repository.SlackUserRepository;
import com.pif.insatplatform.service.dto.SlackUserDTO;
import com.pif.insatplatform.service.mapper.SlackUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
/**
 * Service Implementation for managing SlackUser.
 */
@Service
@Transactional
public class SlackUserServiceImpl implements SlackUserService {

    private final Logger log = LoggerFactory.getLogger(SlackUserServiceImpl.class);

    private final SlackUserRepository slackUserRepository;

    private final SlackUserMapper slackUserMapper;

    public SlackUserServiceImpl(SlackUserRepository slackUserRepository, SlackUserMapper slackUserMapper) {
        this.slackUserRepository = slackUserRepository;
        this.slackUserMapper = slackUserMapper;
    }

    /**
     * Save a slackUser.
     *
     * @param slackUserDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SlackUserDTO save(SlackUserDTO slackUserDTO) {
        log.debug("Request to save SlackUser : {}", slackUserDTO);
        SlackUser slackUser = slackUserMapper.toEntity(slackUserDTO);
        slackUser = slackUserRepository.save(slackUser);
        return slackUserMapper.toDto(slackUser);
    }

    /**
     * Get all the slackUsers.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<SlackUserDTO> findAll() {
        log.debug("Request to get all SlackUsers");
        return slackUserRepository.findAll().stream()
            .map(slackUserMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one slackUser by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SlackUserDTO> findOne(Long id) {
        log.debug("Request to get SlackUser : {}", id);
        return slackUserRepository.findById(id)
            .map(slackUserMapper::toDto);
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
