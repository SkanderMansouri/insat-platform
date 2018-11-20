package insat.company.platform.web.rest;

import insat.company.platform.InsatApp;

import insat.company.platform.domain.SlackUser;
import insat.company.platform.repository.SlackUserRepository;
import insat.company.platform.repository.search.SlackUserSearchRepository;
import insat.company.platform.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
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
 * Test class for the SlackUserResource REST controller.
 *
 * @see SlackUserResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InsatApp.class)
public class SlackUserResourceIntTest {

    private static final String DEFAULT_TEAM_ID = "AAAAAAAAAA";
    private static final String UPDATED_TEAM_ID = "BBBBBBBBBB";

    private static final String DEFAULT_SLACK_ID = "AAAAAAAAAA";
    private static final String UPDATED_SLACK_ID = "BBBBBBBBBB";

    private static final String DEFAULT_USER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_BOT = false;
    private static final Boolean UPDATED_IS_BOT = true;

    @Autowired
    private SlackUserRepository slackUserRepository;

    /**
     * This repository is mocked in the insat.company.platform.repository.search test package.
     *
     * @see insat.company.platform.repository.search.SlackUserSearchRepositoryMockConfiguration
     */
    @Autowired
    private SlackUserSearchRepository mockSlackUserSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSlackUserMockMvc;

    private SlackUser slackUser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SlackUserResource slackUserResource = new SlackUserResource(slackUserRepository, mockSlackUserSearchRepository);
        this.restSlackUserMockMvc = MockMvcBuilders.standaloneSetup(slackUserResource)
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
    public static SlackUser createEntity(EntityManager em) {
        SlackUser slackUser = new SlackUser()
            .teamId(DEFAULT_TEAM_ID)
            .slackId(DEFAULT_SLACK_ID)
            .userName(DEFAULT_USER_NAME)
            .email(DEFAULT_EMAIL)
            .isBot(DEFAULT_IS_BOT);
        return slackUser;
    }

    @Before
    public void initTest() {
        slackUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createSlackUser() throws Exception {
        int databaseSizeBeforeCreate = slackUserRepository.findAll().size();

        // Create the SlackUser
        restSlackUserMockMvc.perform(post("/api/slack-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(slackUser)))
            .andExpect(status().isCreated());

        // Validate the SlackUser in the database
        List<SlackUser> slackUserList = slackUserRepository.findAll();
        assertThat(slackUserList).hasSize(databaseSizeBeforeCreate + 1);
        SlackUser testSlackUser = slackUserList.get(slackUserList.size() - 1);
        assertThat(testSlackUser.getTeamId()).isEqualTo(DEFAULT_TEAM_ID);
        assertThat(testSlackUser.getSlackId()).isEqualTo(DEFAULT_SLACK_ID);
        assertThat(testSlackUser.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testSlackUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testSlackUser.isIsBot()).isEqualTo(DEFAULT_IS_BOT);

        // Validate the SlackUser in Elasticsearch
        verify(mockSlackUserSearchRepository, times(1)).save(testSlackUser);
    }

    @Test
    @Transactional
    public void createSlackUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = slackUserRepository.findAll().size();

        // Create the SlackUser with an existing ID
        slackUser.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSlackUserMockMvc.perform(post("/api/slack-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(slackUser)))
            .andExpect(status().isBadRequest());

        // Validate the SlackUser in the database
        List<SlackUser> slackUserList = slackUserRepository.findAll();
        assertThat(slackUserList).hasSize(databaseSizeBeforeCreate);

        // Validate the SlackUser in Elasticsearch
        verify(mockSlackUserSearchRepository, times(0)).save(slackUser);
    }

    @Test
    @Transactional
    public void getAllSlackUsers() throws Exception {
        // Initialize the database
        slackUserRepository.saveAndFlush(slackUser);

        // Get all the slackUserList
        restSlackUserMockMvc.perform(get("/api/slack-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(slackUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].teamId").value(hasItem(DEFAULT_TEAM_ID.toString())))
            .andExpect(jsonPath("$.[*].slackId").value(hasItem(DEFAULT_SLACK_ID.toString())))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].isBot").value(hasItem(DEFAULT_IS_BOT.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getSlackUser() throws Exception {
        // Initialize the database
        slackUserRepository.saveAndFlush(slackUser);

        // Get the slackUser
        restSlackUserMockMvc.perform(get("/api/slack-users/{id}", slackUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(slackUser.getId().intValue()))
            .andExpect(jsonPath("$.teamId").value(DEFAULT_TEAM_ID.toString()))
            .andExpect(jsonPath("$.slackId").value(DEFAULT_SLACK_ID.toString()))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.isBot").value(DEFAULT_IS_BOT.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSlackUser() throws Exception {
        // Get the slackUser
        restSlackUserMockMvc.perform(get("/api/slack-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSlackUser() throws Exception {
        // Initialize the database
        slackUserRepository.saveAndFlush(slackUser);

        int databaseSizeBeforeUpdate = slackUserRepository.findAll().size();

        // Update the slackUser
        SlackUser updatedSlackUser = slackUserRepository.findById(slackUser.getId()).get();
        // Disconnect from session so that the updates on updatedSlackUser are not directly saved in db
        em.detach(updatedSlackUser);
        updatedSlackUser
            .teamId(UPDATED_TEAM_ID)
            .slackId(UPDATED_SLACK_ID)
            .userName(UPDATED_USER_NAME)
            .email(UPDATED_EMAIL)
            .isBot(UPDATED_IS_BOT);

        restSlackUserMockMvc.perform(put("/api/slack-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSlackUser)))
            .andExpect(status().isOk());

        // Validate the SlackUser in the database
        List<SlackUser> slackUserList = slackUserRepository.findAll();
        assertThat(slackUserList).hasSize(databaseSizeBeforeUpdate);
        SlackUser testSlackUser = slackUserList.get(slackUserList.size() - 1);
        assertThat(testSlackUser.getTeamId()).isEqualTo(UPDATED_TEAM_ID);
        assertThat(testSlackUser.getSlackId()).isEqualTo(UPDATED_SLACK_ID);
        assertThat(testSlackUser.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testSlackUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testSlackUser.isIsBot()).isEqualTo(UPDATED_IS_BOT);

        // Validate the SlackUser in Elasticsearch
        verify(mockSlackUserSearchRepository, times(1)).save(testSlackUser);
    }

    @Test
    @Transactional
    public void updateNonExistingSlackUser() throws Exception {
        int databaseSizeBeforeUpdate = slackUserRepository.findAll().size();

        // Create the SlackUser

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSlackUserMockMvc.perform(put("/api/slack-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(slackUser)))
            .andExpect(status().isBadRequest());

        // Validate the SlackUser in the database
        List<SlackUser> slackUserList = slackUserRepository.findAll();
        assertThat(slackUserList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SlackUser in Elasticsearch
        verify(mockSlackUserSearchRepository, times(0)).save(slackUser);
    }

    @Test
    @Transactional
    public void deleteSlackUser() throws Exception {
        // Initialize the database
        slackUserRepository.saveAndFlush(slackUser);

        int databaseSizeBeforeDelete = slackUserRepository.findAll().size();

        // Get the slackUser
        restSlackUserMockMvc.perform(delete("/api/slack-users/{id}", slackUser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SlackUser> slackUserList = slackUserRepository.findAll();
        assertThat(slackUserList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SlackUser in Elasticsearch
        verify(mockSlackUserSearchRepository, times(1)).deleteById(slackUser.getId());
    }

    @Test
    @Transactional
    public void searchSlackUser() throws Exception {
        // Initialize the database
        slackUserRepository.saveAndFlush(slackUser);
        when(mockSlackUserSearchRepository.search(queryStringQuery("id:" + slackUser.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(slackUser), PageRequest.of(0, 1), 1));
        // Search the slackUser
        restSlackUserMockMvc.perform(get("/api/_search/slack-users?query=id:" + slackUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(slackUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].teamId").value(hasItem(DEFAULT_TEAM_ID)))
            .andExpect(jsonPath("$.[*].slackId").value(hasItem(DEFAULT_SLACK_ID)))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].isBot").value(hasItem(DEFAULT_IS_BOT.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SlackUser.class);
        SlackUser slackUser1 = new SlackUser();
        slackUser1.setId(1L);
        SlackUser slackUser2 = new SlackUser();
        slackUser2.setId(slackUser1.getId());
        assertThat(slackUser1).isEqualTo(slackUser2);
        slackUser2.setId(2L);
        assertThat(slackUser1).isNotEqualTo(slackUser2);
        slackUser1.setId(null);
        assertThat(slackUser1).isNotEqualTo(slackUser2);
    }
}
