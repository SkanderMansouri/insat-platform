package insat.company.platform.web.rest;

import com.codahale.metrics.annotation.Timed;
import insat.company.platform.domain.Integration;
import insat.company.platform.domain.User;
import insat.company.platform.repository.UserRepository;
import insat.company.platform.service.IntegrationService;
import insat.company.platform.service.dto.IntegrationDTO;
import insat.company.platform.web.rest.errors.BadRequestAlertException;
import insat.company.platform.web.rest.util.HeaderUtil;
import insat.company.platform.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

    private final UserRepository userRepository;

    public IntegrationResource(IntegrationService integrationService, UserRepository userRepository) {
        this.integrationService = integrationService;
        this.userRepository = userRepository;
    }

    /**
     * POST  /integrations : Create a new integration.
     *
     * @param integrationDTO the integration to create
     * @return the ResponseEntity with status 201 (Created) and with body the new integration, or with status 400 (Bad Request) if the integration has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/integrations")
    @Timed
    public ResponseEntity<Integration> createIntegration(@RequestBody IntegrationDTO integrationDTO) throws URISyntaxException {
        log.debug("REST request to save Integration : {}", integrationDTO);
        //User user = userRepository.findOneWithAuthoritiesById(integrationDTO.getUserId()).get();
      return  Optional.ofNullable(userRepository.findOneWithAuthoritiesById(integrationDTO.getUserId()).get())
                .map(user->{
                    Integration result = integrationService.save(integrationDTO, user);
                    try {
                        return ResponseEntity.created(new URI("/api/integrations/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                         ResponseEntity.badRequest();
                         return null ;
                    }
                }).orElseGet(()-> {
              ResponseEntity.badRequest();
              return null ;
        });



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
        if (integration.getTeamId() == null) {
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
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of integrations in body
     */
    @GetMapping("/integrations")
    @Timed
    public ResponseEntity<List<Integration>> getAllIntegrations(Pageable pageable) {
        log.debug("REST request to get a page of Integrations");
        Page<Integration> page = integrationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/integrations");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
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

    /**
     * SEARCH  /_search/integrations?query=:query : search for the integration corresponding
     * to the query.
     *
     * @param query    the query of the integration search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/integrations")
    @Timed
    public ResponseEntity<List<Integration>> searchIntegrations(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Integrations for query {}", query);
        Page<Integration> page = integrationService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/integrations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
