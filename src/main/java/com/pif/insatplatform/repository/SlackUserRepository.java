package com.pif.insatplatform.repository;

import com.pif.insatplatform.domain.SlackUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SlackUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SlackUserRepository extends JpaRepository<SlackUser, Long> {

}
