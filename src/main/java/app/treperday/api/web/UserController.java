package app.treperday.api.web;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import app.treperday.api.domain.performance.PerformanceRepository;
import app.treperday.api.domain.task.Category;
import app.treperday.api.domain.task.CategoryCountObject;
import app.treperday.api.domain.task.CategoryRepository;
import app.treperday.api.domain.task.TaskWeekObject;
import app.treperday.api.domain.user.RandomPasswordGenerator;
import app.treperday.api.domain.user.StatsObject;
import app.treperday.api.domain.user.User;
import app.treperday.api.domain.user.UserCreationException;
import app.treperday.api.domain.user.UserGeneratorKey;
import app.treperday.api.domain.user.UserNameObject;
import app.treperday.api.domain.user.UserRepository;

@RestController
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	private UserRepository repository;
	
	@Autowired
	private PerformanceRepository performanceRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
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
		addUser.setAvatarColour("#555");
		addUser.setCreated(new Date());
		repository.save(addUser);
		
		HashMap<String, String> returnValue = new HashMap<>();
		returnValue.put("username", username);
		returnValue.put("password", password);

		return returnValue;
	}
	
	@GetMapping("/details")
	public User showDetails(@AuthenticationPrincipal UserDetails principal) {
		User currUser = repository.findByUsername(principal.getUsername());
		return currUser;
	}
	

	@PutMapping("/update")
	public User updateUser(@RequestBody UserNameObject userNameObject, @AuthenticationPrincipal UserDetails principal) {
		User updateUser = repository.findByUsername(principal.getUsername());
		updateUser.setLastName(userNameObject.getLastName());
		updateUser.setFirstName(userNameObject.getFirstName());
		updateUser.setAvatarIcon(userNameObject.getAvatarIcon());
		updateUser.setAvatarColour(userNameObject.getAvatarColour());
		return repository.save(updateUser);
	}
	
	@GetMapping("/stats")
	public StatsObject getUserStats(@AuthenticationPrincipal UserDetails principal) {
		
		User user = repository.findByUsername(principal.getUsername());
		StatsObject stats = new StatsObject();
		
		// Total number of tasks
		Long numTasks = performanceRepository.countByUserId(user.getId());
		stats.setNumTasks(numTasks);
		
		// Total number of completed tasks
		Long numCompleted = performanceRepository.countByUserIdAndIsCompleted(user.getId(), true);
		stats.setNumCompleted(numCompleted);
		
		// Longest streak in days
		Long longestStreak = performanceRepository.findLongestStreak(user.getId());
		stats.setLongestStreak(longestStreak);
		
		// Tasks completed by category
		// Get categories
		List<Category> categoryList = categoryRepository.findAll();
		List<CategoryCountObject> categoryCount = new ArrayList<CategoryCountObject>();
		
		for (Category category : categoryList) {
			CategoryCountObject obj = new CategoryCountObject();
			obj.setCategory(category);
			obj.setNum(performanceRepository.findCompletedCategoryCount(user.getId(), category.getId()));
			categoryCount.add(obj);
		}
		stats.setCategoryCount(categoryCount);
		
		// Tasks per week
		LocalDate today = LocalDate.now();
		LocalDate created = user.getCreated().toInstant()
			      .atZone(ZoneId.systemDefault()).toLocalDate();
		List<TaskWeekObject> taskWeeks = new ArrayList<TaskWeekObject>();
		TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
		for (LocalDate i = created; i.compareTo(today) < 1; i = i.with(TemporalAdjusters.next(DayOfWeek.MONDAY))) {
			TaskWeekObject tasksInWeek = new TaskWeekObject();
			int year = i.getYear();
			int weekNum = i.get(woy);
			tasksInWeek.setYear(year);
			tasksInWeek.setWeekNum(weekNum);
			String yearWeek = "" + i.getYear() + i.get(woy);
			System.out.println("Yearweek is "+yearWeek);
			Integer numDone = performanceRepository.findCompletedByYearWeek(user.getId(), yearWeek).orElse(0);
			tasksInWeek.setNumCompleted(numDone);
			taskWeeks.add(tasksInWeek);
		}
		stats.setTaskWeeks(taskWeeks);
		
		return stats;
		
	}
	
}
