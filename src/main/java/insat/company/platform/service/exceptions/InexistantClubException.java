package insat.company.platform.service.exceptions;

public class InexistantClubException extends RuntimeException {
    public InexistantClubException(String clubName) {
        super("the club " + clubName + " no longer exists");
    }
}
