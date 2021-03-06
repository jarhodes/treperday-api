package app.treperday.api.domain.user;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import app.treperday.api.domain.performance.Performance;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;

	private String lastName;

	private String firstName;

	@Column(nullable = false, unique = true)
	private String username;

	@Column(nullable = false)
	@JsonIgnore
	private String password;

	@Column(nullable = false)
	@JsonIgnore
	private String role = "API_USER";

	private String avatarIcon;

	private String avatarColour;

	private Date created;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonBackReference
	private Set<Performance> assignedTasks;

	public User(String lastName, String firstName, String username, String password, String role, String avatarIcon,
			String avatarColour) {
		this.lastName = lastName;
		this.firstName = firstName;
		this.username = username;
		this.password = password;
		this.role = role;
		this.avatarIcon = avatarIcon;
		this.avatarColour = avatarColour;
		this.created = new Date();
	}

	public User() {
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Set<Performance> getAssignedTasks() {
		return assignedTasks;
	}

	public void setAssignedTasks(Set<Performance> assignedTasks) {
		this.assignedTasks = assignedTasks;
	}

	public String getAvatarIcon() {
		return avatarIcon;
	}

	public void setAvatarIcon(String avatarIcon) {
		this.avatarIcon = avatarIcon;
	}

	public String getAvatarColour() {
		return avatarColour;
	}

	public void setAvatarColour(String avatarColour) {
		this.avatarColour = avatarColour;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((avatarColour == null) ? 0 : avatarColour.hashCode());
		result = prime * result + ((avatarIcon == null) ? 0 : avatarIcon.hashCode());
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof User))
			return false;
		User other = (User) obj;
		if (avatarColour == null) {
			if (other.avatarColour != null)
				return false;
		} else if (!avatarColour.equals(other.avatarColour))
			return false;
		if (avatarIcon == null) {
			if (other.avatarIcon != null)
				return false;
		} else if (!avatarIcon.equals(other.avatarIcon))
			return false;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", lastName=" + lastName + ", firstName=" + firstName + ", username=" + username
				+ ", password=" + password + ", role=" + role + ", avatarIcon=" + avatarIcon + ", avatarColour="
				+ avatarColour + ", created=" + created + "]";
	}

}
