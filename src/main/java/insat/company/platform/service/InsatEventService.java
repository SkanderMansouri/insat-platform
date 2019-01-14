package insat.company.platform.service;

import insat.company.platform.domain.InsatEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing InsatEvent.
 */
public interface InsatEventService {

    /**
     * Save a insatEvent.
     *
     * @param insatEvent the entity to save
     * @return the persisted entity
     */
    InsatEvent save(InsatEvent insatEvent);

    /**
     * Get all the insatEvents.
     *
     * @return the list of entities
     */
    List<InsatEvent> findAll();

    /**
     * Get all the InsatEvent with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    Page<InsatEvent> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" insatEvent.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<InsatEvent> findOne(Long id);

    /**
     * Delete the "id" insatEvent.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the insatEvent corresponding to the query.
     *
     * @param query the query of the search
     *
     * @return the list of entities
     */
    List<InsatEvent> search(String query);
}
