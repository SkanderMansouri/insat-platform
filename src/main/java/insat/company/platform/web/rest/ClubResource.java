package insat.company.platform.web.rest;

import com.codahale.metrics.annotation.Timed;
import insat.company.platform.service.ClubService;
import insat.company.platform.web.rest.errors.BadRequestAlertException;
import insat.company.platform.web.rest.util.HeaderUtil;
import insat.company.platform.service.dto.ClubDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Club.
 */
@RestController
@RequestMapping("/api")
public class ClubResource {

    private final Logger log = LoggerFactory.getLogger(ClubResource.class);

    private static final String ENTITY_NAME = "club";

    private final ClubService clubService;

    public ClubResource(ClubService clubService) {
        this.clubService = clubService;
    }

    /**
     * POST  /clubs : Create a new club.
     *
     * @param clubDTO the clubDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new clubDTO, or with status 400 (Bad Request) if the club has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/clubs")
    @Timed
    public ResponseEntity<ClubDTO> createClub(@RequestBody ClubDTO clubDTO) throws URISyntaxException {
        log.debug("REST request to save Club : {}", clubDTO);
        if (clubDTO.getId() != null) {
            throw new BadRequestAlertException("A new club cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ClubDTO result = clubService.save(clubDTO);
        return ResponseEntity.created(new URI("/api/clubs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /clubs : Updates an existing club.
     *
     * @param clubDTO the clubDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated clubDTO,
     * or with status 400 (Bad Request) if the clubDTO is not valid,
     * or with status 500 (Internal Server Error) if the clubDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/clubs")
    @Timed
    public ResponseEntity<ClubDTO> updateClub(@RequestBody ClubDTO clubDTO) throws URISyntaxException {
        log.debug("REST request to update Club : {}", clubDTO);
        if (clubDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ClubDTO result = clubService.save(clubDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, clubDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /clubs : get all the clubs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of clubs in body
     */
    @GetMapping("/clubs")
    @Timed
    public List<ClubDTO> getAllClubs() {
        log.debug("REST request to get all Clubs");
        return clubService.findAll();
    }

    /**
     * GET  /clubs/:id : get the "id" club.
     *
     * @param id the id of the clubDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the clubDTO, or with status 404 (Not Found)
     */
    @GetMapping("/clubs/{id}")
    @Timed
    public ResponseEntity<ClubDTO> getClub(@PathVariable Long id) {
        log.debug("REST request to get Club : {}", id);
        Optional<ClubDTO> clubDTO = clubService.findOne(id);
        return ResponseUtil.wrapOrNotFound(clubDTO);
    }

    /**
     * DELETE  /clubs/:id : delete the "id" club.
     *
     * @param id the id of the clubDTO to delete
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
    public List<ClubDTO> searchClubs(@RequestParam String query) {
        log.debug("REST request to search Clubs for query {}", query);
        return clubService.search(query);
    }

}
