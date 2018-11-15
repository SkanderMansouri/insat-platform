package com.pif.insatplatform.repository;

import com.pif.insatplatform.domain.Integration;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Integration entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IntegrationRepository extends JpaRepository<Integration, Long> {

}
