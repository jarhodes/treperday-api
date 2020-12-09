package app.treperday.api.domain.task;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import app.treperday.api.domain.performance.Performance;

@Entity
public class Task {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="category_id")
	@JsonManagedReference
	private Category category;
	
	private String name;
	
	private String description;
	
	private String mapSearchKeyword;
	
	private String helpLink;
	
	@OneToMany(mappedBy = "task")
	@JsonBackReference
	private List<Performance> assignedTasks;
	
	public Task(Long id, Category category, String name, String description) {
		this.id = id;
		this.category = category;
		this.name = name;
		this.description = description;
	}
	
	public Task() {}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getHelpLink() {
		return helpLink;
	}

	public void setHelpLink(String helpLink) {
		this.helpLink = helpLink;
	}

	public List<Performance> getAssignedTasks() {
		return assignedTasks;
	}

	public void setAssignedTasks(List<Performance> assignedTasks) {
		this.assignedTasks = assignedTasks;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getMapSearchKeyword() {
		return mapSearchKeyword;
	}

	public void setMapSearchKeyword(String mapSearchKeyword) {
		this.mapSearchKeyword = mapSearchKeyword;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((helpLink == null) ? 0 : helpLink.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((mapSearchKeyword == null) ? 0 : mapSearchKeyword.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Task))
			return false;
		Task other = (Task) obj;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (helpLink == null) {
			if (other.helpLink != null)
				return false;
		} else if (!helpLink.equals(other.helpLink))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (mapSearchKeyword == null) {
			if (other.mapSearchKeyword != null)
				return false;
		} else if (!mapSearchKeyword.equals(other.mapSearchKeyword))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Task [id=" + id + ", category=" + category + ", name=" + name + ", description=" + description
				+ ", mapSearchKeyword=" + mapSearchKeyword + ", helpLink=" + helpLink + "]";
	}

	
	
}
