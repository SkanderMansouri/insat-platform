package com.pif.insatplatform.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pif.insatplatform.domain.SlackUser;
import com.pif.insatplatform.service.SlackUserService;
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
 * REST controller for managing SlackUser.
 */
@RestController
@RequestMapping("/api")
public class SlackUserResource {

    private final Logger log = LoggerFactory.getLogger(SlackUserResource.class);

    private static final String ENTITY_NAME = "slackUser";

    private final SlackUserService slackUserService;

    public SlackUserResource(SlackUserService slackUserService) {
        this.slackUserService = slackUserService;
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
        SlackUser result = slackUserService.save(slackUser);
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
        SlackUser result = slackUserService.save(slackUser);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, slackUser.getId().toString()))
            .body(result);
    }

    /**
     * GET  /slack-users : get all the slackUsers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of slackUsers in body
     */
    @GetMapping("/slack-users")
    @Timed
    public List<SlackUser> getAllSlackUsers() {
        log.debug("REST request to get all SlackUsers");
        return slackUserService.findAll();
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
        Optional<SlackUser> slackUser = slackUserService.findOne(id);
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
        slackUserService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
