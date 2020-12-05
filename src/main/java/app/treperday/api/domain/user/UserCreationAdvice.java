package app.treperday.api.domain.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserCreationAdvice {
	
	@ResponseBody
	@ExceptionHandler(UserCreationException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String userCreationHandler(UserCreationException ex) {
		return ex.getMessage();
	}
}
