package insat.company.platform.web.rest;

import com.codahale.metrics.annotation.Timed;
import insat.company.platform.domain.Club;
import insat.company.platform.domain.JoinClubRequest;
import insat.company.platform.domain.User;
import insat.company.platform.repository.JoinClubRequestRepository;
import insat.company.platform.security.AuthoritiesConstants;
import insat.company.platform.security.SecurityUtils;
import insat.company.platform.service.ClubService;
import insat.company.platform.service.UserService;
import insat.company.platform.web.rest.errors.BadRequestAlertException;
import insat.company.platform.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Club.
 */
@RestController
@RequestMapping("/api")
public class ClubResource {

    private static final String ENTITY_NAME = "club";
    private final Logger log = LoggerFactory.getLogger(ClubResource.class);
    private final ClubService clubService;
    private final UserService userService;
    private final JoinClubRequestRepository joinClubRequestRepository;

    public ClubResource(ClubService clubService, UserService userService, JoinClubRequestRepository joinClubRequestRepository) {
        this.clubService = clubService;
        this.userService = userService;
        this.joinClubRequestRepository = joinClubRequestRepository;
    }

    /**
     * POST  /clubs : Create a new club.
     *
     * @param club the club to create
     * @return the ResponseEntity with status 201 (Created) and with body the new club, or with status 400 (Bad Request) if the club has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/clubs")
    @Timed
    public ResponseEntity<Club> createClub(@RequestBody Club club) throws URISyntaxException {
        log.debug("REST request to save Club : {}", club);
        if (club.getId() != null) {
            throw new BadRequestAlertException("A new club cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Club result = clubService.save(club);
        return ResponseEntity.created(new URI("/api/clubs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /clubs : Updates an existing club.
     *
     * @param club the club to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated club,
     * or with status 400 (Bad Request) if the club is not valid,
     * or with status 500 (Internal Server Error) if the club couldn't be updated
     */
    @PutMapping("/clubs")
    @Timed
    @Transactional
    public ResponseEntity<Club> updateClub(@RequestBody Club club) {
        log.debug("REST request to update Club : {}", club);
        if (club.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Club result = clubService.save(club);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, club.getId().toString()))
            .body(result);
    }

    /**
     * GET  /clubs : get all the clubs.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of clubs in body
     */
    @GetMapping("/clubs")
    @Timed
    public List<Club> getAllClubs(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Clubs");
        return clubService.findAll();
    }

    /**
     * GET  /clubs/:id : get the "id" club.
     *
     * @param id the id of the club to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the club, or with status 404 (Not Found)
     */
    @GetMapping("/clubs/{id}")
    @Timed
    public ResponseEntity<Club> getClub(@PathVariable Long id) {
        log.debug("REST request to get Club : {}", id);
        Optional<Club> club = clubService.findOne(id);
        return ResponseUtil.wrapOrNotFound(club);
    }

    /**
     * DELETE  /clubs/:id : delete the "id" club.
     *
     * @param id the id of the club to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/clubs/{id}")
    @Timed
    public ResponseEntity<Void> deleteClub(@PathVariable Long id) {
        log.debug("REST request to delete Club : {}", id);
        clubService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/clubs?query=:query : search for the club corresponding
     * to the query.
     *
     * @param query the query of the club search
     * @return the result of the search
     */
    @GetMapping("/_search/clubs")
    @Timed
    public List<Club> searchClubs(@RequestParam String query) {
        log.debug("REST request to search Clubs for query {}", query);
        return clubService.search(query);
    }

    /**
     * @return a string list of the all of the clubs name
     */
    @GetMapping("clubs/list")
    @Timed
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public List<Club> getClubsList() {
        return clubService.getClubsList();
    }


     /**
     * GET  /join/clubs/:id : send joinClubRequest by  club "id"
     *
     * @param id the id of the club  to join
     * @return the ResponseEntity with status 200 (OK), or with status 404 (Not Found) or with status 401 (unauthorized)
     */
    @GetMapping("/join/clubs/{id}")
    @Timed
    public ResponseEntity<?> joinClub(@PathVariable Long id) throws URISyntaxException {
        log.debug("REST request to create a joinClubRequest to join a shouldReturnOkAndCreateAJoinClubRequest\nclub   : {}", id);

        if (!SecurityUtils.isAuthenticated()) {
            log.error("User should be logged in");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String userLogin = SecurityUtils.getCurrentUserLogin().get();

        return clubService.findOne(id).map(club -> {
            User currentUser = userService.getUserWithAuthoritiesByLogin(userLogin).get();
            if(club.hasMember(currentUser))
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            JoinClubRequest joinClubRequest= clubService.sendClubJoinRequest(club, currentUser);
            if(joinClubRequest.getId() >= 0L)
                return new ResponseEntity<>(HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/deleteJoin/clubs/{id}")
    @Timed
    public ResponseEntity<?> deleteRequestJoinClub(@PathVariable Long id) throws URISyntaxException {
        log.debug("REST  to delete a joinClubRequest  : {}", id);

        if (!SecurityUtils.isAuthenticated()) {
            log.error("User should be logged in");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String userLogin = SecurityUtils.getCurrentUserLogin().get();
        return clubService.findOne(id).map(club -> {
            User currentUser = userService.getUserWithAuthoritiesByLogin(userLogin).get();
            ResponseEntity resp = new ResponseEntity(HttpStatus.UNAUTHORIZED);
            try{
                clubService.deleteJoinRequest(club,currentUser);
                resp =  new ResponseEntity<>(HttpStatus.OK);
            }catch (Exception e){
                resp = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }finally {
                return resp;
            }

        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/acceptJoin/joinClubRequests/{id}")
    @Timed
    public ResponseEntity<?> acceptJoinClub(@PathVariable Long id) {
        log.debug("REST request to accept the joinClubRequest {}", id);

        if (!SecurityUtils.isAuthenticated()) {
            log.error("User should be logged in");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Optional<JoinClubRequest> joinClubRequest = joinClubRequestRepository.findOneById(id);

        return joinClubRequest.map(joinClubRequestExists -> {
            try {
                if (clubService.verifyAccessToJoinClubRequest(joinClubRequest)) {
                    clubService.acceptJoinClubRequest(joinClubRequest);
                    log.info("joinClubRequest {} successfully accepted !", id);
                    return new ResponseEntity<>(HttpStatus.OK);
                }
            } catch (Exception e) {
                log.error("conditions to accept joinClubRequest {} not fulfilled !", id);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        })
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/declineJoin/joinClubRequests/{id}")
    @Timed
    public ResponseEntity<?> declineJoinClub(@PathVariable Long id) {
        log.debug("REST request to decline the joinClubRequest {}", id);

        if (!SecurityUtils.isAuthenticated()) {
            log.error("User should be logged in");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Optional<JoinClubRequest> joinClubRequest = joinClubRequestRepository.findOneById(id);

        return joinClubRequest.map(joinClubRequestExists -> {
            try {
                if (clubService.verifyAccessToJoinClubRequest(joinClubRequest)) {
                    clubService.declineJoinClubRequest(joinClubRequest);
                    return new ResponseEntity<>(HttpStatus.OK);
                }
            } catch (Exception e) {
                log.error("conditions to decline joinClubRequest {} not fulfilled !", id);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        })
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
