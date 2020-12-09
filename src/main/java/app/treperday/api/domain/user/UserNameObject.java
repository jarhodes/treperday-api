package app.treperday.api.domain.user;

public class UserNameObject {

	private String lastName;
	
	private String firstName;
	
	private String avatarIcon;
	
	private String avatarColour;

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
	
}
