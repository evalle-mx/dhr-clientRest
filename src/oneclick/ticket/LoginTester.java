package oneclick.ticket;

import org.json.JSONObject;

import netto.AppTester;

public class LoginTester extends AppTester {
	
	public final static String SERV_LOGIN = "/admin/login";
	public final static String METHOD_SETPWD="/setPwd";
	
	/**
	 * Verifica funcionamiento de servicio
	 * @throws Exception
	 */
	public static String login() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put("userName", "admin");
		jsCont.put("password", "12345678");
		
		String jSon = getJsonFromService(jsCont.toString(), SERV_LOGIN+METHOD_EXISTS );	
		return jSon; 
	}
	
	/**
	 * Verifica funcionamiento de servicio
	 * @throws Exception
	 */
	public static String setPwd() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put("idUsuario", "199");
		jsCont.put("password", "abcdefgh");	//12345678
		
		String jSon = getJsonFromService(jsCont.toString(), SERV_LOGIN+METHOD_SETPWD );	
		return jSon; 
	}
	
	
	public static void main(String[] args) {
		try {
			//demo("/admin/login/ping");
			login();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
