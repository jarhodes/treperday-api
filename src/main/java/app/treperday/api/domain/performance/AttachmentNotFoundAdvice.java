package app.treperday.api.domain.performance;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AttachmentNotFoundAdvice {
	
	@ResponseBody
	@ExceptionHandler(AttachmentNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String attachmentNotFoundHandler(AttachmentNotFoundException ex) {
		return ex.getMessage();
	}

}
