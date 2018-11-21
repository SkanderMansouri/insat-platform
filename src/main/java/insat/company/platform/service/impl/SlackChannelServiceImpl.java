package insat.company.platform.service.impl;

import insat.company.platform.service.SlackChannelService;
import insat.company.platform.domain.SlackChannel;
import insat.company.platform.repository.SlackChannelRepository;
import insat.company.platform.repository.search.SlackChannelSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing SlackChannel.
 */
@Service
@Transactional
public class SlackChannelServiceImpl implements SlackChannelService {

    private final Logger log = LoggerFactory.getLogger(SlackChannelServiceImpl.class);

    private final SlackChannelRepository slackChannelRepository;

    private final SlackChannelSearchRepository slackChannelSearchRepository;

    public SlackChannelServiceImpl(SlackChannelRepository slackChannelRepository, SlackChannelSearchRepository slackChannelSearchRepository) {
        this.slackChannelRepository = slackChannelRepository;
        this.slackChannelSearchRepository = slackChannelSearchRepository;
    }

    /**
     * Save a slackChannel.
     *
     * @param slackChannel the entity to save
     * @return the persisted entity
     */
    @Override
    public SlackChannel save(SlackChannel slackChannel) {
        log.debug("Request to save SlackChannel : {}", slackChannel);
        SlackChannel result = slackChannelRepository.save(slackChannel);
        slackChannelSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the slackChannels.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SlackChannel> findAll(Pageable pageable) {
        log.debug("Request to get all SlackChannels");
        return slackChannelRepository.findAll(pageable);
    }


    /**
     * Get one slackChannel by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SlackChannel> findOne(Long id) {
        log.debug("Request to get SlackChannel : {}", id);
        return slackChannelRepository.findById(id);
    }

    /**
     * Delete the slackChannel by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SlackChannel : {}", id);
        slackChannelRepository.deleteById(id);
        slackChannelSearchRepository.deleteById(id);
    }

    /**
     * Search for the slackChannel corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SlackChannel> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SlackChannels for query {}", query);
        return slackChannelSearchRepository.search(queryStringQuery(query), pageable);    }
}
