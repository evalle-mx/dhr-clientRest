package net.dothr;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

import net.utils.ConstantesREST;

import org.apache.log4j.Logger;

public class AppsPinger extends MainAppTester {

	private static Logger log4j = Logger.getLogger( AppsPinger.class );
	
	private static WebTarget target;
			
	public static String AWS_TRANSACTIONAL_WR = ConstantesREST.WEB_TRANSACT_AWS;
	public static String AWS_OPERATIONAL_WR = ConstantesREST.WEB_OPERAT_AWS;
	public static String TRANSACTIONAL_WR = ConstantesREST.WEB_TRANSACT_LOCAL;
	public static String OPERATIONAL_WR = ConstantesREST.WEB_OPERAT_LOCAL;
	public static String AWS_TEST_TRANSACTIONAL_WR = ConstantesREST.WEB_TEST_TRANSACT_AWS;
	public static String AWS_TEST_OPERATIONAL_WR = ConstantesREST.WEB_TEST_OPERAT_AWS;
	
	public static String TRANSACTIONAL_MOCK = ConstantesREST.WEB_TRANSACT_MOCK;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String app = "http://ec2-34-204-5-176.compute-1.amazonaws.com:8090/AppOperationalStructured";
//		simplePing(WEB_RESOURCE);	
		//WEB_RESOURCE | AWS_OPERATIONAL_WR | WEB_TRANSACT_MOCK | WEB_TRANSACT_LOCAL | WEB_TRANSACT_AWS | WEB_OPERAT_LOCAL
		simplePing(
//				WEB_RESOURCE  //Para veriricar el que se usa en pruebas
				//TRANSACTIONAL_WR
//				"http://192.168.0.5:8080/AppTransactionalStructured"//AppOperationalStructured
//				"http://ec2-52-2-52-80.compute-1.amazonaws.com:8090/PrototipoApp"
				AWS_TRANSACTIONAL_WR	//AWS_TRANSACTIONAL_WR | AWS_OPERATIONAL_WR
				);
//		pingMultiple(null, "/ping");
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
		
		log4j.debug("\n Probando ["+ transUriCode(uriService) +"] > " + webResource.concat(uriService) +"\n" );
		
		
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
	
	
	public static void simplePing(final String stWebResource){
		String uriService = "/module/monitor/ping";
		StringBuilder sbBuilder = new StringBuilder();
		try {
			sbBuilder.append("\n Endpoint: ").append(stWebResource);
			String stjSon = getJsonFromService("{}", uriService, stWebResource);
			sbBuilder.append("\n Response: ").append(stjSon);
		} catch (Exception e) {
			e.printStackTrace();
			sbBuilder.append("\n Exception: ").append(e.getMessage());
		}
		System.out.println("***************************");
		System.out.println(sbBuilder.toString());
	}
	
	/**
	 * Metodo para probar conectividad con APP y el AdapterRest solicitado, 
	 * pues todos heredan ruta de servicio "ping" desde ErrorMessageAdapterRest
	 * @param uriRoot   /module/service
	 */
	protected static void pingMultiple(String uriRoot, String method){
		if(uriRoot==null || uriRoot.trim().equals("")){
			uriRoot = "/module/curriculumManagement";
		}
		if(method==null || method.trim().equals("")){
			method = "/ping";
		}
		StringBuilder sbBuilder = new StringBuilder(" \n\n \t >>>>   RESULTADO PRUEBA CONECTIVIDAD (PROD, TEST, DESA[local]) <<<<  \n");
		String stjSon = null;
		/* --------------------------------	AWS-Transactional	---------------------------------------------- */
		try {
			sbBuilder.append("\n<AWS-Transactional> \n Endpoint: ").append(AWS_TRANSACTIONAL_WR);
			stjSon = getJsonFromService("{}", uriRoot+method, AWS_TRANSACTIONAL_WR);
			sbBuilder.append("\n Response: ").append(stjSon);
		} catch (Exception e) {
			e.printStackTrace();
			sbBuilder.append("\n Exception: ").append(e.getMessage());
		}
		/* -------------------------------	AWS-Operational	----------------------------------------------- */
		try{
			sbBuilder.append("\n<AWS-Operational> \n Endpoint: ").append(AWS_OPERATIONAL_WR);
			stjSon = getJsonFromService("{}", uriRoot+method, AWS_OPERATIONAL_WR);
			sbBuilder.append("\n Response: ").append(stjSon).append("\n");			
		}catch (Exception e){
			e.printStackTrace();
			sbBuilder.append("\n Exception: ").append(e.getMessage());
		}
		
		/* --------------------------------	AWS-TEST-Transactional	---------------------------------------------- */
		try {
			sbBuilder.append("\n<AWS-TEST_-Transactional>\n Endpoint: ").append(AWS_TEST_TRANSACTIONAL_WR);
			stjSon = getJsonFromService("{}", uriRoot+method, AWS_TEST_TRANSACTIONAL_WR);
			sbBuilder.append("\n Response: ").append(stjSon);
		} catch (Exception e) {
			e.printStackTrace();
			sbBuilder.append("\n Exception: ").append(e.getMessage());
		}
		/* -------------------------------	AWS-TEST-Operational	----------------------------------------------- */
		try{
			sbBuilder.append("\n<AWS-TEST-Operational> \n Endpoint: ").append(AWS_TEST_OPERATIONAL_WR);
			stjSon = getJsonFromService("{}", uriRoot+method, AWS_TEST_OPERATIONAL_WR);
			sbBuilder.append("\n Response: ").append(stjSon).append("\n");			
		}catch (Exception e){
			e.printStackTrace();
			sbBuilder.append("\n Exception: ").append(e.getMessage());
		}
		
		/* ---------------------------	Local-Transactional 	--------------------------------------------------- */
		try{
			sbBuilder.append("\n <Transactional> \n Endpoint: ").append(TRANSACTIONAL_WR);
			stjSon = getJsonFromService("{}", uriRoot+method, TRANSACTIONAL_WR);
			sbBuilder.append("\n Response: ").append(stjSon).append("\n");
		}catch(Exception e){
			e.printStackTrace();
			sbBuilder.append("\n Exception: ").append(e.getMessage());
		}
		/* ---------------------------	Local-Operational 	--------------------------------------------------- */
		try{
			sbBuilder.append("\n <Operational> \n Endpoint: ").append(OPERATIONAL_WR);
			stjSon = getJsonFromService("{}", uriRoot+method, OPERATIONAL_WR);
			sbBuilder.append("\n Response: ").append(stjSon).append("\n");
		}catch(Exception e){
			e.printStackTrace();
			sbBuilder.append("\n Exception: ").append(e.getMessage());
		}
		
		System.out.println("\n");
		System.out.println(sbBuilder);
	}

}
