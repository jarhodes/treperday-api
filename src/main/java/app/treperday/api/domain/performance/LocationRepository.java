package app.treperday.api.domain.performance;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LocationRepository extends JpaRepository<Location, Long> {

	List<Location> findByPerformanceId(Long id);

	@Query(value = "SELECT * FROM location l WHERE performance_id IN "
			+ "(SELECT id FROM performance WHERE task_id = ?1 AND is_shared = 1 AND user_id NOT IN (?2)) "
			+ "ORDER BY RAND() LIMIT 5", nativeQuery = true)
	List<Location> findFiveRandomCommunityLocations(Long taskId, Long userId);
	
}
