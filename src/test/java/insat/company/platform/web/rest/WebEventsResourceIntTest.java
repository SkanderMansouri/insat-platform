package insat.company.platform.web.rest;

import insat.company.platform.InsatApp;
import insat.company.platform.domain.Integration;
import insat.company.platform.repository.IntegrationRepository;
import insat.company.platform.service.IntegrationService;
import insat.company.platform.service.SlackApiService;
import insat.company.platform.web.rest.errors.ExceptionTranslator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static insat.company.platform.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = InsatApp.class)
public class WebEventsResourceIntTest {

    private WebEventsResource webEventsResource;

    @Autowired
    private IntegrationRepository integrationRepository;

    @Autowired
    private IntegrationService integrationService;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restWebEventMvc;


    @Mock
    private SlackApiService slackApiService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.webEventsResource = new WebEventsResource(integrationService,slackApiService);

        this.restWebEventMvc = MockMvcBuilders.standaloneSetup(webEventsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

    }

    @Test
    @WithMockUser
    public void shouldReturnOkAndSaveIntegrationInDBWhenSlackOauthIsSuccessful() throws Exception {

        int integrationsBeforeSlackOauth = integrationRepository.findAll().size();

        String code = "blabla";

        Integration integration = new Integration();
        integration.setAccessToken("asasda");
        integration.setTeamId("T123");
        integration.setTeamName("PayItForward");

        when(slackApiService.authenticate(code)).thenReturn(integration);

        restWebEventMvc.perform(get("/api/slack/oauth?code=blabla&state=")
            .contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        List<Integration> integrationsAfterSlackOauth = integrationRepository.findAll();
        Optional<Integration> addedIntegration = Optional.ofNullable(integrationRepository.findOneByTeamId("T123"));

        assertThat(integrationsAfterSlackOauth.size()).isEqualTo(integrationsBeforeSlackOauth+1);
        assertThat(addedIntegration.isPresent());
        assertThat(addedIntegration.get().getAccessToken()).isEqualTo("asasda");
        assertThat(addedIntegration.get().getTeamName()).isEqualTo("PayItForward");

        verify(slackApiService,times(1)).authenticate(eq(code));
    }

    @Test
    public void shouldReturnBadRequestWhenUserIsNotAuthenticated() throws Exception {
        restWebEventMvc.perform(get("/api/slack/oauth?code=blabla&state=")
            .contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isUnauthorized());
    }
}
