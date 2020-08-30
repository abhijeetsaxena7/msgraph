package com.microsoft.azure.msalwebsample.model;

import java.io.Serializable;
import java.util.Calendar;

public class OnlineMeetingModel implements Serializable{
	private String objectId;
	private String subject;
	private Calendar startDatetime;
	private Calendar endDatetime;
	private String joinWebUrl;
	
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

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getJoinWebUrl() {
		return joinWebUrl;
	}

	public void setJoinWebUrl(String joinWebUrl) {
		this.joinWebUrl = joinWebUrl;
	}

	
	
}
