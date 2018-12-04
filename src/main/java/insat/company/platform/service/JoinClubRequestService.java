package insat.company.platform.service;

import insat.company.platform.domain.JoinClubRequest;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing JoinClubRequest.
 */
public interface JoinClubRequestService {

    /**
     * Save a joinClubRequest.
     *
     * @param joinClubRequest the entity to save
     * @return the persisted entity
     */
    JoinClubRequest save(JoinClubRequest joinClubRequest);

    /**
     * Get all the joinClubRequests.
     *
     * @return the list of entities
     */
    List<JoinClubRequest> findAll();


    /**
     * Get the "id" joinClubRequest.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<JoinClubRequest> findOne(Long id);

    /**
     * Delete the "id" joinClubRequest.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the joinClubRequest corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<JoinClubRequest> search(String query);
}
