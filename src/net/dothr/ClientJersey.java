package net.dothr;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ClientJersey {

	static Logger log4j = Logger.getLogger( ClientJersey.class);
	private static WebTarget target;
	private static Gson gson;
	private static final String CONTENT_TYPE=";charset=UTF-8";
	
	public static void init(String uri){
		log4j.debug("init() --> target: " + target);
		 if(target == null){
			 target =ClientBuilder.newClient().target(uri);
		 }
	}
	
	public static Response request(String inputJson, String uriService){	//Object servicioDto,String nombreServicio){
		 log4j.debug("request() -->   gson="+gson );
		 if(gson == null){
			 gson = new GsonBuilder().setDateFormat("MMM dd, yyyy HH:mm:ss a").create(); 

		 }
		  log4j.debug("request() -->  \n target: " + target+"\n uriService="+uriService);
		  log4j.debug("Json==>: \n" + inputJson );
		  return  target.path(uriService).
	        		request(MediaType.APPLICATION_JSON_TYPE+CONTENT_TYPE).
	        		post(Entity.entity(inputJson, MediaType.APPLICATION_JSON+CONTENT_TYPE),Response.class);
	}
	
	public static Response reqPing(){
		log4j.debug("Ping");
		String nombreServicio = "/module/curriculumManagement/ping";
		log4j.debug("request() -->  \n target: " + target+"\n nombreServicio="+nombreServicio);
		  log4j.debug("Json==>: \n" + "{}" );
		  return  target.path(nombreServicio).
	        		request(MediaType.APPLICATION_JSON_TYPE+CONTENT_TYPE).
	        		post(Entity.entity("{}", MediaType.APPLICATION_JSON+CONTENT_TYPE),Response.class);
	}
	
}
