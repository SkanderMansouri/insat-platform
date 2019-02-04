package insat.company.platform.repository;

import insat.company.platform.domain.Club;
import insat.company.platform.domain.JoinClubRequest;
import insat.company.platform.domain.User;
import insat.company.platform.domain.enumeration.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.DoubleStream;

/**
 * Spring Data  repository for the JoinClubRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JoinClubRequestRepository extends JpaRepository<JoinClubRequest, Long> {

    @Query("select join_club_request from JoinClubRequest join_club_request where join_club_request.user.login = ?#{principal.username}")
    List<JoinClubRequest> findByUserIsCurrentUser();

    List<JoinClubRequest> findAllByClub(Club club);

    Optional<JoinClubRequest> findOneByUserAndClubAndStatusNot(User user, Club club, Status status);

    Optional<JoinClubRequest> findOneByUserAndClubAndStatus(User user, Club club, Status pending);

    Optional<JoinClubRequest> findOneById(Long id);
}
