package insat.company.platform.service.impl;

import insat.company.platform.InsatApp;
import insat.company.platform.domain.Authority;
import insat.company.platform.domain.Club;
import insat.company.platform.domain.JoinClubRequest;
import insat.company.platform.domain.User;
import insat.company.platform.domain.enumeration.Status;
import insat.company.platform.repository.ClubRepository;
import insat.company.platform.repository.UserRepository;
import insat.company.platform.service.ClubService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = InsatApp.class)
@Transactional
public class ClubServiceImplTest {

    @Autowired
    private ClubService clubService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClubRepository clubRepository;


    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "USER")
    public void shouldRefuseAccessToVerifyJoinClubRequest() {
        Optional<JoinClubRequest> joinClubRequest = Optional.empty();
        boolean acessApproval = clubService.verifyAccessToJoinClubRequest(joinClubRequest);
    }

    @Test
    @WithMockUser(roles = "PRESIDENT")
    public void shouldExpectANullJoinClubRequest() {
        Optional<JoinClubRequest> joinClubRequest = Optional.empty();
        assertFalse(clubService.verifyAccessToJoinClubRequest(joinClubRequest));
    }

    @Test
    @WithMockUser(username = "UnexistingUser", roles = "PRESIDENT")
    public void shouldExpectLoggedUserNotInDatabase() {
        User user = new User();
        user.setFirstName("ExistingUser");
        user.setLastName("ExistingUser");
        user.setLogin("test");
        user.setPassword("$2y$12$ZYPDAI1tNHe07AS.w5m28.BsSHCrTj4AOp34n/euJx0MfoVjG/Zla");
        Club club = new Club();
        club.setId((long) 1);
        club.setName("Club");
        club.setDomain("Nothing");
        clubRepository.save(club);
        JoinClubRequest joinClubRequest = new JoinClubRequest();
        joinClubRequest.setUser(user);
        joinClubRequest.setClub(club);
        Optional<JoinClubRequest> optJoinClubRequest = Optional.of(joinClubRequest);
        assertFalse(clubService.verifyAccessToJoinClubRequest(optJoinClubRequest));
    }

    @Test
    @WithMockUser(username = "test", roles = "PRESIDENT")
    public void shouldExpectUserInRequestNotFound() {
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("Test");
        user.setLogin("test");
        user.setPassword("$2y$12$ZYPDAI1tNHe07AS.w5m28.BsSHCrTj4AOp34n/euJx0MfoVjG/Zla");
        userRepository.save(user);
        User user2 = new User();
        user2.setFirstName("Test2");
        user2.setLastName("Test2");
        user2.setLogin("test2");
        user2.setPassword("$2y$12$ZYPDAI1tNHe07AS.w5m28.BsSHCrTj4AOp34n/euJx0MfoVjG/Zla");
        Club club = new Club();
        club.setId((long) 1);
        club.setName("Club");
        club.setDomain("Nothing");
        clubRepository.save(club);
        JoinClubRequest joinClubRequest = new JoinClubRequest();
        joinClubRequest.setUser(user2);
        joinClubRequest.setClub(club);
        Optional<JoinClubRequest> optJoinClubRequest = Optional.of(joinClubRequest);
        assertFalse(clubService.verifyAccessToJoinClubRequest(optJoinClubRequest));
    }

    @Test
    @WithMockUser(username = "test", roles = "PRESIDENT")
    public void shouldExpectClubNotFound() {
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("Test");
        user.setLogin("test");
        user.setPassword("$2y$12$ZYPDAI1tNHe07AS.w5m28.BsSHCrTj4AOp34n/euJx0MfoVjG/Zla");
        userRepository.save(user);
        Club club = new Club();
        club.setId((long) 404);
        club.setName("Unexisting club");
        club.setDomain("Nothing");
        clubRepository.save(club);
        JoinClubRequest joinClubRequest = new JoinClubRequest();
        joinClubRequest.setUser(user);
        joinClubRequest.setClub(club);
        Optional<JoinClubRequest> optJoinClubRequest = Optional.of(joinClubRequest);
        assertFalse(clubService.verifyAccessToJoinClubRequest(optJoinClubRequest));
    }

    @Test
    @WithMockUser(username = "notclubpresident", roles = "PRESIDENT")
    public void shouldExpectUserNotPresident() {
        User user = new User();
        user.setFirstName("notclubpresident");
        user.setLastName("notclubpresident");
        user.setLogin("notclubpresident");
        user.setPassword("$2y$12$ZYPDAI1tNHe07AS.w5m28.BsSHCrTj4AOp34n/euJx0MfoVjG/Zla");
        Authority authority = new Authority();
        authority.setName("ROLE_PRESIDENT");
        Set<Authority> autho = new HashSet<Authority>();
        autho.add(authority);
        user.setAuthorities(autho);
        userRepository.save(user);
        User president = new User();
        president.setFirstName("President");
        president.setLastName("President");
        president.setLogin("President");
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
        Optional<JoinClubRequest> optJoinClubRequest = Optional.of(joinClubRequest);
        assertFalse(clubService.verifyAccessToJoinClubRequest(optJoinClubRequest));
    }

    @Test
    @WithMockUser(username = "president", roles = "PRESIDENT")
    public void shouldExpectRequestNotPending() {
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
        joinClubRequest.setUser(president);
        joinClubRequest.setClub(club);
        joinClubRequest.setRequestTime(LocalDate.now());
        joinClubRequest.setStatus(Status.ACCEPTED);
        Optional<JoinClubRequest> optJoinClubRequest = Optional.of(joinClubRequest);
        assertFalse(clubService.verifyAccessToJoinClubRequest(optJoinClubRequest));
    }

    @Test
    @WithMockUser(username = "president", roles = "PRESIDENT")
    public void acceptJoinClubRequest() {
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
        Optional<JoinClubRequest> optJoinClubRequest = Optional.of(joinClubRequest);
        clubService.acceptJoinClubRequest(optJoinClubRequest);
    }

    @Test
    @WithMockUser(username = "president", roles = "PRESIDENT")
    public void declineJoinClubRequest() {
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
        Optional<JoinClubRequest> optJoinClubRequest = Optional.of(joinClubRequest);
        clubService.declineJoinClubRequest(optJoinClubRequest);
    }
}
