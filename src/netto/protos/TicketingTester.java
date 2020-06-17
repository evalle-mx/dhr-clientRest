package netto.protos;

import netto.AppTester;
import org.json.JSONObject;

public class TicketingTester extends AppTester {

	public final static String GET_PHONES = "/getPhones";
	public final static String GET_TODOS = "/getTodos";
	
	public final static String EXISTS_URI = "/admin/login/exists";
	
	private static JSONObject jsonReq;
	
	public static void main(String[] args) {
		try {
//			ping();
//			getPhones();
			exists();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Verifica funcionamiento de servicio PHONES.G
	 * @throws Exception
	 */
	public static String getPhones() throws Exception {
		jsonReq = new JSONObject();
		jsonReq.put("idEmpresaConf", IDCONF );	
		
		String stResponse = getJsonFromService(jsonReq.toString(), URI_TEST+GET_PHONES );
		System.out.println(stResponse);
		return stResponse; 
	}
	
	/**
	 * Verifica funcionamiento de servicio Exists (Ticketing/UI-Blocker Login)
	 * @throws Exception
	 */
	public static String exists() throws Exception {
		jsonReq = new JSONObject();
		jsonReq.put("userName", "netto" );
		jsonReq.put("password", "Violeta23" ); //Violeta23	| 12345678
		
		String stResponse = getJsonFromService(jsonReq.toString(), EXISTS_URI );
		System.out.println(stResponse);
		return stResponse; 
	}
	
	
}
