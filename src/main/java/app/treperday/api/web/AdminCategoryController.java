package app.treperday.api.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import app.treperday.api.domain.task.Category;
import app.treperday.api.domain.task.CategoryRepository;

@Controller
@RequestMapping("/admin/category")
public class AdminCategoryController {

	@Autowired
	private CategoryRepository categoryRepository;
	
	@GetMapping("")
	public String categoryList(Model model) {
		model.addAttribute("categories", categoryRepository.findAll());
		return "admin/categorylist";
	}
	
	@GetMapping("/create")
	public String createForm(Model model) {
		model.addAttribute("category", new Category());
		model.addAttribute("title", "Create category");
		return "admin/categoryform";
	}
	
	@PostMapping("/save")
	public String save(Category category) {
		categoryRepository.save(category);
		return "redirect:/admin/category";
	}
	
	@GetMapping("/edit/{id}")
	public String editCategory(@PathVariable("id") Long id, Model model) {
		Category category = categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid category id " + id));
		model.addAttribute("category", category);
		model.addAttribute("title", "Edit category");
		return "admin/categoryform";
	}
	
	@GetMapping("/delete/{id}")
	public String deleteCategory(@PathVariable("id") Long id, Model model) {
		Category category = categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid category id " + id));
		model.addAttribute("category", category);
		model.addAttribute("title", "Delete category");
		return "admin/deletecategory";
	}
	
	@PostMapping("/delete")
	public String deleteCategoryConfirmed(Category category, Model model) {
		categoryRepository.delete(category);
		return "redirect:/admin/category";
	}
}
