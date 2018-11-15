package com.pif.insatplatform.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pif.insatplatform.domain.Field;
import com.pif.insatplatform.repository.FieldRepository;
import com.pif.insatplatform.web.rest.errors.BadRequestAlertException;
import com.pif.insatplatform.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Field.
 */
@RestController
@RequestMapping("/api")
public class FieldResource {

    private final Logger log = LoggerFactory.getLogger(FieldResource.class);

    private static final String ENTITY_NAME = "field";

    private final FieldRepository fieldRepository;

    public FieldResource(FieldRepository fieldRepository) {
        this.fieldRepository = fieldRepository;
    }

    /**
     * POST  /fields : Create a new field.
     *
     * @param field the field to create
     * @return the ResponseEntity with status 201 (Created) and with body the new field, or with status 400 (Bad Request) if the field has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/fields")
    @Timed
    public ResponseEntity<Field> createField(@Valid @RequestBody Field field) throws URISyntaxException {
        log.debug("REST request to save Field : {}", field);
        if (field.getId() != null) {
            throw new BadRequestAlertException("A new field cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Field result = fieldRepository.save(field);
        return ResponseEntity.created(new URI("/api/fields/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /fields : Updates an existing field.
     *
     * @param field the field to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated field,
     * or with status 400 (Bad Request) if the field is not valid,
     * or with status 500 (Internal Server Error) if the field couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/fields")
    @Timed
    public ResponseEntity<Field> updateField(@Valid @RequestBody Field field) throws URISyntaxException {
        log.debug("REST request to update Field : {}", field);
        if (field.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Field result = fieldRepository.save(field);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, field.getId().toString()))
            .body(result);
    }

    /**
     * GET  /fields : get all the fields.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of fields in body
     */
    @GetMapping("/fields")
    @Timed
    public List<Field> getAllFields() {
        log.debug("REST request to get all Fields");
        return fieldRepository.findAll();
    }

    /**
     * GET  /fields/:id : get the "id" field.
     *
     * @param id the id of the field to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the field, or with status 404 (Not Found)
     */
    @GetMapping("/fields/{id}")
    @Timed
    public ResponseEntity<Field> getField(@PathVariable Long id) {
        log.debug("REST request to get Field : {}", id);
        Optional<Field> field = fieldRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(field);
    }

    /**
     * DELETE  /fields/:id : delete the "id" field.
     *
     * @param id the id of the field to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/fields/{id}")
    @Timed
    public ResponseEntity<Void> deleteField(@PathVariable Long id) {
        log.debug("REST request to delete Field : {}", id);

        fieldRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
