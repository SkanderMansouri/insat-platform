package insat.company.platform.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Insat.
 * <p>
 * Properties are configured in the application.yml file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private String slackClientId;
    private String slackClientSecret;
    private String slackRedirectUri;

    public String getSlackClientId() {
        return slackClientId;
    }

    public void setSlackClientId(String slackClientId) {
        this.slackClientId = slackClientId;
    }

    public String getSlackClientSecret() {
        return slackClientSecret;
    }

    public void setSlackClientSecret(String slackClientSecret) {
        this.slackClientSecret = slackClientSecret;
    }

    public String getSlackRedirectUri() {
        return slackRedirectUri;
    }

    public void setSlackRedirectUri(String slackRedirectUri) {
        this.slackRedirectUri = slackRedirectUri;
    }
}
