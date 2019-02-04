package insat.company.platform.web.rest;

import insat.company.platform.InsatApp;
import insat.company.platform.domain.Club;
import insat.company.platform.domain.JoinClubRequest;
import insat.company.platform.domain.User;
import insat.company.platform.domain.enumeration.Status;
import insat.company.platform.repository.ClubRepository;
import insat.company.platform.repository.JoinClubRequestRepository;
import insat.company.platform.repository.UserRepository;
import insat.company.platform.repository.search.ClubSearchRepository;
import insat.company.platform.repository.search.UserSearchRepository;
import insat.company.platform.service.ClubService;
import insat.company.platform.service.JoinClubRequestService;
import insat.company.platform.service.UserService;
import insat.company.platform.web.rest.errors.ExceptionTranslator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static insat.company.platform.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ClubResource REST controller.
 *
 * @see ClubResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InsatApp.class)
@Transactional
public class ClubResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DOMAIN = "AAAAAAAAAA";
    private static final String UPDATED_DOMAIN = "BBBBBBBBBB";

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private UserSearchRepository userSearchRepository;

    @Mock
    private ClubRepository clubRepositoryMock;

    @Mock
    private ClubService clubServiceMock;

    @Autowired
    private ClubService clubService;

    @Autowired
    private UserRepository userRepository;


    /**
     * This repository is mocked in the insat.company.platform.repository.search test package.
     *
     * @see insat.company.platform.repository.search.ClubSearchRepositoryMockConfiguration
     */
    @Autowired
    private ClubSearchRepository mockClubSearchRepository;

    @Autowired
    private JoinClubRequestRepository joinClubRequestRepository;


    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private UserService userService;

    @Autowired
    private JoinClubRequestService joinClubRequestService;


    @Autowired
    private EntityManager em;

    private MockMvc restClubMockMvc;

    private Club club;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Club createEntity(EntityManager em) {
        Club club = new Club()
            .name(DEFAULT_NAME)
            .domain(DEFAULT_DOMAIN);
        return club;
    }


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ClubResource clubResource = new ClubResource(clubService, userService, joinClubRequestRepository);
        this.restClubMockMvc = MockMvcBuilders.standaloneSetup(clubResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        club = createEntity(em);
    }

    @Test
    @Transactional
    public void createClub() throws Exception {
        int databaseSizeBeforeCreate = clubRepository.findAll().size();

        // Create the Club
        restClubMockMvc.perform(post("/api/clubs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(club)))
            .andExpect(status().isCreated());

        // Validate the Club in the database
        List<Club> clubList = clubRepository.findAll();
        assertThat(clubList).hasSize(databaseSizeBeforeCreate + 1);
        Club testClub = clubList.get(clubList.size() - 1);
        assertThat(testClub.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testClub.getDomain()).isEqualTo(DEFAULT_DOMAIN);

        // Validate the Club in Elasticsearch
        verify(mockClubSearchRepository, times(1)).save(testClub);
    }

    @Test
    @Transactional
    public void createClubWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = clubRepository.findAll().size();

        // Create the Club with an existing ID
        club.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClubMockMvc.perform(post("/api/clubs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(club)))
            .andExpect(status().isBadRequest());

        // Validate the Club in the database
        List<Club> clubList = clubRepository.findAll();
        assertThat(clubList).hasSize(databaseSizeBeforeCreate);

        // Validate the Club in Elasticsearch
        verify(mockClubSearchRepository, times(0)).save(club);
    }

    @Test
    @Transactional
    public void getAllClubs() throws Exception {
        // Initialize the database
        clubRepository.saveAndFlush(club);

        // Get all the clubList
        restClubMockMvc.perform(get("/api/clubs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(club.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].domain").value(hasItem(DEFAULT_DOMAIN)));
    }

    @SuppressWarnings({"unchecked"})
    public void getAllClubsWithEagerRelationshipsIsEnabled() throws Exception {
        ClubResource clubResource = new ClubResource(clubServiceMock, userService, joinClubRequestRepository);
        when(clubServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restClubMockMvc = MockMvcBuilders.standaloneSetup(clubResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restClubMockMvc.perform(get("/api/clubs?eagerload=true"))
            .andExpect(status().isOk());

        verify(clubServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllClubsWithEagerRelationshipsIsNotEnabled() throws Exception {
        ClubResource clubResource = new ClubResource(clubServiceMock, userService, joinClubRequestRepository);
        when(clubServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
        MockMvc restClubMockMvc = MockMvcBuilders.standaloneSetup(clubResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restClubMockMvc.perform(get("/api/clubs?eagerload=true"))
            .andExpect(status().isOk());

        verify(clubServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getClub() throws Exception {
        // Initialize the database
        clubRepository.saveAndFlush(club);

        // Get the club
        restClubMockMvc.perform(get("/api/clubs/{id}", club.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(club.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.domain").value(DEFAULT_DOMAIN));
    }

    @Test
    @Transactional
    public void getNonExistingClub() throws Exception {
        // Get the club
        restClubMockMvc.perform(get("/api/clubs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClub() throws Exception {
        // Initialize the database
        clubService.save(club);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockClubSearchRepository);

        int databaseSizeBeforeUpdate = clubRepository.findAll().size();

        // Update the club
        Club updatedClub = clubRepository.findById(club.getId()).get();
        // Disconnect from session so that the updates on updatedClub are not directly saved in db
        em.detach(updatedClub);
        updatedClub
            .name(UPDATED_NAME)
            .domain(UPDATED_DOMAIN);

        restClubMockMvc.perform(put("/api/clubs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedClub)))
            .andExpect(status().isOk());

        // Validate the Club in the database
        List<Club> clubList = clubRepository.findAll();
        assertThat(clubList).hasSize(databaseSizeBeforeUpdate);
        Club testClub = clubList.get(clubList.size() - 1);
        assertThat(testClub.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testClub.getDomain()).isEqualTo(UPDATED_DOMAIN);

        // Validate the Club in Elasticsearch
        verify(mockClubSearchRepository, times(1)).save(testClub);
    }

    @Test
    @Transactional
    public void updateNonExistingClub() throws Exception {
        int databaseSizeBeforeUpdate = clubRepository.findAll().size();

        // Create the Club

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClubMockMvc.perform(put("/api/clubs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(club)))
            .andExpect(status().isBadRequest());

        // Validate the Club in the database
        List<Club> clubList = clubRepository.findAll();
        assertThat(clubList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Club in Elasticsearch
        verify(mockClubSearchRepository, times(0)).save(club);
    }

    @Test
    @Transactional
    public void deleteClub() throws Exception {
        // Initialize the database
        clubService.save(club);

        int databaseSizeBeforeDelete = clubRepository.findAll().size();

        // Get the club
        restClubMockMvc.perform(delete("/api/clubs/{id}", club.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Club> clubList = clubRepository.findAll();
        assertThat(clubList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Club in Elasticsearch
        verify(mockClubSearchRepository, times(1)).deleteById(club.getId());
    }

    @Test
    @Transactional
    public void searchClub() throws Exception {
        // Initialize the database
        clubService.save(club);
        when(mockClubSearchRepository.search(queryStringQuery("id:" + club.getId())))
            .thenReturn(Collections.singletonList(club));
        // Search the club
        restClubMockMvc.perform(get("/api/_search/clubs?query=id:" + club.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(club.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].domain").value(hasItem(DEFAULT_DOMAIN)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Club.class);
        Club club1 = new Club();
        club1.setId(1L);
        Club club2 = new Club();
        club2.setId(club1.getId());
        assertThat(club1).isEqualTo(club2);
        club2.setId(2L);
        assertThat(club1).isNotEqualTo(club2);
        club1.setId(null);
        assertThat(club1).isNotEqualTo(club2);
    }

    //works fine
    @Test
    @WithMockUser
    public void shouldReturnOkAndCreateAJoinClubRequest() throws Exception {
        int joinClubRequestBeforeCreateTheRequest = joinClubRequestRepository.findAll().size();
        clubService.save(club);

        restClubMockMvc.perform(get("/api/clubs/join/{id}", club.getId())
            .contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        List<JoinClubRequest> joinClubRequestsList = joinClubRequestRepository.findAll();
        assertThat(joinClubRequestsList).hasSize(joinClubRequestBeforeCreateTheRequest + 1);
        JoinClubRequest addedJoinClubRequest = joinClubRequestsList.get(joinClubRequestBeforeCreateTheRequest);
        assertThat(addedJoinClubRequest.getStatus() == Status.PENDING);

    }

    //works fine
    @Test
    @WithMockUser
    public void shouldReturnBadRequestWhenClubIdIsNotValid() throws Exception {
        int joinClubRequestBeforeCreateTheRequest = joinClubRequestRepository.findAll().size();
        Long NotAClubId = 3000L;

        restClubMockMvc.perform(get("/api/clubs/join/{id}", NotAClubId)
            .contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNotFound());
        List<JoinClubRequest> joinClubRequestsList = joinClubRequestRepository.findAll();
        assertThat(joinClubRequestsList).hasSize(joinClubRequestBeforeCreateTheRequest);

    }

    //works fine
    @Test
    public void shouldReturnBadRequestWhenUserIsNotAuthenticated() throws Exception {
        clubService.save(club);
        restClubMockMvc.perform(get("/api/clubs/join/{id}", club.getId())
            .contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isUnauthorized());
    }

    // works fine
    @Test
    @WithMockUser(username = "user", password = "user", roles = "USER")
    public void shouldReturnBadRequestWhenRequestAlreadyExists() throws Exception {
        clubService.save(club);
        int joinClubRequestBeforeCreateTheRequestSize = joinClubRequestRepository.findAll().size();

        User user = userRepository.findOneByLogin("user").get();
        JoinClubRequest request1 = new JoinClubRequest();
        request1.setUser(user);
        request1.setClub(club);
        request1.setRequestTime(LocalDate.now());
        request1.setStatus(Status.PENDING);
        joinClubRequestService.save(request1);
        assertThat(joinClubRequestRepository.findAll().size()).isEqualTo(joinClubRequestBeforeCreateTheRequestSize + 1);
        restClubMockMvc.perform(get("/api/clubs/join/{id}", club.getId())
            .contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isBadRequest());
        assertThat(joinClubRequestRepository.findAll().size()).isEqualTo(joinClubRequestBeforeCreateTheRequestSize + 1);

    }

    //works fine
    @Test
    @WithMockUser(username = "user", password = "user", roles = "USER")
    public void shouldReturnOkAndSetStatusRequestDeleted() throws Exception {
        int clubsCount = clubRepository.findAll().size();
        clubService.save(club);
        assertThat(clubRepository.findAll().size()).isEqualTo(clubsCount + 1);

        club = clubRepository.findAll().get(clubsCount);
        JoinClubRequest joinClubRequest = new JoinClubRequest()
            .requestTime(LocalDate.now());

        User user;
        user = userRepository.findOneByLogin("user").get();
        joinClubRequest.setUser(user);
        joinClubRequest.setClub(club);
        joinClubRequest.setStatus(Status.PENDING);
        int size = joinClubRequestRepository.findAll().size();
        joinClubRequestRepository.save(joinClubRequest);
        joinClubRequestService.save(joinClubRequest);
        assertThat(joinClubRequestRepository.findAll().size()).isEqualTo(size + 1);
        restClubMockMvc.perform(get("/api/clubs/deleteJoin/{id}", club.getId())
            .contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());
        assertThat(joinClubRequest.getStatus().equals(Status.DELETED));
    }

    // works fine
    @Test
    @WithMockUser(username = "user", password = "user", roles = "USER")
    public void shouldReturnBadRequestWhenUserAlreadyMember() throws Exception {
        int clubsCount = clubRepository.findAll().size();
        int joinRequestsCount = joinClubRequestRepository.findAll().size();
        User user = userRepository.findOneByLogin("user").get();
        club.members(new HashSet());
        user.setClubs(new HashSet<>());
        club.addMember(user);

        club = clubService.save(club);
        assertThat(clubRepository.findAll().size()).isEqualTo(clubsCount + 1);

        restClubMockMvc.perform(get("/api/clubs/join/{id}", club.getId())
            .contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isBadRequest());

        assertThat(joinClubRequestRepository.findAll().size()).isEqualTo(joinRequestsCount);
    }

    @Test
    @WithMockUser(username = "user", password = "user", roles = "USER")
    public void shouldReturnBadRequestWhenRequestIsNotSentByAuthenticatedUserAndAsksToBeDeleted() throws Exception {
        int joinRequestCount = joinClubRequestRepository.findAll().size();
        User user = userRepository.findOneByLogin("user").get();
        User admin = userRepository.findOneByLogin("admin").get();

        club = clubService.save(club);
        JoinClubRequest joinClubRequestADMIN = new JoinClubRequest();
        joinClubRequestADMIN.setUser(admin);
        joinClubRequestADMIN.setClub(club);
        joinClubRequestADMIN.setRequestTime(LocalDate.now());
        joinClubRequestADMIN.status(Status.PENDING);
        joinClubRequestRepository.save(joinClubRequestADMIN);
        assertThat(joinClubRequestRepository.findAll().size()).isEqualTo(joinRequestCount + 1);

        restClubMockMvc.perform(get("/api/clubs/deleteJoin/{id}", club.getId())
            .contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isBadRequest());

        joinClubRequestADMIN = joinClubRequestRepository.findAll().get(joinRequestCount);
        assertThat(joinClubRequestADMIN.getStatus()).isEqualTo(Status.PENDING);

    }

    @Test
    @WithMockUser(username = "president", roles = "PRESIDENT")
    public void shouldReturnOKAndAcceptRequest() throws Exception {
        User user = new User();
        user.setFirstName("Member");
        user.setLastName("Member");
        user.setLogin("Member");
        user.setPassword("$2y$12$ZYPDAI1tNHe07AS.w5m28.BsSHCrTj4AOp34n/euJx0MfoVjG/Zla");
        userRepository.save(user);
        User president = new User();
        president.setFirstName("president");
        president.setLastName("president");
        president.setLogin("president");
        president.setPassword("$2y$12$ZYPDAI1tNHe07AS.w5m28.BsSHCrTj4AOp34n/euJx0MfoVjG/Zla");
        userRepository.save(president);
        Club club = new Club();
        club.setId((long) 1);
        club.setName("Existing club");
        club.setDomain("Anything");
        club.setPresident(president);
        clubRepository.save(club);
        JoinClubRequest joinClubRequest = new JoinClubRequest();
        joinClubRequest.setId((long) 1);
        joinClubRequest.setUser(user);
        joinClubRequest.setClub(club);
        joinClubRequest.setRequestTime(LocalDate.now());
        joinClubRequest.setStatus(Status.PENDING);
        joinClubRequestRepository.save(joinClubRequest);
        restClubMockMvc.perform(get("/api/acceptJoin/joinClubRequests/{id}", joinClubRequest.getId())
            .contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());
        joinClubRequest = joinClubRequestRepository.getOne(joinClubRequest.getId());
        assertThat(joinClubRequest.getStatus()).isEqualTo(Status.ACCEPTED);
    }

    @Test
    @WithMockUser(username = "president", roles = "PRESIDENT")
    public void shouldReturnOKAndRejectRequest() throws Exception {
        User user = new User();
        user.setFirstName("Member");
        user.setLastName("Member");
        user.setLogin("Member");
        user.setPassword("$2y$12$ZYPDAI1tNHe07AS.w5m28.BsSHCrTj4AOp34n/euJx0MfoVjG/Zla");
        userRepository.save(user);
        User president = new User();
        president.setFirstName("president");
        president.setLastName("president");
        president.setLogin("president");
        president.setPassword("$2y$12$ZYPDAI1tNHe07AS.w5m28.BsSHCrTj4AOp34n/euJx0MfoVjG/Zla");
        userRepository.save(president);
        Club club = new Club();
        club.setId((long) 1);
        club.setName("Existing club");
        club.setDomain("Anything");
        club.setPresident(president);
        clubRepository.save(club);
        JoinClubRequest joinClubRequest = new JoinClubRequest();
        joinClubRequest.setId((long) 1);
        joinClubRequest.setUser(user);
        joinClubRequest.setClub(club);
        joinClubRequest.setRequestTime(LocalDate.now());
        joinClubRequest.setStatus(Status.PENDING);
        joinClubRequestRepository.save(joinClubRequest);
        restClubMockMvc.perform(get("/api/declineJoin/joinClubRequests/{id}", joinClubRequest.getId())
            .contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());
        joinClubRequest = joinClubRequestRepository.getOne(joinClubRequest.getId());
        assertThat(joinClubRequest.getStatus()).isEqualTo(Status.REJECTED);
    }

    @Test
    public void shouldReturnUnauthorized() throws Exception {
        User user = new User();
        user.setFirstName("Member");
        user.setLastName("Member");
        user.setLogin("Member");
        user.setPassword("$2y$12$ZYPDAI1tNHe07AS.w5m28.BsSHCrTj4AOp34n/euJx0MfoVjG/Zla");
        userRepository.save(user);
        User president = new User();
        president.setFirstName("president");
        president.setLastName("president");
        president.setLogin("president");
        president.setPassword("$2y$12$ZYPDAI1tNHe07AS.w5m28.BsSHCrTj4AOp34n/euJx0MfoVjG/Zla");
        userRepository.save(president);
        Club club = new Club();
        club.setId((long) 1);
        club.setName("Existing club");
        club.setDomain("Anything");
        club.setPresident(president);
        clubRepository.save(club);
        JoinClubRequest joinClubRequest = new JoinClubRequest();
        joinClubRequest.setId((long) 1);
        joinClubRequest.setUser(user);
        joinClubRequest.setClub(club);
        joinClubRequest.setRequestTime(LocalDate.now());
        joinClubRequest.setStatus(Status.PENDING);
        joinClubRequestRepository.save(joinClubRequest);
        restClubMockMvc.perform(get("/api/declineJoin/joinClubRequests/{id}", joinClubRequest.getId())
            .contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isUnauthorized());
        joinClubRequest = joinClubRequestRepository.getOne(joinClubRequest.getId());
        assertThat(joinClubRequest.getStatus()).isEqualTo(Status.PENDING);
    }

    @Test
    @WithMockUser(username = "member", roles = "PRESIDENT")
    public void shouldReturnBadRequest() throws Exception {
        User user = new User();
        user.setFirstName("member");
        user.setLastName("member");
        user.setLogin("member");
        user.setPassword("$2y$12$ZYPDAI1tNHe07AS.w5m28.BsSHCrTj4AOp34n/euJx0MfoVjG/Zla");
        userRepository.save(user);
        User president = new User();
        president.setFirstName("president");
        president.setLastName("president");
        president.setLogin("president");
        president.setPassword("$2y$12$ZYPDAI1tNHe07AS.w5m28.BsSHCrTj4AOp34n/euJx0MfoVjG/Zla");
        userRepository.save(president);
        Club club = new Club();
        club.setId((long) 1);
        club.setName("Existing club");
        club.setDomain("Anything");
        club.setPresident(president);
        clubRepository.save(club);
        JoinClubRequest joinClubRequest = new JoinClubRequest();
        joinClubRequest.setId(new Long(1));
        joinClubRequest.setUser(user);
        joinClubRequest.setClub(club);
        joinClubRequest.setRequestTime(LocalDate.now());
        joinClubRequest.setStatus(Status.PENDING);
        joinClubRequestRepository.save(joinClubRequest);
        restClubMockMvc.perform(get("/api/declineJoin/joinClubRequests/{id}", joinClubRequest.getId())
            .contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isBadRequest());
        joinClubRequest = joinClubRequestRepository.getOne(joinClubRequest.getId());
        assertThat(joinClubRequest.getStatus()).isEqualTo(Status.PENDING);
    }
}
