package insat.company.platform.service.impl;

import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.methods.SlackApiException;
import com.github.seratch.jslack.api.methods.request.oauth.OAuthAccessRequest;
import com.github.seratch.jslack.api.methods.request.team.TeamInfoRequest;
import com.github.seratch.jslack.api.methods.response.oauth.OAuthAccessResponse;
import com.github.seratch.jslack.api.methods.response.team.TeamInfoResponse;
import insat.company.platform.config.ApplicationProperties;
import insat.company.platform.domain.Integration;
import insat.company.platform.service.SlackApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

/**
 * Service Implementation for managing Integration.
 */
@Service
@Transactional
public class SlackApiServiceImpl implements SlackApiService {

    private final Logger log = LoggerFactory.getLogger(SlackApiServiceImpl.class);

    private final ApplicationProperties applicationProperties;

    public SlackApiServiceImpl(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Override
    public Integration authenticate(String code) {
        Integration integration = null;
        OAuthAccessRequest oAuthAccessRequest = OAuthAccessRequest.builder()
            .clientId(this.applicationProperties.getSlackClientId())
            .clientSecret(this.applicationProperties.getSlackClientSecret())
            .redirectUri(this.applicationProperties.getSlackRedirectUri())
            .code(code).build();
        try {
            OAuthAccessResponse oAuthAccessResponse = getSlackApiClient().methods().oauthAccess(oAuthAccessRequest);
            if (oAuthAccessResponse.isOk()) {
                log.info("Parsing integration info received from slack");

                integration = getIntegration(oAuthAccessResponse);
            } else {
                log.info("An error occurred while receiving an integration from slack {}", oAuthAccessResponse.getError());
            }
        } catch (IOException | SlackApiException e) {
            log.warn("An error occurred while obtaining oauth token", e);
        }
        return integration;
    }

    protected Slack getSlackApiClient() {
        return Slack.getInstance();
    }

    private Integration getIntegration(OAuthAccessResponse oAuthAccessResponse) {
        Integration integration = new Integration();
        integration.setAccessToken(oAuthAccessResponse.getAccessToken());
        integration.setScope(oAuthAccessResponse.getScope());
        integration.setTeamId(oAuthAccessResponse.getTeamId());
        integration.setTeamName(oAuthAccessResponse.getTeamName());
        integration.setTeamUrl(getTeamUrl(oAuthAccessResponse));
        return integration;
    }

    private String getTeamUrl(OAuthAccessResponse oAuthAccessResponse) {
        try {
            log.info("Fetching the team info for team: {}", oAuthAccessResponse.getTeamName());
            TeamInfoRequest teamInfoRequest = TeamInfoRequest.builder()
                .token(oAuthAccessResponse.getAccessToken())
                .build();
            TeamInfoResponse teamInfoResponse = getSlackApiClient().methods().teamInfo(teamInfoRequest);
            if (!teamInfoResponse.isOk()) {
                log.warn("An error occurred while obtaining the team information {}", teamInfoResponse.getError());
            }
            return teamInfoResponse.getTeam().getDomain();
        } catch (IOException | SlackApiException e) {
            log.warn("An error occurred while obtaining the team info", e);
            return null;
        }
    }
}
