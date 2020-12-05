package app.treperday.api.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import app.treperday.api.domain.performance.Performance;
import app.treperday.api.domain.performance.PerformancePutObject;
import app.treperday.api.domain.performance.PerformanceRepository;
import app.treperday.api.domain.task.TaskRepository;
import app.treperday.api.domain.user.User;
import app.treperday.api.domain.user.UserRepository;

@RestController
@RequestMapping("/api/performance")
public class PerformanceController {

	@Autowired
	private PerformanceRepository performanceRepository;

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/{id}")
	public Performance getPerformance(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetails principal) {

		User user = userRepository.findByUsername(principal.getUsername());
		Long userId = user.getId();

		Performance performance = performanceRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource"));

		if (performance.getUser().getId() == user.getId()) {
			return performance;
		} else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unable to find resource");
		}
	}

	@PutMapping("/{id}")
	public Performance replacePerformance(@RequestBody PerformancePutObject savePerformance, @PathVariable Long id,
			@AuthenticationPrincipal UserDetails principal) {

		User user = userRepository.findByUsername(principal.getUsername());

		Performance performance = performanceRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource"));

		if (performance.getUser().getId() == user.getId()) {
			performance.setPerformanceText(savePerformance.getPerformanceText());
			performance.setRating(savePerformance.getRating());
			performance.setIsShared(savePerformance.getIsShared());
			performance.setIsCompleted(savePerformance.getIsCompleted());
			return performanceRepository.save(performance);
		} else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unable to find resource");
		}
	}

}
