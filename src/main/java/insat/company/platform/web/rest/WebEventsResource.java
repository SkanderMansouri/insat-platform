package insat.company.platform.web.rest;

import com.codahale.metrics.annotation.Timed;
import insat.company.platform.domain.Integration;
import insat.company.platform.domain.SlackChannel;
import insat.company.platform.security.SecurityUtils;
import insat.company.platform.service.ChannelCreatedService;
import insat.company.platform.service.IntegrationService;
import insat.company.platform.service.SlackApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;
import java.util.Optional;

/**
 * REST controller for managing Integration.
 */
@RestController
@RequestMapping("/api")
public class WebEventsResource {

    private final Logger log = LoggerFactory.getLogger(WebEventsResource.class);
    private final String URL_VERIFICATION = "url_verification";
    private final String CHANNEL_CREATED = "channel_created";
    private final ChannelCreatedService channelCreatedService;

    private final IntegrationService integrationService;
    private final SlackApiService slackApiService;

    public WebEventsResource(IntegrationService integrationService, SlackApiService slackApiService, ChannelCreatedService channelCreatedService) {
        this.integrationService = integrationService;
        this.slackApiService = slackApiService;
        this.channelCreatedService=channelCreatedService;
    }

    @GetMapping("/slack/oauth")
    @Timed
    public ResponseEntity<Integration> receiveSlackOauth(@RequestParam("code") String code, @RequestParam("state") String state) {
        log.info("receiving a slack authentication");

        if (!SecurityUtils.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (state.equalsIgnoreCase("error")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return Optional.ofNullable(code).map(codeParam -> {
            Integration authenticateIntegration = slackApiService.authenticate(codeParam);
            Integration integrationToUpdateOrSave = Optional.ofNullable(integrationService.findOneByTeamId(authenticateIntegration.getTeamId()))
                .map(integrationMapped -> {
                    integrationMapped.setAccessToken(authenticateIntegration.getAccessToken());
                    return integrationService.save(integrationMapped);
                }).orElse(authenticateIntegration);
            ;

            integrationService.save(integrationToUpdateOrSave);
            return ResponseEntity.ok().body(authenticateIntegration);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

        @PostMapping(value = "/events")
        public ResponseEntity<?> eventsTest (@RequestBody String chaine){

            log.info("Receiving notification from slack");
            JsonReader jsonReader = Json.createReader(new StringReader(chaine));
            JsonObject object = jsonReader.readObject();
            String team_id = object.getString("team_id");
            JsonObject event = object.getJsonObject("event");
            String type = event.getString("type");
            log.info("Receiving notification from slack ", type);


            if (type.equalsIgnoreCase(URL_VERIFICATION)) {
                String challenge = object.getString("challenge");
                return new ResponseEntity<>(challenge, HttpStatus.OK);
            }

                if (type.equalsIgnoreCase(CHANNEL_CREATED)) {
                    JsonObject channelJson = event.getJsonObject("channel");
                    SlackChannel channel = channelCreatedService.AddChannel(channelJson, team_id);
                    jsonReader.close();
                    if (channel == null) {
                        log.info("Problem adding the channel into Database");
                        return ResponseEntity.badRequest().build();
                    } else {
                        log.info("Channel saved successfully");
                        return ResponseEntity.ok().build();
                    }
                }
                return ResponseEntity.notFound().build();
            }
        }

