package net.dothr;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import net.dothr.transactional.ContactLocationTester;
import net.utils.ClientRestUtily;

public class GenericJsonTest extends MainAppTester {
	
	private static Logger log4j = Logger.getLogger( ContactLocationTester.class );
	private static final String IDCONF = IDCONF_DOTHR;
	
	/**
	 * Lee un archivo plano y genera el json para enviarlo a un endPoint en Transactional
	 * @throws Exception
	 */
	public static void commonRequest() throws Exception {
		String endPoint = URI_SETTLEMENT+URI_CREATE; /* URI_SETTLEMENT+URI_PING | /module/settlement/ping */
		String rutaArchivoJson = "/home/dothr/workspace/ClientRest/files/input/settlement.json";
		JSONObject json = getFromFile(rutaArchivoJson);
//				new JSONObject();
//		json.put(P_JSON_IDCONF, IDCONF);
		
		String jSon = getJsonFromService(json.toString(), endPoint );
	}
	
	
	
	private static JSONObject getFromFile(String rutaArchivoJson) throws Exception {
		String stJson = ClientRestUtily.getBuilderNoTabsFile(rutaArchivoJson, null).toString();
		JSONObject json = null;
		if(stJson.startsWith("{")) {
			json = new JSONObject(stJson);
		}
		else if(stJson.startsWith("[")) {
			JSONArray jsArr = new JSONArray(stJson);
			json = jsArr.getJSONObject(0);
		}
		else {
			log4j.error("NO es cadena valida ", new NullPointerException());
			throw new NullPointerException("CADENA INVALIDA");
		}
		return json;
	}
	
	public static void main(String[] args) {
		try {
			commonRequest();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
