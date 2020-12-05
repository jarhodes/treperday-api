package app.treperday.api.domain.task;

import java.util.Date;
import java.util.List;

import app.treperday.api.domain.performance.Performance;

public class PerformanceListByDate {

	private Date date;
	
	private List<Performance> list;

	public PerformanceListByDate(Date date) {
		this.setDate(date);
	}
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<Performance> getList() {
		return list;
	}

	public void setList(List<Performance> list) {
		this.list = list;
	}
	
}
