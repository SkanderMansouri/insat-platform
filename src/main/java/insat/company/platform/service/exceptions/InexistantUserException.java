package insat.company.platform.service.exceptions;

public class InexistantUserException extends RuntimeException {
    public InexistantUserException() {
        super("the user of the join request is no longer in the database");
    }
}
