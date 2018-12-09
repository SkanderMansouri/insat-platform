package insat.company.platform.service.impl;

import insat.company.platform.domain.Club;
import insat.company.platform.domain.JoinClubRequest;
import insat.company.platform.domain.User;
import insat.company.platform.domain.enumeration.StatusEnumeration;
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
    public Optional<JoinClubRequest> sendClubJoinRequest(Long clubId, String userLogin){

        Optional<String> OptUserLogin = SecurityUtils.getCurrentUserLogin();
        Optional<User> OptUser = userSearchRepository.findByLogin(OptUserLogin.get());
        if (OptUser.isPresent()) {
            Optional<Club> OptClub = clubRepository.findById(clubId);
            if (OptClub.isPresent()) {
                if(!(OptClub.get().isMember(OptUser.get()))) {
                    Optional<JoinClubRequest> requestPendingOfSameUser = joinClubRequestRepository.findOneByUserAndClub(OptUser.get(), OptClub.get());
                    if ((!requestPendingOfSameUser.isPresent())) {

                        LocalDate requestTime = LocalDate.now();
                        StatusEnumeration status = StatusEnumeration.PENDING;
                        JoinClubRequest joinClubRequest = new JoinClubRequest();
                        joinClubRequest.setClub(OptClub.get());
                        joinClubRequest.setUser(OptUser.get());
                        joinClubRequest.setRequestTime(requestTime);
                        joinClubRequest.setStatus(status);
                        log.info("{} {} Request Created  {} successfully !", joinClubRequest.getUser().getId(), joinClubRequest.getClub().getId());

                        return Optional.ofNullable(joinClubRequestRepository.save(joinClubRequest));

                    } else log.info("{} {} User already member !");

                }else log.info("{} {} Request to the same club from the same user already sent");
            }

        }
        log.info("{} {} Request Not Created !");
        return null ;
    }


    @Override
    public void deleteJoinRequest (Long clubId){

        Optional<String> OptUserLogin = SecurityUtils.getCurrentUserLogin();
        Optional<User> OptUser = userSearchRepository.findByLogin(OptUserLogin.get());
        if (OptUser.isPresent()) {
            Optional<Club> OptClub = clubRepository.findById(clubId);
            if (OptClub.isPresent()) {
                Club club =OptClub.get();
                User user =OptUser.get();
                Optional<JoinClubRequest> OptJoinClubRequest = joinClubRequestRepository.findOneByUserAndClub(user,club);

                if (OptJoinClubRequest.isPresent()){

                    JoinClubRequest joinRequestToDelete =OptJoinClubRequest.get();
                    joinClubRequestService.delete(joinRequestToDelete.getId());
                    log.info("Request Deleted ");
                }else log.info("{} {} Request Not found !");

            }log.info("{} {} Request Not found !");
        }
    }
}

