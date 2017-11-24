package rest.model;

import rest.model.Activity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlValue;

import com.fasterxml.jackson.annotation.JsonValue;

@XmlRootElement(name="activity_type")
public class ActivityType implements Serializable {
	private static final long serialVersionUID = 1L;

	private String activityTypeName;
	private List<Activity> activities;

	public ActivityType() {
	}
	
	public ActivityType(String activityTypeName) {
		this.activityTypeName = activityTypeName;
	}

	@XmlValue
	@JsonValue
	public String getActivityTypeName() {
		return activityTypeName;
	}

	public void setActivityTypeName(String activityTypeName) {
		this.activityTypeName = activityTypeName;
	}

	@XmlTransient
	public List<Activity> getActivities() {
		return activities;
	}

	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}
}
