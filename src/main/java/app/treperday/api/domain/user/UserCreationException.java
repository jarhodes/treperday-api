package app.treperday.api.domain.user;

public class UserCreationException extends RuntimeException {
	
	public UserCreationException() {
		super("Unable to create new user from given parameters");
	}

}
