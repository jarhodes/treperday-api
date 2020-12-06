package app.treperday.api.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import app.treperday.api.domain.task.Category;
import app.treperday.api.domain.task.CategoryRepository;
import app.treperday.api.domain.task.Task;
import app.treperday.api.domain.task.TaskRepository;

@Controller
@RequestMapping("/admin/task")
public class AdminTaskController {

	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@GetMapping("")
	public String taskList(Model model) {
		model.addAttribute("tasks", taskRepository.findAll());
		return "admin/tasklist";
	}
	
	@GetMapping("/create")
	public String createTask(Model model) {
		model.addAttribute("task", new Task());
		model.addAttribute("categories", categoryRepository.findAll());
		model.addAttribute("title", "Create task");
		return "admin/taskform";
	}
	
	@PostMapping("/save")
	public String saveTask(Task task) {
		taskRepository.save(task);
		return "redirect:/admin/task";
	}
	
	@GetMapping("/edit/{id}")
	public String editTask(@PathVariable("id") Long id, Model model) {
		Task task = taskRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource"));
		model.addAttribute("task", task);
		model.addAttribute("categories", categoryRepository.findAll());
		model.addAttribute("title", "Edit task");
		return "admin/taskform";
	}
	
	@GetMapping("/delete/{id}")
	public String deleteTask(@PathVariable("id") Long id, Model model) {
		Task task = taskRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid task id " + id));
		model.addAttribute("task", task);
		model.addAttribute("title", "Delete task");
		return "admin/deletetask";
	}
	
	@PostMapping("/delete")
	public String deleteTaskConfirmed(Task task, Model model) {
		taskRepository.delete(task);
		return "redirect:/admin/task";
	}
}
