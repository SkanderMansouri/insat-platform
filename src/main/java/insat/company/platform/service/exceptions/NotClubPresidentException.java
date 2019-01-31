package insat.company.platform.service.exceptions;

public class NotClubPresidentException extends RuntimeException {
    public NotClubPresidentException(String userName, String clubName) {
        super("the user " + userName + " is not the president of " + clubName);
    }
}
