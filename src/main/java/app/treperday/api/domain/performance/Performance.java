package app.treperday.api.domain.performance;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import app.treperday.api.domain.task.Task;
import app.treperday.api.domain.user.User;

@Entity
public class Performance {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "task_id")
	private Task task;

	private String performanceText;

	@Temporal(TemporalType.DATE)
	private Date date;

	private Boolean isCompleted;
	
	private Boolean isShared;
	
	private Boolean isShareApproved;
	
	private Integer rating;

	public Performance(User user, Task task, Date date) {
		this.user = user;
		this.task = task;
		this.date = date;
		this.isCompleted = false;
		this.isShared = false;
		this.isShareApproved = true;
	}

	public Performance() {
		this.isCompleted = false;
		this.isShared = false;
		this.isShareApproved = true;
	}
	
	
	public Boolean getIsShared() {
		return isShared;
	}

	public void setIsShared(Boolean isShared) {
		this.isShared = isShared;
	}

	public Boolean getIsShareApproved() {
		return isShareApproved;
	}

	public void setIsShareApproved(Boolean isShareApproved) {
		this.isShareApproved = isShareApproved;
	}

	public Boolean getIsCompleted() {
		return isCompleted;
	}

	public void setIsCompleted(Boolean isCompleted) {
		this.isCompleted = isCompleted;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((isCompleted == null) ? 0 : isCompleted.hashCode());
		result = prime * result + ((isShareApproved == null) ? 0 : isShareApproved.hashCode());
		result = prime * result + ((isShared == null) ? 0 : isShared.hashCode());
		result = prime * result + ((performanceText == null) ? 0 : performanceText.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Performance))
			return false;
		Performance other = (Performance) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (isCompleted == null) {
			if (other.isCompleted != null)
				return false;
		} else if (!isCompleted.equals(other.isCompleted))
			return false;
		if (isShareApproved == null) {
			if (other.isShareApproved != null)
				return false;
		} else if (!isShareApproved.equals(other.isShareApproved))
			return false;
		if (isShared == null) {
			if (other.isShared != null)
				return false;
		} else if (!isShared.equals(other.isShared))
			return false;
		if (performanceText == null) {
			if (other.performanceText != null)
				return false;
		} else if (!performanceText.equals(other.performanceText))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Performance [id=" + id + ", performanceText=" + performanceText + ", date=" + date + ", isCompleted="
				+ isCompleted + ", isShared=" + isShared + ", isShareApproved=" + isShareApproved + ", rating=" + rating
				+ "]";
	}

}
