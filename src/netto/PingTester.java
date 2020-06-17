package netto;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

import org.apache.log4j.Logger;

public class PingTester extends AppTester {

	private static Logger log4j = Logger.getLogger( PingTester.class );
	
	static final String APP_PROTO = "http://127.0.0.1:8080/ProtoDotHrApp";
	static final String APP_TRANSACTIONAL_MOCK = "http://localhost:8080/TransactionalApp";
	static final String APP_ENCUESTA = "http://localhost:8080/EncuestaApp";
	static final String APP_TICKETING = "http://localhost:8080/TicketingApp";
	static final String APP_TICKETING_ASUS = "http://192.168.0.8:8080/TicketingApp";
	static final String APP_TICKETING_AWS = "http://ec2-34-204-5-176.compute-1.amazonaws.com:8090/TicketingApp";
//	static final String APP_AGROBALSAS = "http://localhost:8080/AgroTransactional";
	static final String APP_DOTHR_TRANS = "http://localhost:8080/AppTransactionalStructured";
	
	protected final static String URI_ENCUESTA = "/admin/area";
	protected final static String SERV_TEST = "/module/test";
	
	
	private static WebTarget target;
	
	public static void main(String[] args) {
		try {
			
//			pingPrototipos(); 
			simplePing(APP_PROTO);	// WEB_RESOURCE | APP_PROTO | APP_ENCUESTA | APP_AGROBALSAS
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Realiza peticiones a todos las aplicaciones definidas en el metodo
	 */
	protected static void pingPrototipos() {
//		String uriService = "/module/test/ping";
		String uriService = "/module/monitor/ping";
		String PROTO_LOCAL = "http://localhost:8080/AppOperationalStructured"; // PrototipoApp | AppTransactionalStructured | AppOperationalStructured
		String PROTO_DEMOEC2 = "http://ec2-34-204-5-176.compute-1.amazonaws.com:8090/AppOperationalStructured"; 
		String PROTO_OPERATEC2 = "http://ec2-52-2-52-80.compute-1.amazonaws.com:8090/AppOperationalStructured"; 
		
		System.out.println("\n\n LOcal:");
		simplePing(PROTO_LOCAL, uriService);
		System.out.println("\n\n DEMOEC2:");
		simplePing(PROTO_DEMOEC2, uriService);	
		System.out.println("\n\n OPERATIONALEC2:");
		simplePing(PROTO_OPERATEC2, uriService);	
	}
	
	/**
	 * Consume el servicio PING para la aplicacion en parametro
	 * @param stWebResource
	 */
	public static void simplePing(final String stWebResource){
		String uriService = SERV_TEST+"/ping";
		simplePing(stWebResource, uriService);
	}
	
	public static void simplePing(final String stWebResource, String uriService){
		StringBuilder sbBuilder = new StringBuilder();
		String jsonParam = "{}";
		try {
			sbBuilder.append("\n WebResource: ").append(stWebResource);
			sbBuilder.append("\n Json: " ).append(jsonParam);
			String stjSon = getJsonFromService(jsonParam, uriService, stWebResource);
			sbBuilder.append("\n Response: ").append(stjSon);
		} catch (Exception e) {
			e.printStackTrace();
			sbBuilder.append("\n Exception: ").append(e.getMessage());
		}
//		System.out.println("***************************");
		System.out.println(sbBuilder.toString());
	}
	
	/**
	 * Metodo comun para utilizar servicio
	 * @param outJson i.e. {}
	 * @param uriService  i.e. / 
	 * @return
	 * @throws Exception
	 */
	public static String getJsonFromService(String inputJson, String uriService, String webResource) throws Exception{
		System.out.println(SEPARADOR);
		String jsonResponse= null;
		Response response = null;
		
		System.out.println("\n EndPoint " + webResource.concat(uriService) +"\n" );
		log4j.debug("\n EndPoint " + webResource.concat(uriService) +"\n" );//*/
		
		
		target = ClientBuilder.newClient().target(webResource);
		response = target.path(uriService).
        		request(MediaType.APPLICATION_JSON_TYPE+CONTENT_TYPE).
        		post(Entity.entity(inputJson, MediaType.APPLICATION_JSON+CONTENT_TYPE), Response.class);
		
		if (response.getStatusInfo().getFamily() == Family.SUCCESSFUL) {
			jsonResponse =  response.readEntity(String.class);
		} else {
			 int cve = response.getStatus();
			 log4j.error("Rest.ERROR --> estatus: " + cve );
			 jsonResponse =  response.readEntity(String.class);
		}
		//log4j.debug("\n ==> Json: "+inputJson + "\n ==> URI: "+uriService + "\n <== response:\n " + jsonResponse );		
		return jsonResponse;
	}

}
