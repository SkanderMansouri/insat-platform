package com.pif.insatplatform.web.rest;

import com.pif.insatplatform.InsatplatformApp;

import com.pif.insatplatform.domain.Integration;
import com.pif.insatplatform.repository.IntegrationRepository;
import com.pif.insatplatform.service.IntegrationService;
import com.pif.insatplatform.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;


import static com.pif.insatplatform.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the IntegrationResource REST controller.
 *
 * @see IntegrationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InsatplatformApp.class)
public class IntegrationResourceIntTest {

    private static final String DEFAULT_ACCESS_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_ACCESS_TOKEN = "BBBBBBBBBB";

    private static final String DEFAULT_SCOPE = "AAAAAAAAAA";
    private static final String UPDATED_SCOPE = "BBBBBBBBBB";

    private static final String DEFAULT_TEAM_ID = "AAAAAAAAAA";
    private static final String UPDATED_TEAM_ID = "BBBBBBBBBB";

    private static final String DEFAULT_TEAM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TEAM_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BOT_ID = "AAAAAAAAAA";
    private static final String UPDATED_BOT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_BOT_ACCESS_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_BOT_ACCESS_TOKEN = "BBBBBBBBBB";

    private static final Integer DEFAULT_USER_ID = 1;
    private static final Integer UPDATED_USER_ID = 2;

    private static final String DEFAULT_TEAM_URL = "AAAAAAAAAA";
    private static final String UPDATED_TEAM_URL = "BBBBBBBBBB";

    @Autowired
    private IntegrationRepository integrationRepository;
    
    @Autowired
    private IntegrationService integrationService;

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

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final IntegrationResource integrationResource = new IntegrationResource(integrationService);
        this.restIntegrationMockMvc = MockMvcBuilders.standaloneSetup(integrationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Integration createEntity(EntityManager em) {
        Integration integration = new Integration()
            .accessToken(DEFAULT_ACCESS_TOKEN)
            .scope(DEFAULT_SCOPE)
            .teamId(DEFAULT_TEAM_ID)
            .teamName(DEFAULT_TEAM_NAME)
            .botId(DEFAULT_BOT_ID)
            .botAccessToken(DEFAULT_BOT_ACCESS_TOKEN)
            .userId(DEFAULT_USER_ID)
            .teamUrl(DEFAULT_TEAM_URL);
        return integration;
    }

    @Before
    public void initTest() {
        integration = createEntity(em);
    }

    @Test
    @Transactional
    public void createIntegration() throws Exception {
        int databaseSizeBeforeCreate = integrationRepository.findAll().size();

        // Create the Integration
        restIntegrationMockMvc.perform(post("/api/integrations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(integration)))
            .andExpect(status().isCreated());

        // Validate the Integration in the database
        List<Integration> integrationList = integrationRepository.findAll();
        assertThat(integrationList).hasSize(databaseSizeBeforeCreate + 1);
        Integration testIntegration = integrationList.get(integrationList.size() - 1);
        assertThat(testIntegration.getAccessToken()).isEqualTo(DEFAULT_ACCESS_TOKEN);
        assertThat(testIntegration.getScope()).isEqualTo(DEFAULT_SCOPE);
        assertThat(testIntegration.getTeamId()).isEqualTo(DEFAULT_TEAM_ID);
        assertThat(testIntegration.getTeamName()).isEqualTo(DEFAULT_TEAM_NAME);
        assertThat(testIntegration.getBotId()).isEqualTo(DEFAULT_BOT_ID);
        assertThat(testIntegration.getBotAccessToken()).isEqualTo(DEFAULT_BOT_ACCESS_TOKEN);
        assertThat(testIntegration.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testIntegration.getTeamUrl()).isEqualTo(DEFAULT_TEAM_URL);
    }

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
            .andExpect(jsonPath("$.[*].scope").value(hasItem(DEFAULT_SCOPE.toString())))
            .andExpect(jsonPath("$.[*].teamId").value(hasItem(DEFAULT_TEAM_ID.toString())))
            .andExpect(jsonPath("$.[*].teamName").value(hasItem(DEFAULT_TEAM_NAME.toString())))
            .andExpect(jsonPath("$.[*].botId").value(hasItem(DEFAULT_BOT_ID.toString())))
            .andExpect(jsonPath("$.[*].botAccessToken").value(hasItem(DEFAULT_BOT_ACCESS_TOKEN.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
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
            .andExpect(jsonPath("$.scope").value(DEFAULT_SCOPE.toString()))
            .andExpect(jsonPath("$.teamId").value(DEFAULT_TEAM_ID.toString()))
            .andExpect(jsonPath("$.teamName").value(DEFAULT_TEAM_NAME.toString()))
            .andExpect(jsonPath("$.botId").value(DEFAULT_BOT_ID.toString()))
            .andExpect(jsonPath("$.botAccessToken").value(DEFAULT_BOT_ACCESS_TOKEN.toString()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID))
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

        int databaseSizeBeforeUpdate = integrationRepository.findAll().size();

        // Update the integration
        Integration updatedIntegration = integrationRepository.findById(integration.getId()).get();
        // Disconnect from session so that the updates on updatedIntegration are not directly saved in db
        em.detach(updatedIntegration);
        updatedIntegration
            .accessToken(UPDATED_ACCESS_TOKEN)
            .scope(UPDATED_SCOPE)
            .teamId(UPDATED_TEAM_ID)
            .teamName(UPDATED_TEAM_NAME)
            .botId(UPDATED_BOT_ID)
            .botAccessToken(UPDATED_BOT_ACCESS_TOKEN)
            .userId(UPDATED_USER_ID)
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
        assertThat(testIntegration.getScope()).isEqualTo(UPDATED_SCOPE);
        assertThat(testIntegration.getTeamId()).isEqualTo(UPDATED_TEAM_ID);
        assertThat(testIntegration.getTeamName()).isEqualTo(UPDATED_TEAM_NAME);
        assertThat(testIntegration.getBotId()).isEqualTo(UPDATED_BOT_ID);
        assertThat(testIntegration.getBotAccessToken()).isEqualTo(UPDATED_BOT_ACCESS_TOKEN);
        assertThat(testIntegration.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testIntegration.getTeamUrl()).isEqualTo(UPDATED_TEAM_URL);
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
}
