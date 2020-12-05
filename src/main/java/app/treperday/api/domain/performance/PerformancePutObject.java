package app.treperday.api.domain.performance;

public class PerformancePutObject {

	private Long id;
	private String performanceText;
	private Integer rating;
	private Boolean isShared;
	private Boolean isCompleted;
	
	
	public Boolean getIsCompleted() {
		return isCompleted;
	}
	public void setIsCompleted(Boolean isCompleted) {
		this.isCompleted = isCompleted;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPerformanceText() {
		return performanceText;
	}
	public void setPerformanceText(String performanceText) {
		this.performanceText = performanceText;
	}
	public Integer getRating() {
		return rating;
	}
	public void setRating(Integer rating) {
		this.rating = rating;
	}
	public Boolean getIsShared() {
		return isShared;
	}
	public void setIsShared(Boolean isShared) {
		this.isShared = isShared;
	}

	
}
