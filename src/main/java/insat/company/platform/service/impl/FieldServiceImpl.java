package insat.company.platform.service.impl;

import insat.company.platform.service.FieldService;
import insat.company.platform.domain.Field;
import insat.company.platform.repository.FieldRepository;
import insat.company.platform.repository.search.FieldSearchRepository;
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
 * Service Implementation for managing Field.
 */
@Service
@Transactional
public class FieldServiceImpl implements FieldService {

    private final Logger log = LoggerFactory.getLogger(FieldServiceImpl.class);

    private final FieldRepository fieldRepository;

    private final FieldSearchRepository fieldSearchRepository;

    public FieldServiceImpl(FieldRepository fieldRepository, FieldSearchRepository fieldSearchRepository) {
        this.fieldRepository = fieldRepository;
        this.fieldSearchRepository = fieldSearchRepository;
    }

    /**
     * Save a field.
     *
     * @param field the entity to save
     * @return the persisted entity
     */
    @Override
    public Field save(Field field) {
        log.debug("Request to save Field : {}", field);
        Field result = fieldRepository.save(field);
        fieldSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the fields.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Field> findAll() {
        log.debug("Request to get all Fields");
        return fieldRepository.findAll();
    }


    /**
     * Get one field by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Field> findOne(Long id) {
        log.debug("Request to get Field : {}", id);
        return fieldRepository.findById(id);
    }

    /**
     * Delete the field by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Field : {}", id);
        fieldRepository.deleteById(id);
        fieldSearchRepository.deleteById(id);
    }

    /**
     * Search for the field corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Field> search(String query) {
        log.debug("Request to search Fields for query {}", query);
        return StreamSupport
            .stream(fieldSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
