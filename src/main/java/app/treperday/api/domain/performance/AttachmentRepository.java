package app.treperday.api.domain.performance;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

	List<Attachment> findByPerformanceId(Long id);
	
	Optional<Attachment> findByIdAndFetchToken(Long id, String token);
	
	@Query(value = "SELECT * FROM attachment a WHERE performance_id IN "
			+ "(SELECT id FROM performance WHERE task_id = ?1 AND is_shared = 1 AND user_id NOT IN (?2)) "
			+ "ORDER BY RAND() LIMIT 5", nativeQuery = true)
	List<Attachment> findFiveRandomCommunityAttachments(Long taskId, Long userId);
	
}
