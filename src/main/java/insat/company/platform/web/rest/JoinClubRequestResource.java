package insat.company.platform.web.rest;

import com.codahale.metrics.annotation.Timed;
import insat.company.platform.domain.JoinClubRequest;
import insat.company.platform.security.SecurityUtils;
import insat.company.platform.service.JoinClubRequestService;
import insat.company.platform.service.UserService;
import insat.company.platform.web.rest.errors.BadRequestAlertException;
import insat.company.platform.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing JoinClubRequest.
 */
@RestController
@RequestMapping("/api")
public class JoinClubRequestResource {

    private final Logger log = LoggerFactory.getLogger(JoinClubRequestResource.class);

    private static final String ENTITY_NAME = "joinClubRequest";

    private final UserService userService;
    private final JoinClubRequestService joinClubRequestService;

    public JoinClubRequestResource(UserService userService, JoinClubRequestService joinClubRequestService) {
        this.userService = userService;
        this.joinClubRequestService = joinClubRequestService;
    }

    /**
     * POST  /join-club-requests : Create a new joinClubRequest.
     *
     * @param joinClubRequest the joinClubRequest to create
     * @return the ResponseEntity with status 201 (Created) and with body the new joinClubRequest, or with status 400 (Bad Request) if the joinClubRequest has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/join-club-requests")
    @Timed
    public ResponseEntity<JoinClubRequest> createJoinClubRequest(@Valid @RequestBody JoinClubRequest joinClubRequest) throws URISyntaxException {
        log.debug("REST request to save JoinClubRequest : {}", joinClubRequest);
        if (joinClubRequest.getId() != null) {
            throw new BadRequestAlertException("A new joinClubRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        JoinClubRequest result = joinClubRequestService.save(joinClubRequest);
        return ResponseEntity.created(new URI("/api/join-club-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /join-club-requests : Updates an existing joinClubRequest.
     *
     * @param joinClubRequest the joinClubRequest to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated joinClubRequest,
     * or with status 400 (Bad Request) if the joinClubRequest is not valid,
     * or with status 500 (Internal Server Error) if the joinClubRequest couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/join-club-requests")
    @Timed
    public ResponseEntity<JoinClubRequest> updateJoinClubRequest(@Valid @RequestBody JoinClubRequest joinClubRequest) throws URISyntaxException {
        log.debug("REST request to update JoinClubRequest : {}", joinClubRequest);
        if (joinClubRequest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        JoinClubRequest result = joinClubRequestService.save(joinClubRequest);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, joinClubRequest.getId().toString()))
            .body(result);
    }

    /**
     * GET  /join-club-requests : get all the joinClubRequests.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of joinClubRequests in body
     */
    @GetMapping("/join-club-requests")
    @Timed
    public List<JoinClubRequest> getAllJoinClubRequests() {
        log.debug("REST request to get all JoinClubRequests");
        return joinClubRequestService.findAll();
    }

    /**
     * GET  /join-club-requests/:id : get the "id" joinClubRequest.
     *
     * @param id the id of the joinClubRequest to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the joinClubRequest, or with status 404 (Not Found)
     */
    @GetMapping("/join-club-requests/{id}")
    @Timed
    public ResponseEntity<JoinClubRequest> getJoinClubRequest(@PathVariable Long id) {
        log.debug("REST request to get JoinClubRequest : {}", id);
        Optional<JoinClubRequest> joinClubRequest = joinClubRequestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(joinClubRequest);
    }

    /**
     * DELETE  /join-club-requests/:id : delete the "id" joinClubRequest.
     *
     * @param id the id of the joinClubRequest to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/join-club-requests/{id}")
    @Timed
    public ResponseEntity<Void> deleteJoinClubRequest(@PathVariable Long id) {
        log.debug("REST request to delete JoinClubRequest : {}", id);
        joinClubRequestService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/join-club-requests?query=:query : search for the joinClubRequest corresponding
     * to the query.
     *
     * @param query the query of the joinClubRequest search
     * @return the result of the search
     */
    @GetMapping("/_search/join-club-requests")
    @Timed
    public List<JoinClubRequest> searchJoinClubRequests(@RequestParam String query) {
        log.debug("REST request to search JoinClubRequests for query {}", query);
        return joinClubRequestService.search(query);
    }

    /**
     * GET  /join-club-request/accept/:id : accepts the joinClubRequest by "id"
     *
     * @param id the id of the joinClubRequest to retrieve
     * @return the ResponseEntity with status 200 (OK), or with status 404 (Not Found)
     */
    @GetMapping("/join-club-request/accept/{id}")
    @Timed
    public ResponseEntity<JoinClubRequest> acceptJoinClubRequestAPI(@RequestBody long id) {
        log.debug("REST request to accept JoinClubRequest : {}", id);
        Optional<JoinClubRequest> joinClubRequest = joinClubRequestService.findOne(id);
        if (!SecurityUtils.isAuthenticated()) {
            log.error("User should be logged in");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if (joinClubRequest.isPresent()) {
            userService.acceptJoinClubRequest(joinClubRequest);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            log.error("The request doesn't exist !");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * GET  /join-club-request/decline/:id : declines the joinClubRequest by "id"
     *
     * @param id the id of the joinClubRequest to retrieve
     * @return the ResponseEntity with status 200 (OK), or with status 404 (Not Found)
     */
    @GetMapping("/join-club-request/decline/{id}")
    @Timed
    public ResponseEntity<JoinClubRequest> declineJoinClubRequestAPI(@RequestBody long id) {
        log.debug("REST request to decline JoinClubRequest : {}", id);
        Optional<JoinClubRequest> joinClubRequest = joinClubRequestService.findOne(id);
        if (!SecurityUtils.isAuthenticated()) {
            log.error("User should be logged in");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if (joinClubRequest.isPresent()) {
            userService.declineJoinClubRequest(joinClubRequest);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            log.error("The request doesn't exist !");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
