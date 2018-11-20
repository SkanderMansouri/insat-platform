package insat.company.platform.service;

import insat.company.platform.domain.Field;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Field.
 */
public interface FieldService {

    /**
     * Save a field.
     *
     * @param field the entity to save
     * @return the persisted entity
     */
    Field save(Field field);

    /**
     * Get all the fields.
     *
     * @return the list of entities
     */
    List<Field> findAll();


    /**
     * Get the "id" field.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Field> findOne(Long id);

    /**
     * Delete the "id" field.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the field corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<Field> search(String query);
}
