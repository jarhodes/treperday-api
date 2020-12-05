package app.treperday.api.web;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import app.treperday.api.domain.user.RandomPasswordGenerator;
import app.treperday.api.domain.user.User;
import app.treperday.api.domain.user.UserCreationException;
import app.treperday.api.domain.user.UserGeneratorKey;
import app.treperday.api.domain.user.UserRepository;

@RestController
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	private UserRepository repository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Value("${api.secret}")
	private String apiSecret;
	
	public UserController(UserRepository repository) {
		this.repository = repository;
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/create")
	public Map<String, String> createUser(@RequestBody UserGeneratorKey userGeneratorKey) throws UserCreationException {

		// Compare the posted key with the one in application.properties
		if (!userGeneratorKey.getKey().equals(apiSecret)) {
			throw new UserCreationException();
		}

		// Generate unique user ID and test for uniqueness
		String username = UUID.randomUUID().toString();
		while (repository.findByUsername(username) != null) {
			username = UUID.randomUUID().toString();
		}
		
		// Generate password
		RandomPasswordGenerator randomPassword = new RandomPasswordGenerator();
		String password = randomPassword.getRandomPassword();
		
		User addUser = new User();
		addUser.setUsername(username);
		addUser.setPassword(passwordEncoder.encode(password));
		repository.save(addUser);
		
		HashMap<String, String> returnValue = new HashMap<>();
		returnValue.put("username", username);
		returnValue.put("password", password);

		return returnValue;
	}
	
	@GetMapping("/test")
	@ResponseBody
	public String userTest() {
		return "Logged in";
	}
	
	@GetMapping("/details")
	public User showDetails(@AuthenticationPrincipal UserDetails principal) {
		User currUser = repository.findByUsername(principal.getUsername());
		return currUser;
	}
	
	@GetMapping("/details2")
	@ResponseBody
	@PreAuthorize("hasAuthority('API_USER')")
	public String showMore(@AuthenticationPrincipal UserDetails principal) {
		return principal.toString();
	}

	@GetMapping("/bcryptit")
	@ResponseBody
	public String bcryptIt(@RequestParam String source) {
		return passwordEncoder.encode(source);
	}
	
}
