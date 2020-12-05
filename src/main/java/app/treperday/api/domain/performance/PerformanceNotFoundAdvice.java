package app.treperday.api.domain.performance;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PerformanceNotFoundAdvice {
	
	@ResponseBody
	@ExceptionHandler(PerformanceNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String performanceNotFoundHandler(PerformanceNotFoundException ex) {
		return ex.getMessage();
	}

}
