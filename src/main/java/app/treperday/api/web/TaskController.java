package app.treperday.api.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import app.treperday.api.domain.performance.Performance;
import app.treperday.api.domain.performance.PerformanceRepository;
import app.treperday.api.domain.task.CategoryRepository;
import app.treperday.api.domain.task.PerformanceListByDate;
import app.treperday.api.domain.task.Task;
import app.treperday.api.domain.task.TaskDate;
import app.treperday.api.domain.task.TaskRepository;
import app.treperday.api.domain.user.User;
import app.treperday.api.domain.user.UserRepository;

@RestController
@RequestMapping("/api/task")
public class TaskController {

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private PerformanceRepository performanceRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/tasksbydate")
	public List<Performance> getTasksByDate(
			@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date,
			@AuthenticationPrincipal UserDetails principal) {

		User user = userRepository.findByUsername(principal.getUsername());
		Long userId = user.getId();

		List<Performance> todaysTasks = performanceRepository.findByUserIdAndDate(userId, date);

		if (todaysTasks.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
		} else {
			return todaysTasks;
		}
	}

	@PostMapping("/assignthree")
	public List<Performance> assignThreeTasks(@AuthenticationPrincipal UserDetails principal) {
		
		User user = userRepository.findByUsername(principal.getUsername());
		Date date = new Date();
		
		List<Task> tasks = taskRepository.findThreeRandomTasks();
		List<Performance> output = new ArrayList<>();
		for (Task task : tasks) {
			Performance performance = new Performance(user, task, date);
			output.add(performanceRepository.save(performance));
		}
		return output;
	}

	@GetMapping("/list/taskdates")
	public List<PerformanceListByDate> listTaskDates(@AuthenticationPrincipal UserDetails principal) {
		
		User user = userRepository.findByUsername(principal.getUsername());
		List<Date> dateList = performanceRepository.findUniqueDateByUserId(user.getId());
		
		List<PerformanceListByDate> output = new ArrayList<>();
		
		for (Date date : dateList) {
			PerformanceListByDate performanceList = new PerformanceListByDate(date);
			performanceList.setList(performanceRepository.findByUserIdAndDate(user.getId(), date));
			output.add(performanceList);
		}
		
		return output;
		
	}

}
