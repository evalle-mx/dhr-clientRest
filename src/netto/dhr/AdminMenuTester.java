package netto.dhr;

import org.json.JSONObject;

public class AdminMenuTester extends DotHRTester {
	
	//End Point
	public final static String URI_ADMIN = "/admin/management";
	
	//http://localhost:8080/DotHRApp/common
	
	//Method
	public final static String URI_MENU = "/menu";
	
	
	public static void main(String[] args) {
		try {
			getMenu();
//			test();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Obtener menú por usuario (idPersona) ADMIN.M
	 * @return
	 * @throws Exception
	 * 
	 * @uriService admin/management/menu
	 */
	public static String getMenu() throws Exception {
		JSONObject json = new JSONObject();
		json.put("idEmpresaConf", "1");
		json.put("idPersona", "1");
				
		String jSon = getJsonFromService(json.toString(), URI_ADMIN + URI_MENU);
		return jSon;
	}
	

}
