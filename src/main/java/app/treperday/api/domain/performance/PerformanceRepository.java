package app.treperday.api.domain.performance;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PerformanceRepository extends JpaRepository<Performance, Long> {
	
	List<Performance> findByUserIdAndDate(Long userId, Date date);
	
	@Query(value = "SELECT * FROM performance p WHERE task_id = ?1 AND is_shared = 1 AND user_id NOT IN (?2) "
			+ "ORDER BY RAND() LIMIT 5", nativeQuery = true)
	List<Performance> findFiveRandomCommunityLocations(Long taskId, Long userId);
	
	List<Performance> findByUserIdOrderByDateDesc(Long userId);
	
	@Query(value = "SELECT DISTINCT(p.date) FROM performance p WHERE p.user_id = ?1 ORDER BY p.date DESC", nativeQuery = true)
	List<Date> findUniqueDateByUserId(Long userId);

}
