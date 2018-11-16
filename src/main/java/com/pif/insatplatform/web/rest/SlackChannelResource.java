package com.pif.insatplatform.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pif.insatplatform.domain.SlackChannel;
import com.pif.insatplatform.service.SlackChannelService;
import com.pif.insatplatform.web.rest.errors.BadRequestAlertException;
import com.pif.insatplatform.web.rest.util.HeaderUtil;
import com.pif.insatplatform.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing SlackChannel.
 */
@RestController
@RequestMapping("/api")
public class SlackChannelResource {

    private final Logger log = LoggerFactory.getLogger(SlackChannelResource.class);

    private static final String ENTITY_NAME = "slackChannel";

    private final SlackChannelService slackChannelService;

    public SlackChannelResource(SlackChannelService slackChannelService) {
        this.slackChannelService = slackChannelService;
    }

    /**
     * POST  /slack-channels : Create a new slackChannel.
     *
     * @param slackChannel the slackChannel to create
     * @return the ResponseEntity with status 201 (Created) and with body the new slackChannel, or with status 400 (Bad Request) if the slackChannel has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/slack-channels")
    @Timed
    public ResponseEntity<SlackChannel> createSlackChannel(@Valid @RequestBody SlackChannel slackChannel) throws URISyntaxException {
        log.debug("REST request to save SlackChannel : {}", slackChannel);
        if (slackChannel.getId() != null) {
            throw new BadRequestAlertException("A new slackChannel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SlackChannel result = slackChannelService.save(slackChannel);
        return ResponseEntity.created(new URI("/api/slack-channels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /slack-channels : Updates an existing slackChannel.
     *
     * @param slackChannel the slackChannel to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated slackChannel,
     * or with status 400 (Bad Request) if the slackChannel is not valid,
     * or with status 500 (Internal Server Error) if the slackChannel couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/slack-channels")
    @Timed
    public ResponseEntity<SlackChannel> updateSlackChannel(@Valid @RequestBody SlackChannel slackChannel) throws URISyntaxException {
        log.debug("REST request to update SlackChannel : {}", slackChannel);
        if (slackChannel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SlackChannel result = slackChannelService.save(slackChannel);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, slackChannel.getId().toString()))
            .body(result);
    }

    /**
     * GET  /slack-channels : get all the slackChannels.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of slackChannels in body
     */
    @GetMapping("/slack-channels")
    @Timed
    public ResponseEntity<List<SlackChannel>> getAllSlackChannels(Pageable pageable) {
        log.debug("REST request to get a page of SlackChannels");
        Page<SlackChannel> page = slackChannelService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/slack-channels");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /slack-channels/:id : get the "id" slackChannel.
     *
     * @param id the id of the slackChannel to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the slackChannel, or with status 404 (Not Found)
     */
    @GetMapping("/slack-channels/{id}")
    @Timed
    public ResponseEntity<SlackChannel> getSlackChannel(@PathVariable Long id) {
        log.debug("REST request to get SlackChannel : {}", id);
        Optional<SlackChannel> slackChannel = slackChannelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(slackChannel);
    }

    /**
     * DELETE  /slack-channels/:id : delete the "id" slackChannel.
     *
     * @param id the id of the slackChannel to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/slack-channels/{id}")
    @Timed
    public ResponseEntity<Void> deleteSlackChannel(@PathVariable Long id) {
        log.debug("REST request to delete SlackChannel : {}", id);
        slackChannelService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
