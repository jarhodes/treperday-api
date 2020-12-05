package app.treperday.api.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import app.treperday.api.domain.performance.PerformanceRepository;
import app.treperday.api.domain.task.TaskRepository;
import app.treperday.api.domain.user.UserRepository;

@Controller
@RequestMapping("/admin/performance")
public class AdminPerformanceController {

	@Autowired
	private PerformanceRepository performanceRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TaskRepository taskRepository;

	@GetMapping("")
	public String performanceList(Model model) {
		model.addAttribute("performances", performanceRepository.findAll());
		return "admin/performancelist";
	}

}
