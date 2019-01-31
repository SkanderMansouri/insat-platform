package insat.company.platform.service.exceptions;

public class InexistantLoggedUserException extends RuntimeException {
    public InexistantLoggedUserException() {
        super("the logged user is no longer in the database");
    }
}
