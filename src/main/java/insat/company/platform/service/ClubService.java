package insat.company.platform.service;

import insat.company.platform.domain.Club;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Club.
 */
public interface ClubService {

    /**
     * Save a club.
     *
     * @param club the entity to save
     * @return the persisted entity
     */
    Club save(Club club);

    /**
     * Get all the clubs.
     *
     * @return the list of entities
     */
    List<Club> findAll();

    /**
     * Get all the Club with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    Page<Club> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" club.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Club> findOne(Long id);

    /**
     * Delete the "id" club.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the club corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    List<Club> search(String query);
}
