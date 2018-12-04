package insat.company.platform.web.rest;

import insat.company.platform.InsatApp;

import insat.company.platform.domain.JoinClubRequest;
import insat.company.platform.domain.User;
import insat.company.platform.domain.Club;
import insat.company.platform.repository.JoinClubRequestRepository;
import insat.company.platform.repository.search.JoinClubRequestSearchRepository;
import insat.company.platform.service.JoinClubRequestService;
import insat.company.platform.web.rest.errors.ExceptionTranslator;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;


import static insat.company.platform.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import insat.company.platform.domain.enumeration.StatusEnumeration;
/**
 * Test class for the JoinClubRequestResource REST controller.
 *
 * @see JoinClubRequestResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InsatApp.class)
public class JoinClubRequestResourceIntTest {

    private static final LocalDate DEFAULT_REQUEST_TIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REQUEST_TIME = LocalDate.now(ZoneId.systemDefault());

    private static final StatusEnumeration DEFAULT_STATUS = StatusEnumeration.PENDING;
    private static final StatusEnumeration UPDATED_STATUS = StatusEnumeration.ACCEPTED;

    @Autowired
    private JoinClubRequestRepository joinClubRequestRepository;

    @Autowired
    private JoinClubRequestService joinClubRequestService;

    /**
     * This repository is mocked in the insat.company.platform.repository.search test package.
     *
     * @see insat.company.platform.repository.search.JoinClubRequestSearchRepositoryMockConfiguration
     */
    @Autowired
    private JoinClubRequestSearchRepository mockJoinClubRequestSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restJoinClubRequestMockMvc;

    private JoinClubRequest joinClubRequest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final JoinClubRequestResource joinClubRequestResource = new JoinClubRequestResource(joinClubRequestService);
        this.restJoinClubRequestMockMvc = MockMvcBuilders.standaloneSetup(joinClubRequestResource)
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
    public static JoinClubRequest createEntity(EntityManager em) {
        JoinClubRequest joinClubRequest = new JoinClubRequest()
            .requestTime(DEFAULT_REQUEST_TIME)
            .status(DEFAULT_STATUS);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        joinClubRequest.setUser(user);
        // Add required entity
        Club club = ClubResourceIntTest.createEntity(em);
        em.persist(club);
        em.flush();
        joinClubRequest.setClub(club);
        return joinClubRequest;
    }

    @Before
    public void initTest() {
        joinClubRequest = createEntity(em);
    }

    @Test
    @Transactional
    public void createJoinClubRequest() throws Exception {
        int databaseSizeBeforeCreate = joinClubRequestRepository.findAll().size();

        // Create the JoinClubRequest
        restJoinClubRequestMockMvc.perform(post("/api/join-club-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(joinClubRequest)))
            .andExpect(status().isCreated());

        // Validate the JoinClubRequest in the database
        List<JoinClubRequest> joinClubRequestList = joinClubRequestRepository.findAll();
        assertThat(joinClubRequestList).hasSize(databaseSizeBeforeCreate + 1);
        JoinClubRequest testJoinClubRequest = joinClubRequestList.get(joinClubRequestList.size() - 1);
        assertThat(testJoinClubRequest.getRequestTime()).isEqualTo(DEFAULT_REQUEST_TIME);
        assertThat(testJoinClubRequest.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the JoinClubRequest in Elasticsearch
        verify(mockJoinClubRequestSearchRepository, times(1)).save(testJoinClubRequest);
    }

    @Test
    @Transactional
    public void createJoinClubRequestWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = joinClubRequestRepository.findAll().size();

        // Create the JoinClubRequest with an existing ID
        joinClubRequest.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJoinClubRequestMockMvc.perform(post("/api/join-club-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(joinClubRequest)))
            .andExpect(status().isBadRequest());

        // Validate the JoinClubRequest in the database
        List<JoinClubRequest> joinClubRequestList = joinClubRequestRepository.findAll();
        assertThat(joinClubRequestList).hasSize(databaseSizeBeforeCreate);

        // Validate the JoinClubRequest in Elasticsearch
        verify(mockJoinClubRequestSearchRepository, times(0)).save(joinClubRequest);
    }

    @Test
    @Transactional
    public void checkRequestTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = joinClubRequestRepository.findAll().size();
        // set the field null
        joinClubRequest.setRequestTime(null);

        // Create the JoinClubRequest, which fails.

        restJoinClubRequestMockMvc.perform(post("/api/join-club-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(joinClubRequest)))
            .andExpect(status().isBadRequest());

        List<JoinClubRequest> joinClubRequestList = joinClubRequestRepository.findAll();
        assertThat(joinClubRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = joinClubRequestRepository.findAll().size();
        // set the field null
        joinClubRequest.setStatus(null);

        // Create the JoinClubRequest, which fails.

        restJoinClubRequestMockMvc.perform(post("/api/join-club-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(joinClubRequest)))
            .andExpect(status().isBadRequest());

        List<JoinClubRequest> joinClubRequestList = joinClubRequestRepository.findAll();
        assertThat(joinClubRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllJoinClubRequests() throws Exception {
        // Initialize the database
        joinClubRequestRepository.saveAndFlush(joinClubRequest);

        // Get all the joinClubRequestList
        restJoinClubRequestMockMvc.perform(get("/api/join-club-requests?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(joinClubRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].requestTime").value(hasItem(DEFAULT_REQUEST_TIME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
    
    @Test
    @Transactional
    public void getJoinClubRequest() throws Exception {
        // Initialize the database
        joinClubRequestRepository.saveAndFlush(joinClubRequest);

        // Get the joinClubRequest
        restJoinClubRequestMockMvc.perform(get("/api/join-club-requests/{id}", joinClubRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(joinClubRequest.getId().intValue()))
            .andExpect(jsonPath("$.requestTime").value(DEFAULT_REQUEST_TIME.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingJoinClubRequest() throws Exception {
        // Get the joinClubRequest
        restJoinClubRequestMockMvc.perform(get("/api/join-club-requests/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJoinClubRequest() throws Exception {
        // Initialize the database
        joinClubRequestService.save(joinClubRequest);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockJoinClubRequestSearchRepository);

        int databaseSizeBeforeUpdate = joinClubRequestRepository.findAll().size();

        // Update the joinClubRequest
        JoinClubRequest updatedJoinClubRequest = joinClubRequestRepository.findById(joinClubRequest.getId()).get();
        // Disconnect from session so that the updates on updatedJoinClubRequest are not directly saved in db
        em.detach(updatedJoinClubRequest);
        updatedJoinClubRequest
            .requestTime(UPDATED_REQUEST_TIME)
            .status(UPDATED_STATUS);

        restJoinClubRequestMockMvc.perform(put("/api/join-club-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedJoinClubRequest)))
            .andExpect(status().isOk());

        // Validate the JoinClubRequest in the database
        List<JoinClubRequest> joinClubRequestList = joinClubRequestRepository.findAll();
        assertThat(joinClubRequestList).hasSize(databaseSizeBeforeUpdate);
        JoinClubRequest testJoinClubRequest = joinClubRequestList.get(joinClubRequestList.size() - 1);
        assertThat(testJoinClubRequest.getRequestTime()).isEqualTo(UPDATED_REQUEST_TIME);
        assertThat(testJoinClubRequest.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the JoinClubRequest in Elasticsearch
        verify(mockJoinClubRequestSearchRepository, times(1)).save(testJoinClubRequest);
    }

    @Test
    @Transactional
    public void updateNonExistingJoinClubRequest() throws Exception {
        int databaseSizeBeforeUpdate = joinClubRequestRepository.findAll().size();

        // Create the JoinClubRequest

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJoinClubRequestMockMvc.perform(put("/api/join-club-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(joinClubRequest)))
            .andExpect(status().isBadRequest());

        // Validate the JoinClubRequest in the database
        List<JoinClubRequest> joinClubRequestList = joinClubRequestRepository.findAll();
        assertThat(joinClubRequestList).hasSize(databaseSizeBeforeUpdate);

        // Validate the JoinClubRequest in Elasticsearch
        verify(mockJoinClubRequestSearchRepository, times(0)).save(joinClubRequest);
    }

    @Test
    @Transactional
    public void deleteJoinClubRequest() throws Exception {
        // Initialize the database
        joinClubRequestService.save(joinClubRequest);

        int databaseSizeBeforeDelete = joinClubRequestRepository.findAll().size();

        // Get the joinClubRequest
        restJoinClubRequestMockMvc.perform(delete("/api/join-club-requests/{id}", joinClubRequest.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<JoinClubRequest> joinClubRequestList = joinClubRequestRepository.findAll();
        assertThat(joinClubRequestList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the JoinClubRequest in Elasticsearch
        verify(mockJoinClubRequestSearchRepository, times(1)).deleteById(joinClubRequest.getId());
    }

    @Test
    @Transactional
    public void searchJoinClubRequest() throws Exception {
        // Initialize the database
        joinClubRequestService.save(joinClubRequest);
        when(mockJoinClubRequestSearchRepository.search(queryStringQuery("id:" + joinClubRequest.getId())))
            .thenReturn(Collections.singletonList(joinClubRequest));
        // Search the joinClubRequest
        restJoinClubRequestMockMvc.perform(get("/api/_search/join-club-requests?query=id:" + joinClubRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(joinClubRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].requestTime").value(hasItem(DEFAULT_REQUEST_TIME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JoinClubRequest.class);
        JoinClubRequest joinClubRequest1 = new JoinClubRequest();
        joinClubRequest1.setId(1L);
        JoinClubRequest joinClubRequest2 = new JoinClubRequest();
        joinClubRequest2.setId(joinClubRequest1.getId());
        assertThat(joinClubRequest1).isEqualTo(joinClubRequest2);
        joinClubRequest2.setId(2L);
        assertThat(joinClubRequest1).isNotEqualTo(joinClubRequest2);
        joinClubRequest1.setId(null);
        assertThat(joinClubRequest1).isNotEqualTo(joinClubRequest2);
    }
}
