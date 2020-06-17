package net.dothr.transactional;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import net.dothr.MainAppTester;

public class AdminTaskTester extends MainAppTester {

	static Logger log4j = Logger.getLogger( AdminTaskTester.class );
	
//	protected final static String URI_UPD_PWD="/updpwd";
//	protected final static String URI_MENU = "/menu";
//	
//	protected final static String URI_ADMIN="/admin/management";
	
	
	
	
	/** ***********************************************************************************
	 *  ******************************    M A I N    **************************************
	 *  ***********************************************************************************
	 * @param args
	 */
	public static void main(String[] args) {
		try {
//			updatePassword();
			getMenu();
//			pingApp();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	/**
	 * Petición para actualizar contraseña
	 * @return
	 * @throws Exception
	 * 
	 * @uriService admin/management/updpwd
	 */
	public static String updatePassword() throws Exception {
		//PERSON.PW
		JSONObject json = new JSONObject();

		json.put(P_JSON_IDCONF, IDSELEX);
		json.put(P_JSON_PERSONA, "2");
		
		json.put(	"password",  "2ef85fbe1a34abf0bc7bdfb9159436ce2934470e76a79b173b8e2b8ded0619dJJ");
		json.put("passwordUpd1", "2ef85fbe1a34abf0bc7bdfb9159436ce2934470e76a79b173b8e2b8ded0619dH");
		json.put("passwordUpd2", "2ef85fbe1a34abf0bc7bdfb9159436ce2934470e76a79b173b8e2b8ded0619dH");
		
		System.out.println(json.toString());
		String jSon = getJsonFromService(json.toString(), URI_ADMIN+ URI_UPD_PWD);
		return jSon;
	}
	
	/**
	 * Obtener menú por usuario (idPersona)
	 * @return
	 * @throws Exception
	 * 
	 * @uriService admin/management/menu
	 */
	public static String getMenu() throws Exception {
		// ADMIN.M...
		JSONObject json = new JSONObject();

		json.put(P_JSON_IDCONF, IDSELEX);
		json.put(P_JSON_PERSONA, "1");
				
		String jSon = getJsonFromService(json.toString(), URI_ADMIN + URI_MENU);
		return jSon;
	}
	
	
	public static String pingApp() throws Exception {
		JSONObject json = new JSONObject();

		json.put(P_JSON_IDCONF, IDSELEX);
		json.put("hostName", "OPERATIONAL");
		
		String jSon = getJsonFromService(json.toString(), URI_ADMIN + URI_PING);
		return jSon;
		
	}
}
