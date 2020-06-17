package oneclick.ticket;

import org.json.JSONObject;

import netto.AppTester;

public class SucursalTester extends AppTester {
	
	public final static String SERV_BRANCH = "/module/branch";
	
	public static void main(String[] args) {
		try {
			getSucursales();
//			createSucursal();
//			readSucursal();
//			updSucursal();
//			delSucursal();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getSucursales() throws Exception {
		JSONObject jsCont = new JSONObject();
		
		String jSon = getJsonFromService(jsCont.toString(), SERV_BRANCH+METHOD_GET );	
		return jSon; 
	}
	
	public static String createSucursal() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put("nombre", "Azcapotzalco");
		jsCont.put("Ubicacion", "Avenida Azcapotzalco 25, Azcapotzalco");
		
		String jSon = getJsonFromService(jsCont.toString(), SERV_BRANCH+METHOD_CREATE );	
		return jSon; 
	}
	
	public static String readSucursal() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put("idSucursal", "14");
		
		String jSon = getJsonFromService(jsCont.toString(), SERV_BRANCH+METHOD_READ );	
		return jSon; 
	}
	
	public static String updSucursal() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put("idSucursal", "14");
		jsCont.put("nombre", "Vallejo");
		jsCont.put("ubicacion", "Avenida Vallejo 25, Azcapotzalco");
		
		String jSon = getJsonFromService(jsCont.toString(), SERV_BRANCH+METHOD_UPDATE );	
		return jSon; 
	}
	
	public static String delSucursal() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put("idSucursal", "14");
		
		String jSon = getJsonFromService(jsCont.toString(), SERV_BRANCH+METHOD_DELETE );	
		return jSon; 
	}

}
