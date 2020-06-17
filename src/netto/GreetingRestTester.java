package netto;

import java.net.ConnectException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

import org.apache.log4j.Logger;

import oneclick.twise.wserv.ClientJersey;

public class GreetingRestTester {
	
	private static Logger log4j = Logger.getLogger(GreetingRestTester.class);
	protected static String APP_RESOURCE = "http://localhost:8080/";
	
	public static void main(String[] args) {
		try {
			pingGretting();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	/** Metodo para probar aplicacion por defecto */
	public static String pingGretting() throws Exception {
		//return ping(null);
		
		String resp = getJsonFromService("{\"name\":\"Netto\"}", "common");
		return resp;
	}
	
	
	/**
	 * Metodo PRINCIPAL para Consumir servicios REST
	 * @param outJson i.e. {}
	 * @param uriService  i.e. / 
	 * @return
	 * @throws Exception
	 */
	protected static String getJsonFromService(String inputJson, String uriService) throws ConnectException, Exception{
		
		String jsonResponse= null;
		Response response = null;
		/*if(WEB_RESOURCE.equals(ConstantesREST.WEB_TRANSACT_MOCK)){
			log4j.debug("\n\n ?????????  APUNTANDO A AMBIENTE EMULADO MOCK "+ ConstantesREST.WEB_TRANSACT_MOCK +" ?????????????????????\n"); 
		}
		log4j.debug("\n Probando ["+ transUriCode(uriService) +"] > " + WEB_RESOURCE.concat(uriService) +"\n" );//*/
		log4j.debug("\n Probando " + APP_RESOURCE.concat(uriService) +"\n" );
		
		ClientJersey.init(APP_RESOURCE);
		response = ClientJersey.request(inputJson, uriService );
		if (response.getStatusInfo().getFamily() == Family.SUCCESSFUL) {
			jsonResponse =  response.readEntity(String.class);
		} else {
			 int cve = response.getStatus();
			 log4j.error("Rest.ERROR --> estatus: " + cve );
			 jsonResponse =  response.readEntity(String.class);
			 
			 if(jsonResponse.startsWith("<!DOCTYPE html")){
				 log4j.error("Error de comunicación con: "+APP_RESOURCE +", \n 1. verificar conectividad. \n 2.Verificar servicio "+ uriService, new NullPointerException());
			 }
		}
		log4j.debug("\n ==> Json: "+inputJson + "\n ==> URI: "+uriService + "\n <== response:\n " + jsonResponse +"\n");
		return jsonResponse;
	}

}
