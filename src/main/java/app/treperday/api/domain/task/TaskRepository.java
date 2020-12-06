package app.treperday.api.domain.task;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TaskRepository extends JpaRepository<Task, Long> {

	@Query(value = "SELECT * FROM task t ORDER BY RAND() LIMIT 3", nativeQuery = true)
	List<Task> findThreeRandomTasks();
}
