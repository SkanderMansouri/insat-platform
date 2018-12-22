package insat.company.platform.repository;

import insat.company.platform.domain.Club;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Club entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {

    @Query(value = "select distinct club from Club club left join fetch club.members",
        countQuery = "select count(distinct club) from Club club")
    Page<Club> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct club from Club club left join fetch club.members")
    List<Club> findAllWithEagerRelationships();

    @Query("select club from Club club left join fetch club.members where club.id =:id")
    Optional<Club> findOneWithEagerRelationships(@Param("id") Long id);

    Optional<Club> findOneById(Long id);

    Optional<Club> findById(Long aLong);

}
