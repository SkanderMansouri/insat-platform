package insat.company.platform.service.exceptions;

public class InexistantJoinRequestException extends RuntimeException {
    public InexistantJoinRequestException() {
        super("the join request no longer exists");
    }
}
