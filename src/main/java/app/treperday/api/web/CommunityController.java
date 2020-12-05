package app.treperday.api.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.treperday.api.domain.performance.Attachment;
import app.treperday.api.domain.performance.AttachmentRepository;
import app.treperday.api.domain.performance.Location;
import app.treperday.api.domain.performance.LocationRepository;
import app.treperday.api.domain.performance.Performance;
import app.treperday.api.domain.performance.PerformanceRepository;
import app.treperday.api.domain.task.TaskRepository;
import app.treperday.api.domain.user.User;
import app.treperday.api.domain.user.UserRepository;

@RestController
@RequestMapping("/api/community")
public class CommunityController {

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private AttachmentRepository attachmentRepository;

	@Autowired
	private LocationRepository locationRepository;
	
	@Autowired
	private PerformanceRepository performanceRepository;
	
	@Autowired
	private UserRepository userRepository;

	@GetMapping("/attachmentsbytask/{id}")
	public List<Attachment> fetchFiveCommunityAttachments(@PathVariable("id") Long taskId,
			@AuthenticationPrincipal UserDetails principal) {

		User user = userRepository.findByUsername(principal.getUsername());
		return attachmentRepository.findFiveRandomCommunityAttachments(taskId, user.getId());
		
	}
	
	@GetMapping("/locationsbytask/{id}")
	public List<Location> fetchFiveCommunityLocations(@PathVariable("id") Long taskId,
			@AuthenticationPrincipal UserDetails principal) {
		
		User user = userRepository.findByUsername(principal.getUsername());
		return locationRepository.findFiveRandomCommunityLocations(taskId, user.getId());
		
	}
	
	@GetMapping("/performancesbytask/{id}")
	public List<Performance> fetchFiveCommunityPerformances(@PathVariable("id") Long taskId,
			@AuthenticationPrincipal UserDetails principal) {
		
		User user = userRepository.findByUsername(principal.getUsername());
		return performanceRepository.findFiveRandomCommunityLocations(taskId, user.getId());
		
	}
	

}
