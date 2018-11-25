package insat.company.platform.repository;

import insat.company.platform.domain.Field;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the Field entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FieldRepository extends JpaRepository<Field, Long> {

    Optional<Field> findOneByYearAndSection(Long year, String section);
}
