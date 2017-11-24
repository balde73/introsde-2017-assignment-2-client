package rest.model;

import rest.model.ActivityType;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.OneToOne;


@XmlRootElement(name="activity")
public class Activity implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String name;
	private String startdate;
	private String description;
	private String place;
	private ActivityType type;
	private Person person;
	
	@XmlAttribute(name="id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer idActivity) {
		this.id = idActivity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if(name!=null)
			this.name = name;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		if(description!=null)
			this.description = description;
	}
	
	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		if(place!=null)
			this.place = place;
	}

	public ActivityType getActivityType() {
		return type;
	}

	public void setActivityType(ActivityType type) {
		if(type!=null)
			this.type = type;
	}
	
	@XmlSchemaType(name = "date")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="GMT+1")
	public Date getStartDate() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = null;
		
		if(this.startdate==null)
			return null;
		try {
			startDate = df.parse(this.startdate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return startDate;
	}

	public void setStartDate(Date date) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		if(date!=null)
			this.startdate = df.format(date);
	}
	
	// we make this transient for JAXB to avoid and infinite loop on serialization
	@XmlTransient
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		if(person!=null)
			this.person = person;
	}

	public Activity() {
	}

	public Activity(Integer id, String name, String description, String place, String date, ActivityType type, Person person) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.place = place;
		this.startdate = date;
		this.type = type;
		this.person = person;
	}
}
