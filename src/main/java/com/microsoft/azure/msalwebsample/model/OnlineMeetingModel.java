package com.microsoft.azure.msalwebsample.model;

import java.util.Calendar;

public class OnlineMeetingModel {
	private String subject;
	private Calendar startDatetime;
	private Calendar endDatetime;
	
	public OnlineMeetingModel() {
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Calendar getStartDatetime() {
		return startDatetime;
	}

	public void setStartDatetime(Calendar startDatetime) {
		this.startDatetime = startDatetime;
	}

	public Calendar getEndDatetime() {
		return endDatetime;
	}

	public void setEndDatetime(Calendar endDatetime) {
		this.endDatetime = endDatetime;
	}

	
}
