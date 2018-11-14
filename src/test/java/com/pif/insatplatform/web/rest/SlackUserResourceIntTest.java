package com.pif.insatplatform.web.rest;

import com.pif.insatplatform.InsatplatformApp;

import com.pif.insatplatform.domain.SlackUser;
import com.pif.insatplatform.repository.SlackUserRepository;
import com.pif.insatplatform.service.SlackUserService;
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
 * Test class for the SlackUserResource REST controller.
 *
 * @see SlackUserResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InsatplatformApp.class)
public class SlackUserResourceIntTest {

    private static final Integer DEFAULT_SLACK_ID = 1;
    private static final Integer UPDATED_SLACK_ID = 2;

    private static final String DEFAULT_REAL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_REAL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Integer DEFAULT_TEAM_ID = 1;
    private static final Integer UPDATED_TEAM_ID = 2;

    @Autowired
    private SlackUserRepository slackUserRepository;
    
    @Autowired
    private SlackUserService slackUserService;

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
        final SlackUserResource slackUserResource = new SlackUserResource(slackUserService);
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
            .slackId(DEFAULT_SLACK_ID)
            .realName(DEFAULT_REAL_NAME)
            .email(DEFAULT_EMAIL)
            .teamId(DEFAULT_TEAM_ID);
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
        assertThat(testSlackUser.getSlackId()).isEqualTo(DEFAULT_SLACK_ID);
        assertThat(testSlackUser.getRealName()).isEqualTo(DEFAULT_REAL_NAME);
        assertThat(testSlackUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testSlackUser.getTeamId()).isEqualTo(DEFAULT_TEAM_ID);
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
            .andExpect(jsonPath("$.[*].slackId").value(hasItem(DEFAULT_SLACK_ID)))
            .andExpect(jsonPath("$.[*].realName").value(hasItem(DEFAULT_REAL_NAME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].teamId").value(hasItem(DEFAULT_TEAM_ID)));
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
            .andExpect(jsonPath("$.slackId").value(DEFAULT_SLACK_ID))
            .andExpect(jsonPath("$.realName").value(DEFAULT_REAL_NAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.teamId").value(DEFAULT_TEAM_ID));
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
        slackUserService.save(slackUser);

        int databaseSizeBeforeUpdate = slackUserRepository.findAll().size();

        // Update the slackUser
        SlackUser updatedSlackUser = slackUserRepository.findById(slackUser.getId()).get();
        // Disconnect from session so that the updates on updatedSlackUser are not directly saved in db
        em.detach(updatedSlackUser);
        updatedSlackUser
            .slackId(UPDATED_SLACK_ID)
            .realName(UPDATED_REAL_NAME)
            .email(UPDATED_EMAIL)
            .teamId(UPDATED_TEAM_ID);

        restSlackUserMockMvc.perform(put("/api/slack-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSlackUser)))
            .andExpect(status().isOk());

        // Validate the SlackUser in the database
        List<SlackUser> slackUserList = slackUserRepository.findAll();
        assertThat(slackUserList).hasSize(databaseSizeBeforeUpdate);
        SlackUser testSlackUser = slackUserList.get(slackUserList.size() - 1);
        assertThat(testSlackUser.getSlackId()).isEqualTo(UPDATED_SLACK_ID);
        assertThat(testSlackUser.getRealName()).isEqualTo(UPDATED_REAL_NAME);
        assertThat(testSlackUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testSlackUser.getTeamId()).isEqualTo(UPDATED_TEAM_ID);
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
    }

    @Test
    @Transactional
    public void deleteSlackUser() throws Exception {
        // Initialize the database
        slackUserService.save(slackUser);

        int databaseSizeBeforeDelete = slackUserRepository.findAll().size();

        // Get the slackUser
        restSlackUserMockMvc.perform(delete("/api/slack-users/{id}", slackUser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SlackUser> slackUserList = slackUserRepository.findAll();
        assertThat(slackUserList).hasSize(databaseSizeBeforeDelete - 1);
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
