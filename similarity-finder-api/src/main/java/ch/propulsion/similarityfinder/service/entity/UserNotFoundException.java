package ch.propulsion.similarityfinder.service.entity;

public class UserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1996134241417220378L;

	public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
}