package app.treperday.api.domain.performance;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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

	Long countByUserId(Long userId);
	
	Long countByUserIdAndIsCompleted(Long userId, boolean isCompleted);
	
	@Query(value = "SELECT COUNT(*) max_streak "
			+ "FROM ( SELECT x.*, CASE WHEN @prev=x.date - INTERVAL 1 DAY THEN @i \\:= @i ELSE @i \\:= @i+1 END i, "
			+ "@prev \\:= x.date "
			+ "FROM ( SELECT DISTINCT p.date FROM performance p WHERE p.user_id = ?1 ) x "
			+ "JOIN ( SELECT @prev \\:= null, @i \\:= 0 ) vars "
			+ "ORDER BY date ) a "
			+ "GROUP BY i "
			+ "ORDER BY max_streak DESC LIMIT 1;", nativeQuery = true)
	Long findLongestStreak(Long userId);
	
	@Query(value = "SELECT COUNT(p.id) FROM performance p JOIN task t "
			+ "ON p.task_id = t.id "
			+ "WHERE p.user_id = ?1 "
			+ "AND t.category_id = ?2 "
			+ "AND p.is_completed = '1'", nativeQuery = true)
	Long findCompletedCategoryCount(Long userId, Long categoryId);

	@Query(value = "SELECT COUNT(p.id) "
			+ "FROM performance p "
			+ "WHERE p.user_id = ?1 AND YEAR(p.date) = ?2 AND WEEKOFYEAR(p.date) = ?3 AND is_completed = '1'"
			+ "GROUP BY WEEKOFYEAR(p.date)", nativeQuery = true)
	Optional<Integer> findCompletedByYearWeek(Long userId, int year, int weekNo);
	
}
