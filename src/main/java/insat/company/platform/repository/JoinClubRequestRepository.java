package insat.company.platform.repository;

import insat.company.platform.domain.Club;
import insat.company.platform.domain.JoinClubRequest;
import insat.company.platform.domain.User;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the JoinClubRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JoinClubRequestRepository extends JpaRepository<JoinClubRequest, Long> {

    @Query("select join_club_request from JoinClubRequest join_club_request where join_club_request.user.login = ?#{principal.username}")
    List<JoinClubRequest> findByUserIsCurrentUser();


    Optional<JoinClubRequest> findOneByUserAndClub(User user, Club club);
}
