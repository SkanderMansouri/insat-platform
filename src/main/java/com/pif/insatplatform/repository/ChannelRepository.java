package com.pif.insatplatform.repository;

import com.pif.insatplatform.domain.Channel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Channel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long>, JpaSpecificationExecutor<Channel> {

}
