package insat.company.platform.repository;

import insat.company.platform.domain.InsatEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the InsatEvent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InsatEventRepository extends JpaRepository<InsatEvent, Long> {

    @Query(value = "select distinct insat_event from InsatEvent insat_event left join fetch insat_event.members left join fetch insat_event.participants",
        countQuery = "select count(distinct insat_event) from InsatEvent insat_event")
    Page<InsatEvent> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct insat_event from InsatEvent insat_event left join fetch insat_event.members left join fetch insat_event.participants  left join fetch insat_event.clubs")
    List<InsatEvent> findAllWithEagerRelationships();

    @Query("select insat_event from InsatEvent insat_event left join fetch insat_event.members left join fetch insat_event.participants where insat_event.id =:id")
    Optional<InsatEvent> findOneWithEagerRelationships(@Param("id") Long id);

}
