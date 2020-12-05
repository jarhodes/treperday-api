package app.treperday.api.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import app.treperday.api.domain.user.User;
import app.treperday.api.domain.user.UserRegistrationForm;
import app.treperday.api.domain.user.UserRepository;

@Controller
@RequestMapping("/admin/user")
public class AdminUserController {

	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("")
	public String userList(Model model) {
		model.addAttribute("users", userRepository.findAll());
		return "admin/userlist";
	}
	
	@GetMapping("/create")
	public String createForm(Model model) {
		model.addAttribute("userregistrationform", new UserRegistrationForm());
		model.addAttribute("title", "Create user");
		return "admin/registrationform";
	}
	
	@PostMapping("/create")
	public String createUser(@Validated @ModelAttribute("userform") UserRegistrationForm newUser, BindingResult bindingResult) {
		// Check for errors as per annotations in UserRegistrationForm class
		if (bindingResult.hasErrors()) {
			return "admin/registrationform";
		}
		// Check that the passwords match
		if (!newUser.getPassword().equals(newUser.getPasswordVerify())) {
			bindingResult.rejectValue("passwordVerify", "err.passwordcheck", "Passwords do not match!");
			return "admin/registrationform";
		}
		// Check that the username does not already exist
		User checkUser = userRepository.findByUsername(newUser.getUsername());
		if (checkUser != null) {
			bindingResult.rejectValue("username", "err.username", "This username is already registered");
			return "admin/registrationform";
		}
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String hashPassword = encoder.encode(newUser.getPassword());
		
		User createUser = new User();
		createUser.setLastName(newUser.getLastName());
		createUser.setFirstName(newUser.getFirstName());
		createUser.setUsername(newUser.getUsername());
		createUser.setRole(newUser.getRole());
		createUser.setPassword(hashPassword);
		userRepository.save(createUser);
		
		return "redirect:/admin/user";
	}
	
	@PostMapping("/save")
	public String saveUser(User user) {
		userRepository.save(user);
		return "redirect:/admin/user";
	}
	
	@GetMapping("/edit/{id}")
	public String editUser(@PathVariable("id") Long id, Model model) {
		User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user id " + id));
		model.addAttribute("user", user);
		model.addAttribute("title", "Edit user");
		return "admin/userform";
	}
	
	@GetMapping("/delete/{id}")
	public String deleteUser(@PathVariable("id") Long id, Model model) {
		User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user id " + id));
		model.addAttribute("user", user);
		model.addAttribute("title", "Delete user");
		return "admin/deleteuser";
	}
	
	@PostMapping("/delete")
	public String deleteUserConfirmed(User user, Model model) {
		userRepository.delete(user);
		return "redirect:/admin/user";
	}
}
