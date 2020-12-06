package app.treperday.api.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import app.treperday.api.domain.performance.Performance;
import app.treperday.api.domain.performance.PerformanceRepository;
import app.treperday.api.domain.task.Task;
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

	@GetMapping("/delete/{id}")
	public String deletePerformance(@PathVariable("id") Long id, Model model) {
		Performance performance = performanceRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid performance id " + id));
		model.addAttribute("performance", performance);
		model.addAttribute("title", "Delete performance");
		return "admin/deleteperformance";
	}
	
	@PostMapping("/delete")
	public String deletePerformanceConfirmed(Performance performance, Model model) {
		performanceRepository.delete(performance);
		return "redirect:/admin/performance";
	}
	
}
