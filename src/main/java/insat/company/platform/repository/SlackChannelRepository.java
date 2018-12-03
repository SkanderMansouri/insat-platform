package insat.company.platform.repository;

import insat.company.platform.domain.SlackChannel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SlackChannel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SlackChannelRepository extends JpaRepository<SlackChannel, Long> {


}
