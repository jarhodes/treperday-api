package app.treperday.api;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import app.treperday.api.domain.performance.AttachmentRepository;
import app.treperday.api.domain.performance.LocationRepository;
import app.treperday.api.domain.performance.PerformanceRepository;
import app.treperday.api.domain.task.CategoryRepository;
import app.treperday.api.domain.task.TaskRepository;
import app.treperday.api.domain.user.RandomPasswordGenerator;
import app.treperday.api.domain.user.User;
import app.treperday.api.domain.user.UserRepository;

@SpringBootApplication
public class TrePerDayApiApplication {

	private static final Logger log = LoggerFactory.getLogger(TrePerDayApiApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(TrePerDayApiApplication.class, args);
	}
	
	@Bean
	Path path(){
		return Paths.get(System.getProperty("java.io.tmpdir"));
	}

	@Bean
	public CommandLineRunner initDatabase(UserRepository userRepository,
			CategoryRepository categoryRepository, TaskRepository taskRepository, 
			AttachmentRepository attachmentRepository, PerformanceRepository performanceRepository,
			LocationRepository locationRepository) {
		return (args) -> {
			User adminUser = userRepository.findByUsername("jonathan");
			if (adminUser != null) {
				userRepository.delete(adminUser);
			}
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			RandomPasswordGenerator generator = new RandomPasswordGenerator();
			String password = generator.getRandomPassword();
			String hashedPassword = passwordEncoder.encode(password);
			User newAdminUser = new User("Rhodes", "Jonathan", "jonathan", hashedPassword, "ADMIN");
			userRepository.save(newAdminUser);
			log.info("Started with admin password " + password);

			log.info("Database initiated");
		};
	}
}