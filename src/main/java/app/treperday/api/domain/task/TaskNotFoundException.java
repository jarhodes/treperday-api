package app.treperday.api.domain.task;

public class TaskNotFoundException extends RuntimeException {

	public TaskNotFoundException(Long id) {
		super("Could not find task " + id);
	}
	
}
