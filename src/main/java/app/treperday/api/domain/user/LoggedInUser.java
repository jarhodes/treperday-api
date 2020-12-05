package app.treperday.api.domain.user;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class LoggedInUser extends User {
	
	private app.treperday.api.domain.user.User user;

	public LoggedInUser(app.treperday.api.domain.user.User user, Collection<? extends GrantedAuthority> authorities) {
		super(user.getUsername(), user.getPassword(), true, true, true, true, authorities);
	}

	public app.treperday.api.domain.user.User getUser() {
		return user;
	}

	public void setUser(app.treperday.api.domain.user.User user) {
		this.user = user;
	}
}
