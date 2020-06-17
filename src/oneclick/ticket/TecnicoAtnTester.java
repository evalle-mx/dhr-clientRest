package oneclick.ticket;

import org.json.JSONObject;

import netto.AppTester;

public class TecnicoAtnTester extends AppTester {

	public final static String SERV_TECHNICIAN = "/module/technician";
	
	
	public static void main(String[] args) {
		try {
			getTecnicos();
//			createTecnicoAtn();
//			readTecnicoAtn();
//			updateTecnicoAtn();
//			deleteTecnicoAtn();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String getTecnicos() throws Exception {
		JSONObject jsCont = new JSONObject();
//		jsCont.put("idUsuario", "99");
		
//		jsCont.put("idTipoTicket", "1");
//		jsCont.put("idTipoEstatus", "1");		
//		jsCont.put("idTecnicoAtencion", "1");
		
		String jSon = getJsonFromService(jsCont.toString(), SERV_TECHNICIAN+METHOD_GET );	
		return jSon; 
	}
	
	
	public static String createTecnicoAtn() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put("idUsuario", "1");	//Usuario a asignar como Tecnico
		jsCont.put("idArea", "1");
		jsCont.put("idSucursal", "1");
		//Opcional:
		jsCont.put("nombreCompleto", "Tecnico pruebas"); //Alias del tecnico //TODO cambiar nombreCompleto a alias
		
		String jSon = getJsonFromService(jsCont.toString(), SERV_TECHNICIAN+METHOD_CREATE );	
		return jSon; 
	}
	
	public static String readTecnicoAtn() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put("idTecnicoAtencion", "1");
		
		String jSon = getJsonFromService(jsCont.toString(), SERV_TECHNICIAN+METHOD_READ );	
		return jSon; 
	}
	
	public static String updateTecnicoAtn() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put("idTecnicoAtencion", "1");
		jsCont.put("idArea", "1");
		jsCont.put("idSucursal", "1");
		//Opcional:
		jsCont.put("nombreCompleto", "Tecnico pruebas"); 		
		
		String jSon = getJsonFromService(jsCont.toString(), SERV_TECHNICIAN+METHOD_UPDATE );	
		return jSon; 
	}
	
	public static String deleteTecnicoAtn() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put("idTecnicoAtencion", "1");
		
		String jSon = getJsonFromService(jsCont.toString(), SERV_TECHNICIAN+METHOD_DELETE );	
		return jSon; 
	}
	
}
