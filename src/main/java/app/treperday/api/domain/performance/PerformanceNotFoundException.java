package app.treperday.api.domain.performance;

public class PerformanceNotFoundException extends RuntimeException {
	
	public PerformanceNotFoundException(Long id) {
		super("Could not find performance " + id);
	}

}
