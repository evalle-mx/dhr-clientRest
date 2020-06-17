package net.dothr.transactional;

import org.json.JSONObject;

import net.dothr.MainAppTester;

public class PostulantTester extends MainAppTester {
	
	private static final String IDCONF = IDSELEX;
	public final static String URI_POSTULANT = "/module/postulante";
	
	
	public static void main(String[] args) {
		try {
//			getPostulant();
			countPostulant();
			
			
//			getPostulantTest();
//			ping(URI_POSTULANT);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	public static void getPostulant() throws Exception{
		String idPosicion = "5";
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDCONF);
		
		json.put("idPosicion", idPosicion); 
		json.put("activos", "1");
		
		String jSon = getJsonFromService(json.toString(), URI_POSTULANT+URI_GET );
	}
	
	public static void countPostulant() throws Exception{
		String idPosicion = "5";
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDCONF);
		
		json.put("idPosicion", idPosicion); 
//		json.put("activos", "1");
		
		String jSon = getJsonFromService(json.toString(), URI_POSTULANT+"/count" );
	}
	

}
