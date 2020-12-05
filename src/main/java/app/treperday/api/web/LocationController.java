package app.treperday.api.web;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import app.treperday.api.domain.performance.Attachment;
import app.treperday.api.domain.performance.AttachmentRepository;
import app.treperday.api.domain.performance.Location;
import app.treperday.api.domain.performance.LocationPutObject;
import app.treperday.api.domain.performance.LocationRepository;
import app.treperday.api.domain.performance.Performance;
import app.treperday.api.domain.performance.PerformanceRepository;
import app.treperday.api.domain.user.User;
import app.treperday.api.domain.user.UserRepository;

@RestController
@RequestMapping("/api/location")
public class LocationController {

	Logger logger = LoggerFactory.getLogger(LocationController.class);

	@Autowired
	private LocationRepository locationRepository;

	@Autowired
	private PerformanceRepository performanceRepository;

	@Autowired
	private UserRepository userRepository;

	@PostMapping("/addtoperformance/{id}")
	public Location addLocation(@RequestBody LocationPutObject saveLocation, @PathVariable("id") Long performanceId,
			@AuthenticationPrincipal UserDetails principal) {

		User user = userRepository.findByUsername(principal.getUsername());
		Performance performance = performanceRepository.findById(performanceId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource"));

		if (performance.getUser().getId() == user.getId()) {
			Location location = new Location();
			location.setPerformance(performance);
			location.setLatitude(saveLocation.getLatitude());
			location.setLongitude(saveLocation.getLongitude());
			return locationRepository.save(location);
		} else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No access to this resource");
		}

	}

	@GetMapping("/list/{id}")
	public List<Location> locationsByPerformance(@PathVariable Long id,
			@AuthenticationPrincipal UserDetails principal) {

		User user = userRepository.findByUsername(principal.getUsername());
		Performance performance = performanceRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource"));

		if (performance.getUser().getId() == user.getId()) {
			List<Location> locationList = locationRepository.findByPerformanceId(id);
			if (locationList.size() == 0) {
				throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Nothing to return");
			}
			return locationList;
		} else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No access to this resource");
		}
	}

	@GetMapping("/{id}")
	public Location getLocation(@PathVariable("id") Long locationId, @AuthenticationPrincipal UserDetails principal) {

		User user = userRepository.findByUsername(principal.getUsername());
		Location location = locationRepository.findById(locationId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource"));

		if (location.getPerformance().getUser().getId() == user.getId()) {
			return location;
		} 
		else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No access to resource");
		}

	}

	@GetMapping("/community/{id}")
	public Location getCommunityLocation(@PathVariable("id") Long locationId, @AuthenticationPrincipal UserDetails principal) {

		Location location = locationRepository.findById(locationId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource"));

		if (location.getPerformance().getIsShared() == true) {
			return location;
		} 
		else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No access to resource");
		}

	}
	

	@DeleteMapping("/{id}")
	public ResponseEntity<Long> deleteLocation(@PathVariable Long id, @AuthenticationPrincipal UserDetails principal) {

		User user = userRepository.findByUsername(principal.getUsername());
		Location location = locationRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource"));

		if (location.getPerformance().getUser().getId() == user.getId()) {
			locationRepository.delete(location);
			return new ResponseEntity<>(id, HttpStatus.OK);
		} else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot access this resource");
		}
	}

}
