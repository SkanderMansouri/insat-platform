package insat.company.platform.service.impl;

import insat.company.platform.service.ClubService;
import insat.company.platform.domain.Club;
import insat.company.platform.repository.ClubRepository;
import insat.company.platform.repository.search.ClubSearchRepository;
import insat.company.platform.service.dto.ClubDTO;
import insat.company.platform.service.mapper.ClubMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Club.
 */
@Service
@Transactional
public class ClubServiceImpl implements ClubService {

    private final Logger log = LoggerFactory.getLogger(ClubServiceImpl.class);

    private final ClubRepository clubRepository;

    private final ClubMapper clubMapper;

    private final ClubSearchRepository clubSearchRepository;

    public ClubServiceImpl(ClubRepository clubRepository, ClubMapper clubMapper, ClubSearchRepository clubSearchRepository) {
        this.clubRepository = clubRepository;
        this.clubMapper = clubMapper;
        this.clubSearchRepository = clubSearchRepository;
    }

    /**
     * Save a club.
     *
     * @param clubDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ClubDTO save(ClubDTO clubDTO) {
        log.debug("Request to save Club : {}", clubDTO);

        Club club = clubMapper.toEntity(clubDTO);
        club = clubRepository.save(club);
        ClubDTO result = clubMapper.toDto(club);
        clubSearchRepository.save(club);
        return result;
    }

    /**
     * Get all the clubs.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ClubDTO> findAll() {
        log.debug("Request to get all Clubs");
        return clubRepository.findAll().stream()
            .map(clubMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one club by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ClubDTO> findOne(Long id) {
        log.debug("Request to get Club : {}", id);
        return clubRepository.findById(id)
            .map(clubMapper::toDto);
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
    public List<ClubDTO> search(String query) {
        log.debug("Request to search Clubs for query {}", query);
        return StreamSupport
            .stream(clubSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(clubMapper::toDto)
            .collect(Collectors.toList());
    }
}
