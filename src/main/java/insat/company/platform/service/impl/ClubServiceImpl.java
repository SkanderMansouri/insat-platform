package insat.company.platform.service.impl;

import insat.company.platform.domain.Club;
import insat.company.platform.domain.JoinClubRequest;
import insat.company.platform.domain.User;
import insat.company.platform.domain.enumeration.Status;
import insat.company.platform.repository.ClubRepository;
import insat.company.platform.repository.JoinClubRequestRepository;
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

    private final UserSearchRepository userSearchRepository;

    private final JoinClubRequestRepository joinClubRequestRepository;

    private final JoinClubRequestService joinClubRequestService;

    public ClubServiceImpl(ClubRepository clubRepository, ClubSearchRepository clubSearchRepository, UserSearchRepository userSearchRepository, JoinClubRequestRepository joinClubRequestRepository, JoinClubRequestService joinClubRequestService) {
        this.clubRepository = clubRepository;
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

    @Override
    public JoinClubRequest sendClubJoinRequest(Club club, User user) {
        if (!(club.hasMember(user))) {
            return (JoinClubRequest) Optional.ofNullable(joinClubRequestRepository.findOneByUserAndClubAndStatus(user, club,Status.PENDING))
                .map( obj -> null)

                .orElseGet(() -> {
                JoinClubRequest joinClubRequest = new JoinClubRequest();
                joinClubRequest.setClub(club);
                joinClubRequest.setUser(user);
                joinClubRequest.setRequestTime(LocalDate.now());
                joinClubRequest.setStatus(Status.PENDING);
                log.info("{} {} Request Created  {} successfully !", joinClubRequest.getUser().getId(), joinClubRequest.getClub().getId());
                //return joinClubRequestRepository.save(joinClubRequest);

                return joinClubRequestRepository.findOneByUserAndClubAndStatus(user, club,Status.PENDING);
            });
        } else {
            log.info("{} {} Request to the same club from the same user already sent");
            throw new IllegalArgumentException("You cannot send two requests");

        }
    }


    @Override
    public void deleteJoinRequest(Club club, User user) {
        joinClubRequestRepository.findOneByUserAndClubAndStatus(user, club, Status.PENDING).map(
            RequestToDelete -> {
                RequestToDelete.setStatus(Status.DELETED);
                log.info("Accepting to delete join Request ");
                return new ResponseEntity(HttpStatus.OK);
            }
            ).orElseThrow(IllegalArgumentException::new);
    }
}
