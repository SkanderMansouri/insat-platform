package insat.company.platform.web.rest;

import insat.company.platform.InsatApp;

import insat.company.platform.domain.SlackChannel;
import insat.company.platform.repository.SlackChannelRepository;
import insat.company.platform.repository.search.SlackChannelSearchRepository;
import insat.company.platform.service.SlackChannelService;
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
 * Test class for the SlackChannelResource REST controller.
 *
 * @see SlackChannelResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InsatApp.class)
public class SlackChannelResourceIntTest {

    private static final String DEFAULT_CHANNEL_ID = "AAAAAAAAAA";
    private static final String UPDATED_CHANNEL_ID = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TEAM_ID = "AAAAAAAAAA";
    private static final String UPDATED_TEAM_ID = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_PRIVATE = false;
    private static final Boolean UPDATED_IS_PRIVATE = true;

    @Autowired
    private SlackChannelRepository slackChannelRepository;

    @Autowired
    private SlackChannelService slackChannelService;

    /**
     * This repository is mocked in the insat.company.platform.repository.search test package.
     *
     * @see insat.company.platform.repository.search.SlackChannelSearchRepositoryMockConfiguration
     */
    @Autowired
    private SlackChannelSearchRepository mockSlackChannelSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSlackChannelMockMvc;

    private SlackChannel slackChannel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SlackChannelResource slackChannelResource = new SlackChannelResource(slackChannelService);
        this.restSlackChannelMockMvc = MockMvcBuilders.standaloneSetup(slackChannelResource)
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
    public static SlackChannel createEntity(EntityManager em) {
        SlackChannel slackChannel = new SlackChannel()
            .channelId(DEFAULT_CHANNEL_ID)
            .name(DEFAULT_NAME)
            .teamId(DEFAULT_TEAM_ID)
            .isPrivate(DEFAULT_IS_PRIVATE);
        return slackChannel;
    }

    @Before
    public void initTest() {
        slackChannel = createEntity(em);
    }

    @Test
    @Transactional
    public void createSlackChannel() throws Exception {
        int databaseSizeBeforeCreate = slackChannelRepository.findAll().size();

        // Create the SlackChannel
        restSlackChannelMockMvc.perform(post("/api/slack-channels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(slackChannel)))
            .andExpect(status().isCreated());

        // Validate the SlackChannel in the database
        List<SlackChannel> slackChannelList = slackChannelRepository.findAll();
        assertThat(slackChannelList).hasSize(databaseSizeBeforeCreate + 1);
        SlackChannel testSlackChannel = slackChannelList.get(slackChannelList.size() - 1);
        assertThat(testSlackChannel.getChannelId()).isEqualTo(DEFAULT_CHANNEL_ID);
        assertThat(testSlackChannel.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSlackChannel.getTeamId()).isEqualTo(DEFAULT_TEAM_ID);
        assertThat(testSlackChannel.getIsPrivate()).isEqualTo(DEFAULT_IS_PRIVATE);

        // Validate the SlackChannel in Elasticsearch
        verify(mockSlackChannelSearchRepository, times(1)).save(testSlackChannel);
    }

    @Test
    @Transactional
    public void createSlackChannelWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = slackChannelRepository.findAll().size();

        // Create the SlackChannel with an existing ID
        slackChannel.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSlackChannelMockMvc.perform(post("/api/slack-channels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(slackChannel)))
            .andExpect(status().isBadRequest());

        // Validate the SlackChannel in the database
        List<SlackChannel> slackChannelList = slackChannelRepository.findAll();
        assertThat(slackChannelList).hasSize(databaseSizeBeforeCreate);

        // Validate the SlackChannel in Elasticsearch
        verify(mockSlackChannelSearchRepository, times(0)).save(slackChannel);
    }

    @Test
    @Transactional
    public void checkChannelIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = slackChannelRepository.findAll().size();
        // set the field null
        slackChannel.setChannelId(null);

        // Create the SlackChannel, which fails.

        restSlackChannelMockMvc.perform(post("/api/slack-channels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(slackChannel)))
            .andExpect(status().isBadRequest());

        List<SlackChannel> slackChannelList = slackChannelRepository.findAll();
        assertThat(slackChannelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = slackChannelRepository.findAll().size();
        // set the field null
        slackChannel.setName(null);

        // Create the SlackChannel, which fails.

        restSlackChannelMockMvc.perform(post("/api/slack-channels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(slackChannel)))
            .andExpect(status().isBadRequest());

        List<SlackChannel> slackChannelList = slackChannelRepository.findAll();
        assertThat(slackChannelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTeamIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = slackChannelRepository.findAll().size();
        // set the field null
        slackChannel.setTeamId(null);

        // Create the SlackChannel, which fails.

        restSlackChannelMockMvc.perform(post("/api/slack-channels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(slackChannel)))
            .andExpect(status().isBadRequest());

        List<SlackChannel> slackChannelList = slackChannelRepository.findAll();
        assertThat(slackChannelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsPrivateIsRequired() throws Exception {
        int databaseSizeBeforeTest = slackChannelRepository.findAll().size();
        // set the field null
        slackChannel.setIsPrivate(null);

        // Create the SlackChannel, which fails.

        restSlackChannelMockMvc.perform(post("/api/slack-channels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(slackChannel)))
            .andExpect(status().isBadRequest());

        List<SlackChannel> slackChannelList = slackChannelRepository.findAll();
        assertThat(slackChannelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSlackChannels() throws Exception {
        // Initialize the database
        slackChannelRepository.saveAndFlush(slackChannel);

        // Get all the slackChannelList
        restSlackChannelMockMvc.perform(get("/api/slack-channels?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(slackChannel.getId().intValue())))
            .andExpect(jsonPath("$.[*].channelId").value(hasItem(DEFAULT_CHANNEL_ID.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].teamId").value(hasItem(DEFAULT_TEAM_ID.toString())))
            .andExpect(jsonPath("$.[*].isPrivate").value(hasItem(DEFAULT_IS_PRIVATE.booleanValue())));
    }

    @Test
    @Transactional
    public void getSlackChannel() throws Exception {
        // Initialize the database
        slackChannelRepository.saveAndFlush(slackChannel);

        // Get the slackChannel
        restSlackChannelMockMvc.perform(get("/api/slack-channels/{id}", slackChannel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(slackChannel.getId().intValue()))
            .andExpect(jsonPath("$.channelId").value(DEFAULT_CHANNEL_ID.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.teamId").value(DEFAULT_TEAM_ID.toString()))
            .andExpect(jsonPath("$.isPrivate").value(DEFAULT_IS_PRIVATE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSlackChannel() throws Exception {
        // Get the slackChannel
        restSlackChannelMockMvc.perform(get("/api/slack-channels/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSlackChannel() throws Exception {
        // Initialize the database
        slackChannelService.save(slackChannel);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockSlackChannelSearchRepository);

        int databaseSizeBeforeUpdate = slackChannelRepository.findAll().size();

        // Update the slackChannel
        SlackChannel updatedSlackChannel = slackChannelRepository.findById(slackChannel.getId()).get();
        // Disconnect from session so that the updates on updatedSlackChannel are not directly saved in db
        em.detach(updatedSlackChannel);
        updatedSlackChannel
            .channelId(UPDATED_CHANNEL_ID)
            .name(UPDATED_NAME)
            .teamId(UPDATED_TEAM_ID)
            .isPrivate(UPDATED_IS_PRIVATE);

        restSlackChannelMockMvc.perform(put("/api/slack-channels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSlackChannel)))
            .andExpect(status().isOk());

        // Validate the SlackChannel in the database
        List<SlackChannel> slackChannelList = slackChannelRepository.findAll();
        assertThat(slackChannelList).hasSize(databaseSizeBeforeUpdate);
        SlackChannel testSlackChannel = slackChannelList.get(slackChannelList.size() - 1);
        assertThat(testSlackChannel.getChannelId()).isEqualTo(UPDATED_CHANNEL_ID);
        assertThat(testSlackChannel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSlackChannel.getTeamId()).isEqualTo(UPDATED_TEAM_ID);
        assertThat(testSlackChannel.getIsPrivate()).isEqualTo(UPDATED_IS_PRIVATE);

        // Validate the SlackChannel in Elasticsearch
        verify(mockSlackChannelSearchRepository, times(1)).save(testSlackChannel);
    }

    @Test
    @Transactional
    public void updateNonExistingSlackChannel() throws Exception {
        int databaseSizeBeforeUpdate = slackChannelRepository.findAll().size();

        // Create the SlackChannel

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSlackChannelMockMvc.perform(put("/api/slack-channels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(slackChannel)))
            .andExpect(status().isBadRequest());

        // Validate the SlackChannel in the database
        List<SlackChannel> slackChannelList = slackChannelRepository.findAll();
        assertThat(slackChannelList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SlackChannel in Elasticsearch
        verify(mockSlackChannelSearchRepository, times(0)).save(slackChannel);
    }

    @Test
    @Transactional
    public void deleteSlackChannel() throws Exception {
        // Initialize the database
        slackChannelService.save(slackChannel);

        int databaseSizeBeforeDelete = slackChannelRepository.findAll().size();

        // Get the slackChannel
        restSlackChannelMockMvc.perform(delete("/api/slack-channels/{id}", slackChannel.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SlackChannel> slackChannelList = slackChannelRepository.findAll();
        assertThat(slackChannelList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SlackChannel in Elasticsearch
        verify(mockSlackChannelSearchRepository, times(1)).deleteById(slackChannel.getId());
    }

    @Test
    @Transactional
    public void searchSlackChannel() throws Exception {
        // Initialize the database
        slackChannelService.save(slackChannel);
        when(mockSlackChannelSearchRepository.search(queryStringQuery("id:" + slackChannel.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(slackChannel), PageRequest.of(0, 1), 1));
        // Search the slackChannel
        restSlackChannelMockMvc.perform(get("/api/_search/slack-channels?query=id:" + slackChannel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(slackChannel.getId().intValue())))
            .andExpect(jsonPath("$.[*].channelId").value(hasItem(DEFAULT_CHANNEL_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].teamId").value(hasItem(DEFAULT_TEAM_ID)))
            .andExpect(jsonPath("$.[*].isPrivate").value(hasItem(DEFAULT_IS_PRIVATE.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SlackChannel.class);
        SlackChannel slackChannel1 = new SlackChannel();
        slackChannel1.setId(1L);
        SlackChannel slackChannel2 = new SlackChannel();
        slackChannel2.setId(slackChannel1.getId());
        assertThat(slackChannel1).isEqualTo(slackChannel2);
        slackChannel2.setId(2L);
        assertThat(slackChannel1).isNotEqualTo(slackChannel2);
        slackChannel1.setId(null);
        assertThat(slackChannel1).isNotEqualTo(slackChannel2);
    }
}
