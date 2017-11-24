package rest.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.glassfish.jersey.client.ClientConfig;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonProcessingException;

import rest.model.Activity;
import rest.model.ActivityType;
import rest.model.Person;
import rest.utils.Postman;
import rest.utils.PrettyPrinter;

public class MyClient {
	public static void main(String[] args) throws JsonProcessingException, IOException, XPathExpressionException, ParserConfigurationException, SAXException {
		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget service = client.target("https://example-balde.herokuapp.com/sdelab");
		
		MyClient.doAllTask("xml", service);
		MyClient.doAllTask("json", service);
	}
	
	public static void doAllTask(String format, WebTarget service) throws FileNotFoundException {
		Postman postman = new Postman(service, "application/"+format);
		PrettyPrinter printer = new PrettyPrinter(format);
		
		String result = "ERROR";
		
		// 1
		printer.printStep(1);
		Response res = postman.get("/person");
		res.bufferEntity();
		int size = 0;
		List<Person> people = res.readEntity(new GenericType<List<Person>>() {});
		size = people.size();
		result = (size > 4) ? "OK" : "ERROR";
		printer.print("Request #1: GET /person", result, res.getStatus(), res.readEntity(String.class), format);
		
		// 2
		printer.printStep(2);
		Person firstPerson = people.get(0);
		Person lastPerson = people.get(people.size()-1);
		int firstId = firstPerson.getId();
		int lastId = lastPerson.getId();
		
		Response res2 = postman.get("/person/"+firstId);
		String responseString2 = res2.readEntity(String.class);
		result = (res2.getStatus() == 200 || res2.getStatus() == 202) ? "OK" : "ERROR";
		printer.print("Request #2: GET /person/"+firstId, result, res2.getStatus(), responseString2, format);
		
		// 3
		printer.printStep(3);
		firstPerson.setFirstname("Martino");
		Response res3 = postman.putPerson("/person/"+firstId, firstPerson);
		res3.bufferEntity();
		String responseString3 = res3.readEntity(String.class);
		result = "ERROR";
		if(res3.getStatus() == 200) {
			Person personUpdated = res3.readEntity(Person.class);
			String firstname = personUpdated.getFirstname();
			result = (firstname.equals("Martino")) ? "OK" : "ERROR";
		}
		printer.print("Request #3: PUT /person/"+firstId+" - changing firstname", result, res3.getStatus(), responseString3, format);
		
		// 4
		printer.printStep(4);
		Response res4 = postman.post("/person", MyClient.getXmlPeople());
		res4.bufferEntity();
		Person createdPerson = res4.readEntity(Person.class);
		int createdId = createdPerson.getId();
		result = (res4.getStatus() == 200 || res4.getStatus() == 202) ? "OK" : "ERROR";
		printer.print("Request #4: POST /person", result, res4.getStatus(), res4.readEntity(String.class), format);
		
		// 5
		printer.printStep(5);
		Response res5 = postman.delete("/person/"+createdId);
		String responseString5 = res5.readEntity(String.class);
		printer.print("Request #5.1: DELETE /person/"+createdId, "WAIT", res5.getStatus(), responseString5, "plain");
		
		Response res5_1 = postman.get("/person/"+createdId);
		String responseString5_1 = res5_1.readEntity(String.class);
		result = (res5_1.getStatus() == 404) ? "OK" : "ERROR";
		printer.print("Request #5.2: GET /person/"+createdId, result, res5_1.getStatus(), responseString5_1, "plain");

		// 6
		printer.printStep(6);
		Response res6 = postman.get("/activity_types");
		res6.bufferEntity();
		String responseString6 = res6.readEntity(String.class);
		List<ActivityType> activityTypes = res6.readEntity(new GenericType<List<ActivityType>>() {});
		result = (activityTypes.size() > 2) ? "OK" : "ERROR";
		printer.print("Request #6: GET /activity_types", result, res6.getStatus(), responseString6, format);
		

		// 7
		printer.printStep(7);
		result = "ERROR";
		String activityTypeName = "";
		String activityId = "";
		String personId = "";
		String[] myIntArray = {firstId+"", lastId+""};
		for(String person : myIntArray) {
			for(ActivityType activityType : activityTypes) {
				Response res7 = postman.get("/person/"+person+"/"+activityType.getActivityTypeName());
				res7.bufferEntity();
				List<Activity> activities = res7.readEntity(new GenericType<List<Activity>>() {});
				if(activities.size()>0) {
					result = "OK";
					ActivityType at_9 = activities.get(0).getActivityType();
					if(at_9 != null) {
						activityTypeName = at_9.getActivityTypeName();
						activityId = activities.get(0).getId()+"";
						personId = person;
					}
				}
				printer.print("Request #7_: GET /person/"+person+"/"+activityType.getActivityTypeName(), "wait", res7.getStatus(), res7.readEntity(String.class), format);
			}
		}
		printer.print("Request #7 complessivo", result, 200, "", "plain");
		
		if(activityId == null || personId == null) {
			printer.printWarning("Request 7 ERROR!! Step 8, 9 will have problems");
		}
		
		
		// 8 
		printer.printStep(8);
		Response res8 = postman.get("/person/"+personId+"/"+activityTypeName+"/"+activityId);
		String responseString8 = res8.readEntity(String.class);
		result = (res8.getStatus() == 200) ? "OK" : "ERROR";
		printer.print("Request #8: GET /person/"+personId+"/"+activityTypeName+"/"+activityId, result, res8.getStatus(), responseString8, format);
		
		// 9
		printer.printStep(9);
		Response res9_1 = postman.get("/person/"+personId+"/"+activityTypeName);
		res9_1.bufferEntity();
		List<Activity> activitiesBefore = res9_1.readEntity(new GenericType<List<Activity>>() {});
		printer.print("Request #9: GET /person/"+personId+"/"+activityTypeName, "wait", res9_1.getStatus(), res9_1.readEntity(String.class), format);
		
		Response res9_2 = postman.post("/person/"+personId+"/"+activityTypeName, MyClient.getXmlActivity());
		res9_2.bufferEntity();
		Activity newActivity = res9_2.readEntity(Activity.class);
		printer.print("Request #9: POST /person/"+personId+"/"+activityTypeName, "wait", res9_2.getStatus(), res9_2.readEntity(String.class), format);
	
		Response res9_3 = postman.get("/person/"+personId+"/"+activityTypeName);
		res9_3.bufferEntity();
		List<Activity> activitiesAfter = res9_3.readEntity(new GenericType<List<Activity>>() {});
		
		result = (activitiesAfter.size() > activitiesBefore.size()) ? "OK" : "ERROR";
		printer.print("Request #9: GET /person/"+personId+"/"+activityTypeName, result, res9_3.getStatus(), res9_3.readEntity(String.class), format);
	
		// 10
		printer.printStep(10);
		ActivityType at = new ActivityType("Politics");
		Response res10_1 = postman.put("/person/"+personId+"/"+activityTypeName+"/"+newActivity.getId(), "<activity_type>"+at.getActivityTypeName()+"</activity_type>");
		printer.print("Request #10.1: PUT /person/"+personId+"/"+activityTypeName+"/"+newActivity.getId(), result, res10_1.getStatus(), res10_1.readEntity(String.class), format);
		
		Response res10_2 = postman.get("/activity_types");
		res10_2.bufferEntity();
		result = "ERROR";
		List<ActivityType> newActivityTypes = res10_2.readEntity(new GenericType<List<ActivityType>>() {});
		for(ActivityType atypes : newActivityTypes) {
			if(atypes.getActivityTypeName() == at.getActivityTypeName())
				result = "OK";
		}
		printer.print("Request #10.2: GET /activity_types", result, res10_2.getStatus(), res10_2.readEntity(String.class), format);
		
		
		// 11
		printer.printStep(11);
		List<String> params = new ArrayList<>();
		params.add("before=2018-01-01");
		params.add("after=2017-01-01");
		Response res11 = postman.getWithParams("/person/"+firstId+"/Social", params);
		res11.bufferEntity();
		List<Activity> filteredActivity = res11.readEntity(new GenericType<List<Activity>>() {});
		result = (filteredActivity.size()>0 && res11.getStatus()==200) ? "OK" : "ERROR";
		printer.print("Request #11: GET /person/"+firstId+"/Social?before=2018-01-01&after=2017-01-01", result, res11.getStatus(), res11.readEntity(String.class), format);
		
		System.out.println("--- additional query ---");
		List<String> params2 = new ArrayList<>();
		params2.add("before=2017-01-01");
		params2.add("after=2016-01-01");
		Response res11_1 = postman.getWithParams("/person/"+firstId+"/Social", params2);
		printer.print("Request #11: GET /person/"+firstId+"/Social?before=2017-01-01&after=2016-01-01", "OK", res11_1.getStatus(), res11_1.readEntity(String.class), format);
	}
	
	
	public static String getXmlActivity() {
		/* 
		 * omitting the activityType field since it should be trivial from the request made
		 * request: /person/<idPerson>/<activityType>
		 */
		return "	<activity>" + 
				"        <name>Swimming</name>" + 
				"        <description>Swimming in the river</description>" + 
				"        <place>Adige river</place>" + 
				"        <startdate>2017-12-28</startdate>" + 
				"    </activity>";
	}
	
	public static String getXmlPeopleFirstname() {
		return  "<person>" + 
				"  <firstname>Alex</firstname>" + 
				"</person>";
	}
	
	public static String getXmlPeople() {
		return  "<person>" + 
				"  <firstname>Alex</firstname>" + 
				"  <lastname>Bertamini</lastname>" + 
				"  <username>berta</username>" + 
				"  <email>alex.bertamini@studenti.unitn.it</email>" + 
				"  <activities>" + 
				"    <activity>" + 
				"      <name>Golf lesson</name>" + 
				"      <description>Golf lesson with Katerina</description>" + 
				"      <place>Madrid</place>" + 
				"      <startdate>2017-12-09</startdate>" + 
				"      <activityType>Sport</activityType>" + 
				"    </activity>" + 
				"  </activities>" + 
				"</person>";
	}
	
	
}
