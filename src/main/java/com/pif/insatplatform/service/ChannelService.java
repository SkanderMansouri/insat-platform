package com.pif.insatplatform.service;

import com.pif.insatplatform.domain.Channel;
import com.pif.insatplatform.repository.ChannelRepository;
import com.pif.insatplatform.service.dto.ChannelDTO;
import com.pif.insatplatform.service.mapper.ChannelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing Channel.
 */
@Service
@Transactional
public class ChannelService {

    private final Logger log = LoggerFactory.getLogger(ChannelService.class);

    private final ChannelRepository channelRepository;

    private final ChannelMapper channelMapper;

    public ChannelService(ChannelRepository channelRepository, ChannelMapper channelMapper) {
        this.channelRepository = channelRepository;
        this.channelMapper = channelMapper;
    }

    /**
     * Save a channel.
     *
     * @param channelDTO the entity to save
     * @return the persisted entity
     */
    public ChannelDTO save(ChannelDTO channelDTO) {
        log.debug("Request to save Channel : {}", channelDTO);
        Channel channel = channelMapper.toEntity(channelDTO);
        channel = channelRepository.save(channel);
        return channelMapper.toDto(channel);
    }

    /**
     * Get all the channels.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ChannelDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Channels");
        return channelRepository.findAll(pageable)
            .map(channelMapper::toDto);
    }


    /**
     * Get one channel by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ChannelDTO> findOne(Long id) {
        log.debug("Request to get Channel : {}", id);
        return channelRepository.findById(id)
            .map(channelMapper::toDto);
    }

    /**
     * Delete the channel by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Channel : {}", id);
        channelRepository.deleteById(id);
    }
}
