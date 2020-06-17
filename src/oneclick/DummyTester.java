package oneclick;

import java.util.ArrayList;
import java.util.Iterator;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

import oneclick.twise.util.Constante;

import org.apache.log4j.Logger;
import org.json.JSONObject;

public class DummyTester {
	
	private static Logger log4j = Logger.getLogger( DummyTester.class );
	
	static final String APP_PROTO = "http://localhost:8080/PrototipoApp";
	static final String APP_ENCUESTA = "http://localhost:8080/EncuestaApp";
	static final String APP_TICKETING = "http://localhost:8080/TicketingApp";
	
	static final String WEBRESOURCE = APP_PROTO;	// APP_PROTO | APP_TICKETING
	
	private final static String SERV_TEST = "/module/test";
	private static WebTarget target;
	
	
	public static void main(String[] args) {
		try {
//			simplePing(WEBRESOURCE);
			
//			getPhones();
//			getTodos();
//			simpleNumbers();
			generateRolPermiso();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void generateRolPermiso(){
		ArrayList<String> lsPermiso = new ArrayList<String>();
		lsPermiso.add("199");
		lsPermiso.add("200");
		lsPermiso.add("201");
		lsPermiso.add("202");
		lsPermiso.add("203");
		lsPermiso.add("204");
		lsPermiso.add("205");
		
		
		int idRolP = 793;
		for(int idRol=1;idRol<5;idRol++){
//			System.out.println("para rol: "+x);
			Iterator<String> itPerm = lsPermiso.iterator();
			while(itPerm.hasNext()){
				System.out.println(idRolP + ";"+idRol+";"+itPerm.next()+";t");
				idRolP++;
			}
			
		}
		
	}
	
	private static void simpleNumbers(){
		int max = 16;
		for(int x =1; x<=max;x++){
			//System.out.println(x);
			System.out.println("mapaEdos.put("+x+", new TrackingDto("+x+",\"");
		}
	}
	
	public static void getTodos() throws Exception {
		JSONObject jsReq = new JSONObject();
		
		jsReq.put("idUsuario", "199");	//phones | nexus-s
		
		String jSon = getJsonFromService(jsReq.toString(), SERV_TEST+"/getTodos" );	
		log4j.debug("jSon: \n" + jSon );
		
	}
	
	public static void getPhones() throws Exception {
		JSONObject jsReq = new JSONObject();
		
		jsReq.put("code", "phones");	//phones | nexus-s
		
		String jSon = getJsonFromService(jsReq.toString(), SERV_TEST+"/getPhones" );	
		log4j.debug("jSon: \n" + jSon );
		
	}
	
	
	protected static void simplePing(final String stWebResource) throws Exception {
		String uriService = SERV_TEST+"/ping";
		StringBuilder sbBuilder = new StringBuilder();
		
		sbBuilder.append("\n Endpoint: ").append(stWebResource);
		String stjSon = getJsonFromService("{}", uriService, stWebResource);
		sbBuilder.append("\n Response: ").append(stjSon);
		
		System.out.println("***************************");
		System.out.println(sbBuilder.toString());
	}
	
	private static String getJsonFromService(String inputJson, String uriService) throws Exception{
		
		return getJsonFromService(inputJson, uriService, WEBRESOURCE);
	}
	
	/**
	 * Metodo comun para utilizar servicio
	 * @param outJson i.e. {}
	 * @param uriService  i.e. / 
	 * @return
	 * @throws Exception
	 */
	private static String getJsonFromService(String inputJson, String uriService, String webResource) throws Exception{
		
		String jsonResponse= null;
		Response response = null;
		
		//log4j.debug("\n Probando ["+ transUriCode(uriService) +"] > " + webResource.concat(uriService) +"\n" );
		log4j.debug("\n Probando " + webResource.concat(uriService) +"\n" );//*/
		
		
		target = ClientBuilder.newClient().target(webResource);
		response = target.path(uriService).
        		request(MediaType.APPLICATION_JSON_TYPE+Constante.CONTENT_TYPE).
        		post(Entity.entity(inputJson, MediaType.APPLICATION_JSON+Constante.CONTENT_TYPE), Response.class);
		
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
