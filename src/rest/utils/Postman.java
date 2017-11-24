package rest.utils;

import java.util.List;

import javax.persistence.Tuple;
import javax.ws.rs.Path;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.tuple.Pair;

import rest.model.Person;

public class Postman {
	WebTarget service;
	String mediaType;
	
	public Postman(WebTarget service, String mediaType) {
		this.service = service;
		this.mediaType = mediaType;
	}
	
	public Response get(String path) {
		Response res = service.path(path).request().accept(mediaType).get();
		return res;
	}

	public Response getWithParams(String path, List<String> params) {
		WebTarget e = service.path(path);
		for(String param : params){
            String[] elem = param.split("=");
            e = e.queryParam(elem[0], elem[1]);
		}
		Response res = e.request().accept(mediaType).get();
		return res;
	}
	
	public Response post(String path, String content) {
		return service.path(path).request().accept(mediaType).post(Entity.entity(content, "application/xml"));
	}
	
	public Response put(String path, String content) {
		return service.path(path).request().accept(mediaType).put(Entity.entity(content, "application/xml"));
	}
	
	public Response putPerson(String path, Person person) {
		return service.path(path).request().accept(mediaType).put(Entity.entity(person, "application/xml"));
	}
	
	public Response delete(String path) {
		return service.path(path).request().delete();
	}
}
