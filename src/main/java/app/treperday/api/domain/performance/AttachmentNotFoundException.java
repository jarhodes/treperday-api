package app.treperday.api.domain.performance;

public class AttachmentNotFoundException extends RuntimeException {
	
	public AttachmentNotFoundException(Long id) {
		super("Could not find attachment " + id);
	}

}
