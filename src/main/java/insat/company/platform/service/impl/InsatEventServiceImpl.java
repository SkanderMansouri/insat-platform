package insat.company.platform.service.impl;

import insat.company.platform.service.InsatEventService;
import insat.company.platform.domain.InsatEvent;
import insat.company.platform.repository.InsatEventRepository;
import insat.company.platform.repository.search.InsatEventSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing InsatEvent.
 */
@Service
@Transactional
public class InsatEventServiceImpl implements InsatEventService {

    private final Logger log = LoggerFactory.getLogger(InsatEventServiceImpl.class);

    private final InsatEventRepository insatEventRepository;

    private final InsatEventSearchRepository insatEventSearchRepository;

    public InsatEventServiceImpl(InsatEventRepository insatEventRepository, InsatEventSearchRepository insatEventSearchRepository) {
        this.insatEventRepository = insatEventRepository;
        this.insatEventSearchRepository = insatEventSearchRepository;
    }

    /**
     * Save a insatEvent.
     *
     * @param insatEvent the entity to save
     * @return the persisted entity
     */
    @Override
    public InsatEvent save(InsatEvent insatEvent) {
        log.debug("Request to save InsatEvent : {}", insatEvent);
        InsatEvent result = insatEventRepository.save(insatEvent);
        insatEventSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the insatEvents.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<InsatEvent> findAll() {
        log.debug("Request to get all InsatEvents");
        return insatEventRepository.findAllWithEagerRelationships();
    }

    /**
     * Get all the InsatEvent with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<InsatEvent> findAllWithEagerRelationships(Pageable pageable) {
        return insatEventRepository.findAllWithEagerRelationships(pageable);
    }
    

    /**
     * Get one insatEvent by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<InsatEvent> findOne(Long id) {
        log.debug("Request to get InsatEvent : {}", id);
        return insatEventRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the insatEvent by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete InsatEvent : {}", id);
        insatEventRepository.deleteById(id);
        insatEventSearchRepository.deleteById(id);
    }

    /**
     * Search for the insatEvent corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<InsatEvent> search(String query) {
        log.debug("Request to search InsatEvents for query {}", query);
        return StreamSupport
            .stream(insatEventSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
