package app.treperday.api.domain.user;

import java.util.List;

import app.treperday.api.domain.task.CategoryCountObject;
import app.treperday.api.domain.task.TaskWeekObject;

public class StatsObject {

	private Long numTasks;

	private Long numCompleted;

	private Long longestStreak;

	private List<CategoryCountObject> categoryCount;
	
	private List<TaskWeekObject> taskWeeks;
	
	public List<TaskWeekObject> getTaskWeeks() {
		return taskWeeks;
	}

	public void setTaskWeeks(List<TaskWeekObject> taskWeeks) {
		this.taskWeeks = taskWeeks;
	}

	public Long getNumTasks() {
		return numTasks;
	}

	public void setNumTasks(Long numTasks) {
		this.numTasks = numTasks;
	}

	public Long getNumCompleted() {
		return numCompleted;
	}

	public void setNumCompleted(Long numCompleted) {
		this.numCompleted = numCompleted;
	}

	public Long getLongestStreak() {
		return longestStreak;
	}

	public void setLongestStreak(Long longestStreak) {
		this.longestStreak = longestStreak;
	}

	public List<CategoryCountObject> getCategoryCount() {
		return categoryCount;
	}

	public void setCategoryCount(List<CategoryCountObject> categoryCount) {
		this.categoryCount = categoryCount;
	}

}
