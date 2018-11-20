package insat.company.platform.web.rest;

import com.codahale.metrics.annotation.Timed;
import insat.company.platform.domain.SlackUser;
import insat.company.platform.repository.SlackUserRepository;
import insat.company.platform.repository.search.SlackUserSearchRepository;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing SlackUser.
 */
@RestController
@RequestMapping("/api")
public class SlackUserResource {

    private final Logger log = LoggerFactory.getLogger(SlackUserResource.class);

    private static final String ENTITY_NAME = "slackUser";

    private final SlackUserRepository slackUserRepository;

    private final SlackUserSearchRepository slackUserSearchRepository;

    public SlackUserResource(SlackUserRepository slackUserRepository, SlackUserSearchRepository slackUserSearchRepository) {
        this.slackUserRepository = slackUserRepository;
        this.slackUserSearchRepository = slackUserSearchRepository;
    }

    /**
     * POST  /slack-users : Create a new slackUser.
     *
     * @param slackUser the slackUser to create
     * @return the ResponseEntity with status 201 (Created) and with body the new slackUser, or with status 400 (Bad Request) if the slackUser has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/slack-users")
    @Timed
    public ResponseEntity<SlackUser> createSlackUser(@RequestBody SlackUser slackUser) throws URISyntaxException {
        log.debug("REST request to save SlackUser : {}", slackUser);
        if (slackUser.getId() != null) {
            throw new BadRequestAlertException("A new slackUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SlackUser result = slackUserRepository.save(slackUser);
        slackUserSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/slack-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /slack-users : Updates an existing slackUser.
     *
     * @param slackUser the slackUser to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated slackUser,
     * or with status 400 (Bad Request) if the slackUser is not valid,
     * or with status 500 (Internal Server Error) if the slackUser couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/slack-users")
    @Timed
    public ResponseEntity<SlackUser> updateSlackUser(@RequestBody SlackUser slackUser) throws URISyntaxException {
        log.debug("REST request to update SlackUser : {}", slackUser);
        if (slackUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SlackUser result = slackUserRepository.save(slackUser);
        slackUserSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, slackUser.getId().toString()))
            .body(result);
    }

    /**
     * GET  /slack-users : get all the slackUsers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of slackUsers in body
     */
    @GetMapping("/slack-users")
    @Timed
    public ResponseEntity<List<SlackUser>> getAllSlackUsers(Pageable pageable) {
        log.debug("REST request to get a page of SlackUsers");
        Page<SlackUser> page = slackUserRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/slack-users");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /slack-users/:id : get the "id" slackUser.
     *
     * @param id the id of the slackUser to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the slackUser, or with status 404 (Not Found)
     */
    @GetMapping("/slack-users/{id}")
    @Timed
    public ResponseEntity<SlackUser> getSlackUser(@PathVariable Long id) {
        log.debug("REST request to get SlackUser : {}", id);
        Optional<SlackUser> slackUser = slackUserRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(slackUser);
    }

    /**
     * DELETE  /slack-users/:id : delete the "id" slackUser.
     *
     * @param id the id of the slackUser to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/slack-users/{id}")
    @Timed
    public ResponseEntity<Void> deleteSlackUser(@PathVariable Long id) {
        log.debug("REST request to delete SlackUser : {}", id);

        slackUserRepository.deleteById(id);
        slackUserSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/slack-users?query=:query : search for the slackUser corresponding
     * to the query.
     *
     * @param query the query of the slackUser search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/slack-users")
    @Timed
    public ResponseEntity<List<SlackUser>> searchSlackUsers(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of SlackUsers for query {}", query);
        Page<SlackUser> page = slackUserSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/slack-users");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
