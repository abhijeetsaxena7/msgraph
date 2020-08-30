package com.microsoft.azure.msalwebsample.model;

import java.io.Serializable;

public class GroupModel implements Serializable{
	private String objectId;
	private String displayName;
	private String description;
	
	public GroupModel() {
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	
	
}
