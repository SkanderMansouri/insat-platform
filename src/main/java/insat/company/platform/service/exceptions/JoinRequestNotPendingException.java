package insat.company.platform.service.exceptions;

public class JoinRequestNotPendingException extends RuntimeException {
    public JoinRequestNotPendingException() {
        super("the join request is not pending");
    }
}
