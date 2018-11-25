package insat.company.platform.web.rest;

import com.codahale.metrics.annotation.Timed;
import insat.company.platform.domain.Integration;
import insat.company.platform.security.SecurityUtils;
import insat.company.platform.service.IntegrationService;
import insat.company.platform.service.SlackApiService;
import insat.company.platform.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * REST controller for managing Integration.
 */
@RestController
@RequestMapping("/api")
public class WebEventsResource {

    private final Logger log = LoggerFactory.getLogger(WebEventsResource.class);

    private final IntegrationService integrationService;
    private final SlackApiService slackApiService;

    public WebEventsResource(IntegrationService integrationService, SlackApiService slackApiService, UserService userService) {
        this.integrationService = integrationService;
        this.slackApiService = slackApiService;
    }

    @GetMapping("/slack/oauth")
    @Timed
    public ResponseEntity<Integration> receiveSlackOauth(@RequestParam("code") String code, @RequestParam("state") String state) {
        log.info("receiving a slack authentication");

        if(!SecurityUtils.isAuthenticated()){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if(state.equalsIgnoreCase("error")){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return Optional.ofNullable(code).map(codeParam -> {
            Integration authenticateIntegration = slackApiService.authenticate(codeParam);
            Integration integrationToUpdateOrSave = Optional.ofNullable(integrationService.findOneByTeamId(authenticateIntegration.getTeamId()))
                .map(integrationMapped -> {
                    integrationMapped.setAccessToken(authenticateIntegration.getAccessToken());
                    return integrationService.save(integrationMapped);
                }).orElse(authenticateIntegration);;

            integrationService.save(integrationToUpdateOrSave);
            return ResponseEntity.ok().body(authenticateIntegration);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }
}
