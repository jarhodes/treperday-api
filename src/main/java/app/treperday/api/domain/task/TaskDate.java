package app.treperday.api.domain.task;

import java.sql.Date;

import javax.persistence.Basic;

public class TaskDate {

	@Basic
	private Date date;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
