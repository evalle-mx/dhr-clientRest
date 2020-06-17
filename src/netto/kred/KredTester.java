package netto.kred;

import netto.AppTester;
import org.json.JSONObject;

public class KredTester extends AppTester {

	public final static String GET_PHONES = "/getPhones";
	public final static String GET_TODOS = "/getTodos";
	
	public final static String EXISTS_URI = "/admin/login/exists";
	public final static String S_USER = "/admin/user";
	
	public final static String S_KRED = "/module/kred";
	
	private static JSONObject jsonReq;
	
	public static void main(String[] args) {
		try {
//			ping();
//			getPhones();
//			getUsers();
//			getJson();
//			getMenu();
			
			
//			getUsersList();//USER.G
//			pwdUpdate(); //PERSON.PW
//			userCreate();//USER.C
//			userRead();//USERINFO.R
//			cashflow(); //CASHFLOW.R
//			creditProf(); //CREDPROFILE.R
			

//			exists();	//LOGIN
//			logout();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * Verifica funcionamiento de servicio Logoug
	 * /admin/login/logout
	 * @throws Exception
	 */
	public static String logout() throws Exception {
		jsonReq = new JSONObject();
		jsonReq.put("idUser", "1" );
		
		String stResponse = getJsonFromService(jsonReq.toString(), "/admin/login/logout" );
		System.out.println(stResponse);
		return stResponse; 
	}
	
	/**
	 * Verifica funcionamiento de servicio Exists (Ticketing/UI-Blocker Login)
	 * @throws Exception
	 */
	public static String exists() throws Exception {
		jsonReq = new JSONObject();
		jsonReq.put("userName", "evalle" );	//evalle ` admin rmateus
		jsonReq.put("password", "87b07c6dd5ddbae638fbc78a116bcaea85afabab50c06859a850d84594078eda" );  
		// Centennial ==> 1ce01bc6131ca752a0a1e15384d2172cf6c0e072daf6576e833730b4b18ace25
		// Violeta23 ==> 87b07c6dd5ddbae638fbc78a116bcaea85afabab50c06859a850d84594078eda
		String stResponse = getJsonFromService(jsonReq.toString(), EXISTS_URI );
		System.out.println(stResponse);
		return stResponse; 
	}
	
	
	/**
	 * CREDPROFILE.R /module/kred/creditProf
	 * @return
	 * @throws Exception
	 */
	public static String creditProf() throws Exception {
		jsonReq = new JSONObject();
		jsonReq.put("idUser", "1" );
		//
		String jSon = getJsonFromService(jsonReq.toString(), "/module/kred/creditProf" );	
		return jSon; 
	}
	
	/**
	 * CASHFLOW.R /module/kred/cashflow
	 * @return
	 * @throws Exception
	 */
	public static String cashflow() throws Exception {
		jsonReq = new JSONObject();
		jsonReq.put("idUser", "1" );
		//
		String jSon = getJsonFromService(jsonReq.toString(), "/module/kred/cashflow" );	
		return jSon; 
	}
	
	/**
	 * USERINFO.R "/module/user/read"
	 * @return
	 * @throws Exception
	 */
	public static String userRead() throws Exception {
		jsonReq = new JSONObject();
		jsonReq.put("idUser", "1" );
		//
		String jSon = getJsonFromService(jsonReq.toString(), "/module/user/read" );	
		return jSon; 
	}
	/**
	 * USER.C "/module/user/create"
	 * @return
	 * @throws Exception
	 */
	public static String userCreate() throws Exception {
		jsonReq = new JSONObject();
		jsonReq.put("idUser", "999" );
		//
		String jSon = getJsonFromService(jsonReq.toString(), "/module/user/create" );	
		return jSon; 
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
	 * Obtiene la lista de usuarios Servicio USUARIO.G
	 * @return
	 * @throws Exception
	 */
	public static String getUsers() throws Exception {
		jsonReq = new JSONObject();
//		jsCont.put("idUsuario", "99");
//		jsCont.put("idSucursal", "1");
//		jsCont.put("idRol", "1");
		//TODO agregar busqueda por nombre
//		jsCont.put("nombre", "99");
		String jSon = getJsonFromService(jsonReq.toString(), S_USER+METHOD_GET );	
		return jSon; 
	}
	
	
	public static String getJson() throws Exception {
		jsonReq = new JSONObject();
		jsonReq.put("jsonName", "menu" );	// menu | users | assets
		//
		String jSon = getJsonFromService(jsonReq.toString(), S_KRED+"/getJson" );	
		return jSon; 
	}
	
	
	public static String getMenu() throws Exception {
		jsonReq = new JSONObject();
		jsonReq.put("idUser", "999" );
		//
		String jSon = getJsonFromService(jsonReq.toString(), S_KRED+"/menu" );	
		return jSon; 
	}
	/**
	 * "/module/user/get"
	 * @return
	 * @throws Exception
	 */
	public static String getUsersList() throws Exception {
		jsonReq = new JSONObject();
		jsonReq.put("idUser", "999" );
		//
		String jSon = getJsonFromService(jsonReq.toString(), "/module/user/get" );	
		return jSon; 
	}
	//PERSON.PW ==> cambiara a PWDUPD
	/**
	 * "/module/user/pwdupdate"
	 * @return
	 * @throws Exception
	 */
	public static String pwdUpdate() throws Exception {
		jsonReq = new JSONObject();
		jsonReq.put("idUser", "999" );
		//
		jsonReq.put("password", "123456789" );
		jsonReq.put("passwordUpd1", "ABCDEFGHI" );
		jsonReq.put("passwordUpd2", "ABCDEFGHI" );
		String jSon = getJsonFromService(jsonReq.toString(), "/module/user/pwdupdate" );	
		return jSon; 
	}
	
}
