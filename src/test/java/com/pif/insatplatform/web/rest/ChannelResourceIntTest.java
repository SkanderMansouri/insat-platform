package com.pif.insatplatform.web.rest;

import com.pif.insatplatform.InsatplatformApp;

import com.pif.insatplatform.domain.Channel;
import com.pif.insatplatform.repository.ChannelRepository;
import com.pif.insatplatform.service.ChannelService;
import com.pif.insatplatform.service.dto.ChannelDTO;
import com.pif.insatplatform.service.mapper.ChannelMapper;
import com.pif.insatplatform.web.rest.errors.ExceptionTranslator;
import com.pif.insatplatform.service.dto.ChannelCriteria;
import com.pif.insatplatform.service.ChannelQueryService;

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
 * Test class for the ChannelResource REST controller.
 *
 * @see ChannelResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InsatplatformApp.class)
public class ChannelResourceIntTest {

    private static final String DEFAULT_CHANNEL_ID = "AAAAAAAAAA";
    private static final String UPDATED_CHANNEL_ID = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TEAM_ID = "AAAAAAAAAA";
    private static final String UPDATED_TEAM_ID = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_PRIVATE = false;
    private static final Boolean UPDATED_IS_PRIVATE = true;

    private static final String DEFAULT_PURPOSE = "AAAAAAAAAA";
    private static final String UPDATED_PURPOSE = "BBBBBBBBBB";

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private ChannelMapper channelMapper;
    
    @Autowired
    private ChannelService channelService;

    @Autowired
    private ChannelQueryService channelQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restChannelMockMvc;

    private Channel channel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ChannelResource channelResource = new ChannelResource(channelService, channelQueryService);
        this.restChannelMockMvc = MockMvcBuilders.standaloneSetup(channelResource)
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
    public static Channel createEntity(EntityManager em) {
        Channel channel = new Channel()
            .channelID(DEFAULT_CHANNEL_ID)
            .name(DEFAULT_NAME)
            .teamID(DEFAULT_TEAM_ID)
            .isPrivate(DEFAULT_IS_PRIVATE)
            .purpose(DEFAULT_PURPOSE);
        return channel;
    }

    @Before
    public void initTest() {
        channel = createEntity(em);
    }

    @Test
    @Transactional
    public void createChannel() throws Exception {
        int databaseSizeBeforeCreate = channelRepository.findAll().size();

        // Create the Channel
        ChannelDTO channelDTO = channelMapper.toDto(channel);
        restChannelMockMvc.perform(post("/api/channels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(channelDTO)))
            .andExpect(status().isCreated());

        // Validate the Channel in the database
        List<Channel> channelList = channelRepository.findAll();
        assertThat(channelList).hasSize(databaseSizeBeforeCreate + 1);
        Channel testChannel = channelList.get(channelList.size() - 1);
        assertThat(testChannel.getChannelID()).isEqualTo(DEFAULT_CHANNEL_ID);
        assertThat(testChannel.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testChannel.getTeamID()).isEqualTo(DEFAULT_TEAM_ID);
        assertThat(testChannel.isIsPrivate()).isEqualTo(DEFAULT_IS_PRIVATE);
        assertThat(testChannel.getPurpose()).isEqualTo(DEFAULT_PURPOSE);
    }

    @Test
    @Transactional
    public void createChannelWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = channelRepository.findAll().size();

        // Create the Channel with an existing ID
        channel.setId(1L);
        ChannelDTO channelDTO = channelMapper.toDto(channel);

        // An entity with an existing ID cannot be created, so this API call must fail
        restChannelMockMvc.perform(post("/api/channels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(channelDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Channel in the database
        List<Channel> channelList = channelRepository.findAll();
        assertThat(channelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkChannelIDIsRequired() throws Exception {
        int databaseSizeBeforeTest = channelRepository.findAll().size();
        // set the field null
        channel.setChannelID(null);

        // Create the Channel, which fails.
        ChannelDTO channelDTO = channelMapper.toDto(channel);

        restChannelMockMvc.perform(post("/api/channels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(channelDTO)))
            .andExpect(status().isBadRequest());

        List<Channel> channelList = channelRepository.findAll();
        assertThat(channelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = channelRepository.findAll().size();
        // set the field null
        channel.setName(null);

        // Create the Channel, which fails.
        ChannelDTO channelDTO = channelMapper.toDto(channel);

        restChannelMockMvc.perform(post("/api/channels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(channelDTO)))
            .andExpect(status().isBadRequest());

        List<Channel> channelList = channelRepository.findAll();
        assertThat(channelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTeamIDIsRequired() throws Exception {
        int databaseSizeBeforeTest = channelRepository.findAll().size();
        // set the field null
        channel.setTeamID(null);

        // Create the Channel, which fails.
        ChannelDTO channelDTO = channelMapper.toDto(channel);

        restChannelMockMvc.perform(post("/api/channels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(channelDTO)))
            .andExpect(status().isBadRequest());

        List<Channel> channelList = channelRepository.findAll();
        assertThat(channelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsPrivateIsRequired() throws Exception {
        int databaseSizeBeforeTest = channelRepository.findAll().size();
        // set the field null
        channel.setIsPrivate(null);

        // Create the Channel, which fails.
        ChannelDTO channelDTO = channelMapper.toDto(channel);

        restChannelMockMvc.perform(post("/api/channels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(channelDTO)))
            .andExpect(status().isBadRequest());

        List<Channel> channelList = channelRepository.findAll();
        assertThat(channelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllChannels() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList
        restChannelMockMvc.perform(get("/api/channels?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(channel.getId().intValue())))
            .andExpect(jsonPath("$.[*].channelID").value(hasItem(DEFAULT_CHANNEL_ID.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].teamID").value(hasItem(DEFAULT_TEAM_ID.toString())))
            .andExpect(jsonPath("$.[*].isPrivate").value(hasItem(DEFAULT_IS_PRIVATE.booleanValue())))
            .andExpect(jsonPath("$.[*].purpose").value(hasItem(DEFAULT_PURPOSE.toString())));
    }
    
    @Test
    @Transactional
    public void getChannel() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get the channel
        restChannelMockMvc.perform(get("/api/channels/{id}", channel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(channel.getId().intValue()))
            .andExpect(jsonPath("$.channelID").value(DEFAULT_CHANNEL_ID.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.teamID").value(DEFAULT_TEAM_ID.toString()))
            .andExpect(jsonPath("$.isPrivate").value(DEFAULT_IS_PRIVATE.booleanValue()))
            .andExpect(jsonPath("$.purpose").value(DEFAULT_PURPOSE.toString()));
    }

    @Test
    @Transactional
    public void getAllChannelsByChannelIDIsEqualToSomething() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList where channelID equals to DEFAULT_CHANNEL_ID
        defaultChannelShouldBeFound("channelID.equals=" + DEFAULT_CHANNEL_ID);

        // Get all the channelList where channelID equals to UPDATED_CHANNEL_ID
        defaultChannelShouldNotBeFound("channelID.equals=" + UPDATED_CHANNEL_ID);
    }

    @Test
    @Transactional
    public void getAllChannelsByChannelIDIsInShouldWork() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList where channelID in DEFAULT_CHANNEL_ID or UPDATED_CHANNEL_ID
        defaultChannelShouldBeFound("channelID.in=" + DEFAULT_CHANNEL_ID + "," + UPDATED_CHANNEL_ID);

        // Get all the channelList where channelID equals to UPDATED_CHANNEL_ID
        defaultChannelShouldNotBeFound("channelID.in=" + UPDATED_CHANNEL_ID);
    }

    @Test
    @Transactional
    public void getAllChannelsByChannelIDIsNullOrNotNull() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList where channelID is not null
        defaultChannelShouldBeFound("channelID.specified=true");

        // Get all the channelList where channelID is null
        defaultChannelShouldNotBeFound("channelID.specified=false");
    }

    @Test
    @Transactional
    public void getAllChannelsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList where name equals to DEFAULT_NAME
        defaultChannelShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the channelList where name equals to UPDATED_NAME
        defaultChannelShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllChannelsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList where name in DEFAULT_NAME or UPDATED_NAME
        defaultChannelShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the channelList where name equals to UPDATED_NAME
        defaultChannelShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllChannelsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList where name is not null
        defaultChannelShouldBeFound("name.specified=true");

        // Get all the channelList where name is null
        defaultChannelShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllChannelsByTeamIDIsEqualToSomething() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList where teamID equals to DEFAULT_TEAM_ID
        defaultChannelShouldBeFound("teamID.equals=" + DEFAULT_TEAM_ID);

        // Get all the channelList where teamID equals to UPDATED_TEAM_ID
        defaultChannelShouldNotBeFound("teamID.equals=" + UPDATED_TEAM_ID);
    }

    @Test
    @Transactional
    public void getAllChannelsByTeamIDIsInShouldWork() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList where teamID in DEFAULT_TEAM_ID or UPDATED_TEAM_ID
        defaultChannelShouldBeFound("teamID.in=" + DEFAULT_TEAM_ID + "," + UPDATED_TEAM_ID);

        // Get all the channelList where teamID equals to UPDATED_TEAM_ID
        defaultChannelShouldNotBeFound("teamID.in=" + UPDATED_TEAM_ID);
    }

    @Test
    @Transactional
    public void getAllChannelsByTeamIDIsNullOrNotNull() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList where teamID is not null
        defaultChannelShouldBeFound("teamID.specified=true");

        // Get all the channelList where teamID is null
        defaultChannelShouldNotBeFound("teamID.specified=false");
    }

    @Test
    @Transactional
    public void getAllChannelsByIsPrivateIsEqualToSomething() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList where isPrivate equals to DEFAULT_IS_PRIVATE
        defaultChannelShouldBeFound("isPrivate.equals=" + DEFAULT_IS_PRIVATE);

        // Get all the channelList where isPrivate equals to UPDATED_IS_PRIVATE
        defaultChannelShouldNotBeFound("isPrivate.equals=" + UPDATED_IS_PRIVATE);
    }

    @Test
    @Transactional
    public void getAllChannelsByIsPrivateIsInShouldWork() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList where isPrivate in DEFAULT_IS_PRIVATE or UPDATED_IS_PRIVATE
        defaultChannelShouldBeFound("isPrivate.in=" + DEFAULT_IS_PRIVATE + "," + UPDATED_IS_PRIVATE);

        // Get all the channelList where isPrivate equals to UPDATED_IS_PRIVATE
        defaultChannelShouldNotBeFound("isPrivate.in=" + UPDATED_IS_PRIVATE);
    }

    @Test
    @Transactional
    public void getAllChannelsByIsPrivateIsNullOrNotNull() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList where isPrivate is not null
        defaultChannelShouldBeFound("isPrivate.specified=true");

        // Get all the channelList where isPrivate is null
        defaultChannelShouldNotBeFound("isPrivate.specified=false");
    }

    @Test
    @Transactional
    public void getAllChannelsByPurposeIsEqualToSomething() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList where purpose equals to DEFAULT_PURPOSE
        defaultChannelShouldBeFound("purpose.equals=" + DEFAULT_PURPOSE);

        // Get all the channelList where purpose equals to UPDATED_PURPOSE
        defaultChannelShouldNotBeFound("purpose.equals=" + UPDATED_PURPOSE);
    }

    @Test
    @Transactional
    public void getAllChannelsByPurposeIsInShouldWork() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList where purpose in DEFAULT_PURPOSE or UPDATED_PURPOSE
        defaultChannelShouldBeFound("purpose.in=" + DEFAULT_PURPOSE + "," + UPDATED_PURPOSE);

        // Get all the channelList where purpose equals to UPDATED_PURPOSE
        defaultChannelShouldNotBeFound("purpose.in=" + UPDATED_PURPOSE);
    }

    @Test
    @Transactional
    public void getAllChannelsByPurposeIsNullOrNotNull() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList where purpose is not null
        defaultChannelShouldBeFound("purpose.specified=true");

        // Get all the channelList where purpose is null
        defaultChannelShouldNotBeFound("purpose.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultChannelShouldBeFound(String filter) throws Exception {
        restChannelMockMvc.perform(get("/api/channels?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(channel.getId().intValue())))
            .andExpect(jsonPath("$.[*].channelID").value(hasItem(DEFAULT_CHANNEL_ID.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].teamID").value(hasItem(DEFAULT_TEAM_ID.toString())))
            .andExpect(jsonPath("$.[*].isPrivate").value(hasItem(DEFAULT_IS_PRIVATE.booleanValue())))
            .andExpect(jsonPath("$.[*].purpose").value(hasItem(DEFAULT_PURPOSE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultChannelShouldNotBeFound(String filter) throws Exception {
        restChannelMockMvc.perform(get("/api/channels?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingChannel() throws Exception {
        // Get the channel
        restChannelMockMvc.perform(get("/api/channels/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChannel() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        int databaseSizeBeforeUpdate = channelRepository.findAll().size();

        // Update the channel
        Channel updatedChannel = channelRepository.findById(channel.getId()).get();
        // Disconnect from session so that the updates on updatedChannel are not directly saved in db
        em.detach(updatedChannel);
        updatedChannel
            .channelID(UPDATED_CHANNEL_ID)
            .name(UPDATED_NAME)
            .teamID(UPDATED_TEAM_ID)
            .isPrivate(UPDATED_IS_PRIVATE)
            .purpose(UPDATED_PURPOSE);
        ChannelDTO channelDTO = channelMapper.toDto(updatedChannel);

        restChannelMockMvc.perform(put("/api/channels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(channelDTO)))
            .andExpect(status().isOk());

        // Validate the Channel in the database
        List<Channel> channelList = channelRepository.findAll();
        assertThat(channelList).hasSize(databaseSizeBeforeUpdate);
        Channel testChannel = channelList.get(channelList.size() - 1);
        assertThat(testChannel.getChannelID()).isEqualTo(UPDATED_CHANNEL_ID);
        assertThat(testChannel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testChannel.getTeamID()).isEqualTo(UPDATED_TEAM_ID);
        assertThat(testChannel.isIsPrivate()).isEqualTo(UPDATED_IS_PRIVATE);
        assertThat(testChannel.getPurpose()).isEqualTo(UPDATED_PURPOSE);
    }

    @Test
    @Transactional
    public void updateNonExistingChannel() throws Exception {
        int databaseSizeBeforeUpdate = channelRepository.findAll().size();

        // Create the Channel
        ChannelDTO channelDTO = channelMapper.toDto(channel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChannelMockMvc.perform(put("/api/channels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(channelDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Channel in the database
        List<Channel> channelList = channelRepository.findAll();
        assertThat(channelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteChannel() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        int databaseSizeBeforeDelete = channelRepository.findAll().size();

        // Get the channel
        restChannelMockMvc.perform(delete("/api/channels/{id}", channel.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Channel> channelList = channelRepository.findAll();
        assertThat(channelList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Channel.class);
        Channel channel1 = new Channel();
        channel1.setId(1L);
        Channel channel2 = new Channel();
        channel2.setId(channel1.getId());
        assertThat(channel1).isEqualTo(channel2);
        channel2.setId(2L);
        assertThat(channel1).isNotEqualTo(channel2);
        channel1.setId(null);
        assertThat(channel1).isNotEqualTo(channel2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChannelDTO.class);
        ChannelDTO channelDTO1 = new ChannelDTO();
        channelDTO1.setId(1L);
        ChannelDTO channelDTO2 = new ChannelDTO();
        assertThat(channelDTO1).isNotEqualTo(channelDTO2);
        channelDTO2.setId(channelDTO1.getId());
        assertThat(channelDTO1).isEqualTo(channelDTO2);
        channelDTO2.setId(2L);
        assertThat(channelDTO1).isNotEqualTo(channelDTO2);
        channelDTO1.setId(null);
        assertThat(channelDTO1).isNotEqualTo(channelDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(channelMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(channelMapper.fromId(null)).isNull();
    }
}
