package rest.model;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonValue;

@XmlRootElement(name="people")
public class People implements Serializable{
	private List<Person> people;

	@XmlElement(name="person")
	@JsonValue
	public List<Person> getPeople() {
		return people;
	}

	public void setPeople(List<Person> people) {
		this.people = people;
	}
	
	public int getSize() {
		return this.people.size();
	}

	public People(List<Person> people) {
		super();
		this.people = people;
	}

	public People() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
