package com.pif.insatplatform.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pif.insatplatform.domain.Integration;
import com.pif.insatplatform.service.IntegrationService;
import com.pif.insatplatform.web.rest.errors.BadRequestAlertException;
import com.pif.insatplatform.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Integration.
 */
@RestController
@RequestMapping("/api")
public class IntegrationResource {

    private final Logger log = LoggerFactory.getLogger(IntegrationResource.class);

    private static final String ENTITY_NAME = "integration";

    private final IntegrationService integrationService;

    public IntegrationResource(IntegrationService integrationService) {
        this.integrationService = integrationService;
    }

    /**
     * POST  /integrations : Create a new integration.
     *
     * @param integration the integration to create
     * @return the ResponseEntity with status 201 (Created) and with body the new integration, or with status 400 (Bad Request) if the integration has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/integrations")
    @Timed
    public ResponseEntity<Integration> createIntegration(@RequestBody Integration integration) throws URISyntaxException {
        log.debug("REST request to save Integration : {}", integration);
        if (integration.getId() != null) {
            throw new BadRequestAlertException("A new integration cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Integration result = integrationService.save(integration);
        return ResponseEntity.created(new URI("/api/integrations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /integrations : Updates an existing integration.
     *
     * @param integration the integration to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated integration,
     * or with status 400 (Bad Request) if the integration is not valid,
     * or with status 500 (Internal Server Error) if the integration couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/integrations")
    @Timed
    public ResponseEntity<Integration> updateIntegration(@RequestBody Integration integration) throws URISyntaxException {
        log.debug("REST request to update Integration : {}", integration);
        if (integration.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Integration result = integrationService.save(integration);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, integration.getId().toString()))
            .body(result);
    }

    /**
     * GET  /integrations : get all the integrations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of integrations in body
     */
    @GetMapping("/integrations")
    @Timed
    public List<Integration> getAllIntegrations() {
        log.debug("REST request to get all Integrations");
        return integrationService.findAll();
    }

    /**
     * GET  /integrations/:id : get the "id" integration.
     *
     * @param id the id of the integration to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the integration, or with status 404 (Not Found)
     */
    @GetMapping("/integrations/{id}")
    @Timed
    public ResponseEntity<Integration> getIntegration(@PathVariable Long id) {
        log.debug("REST request to get Integration : {}", id);
        Optional<Integration> integration = integrationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(integration);
    }

    /**
     * DELETE  /integrations/:id : delete the "id" integration.
     *
     * @param id the id of the integration to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/integrations/{id}")
    @Timed
    public ResponseEntity<Void> deleteIntegration(@PathVariable Long id) {
        log.debug("REST request to delete Integration : {}", id);
        integrationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
