package com.pif.insatplatform.repository;

import com.pif.insatplatform.domain.Field;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Field entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FieldRepository extends JpaRepository<Field, Long> {

    @Query("select field from Field field where field.belongs.login = ?#{principal.username}")
    List<Field> findByBelongsIsCurrentUser();

}
