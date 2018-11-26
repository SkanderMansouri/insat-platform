package insat.company.platform.web.rest;

import insat.company.platform.InsatApp;

import insat.company.platform.domain.Integration;
import insat.company.platform.domain.User;
import insat.company.platform.repository.IntegrationRepository;
import insat.company.platform.repository.UserRepository;
import insat.company.platform.repository.search.IntegrationSearchRepository;
import insat.company.platform.service.IntegrationService;
import insat.company.platform.service.dto.IntegrationDTO;
import insat.company.platform.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static insat.company.platform.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the IntegrationResource REST controller.
 *
 * @see IntegrationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InsatApp.class)
public class IntegrationResourceIntTest {

    private static final String DEFAULT_ACCESS_TOKEN = "";
    private static final String UPDATED_ACCESS_TOKEN = "BBBBBBBBBB";

    private static final String DEFAULT_TEAM_ID = "teamid";
    private static final String DEFAULT_TEAM_ID_NOT_FOUND_USER = "teamid";
    private static final String UPDATED_TEAM_ID = "BBBBBBBBBB";

    private static final String DEFAULT_SCOPE = "scope";
    private static final String DEFAULT_SCOPE_NOT_FOUND_USER = "scope";
    private static final String UPDATED_SCOPE = "BBBBBBBBBB";

    private static final String DEFAULT_TEAM_NAME = "our team";
    private static final String DEFAULT_TEAM_NAME_NOT_FOUND_USER = "our team";
    private static final String UPDATED_TEAM_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TEAM_URL = "teamurl";
    private static final String DEFAULT_TEAM_URL_NOT_FOUND_USER = "teamurl";
    private static final String UPDATED_TEAM_URL = "BBBBBBBBBB";

    private static final long DEFAULT_USER_ID = 4L;
    private static final long DEFAULT_NOT_FOUND_ID_USER = -1L;
    private static final long UPDATED_USER_ID = 4L;

    private final Logger log = LoggerFactory.getLogger(IntegrationResource.class);

    private static User user;

    @Autowired
    private IntegrationRepository integrationRepository;

    @Autowired
    private UserRepository userRepository ;

    @Autowired
    private IntegrationService integrationService;

    /**
     * This repository is mocked in the insat.company.platform.repository.search test package.
     *
     * @see insat.company.platform.repository.search.IntegrationSearchRepositoryMockConfiguration
     */
    @Autowired
    private IntegrationSearchRepository mockIntegrationSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restIntegrationMockMvc;

    private Integration integration;

    private IntegrationDTO integrationDTO;
    private IntegrationDTO integrationDTO_userIdNotFound;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final IntegrationResource integrationResource = new IntegrationResource(integrationService, userRepository);
        this.restIntegrationMockMvc = MockMvcBuilders.standaloneSetup(integrationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Integration createEntity(EntityManager em) {
        Integration integration = new Integration();
        return integration;
    }

    public static IntegrationDTO createDTOEntity(EntityManager em, Long userId) {
        IntegrationDTO integrationDTO = new IntegrationDTO();
        integrationDTO.setScope(DEFAULT_SCOPE);
        integrationDTO.setTeamId(DEFAULT_TEAM_ID);
        integrationDTO.setUserId(userId);
        integrationDTO.setTeamName(DEFAULT_TEAM_NAME);
        integrationDTO.setTeamUrl(DEFAULT_TEAM_URL);
        return integrationDTO;
    }


    @Before
    public void initTest() {
        // getting user from userRepository using provided id
        if (userRepository.findOneWithAuthoritiesById(DEFAULT_USER_ID).isPresent())
            user = userRepository.findOneWithAuthoritiesById(DEFAULT_USER_ID).get();
        else
            user = null;

        integration = createEntity(em);
        integrationDTO = createDTOEntity(em, DEFAULT_USER_ID);
        integrationDTO_userIdNotFound = createDTOEntity(em, DEFAULT_NOT_FOUND_ID_USER);
    }

    @Test
    @Transactional
    public void createIntegration() throws Exception {
        int databaseSizeBeforeCreate = integrationRepository.findAll().size();
        // Create the Integration
        try {
            restIntegrationMockMvc.perform(post("/api/integrations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(integrationDTO)))
                .andExpect(status().isCreated());
        } catch (Exception ex) {
            log.debug(ex.getMessage());
        }
        // Validate the Integration in the database
        List<Integration> integrationList = integrationRepository.findAll();
        assertThat(integrationList).hasSize(databaseSizeBeforeCreate + 1);
        Integration testIntegration = integrationList.get(integrationList.size() - 1);
        assertThat(testIntegration.getTeamId()).isEqualTo(DEFAULT_TEAM_ID);
        assertThat(testIntegration.getScope()).isEqualTo(DEFAULT_SCOPE);
        assertThat(testIntegration.getTeamName()).isEqualTo(DEFAULT_TEAM_NAME);
        assertThat(testIntegration.getTeamUrl()).isEqualTo(DEFAULT_TEAM_URL);
        assertThat(testIntegration.getUser().getId()).isEqualTo(DEFAULT_USER_ID);

        // Validate the Integration in Elasticsearch
        verify(mockIntegrationSearchRepository, times(1)).save(testIntegration);
    }

    @Test
    @Transactional
    public void createIntegrationForNotExistingUser() throws Exception {
        int databaseSizeBeforeCreate = integrationRepository.findAll().size();
        // Create the Integration
        try {
            restIntegrationMockMvc.perform(post("/api/integrations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(integrationDTO_userIdNotFound)))
                .andExpect(status().isBadRequest());
        } catch (Exception ex) {
            log.debug(ex.getMessage());
        }
        // Validate the Integration in the database
        List<Integration> integrationList = integrationRepository.findAll();
        assertThat(integrationList).hasSize(databaseSizeBeforeCreate);
    }

/*
    @Test
    @Transactional
    public void createIntegrationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = integrationRepository.findAll().size();

        // Create the Integration with an existing ID
        integration.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIntegrationMockMvc.perform(post("/api/integrations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(integration)))
            .andExpect(status().isBadRequest());

        // Validate the Integration in the database
        List<Integration> integrationList = integrationRepository.findAll();
        assertThat(integrationList).hasSize(databaseSizeBeforeCreate);

        // Validate the Integration in Elasticsearch
        verify(mockIntegrationSearchRepository, times(0)).save(integration);
    }

    @Test
    @Transactional
    public void getAllIntegrations() throws Exception {
        // Initialize the database
        integrationRepository.saveAndFlush(integration);

        // Get all the integrationList
        restIntegrationMockMvc.perform(get("/api/integrations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(integration.getId().intValue())))
            .andExpect(jsonPath("$.[*].accessToken").value(hasItem(DEFAULT_ACCESS_TOKEN.toString())))
            .andExpect(jsonPath("$.[*].teamId").value(hasItem(DEFAULT_TEAM_ID.toString())))
            .andExpect(jsonPath("$.[*].scope").value(hasItem(DEFAULT_SCOPE.toString())))
            .andExpect(jsonPath("$.[*].teamName").value(hasItem(DEFAULT_TEAM_NAME.toString())))
            .andExpect(jsonPath("$.[*].teamUrl").value(hasItem(DEFAULT_TEAM_URL.toString())));
    }

    @Test
    @Transactional
    public void getIntegration() throws Exception {
        // Initialize the database
        integrationRepository.saveAndFlush(integration);

        // Get the integration
        restIntegrationMockMvc.perform(get("/api/integrations/{id}", integration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(integration.getId().intValue()))
            .andExpect(jsonPath("$.accessToken").value(DEFAULT_ACCESS_TOKEN.toString()))
            .andExpect(jsonPath("$.teamId").value(DEFAULT_TEAM_ID.toString()))
            .andExpect(jsonPath("$.scope").value(DEFAULT_SCOPE.toString()))
            .andExpect(jsonPath("$.teamName").value(DEFAULT_TEAM_NAME.toString()))
            .andExpect(jsonPath("$.teamUrl").value(DEFAULT_TEAM_URL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingIntegration() throws Exception {
        // Get the integration
        restIntegrationMockMvc.perform(get("/api/integrations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIntegration() throws Exception {
        // Initialize the database
        integrationService.save(integration);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockIntegrationSearchRepository);

        int databaseSizeBeforeUpdate = integrationRepository.findAll().size();

        // Update the integration
        Integration updatedIntegration = integrationRepository.findById(integration.getId()).get();
        // Disconnect from session so that the updates on updatedIntegration are not directly saved in db
        em.detach(updatedIntegration);
        updatedIntegration
            .accessToken(UPDATED_ACCESS_TOKEN)
            .teamId(UPDATED_TEAM_ID)
            .scope(UPDATED_SCOPE)
            .teamName(UPDATED_TEAM_NAME)
            .teamUrl(UPDATED_TEAM_URL);

        restIntegrationMockMvc.perform(put("/api/integrations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedIntegration)))
            .andExpect(status().isOk());

        // Validate the Integration in the database
        List<Integration> integrationList = integrationRepository.findAll();
        assertThat(integrationList).hasSize(databaseSizeBeforeUpdate);
        Integration testIntegration = integrationList.get(integrationList.size() - 1);
        assertThat(testIntegration.getAccessToken()).isEqualTo(UPDATED_ACCESS_TOKEN);
        assertThat(testIntegration.getTeamId()).isEqualTo(UPDATED_TEAM_ID);
        assertThat(testIntegration.getScope()).isEqualTo(UPDATED_SCOPE);
        assertThat(testIntegration.getTeamName()).isEqualTo(UPDATED_TEAM_NAME);
        assertThat(testIntegration.getTeamUrl()).isEqualTo(UPDATED_TEAM_URL);

        // Validate the Integration in Elasticsearch
        verify(mockIntegrationSearchRepository, times(1)).save(testIntegration);
    }

    @Test
    @Transactional
    public void updateNonExistingIntegration() throws Exception {
        int databaseSizeBeforeUpdate = integrationRepository.findAll().size();

        // Create the Integration

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIntegrationMockMvc.perform(put("/api/integrations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(integration)))
            .andExpect(status().isBadRequest());

        // Validate the Integration in the database
        List<Integration> integrationList = integrationRepository.findAll();
        assertThat(integrationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Integration in Elasticsearch
        verify(mockIntegrationSearchRepository, times(0)).save(integration);
    }

    @Test
    @Transactional
    public void deleteIntegration() throws Exception {
        // Initialize the database
        integrationService.save(integration);

        int databaseSizeBeforeDelete = integrationRepository.findAll().size();

        // Get the integration
        restIntegrationMockMvc.perform(delete("/api/integrations/{id}", integration.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Integration> integrationList = integrationRepository.findAll();
        assertThat(integrationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Integration in Elasticsearch
        verify(mockIntegrationSearchRepository, times(1)).deleteById(integration.getId());
    }

    @Test
    @Transactional
    public void searchIntegration() throws Exception {
        // Initialize the database
        integrationService.save(integration);
        when(mockIntegrationSearchRepository.search(queryStringQuery("id:" + integration.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(integration), PageRequest.of(0, 1), 1));
        // Search the integration
        restIntegrationMockMvc.perform(get("/api/_search/integrations?query=id:" + integration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(integration.getId().intValue())))
            .andExpect(jsonPath("$.[*].accessToken").value(hasItem(DEFAULT_ACCESS_TOKEN)))
            .andExpect(jsonPath("$.[*].teamId").value(hasItem(DEFAULT_TEAM_ID)))
            .andExpect(jsonPath("$.[*].scope").value(hasItem(DEFAULT_SCOPE)))
            .andExpect(jsonPath("$.[*].teamName").value(hasItem(DEFAULT_TEAM_NAME)))
            .andExpect(jsonPath("$.[*].teamUrl").value(hasItem(DEFAULT_TEAM_URL)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Integration.class);
        Integration integration1 = new Integration();
        integration1.setId(1L);
        Integration integration2 = new Integration();
        integration2.setId(integration1.getId());
        assertThat(integration1).isEqualTo(integration2);
        integration2.setId(2L);
        assertThat(integration1).isNotEqualTo(integration2);
        integration1.setId(null);
        assertThat(integration1).isNotEqualTo(integration2);
    }
*/
}
