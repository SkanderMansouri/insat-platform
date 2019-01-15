package insat.company.platform.web.rest;

import insat.company.platform.InsatApp;

import insat.company.platform.domain.InsatEvent;
import insat.company.platform.repository.InsatEventRepository;
import insat.company.platform.repository.search.InsatEventSearchRepository;
import insat.company.platform.service.InsatEventService;
import insat.company.platform.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import static insat.company.platform.web.rest.TestUtil.sameInstant;
import static insat.company.platform.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the InsatEventResource REST controller.
 *
 * @see InsatEventResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InsatApp.class)
public class InsatEventResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_PLACE = "AAAAAAAAAA";
    private static final String UPDATED_PLACE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private InsatEventRepository insatEventRepository;

    @Mock
    private InsatEventRepository insatEventRepositoryMock;

    @Mock
    private InsatEventService insatEventServiceMock;

    @Autowired
    private InsatEventService insatEventService;

    /**
     * This repository is mocked in the insat.company.platform.repository.search test package.
     *
     * @see insat.company.platform.repository.search.InsatEventSearchRepositoryMockConfiguration
     */
    @Autowired
    private InsatEventSearchRepository mockInsatEventSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restInsatEventMockMvc;

    private InsatEvent insatEvent;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InsatEventResource insatEventResource = new InsatEventResource(insatEventService);
        this.restInsatEventMockMvc = MockMvcBuilders.standaloneSetup(insatEventResource)
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
    public static InsatEvent createEntity(EntityManager em) {
        InsatEvent insatEvent = new InsatEvent()
            .name(DEFAULT_NAME)
            .date(DEFAULT_DATE)
            .place(DEFAULT_PLACE)
            .description(DEFAULT_DESCRIPTION);
        return insatEvent;
    }

    @Before
    public void initTest() {
        insatEvent = createEntity(em);
    }

    @Test
    @Transactional
    public void createInsatEvent() throws Exception {
        int databaseSizeBeforeCreate = insatEventRepository.findAll().size();

        // Create the InsatEvent
        restInsatEventMockMvc.perform(post("/api/insat-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(insatEvent)))
            .andExpect(status().isCreated());

        // Validate the InsatEvent in the database
        List<InsatEvent> insatEventList = insatEventRepository.findAll();
        assertThat(insatEventList).hasSize(databaseSizeBeforeCreate + 1);
        InsatEvent testInsatEvent = insatEventList.get(insatEventList.size() - 1);
        assertThat(testInsatEvent.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testInsatEvent.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testInsatEvent.getPlace()).isEqualTo(DEFAULT_PLACE);
        assertThat(testInsatEvent.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the InsatEvent in Elasticsearch
        verify(mockInsatEventSearchRepository, times(1)).save(testInsatEvent);
        verify(mockInsatEventSearchRepository, times(1)).save(testInsatEvent);
    }

    @Test
    @Transactional
    public void createInsatEventWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = insatEventRepository.findAll().size();

        // Create the InsatEvent with an existing ID
        insatEvent.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInsatEventMockMvc.perform(post("/api/insat-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(insatEvent)))
            .andExpect(status().isBadRequest());

        // Validate the InsatEvent in the database
        List<InsatEvent> insatEventList = insatEventRepository.findAll();
        assertThat(insatEventList).hasSize(databaseSizeBeforeCreate);

        // Validate the InsatEvent in Elasticsearch
        verify(mockInsatEventSearchRepository, times(0)).save(insatEvent);
    }

    @Test
    @Transactional
    public void getAllInsatEvents() throws Exception {
        // Initialize the database
        insatEventRepository.saveAndFlush(insatEvent);

        // Get all the insatEventList
        restInsatEventMockMvc.perform(get("/api/insat-events?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insatEvent.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].place").value(hasItem(DEFAULT_PLACE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllInsatEventsWithEagerRelationshipsIsEnabled() throws Exception {
        InsatEventResource insatEventResource = new InsatEventResource(insatEventServiceMock);
        when(insatEventServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restInsatEventMockMvc = MockMvcBuilders.standaloneSetup(insatEventResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restInsatEventMockMvc.perform(get("/api/insat-events?eagerload=true"))
        .andExpect(status().isOk());

        verify(insatEventServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllInsatEventsWithEagerRelationshipsIsNotEnabled() throws Exception {
        InsatEventResource insatEventResource = new InsatEventResource(insatEventServiceMock);
            when(insatEventServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restInsatEventMockMvc = MockMvcBuilders.standaloneSetup(insatEventResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restInsatEventMockMvc.perform(get("/api/insat-events?eagerload=true"))
        .andExpect(status().isOk());

            verify(insatEventServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getInsatEvent() throws Exception {
        // Initialize the database
        insatEventRepository.saveAndFlush(insatEvent);

        // Get the insatEvent
        restInsatEventMockMvc.perform(get("/api/insat-events/{id}", insatEvent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(insatEvent.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.place").value(DEFAULT_PLACE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInsatEvent() throws Exception {
        // Get the insatEvent
        restInsatEventMockMvc.perform(get("/api/insat-events/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInsatEvent() throws Exception {
        // Initialize the database
        insatEventService.save(insatEvent);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockInsatEventSearchRepository);

        int databaseSizeBeforeUpdate = insatEventRepository.findAll().size();

        // Update the insatEvent
        InsatEvent updatedInsatEvent = insatEventRepository.findById(insatEvent.getId()).get();
        // Disconnect from session so that the updates on updatedInsatEvent are not directly saved in db
        em.detach(updatedInsatEvent);
        updatedInsatEvent
            .name(UPDATED_NAME)
            .date(UPDATED_DATE)
            .place(UPDATED_PLACE)
            .description(UPDATED_DESCRIPTION);

        restInsatEventMockMvc.perform(put("/api/insat-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedInsatEvent)))
            .andExpect(status().isOk());

        // Validate the InsatEvent in the database
        List<InsatEvent> insatEventList = insatEventRepository.findAll();
        assertThat(insatEventList).hasSize(databaseSizeBeforeUpdate);
        InsatEvent testInsatEvent = insatEventList.get(insatEventList.size() - 1);
        assertThat(testInsatEvent.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInsatEvent.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testInsatEvent.getPlace()).isEqualTo(UPDATED_PLACE);
        assertThat(testInsatEvent.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the InsatEvent in Elasticsearch
        verify(mockInsatEventSearchRepository, times(1)).save(testInsatEvent);
    }

    @Test
    @Transactional
    public void updateNonExistingInsatEvent() throws Exception {
        int databaseSizeBeforeUpdate = insatEventRepository.findAll().size();

        // Create the InsatEvent

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsatEventMockMvc.perform(put("/api/insat-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(insatEvent)))
            .andExpect(status().isBadRequest());

        // Validate the InsatEvent in the database
        List<InsatEvent> insatEventList = insatEventRepository.findAll();
        assertThat(insatEventList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InsatEvent in Elasticsearch
        verify(mockInsatEventSearchRepository, times(0)).save(insatEvent);
    }

    @Test
    @Transactional
    public void deleteInsatEvent() throws Exception {
        // Initialize the database
        insatEventService.save(insatEvent);

        int databaseSizeBeforeDelete = insatEventRepository.findAll().size();

        // Get the insatEvent
        restInsatEventMockMvc.perform(delete("/api/insat-events/{id}", insatEvent.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<InsatEvent> insatEventList = insatEventRepository.findAll();
        assertThat(insatEventList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the InsatEvent in Elasticsearch
        verify(mockInsatEventSearchRepository, times(1)).deleteById(insatEvent.getId());
    }

    @Test
    @Transactional
    public void searchInsatEvent() throws Exception {
        // Initialize the database
        insatEventService.save(insatEvent);
        when(mockInsatEventSearchRepository.search(queryStringQuery("id:" + insatEvent.getId())))
            .thenReturn(Collections.singletonList(insatEvent));
        // Search the insatEvent
        restInsatEventMockMvc.perform(get("/api/_search/insat-events?query=id:" + insatEvent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insatEvent.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].place").value(hasItem(DEFAULT_PLACE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InsatEvent.class);
        InsatEvent insatEvent1 = new InsatEvent();
        insatEvent1.setId(1L);
        InsatEvent insatEvent2 = new InsatEvent();
        insatEvent2.setId(insatEvent1.getId());
        assertThat(insatEvent1).isEqualTo(insatEvent2);
        insatEvent2.setId(2L);
        assertThat(insatEvent1).isNotEqualTo(insatEvent2);
        insatEvent1.setId(null);
        assertThat(insatEvent1).isNotEqualTo(insatEvent2);
    }
}
