package insat.company.platform.web.rest;

import com.codahale.metrics.annotation.Timed;
import insat.company.platform.config.ApplicationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.json.JsonObject;


/**
 * Controller for view and managing Log Level at runtime.
 */
@RestController
@RequestMapping("/api")
public class ConfigRessource {

    private final Logger log = LoggerFactory.getLogger(FieldResource.class);

    private final ApplicationProperties applicationProperties;

    public ConfigRessource(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @GetMapping("/config")
    @Timed
    public ResponseEntity<JsonObject> getSlackClientId() {
       log.info("REST Request to get slack client id");
        JsonObject result = javax.json.Json.createObjectBuilder()
            .add("slackClientId", applicationProperties.getSlackClientId())
            .build();
        return ResponseEntity.ok().body(result);
    }

}
