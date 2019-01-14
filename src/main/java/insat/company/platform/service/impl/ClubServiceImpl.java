package insat.company.platform.service.impl;

import insat.company.platform.domain.Club;
import insat.company.platform.domain.JoinClubRequest;
import insat.company.platform.domain.User;
import insat.company.platform.domain.enumeration.Status;
import insat.company.platform.repository.ClubRepository;
import insat.company.platform.repository.JoinClubRequestRepository;
import insat.company.platform.repository.UserRepository;
import insat.company.platform.repository.search.ClubSearchRepository;
import insat.company.platform.repository.search.UserSearchRepository;
import insat.company.platform.security.SecurityUtils;
import insat.company.platform.service.ClubService;
import insat.company.platform.service.JoinClubRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing Club.
 */
@Service
@Transactional
public class ClubServiceImpl implements ClubService {

    private final Logger log = LoggerFactory.getLogger(ClubServiceImpl.class);

    private final ClubRepository clubRepository;

    private final ClubSearchRepository clubSearchRepository;

    private final UserRepository userRepository;

    private final UserSearchRepository userSearchRepository;

    private final JoinClubRequestRepository joinClubRequestRepository;

    private final JoinClubRequestService joinClubRequestService;

    public ClubServiceImpl(ClubRepository clubRepository, ClubSearchRepository clubSearchRepository, UserSearchRepository userSearchRepository, JoinClubRequestRepository joinClubRequestRepository, JoinClubRequestService joinClubRequestService, UserRepository userRepository) {
        this.clubRepository = clubRepository;
        this.userRepository = userRepository;
        this.clubSearchRepository = clubSearchRepository;
        this.userSearchRepository = userSearchRepository;
        this.joinClubRequestRepository = joinClubRequestRepository;
        this.joinClubRequestService = joinClubRequestService;
    }

    /**
     * Save a club.
     *
     * @param club the entity to save
     * @return the persisted entity
     */
    @Override
    public Club save(Club club) {
        log.debug("Request to save Club : {}", club);
        Club result = clubRepository.save(club);
        clubSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the clubs.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Club> findAll() {
        log.debug("Request to get all Clubs");
        return clubRepository.findAllWithEagerRelationships();
    }

    /**
     * Get all the Club with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<Club> findAllWithEagerRelationships(Pageable pageable) {
        return clubRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one club by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Club> findOne(Long id) {
        log.debug("Request to get Club : {}", id);
        return clubRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the club by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Club : {}", id);
        clubRepository.deleteById(id);
        clubSearchRepository.deleteById(id);
    }

    /**
     * Search for the club corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Club> search(String query) {
        log.debug("Request to search Clubs for query {}", query);
        return StreamSupport
            .stream(clubSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

    /**
     * @return a list of all the clubs
     */
    public List<Club> getClubsList() {

        return clubRepository.findAll();
    }

    @Override
    public JoinClubRequest sendClubJoinRequest(Club club, User user) {
        if (!(club.hasMember(user))) {
            return Optional.ofNullable(joinClubRequestRepository.findOneByUserAndClubAndStatus(user, club, Status.PENDING))
                .map((obj) -> {
                    if (obj.isPresent()) {
                        return null;
                    }
                    JoinClubRequest joinClubRequest = new JoinClubRequest();
                    joinClubRequest.setClub(club);
                    joinClubRequest.setUser(user);
                    joinClubRequest.setRequestTime(LocalDate.now());
                    joinClubRequest.setStatus(Status.PENDING);
                    joinClubRequest = joinClubRequestRepository.save(joinClubRequest);
                    log.info("{} {} Request Created  {} successfully !", joinClubRequest.getUser().getId(), joinClubRequest.getClub().getId());
                    return joinClubRequest;
                })
                .orElseGet(() -> {
                    JoinClubRequest joinClubRequest = new JoinClubRequest();
                    joinClubRequest.setId(-1L);
                    joinClubRequest.setStatus(Status.PENDING);
                    joinClubRequest.setClub(club);
                    joinClubRequest.setUser(user);
                    return joinClubRequest;
                });
        } else {
            log.info("{} {} Request to the same club from the same user already sent");
            throw new IllegalArgumentException("You cannot send two requests");

        }
    }


    @Override
    public void deleteJoinRequest(Club club, User user) {
        joinClubRequestRepository.findOneByUserAndClubAndStatus(user, club, Status.PENDING).map(
            requestToDelete -> {
                requestToDelete.status(Status.DELETED);
                log.info("Accepting to delete join Request");
                return new ResponseEntity(HttpStatus.OK);
            }).orElseThrow(IllegalArgumentException::new);
    }

    @PreAuthorize("hasRole('ROLE_PRESIDENT')")
    public boolean verifyAccessToJoinClubRequest(Optional<JoinClubRequest> joinClubRequest) {
        boolean result = joinClubRequest.map(joinClubRequestExists -> {
            boolean result2 = SecurityUtils.getCurrentUserLogin()
                .flatMap(userRepository::findOneByLogin)
                .map(user -> {
                    boolean result3 = Optional.ofNullable(joinClubRequestExists.getUser().getLogin())
                        .flatMap(userRepository::findOneByLogin)
                        .map(joinClubRequestUser -> {
                            log.info("Accepting {}'s request to join {}", joinClubRequestExists.getUser().getFirstName(), joinClubRequestExists.getClub().getName());
                            if (!(clubRepository.findOneById(joinClubRequestExists.getClub().getId()).isPresent())) {
                                log.error("the club {} no longer exists", joinClubRequestExists.getClub().getName());
                                return false;
                            } else if (!(user.equals(joinClubRequestExists.getClub().getPresident()))) {
                                log.error("{} {} is not the president of {}", user.getFirstName(), user.getLastName(), joinClubRequestExists.getClub().getName());
                                return false;
                            } else if (!(joinClubRequestExists.getStatus().equals(Status.PENDING))) {
                                switch (joinClubRequestExists.getStatus()) {
                                    case ACCEPTED:
                                        log.error("the request is already accepted");
                                        break;
                                    case REJECTED:
                                        log.error("the request is already rejected");
                                        break;
                                    case DELETED:
                                        log.error("the request was deleted by the user");
                                        break;
                                    default:
                                        log.error("Error while treating the request status");
                                }
                                return false;
                            }
                            log.info("No errors found");
                            return true;
                        })
                        .orElseGet(() -> {
                            log.error("User in the joinClubRequest doesn't exist in the database !");
                            return false;
                        });
                    return result3;
                })
                .orElseGet(() -> {
                    log.error("Logged user doesn't exist in the database !");
                    return false;
                });
            return result2;
        })
            .orElseGet(() -> {
                log.error("this JoinClubRequest doesn't exist !");
                return false;
            });
        return result;
    }

    @PreAuthorize("hasRole('ROLE_PRESIDENT')")
    public void acceptJoinClubRequest(Optional<JoinClubRequest> joinClubRequest) {
        joinClubRequest.map(joinClubRequestExists -> {
            if (verifyAccessToJoinClubRequest(joinClubRequest)) {
                Club club = joinClubRequestExists.getClub();
                User user = joinClubRequestExists.getUser();
                joinClubRequestExists.setStatus(Status.ACCEPTED);
                club.addMember(user);
                user.getClubs().add(club);
                userRepository.save(user);
                clubRepository.save(club);
                joinClubRequestRepository.save(joinClubRequestExists);
                log.info("{} {} joined {} successfully !", user.getFirstName(), user.getLastName(), club.getName());
            } else log.error("There was an error while accepting the request !");
            return null;
        })
            .orElse(null);
    }

    @PreAuthorize("hasRole('ROLE_PRESIDENT')")
    public void declineJoinClubRequest(Optional<JoinClubRequest> joinClubRequest) {
        joinClubRequest.map(joinClubRequestExists -> {
            if (verifyAccessToJoinClubRequest(joinClubRequest)) {
                joinClubRequestExists.setStatus(Status.REJECTED);
                joinClubRequestRepository.save(joinClubRequestExists);
                log.info("The request was rejected successfully !");
            } else log.error("There was an error while rejecting the request !");
            return null;
        })
            .orElse(null);
    }

}
