package insat.company.platform.service.impl;

import insat.company.platform.service.JoinClubRequestService;
import insat.company.platform.domain.JoinClubRequest;
import insat.company.platform.repository.JoinClubRequestRepository;
import insat.company.platform.repository.search.JoinClubRequestSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing JoinClubRequest.
 */
@Service
@Transactional
public class JoinClubRequestServiceImpl implements JoinClubRequestService {

    private final Logger log = LoggerFactory.getLogger(JoinClubRequestServiceImpl.class);

    private final JoinClubRequestRepository joinClubRequestRepository;

    private final JoinClubRequestSearchRepository joinClubRequestSearchRepository;

    public JoinClubRequestServiceImpl(JoinClubRequestRepository joinClubRequestRepository, JoinClubRequestSearchRepository joinClubRequestSearchRepository) {
        this.joinClubRequestRepository = joinClubRequestRepository;
        this.joinClubRequestSearchRepository = joinClubRequestSearchRepository;
    }

    /**
     * Save a joinClubRequest.
     *
     * @param joinClubRequest the entity to save
     * @return the persisted entity
     */
    @Override
    public JoinClubRequest save(JoinClubRequest joinClubRequest) {
        log.debug("Request to save JoinClubRequest : {}", joinClubRequest);
        JoinClubRequest result = joinClubRequestRepository.save(joinClubRequest);
        joinClubRequestSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the joinClubRequests.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<JoinClubRequest> findAll() {
        log.debug("Request to get all JoinClubRequests");
        return joinClubRequestRepository.findAll();
    }


    /**
     * Get one joinClubRequest by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<JoinClubRequest> findOne(Long id) {
        log.debug("Request to get JoinClubRequest : {}", id);
        return joinClubRequestRepository.findById(id);
    }

    /**
     * Delete the joinClubRequest by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete JoinClubRequest : {}", id);
        joinClubRequestRepository.deleteById(id);
        joinClubRequestSearchRepository.deleteById(id);
    }

    /**
     * Search for the joinClubRequest corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<JoinClubRequest> search(String query) {
        log.debug("Request to search JoinClubRequests for query {}", query);
        return StreamSupport
            .stream(joinClubRequestSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
