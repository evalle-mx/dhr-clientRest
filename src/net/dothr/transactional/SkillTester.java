package net.dothr.transactional;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import net.dothr.MainAppTester;

/**
 * Clase de pruebas para modulo de Habilidades en Persona y Posición
 * @author dothr
 */

public class SkillTester extends MainAppTester {
	
	private static final String IDCONF = IDCONF_DOTHR;
	protected static final String URI_PERSONA_HABILIDAD = "/module/personSkill";
	protected static final String URI_POSICION_HABILIDAD = "/module/positionSkill";
	
	private static Logger log4j = Logger.getLogger( SkillTester.class );
	
	
	
	public static void main(String[] args) {
		try{
		
			personaHabilidadInsert();
//			personaHabilidadUpdate();
//			personaHabilidadDelete();
			
//			posicionHabilidadInsert();
//			posicionHabilidadUpdate();
//			posicionHabilidadDelete();
			
//			posicionHabilidadGet();
			
			
			
		}catch (Exception e){
			log4j.fatal(e);
		}
	}
	
	
	/* ************************  PERSONA   ******************************** */
	/**
	 * Verifica funcionamiento de servicio para agregar Habilidad a persona PERSKILL.C
	 * @throws Exception
	 */
	public static String personaHabilidadInsert() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );
		jsCont.put(P_JSON_PERSONA, "3");
		
		
//		jsCont.put("descripcion", "Inglés");	//INGLES
//		jsCont.put("idDominio", "3");	//"Bueno" 
		
		/* [{"idHabilidad":"13","name":"idHabilidad","value":"21","code":"004","type":"I"}] */
		String jSon = getJsonFromService(jsCont.toString(), URI_PERSONA_HABILIDAD+URI_CREATE );	
		return jSon; 
	}
	
	/**
	 * Verifica funcionamiento de servicio Update de Habilidad en Persona  PERSKILL.U
	 * @throws Exception
	 */
	public static String personaHabilidadUpdate() throws Exception {
		JSONObject jsCont = new JSONObject();
//		jsCont.put(P_JSON_PERSONA, "3");
		jsCont.put(P_JSON_IDCONF, IDCONF );
		jsCont.put("idHabilidad", "21");
		
		jsCont.put("descripcion", "Inglés");
		
		jsCont.put("idDominio", "4");	//Avanzado
		
		/* [] */
		String jSon = getJsonFromService(jsCont.toString(), URI_PERSONA_HABILIDAD+URI_UPDATE );
		return jSon;
	}
	
	/**
	 * Verifica funcionamiento de borrado de Habilidad Persona PERSKILL.D
	 * @throws Exception
	 */
	public static String personaHabilidadDelete() throws Exception {
		
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );		
		jsCont.put(P_JSON_PERSONA, "3");	
		
		jsCont.put("idHabilidad", "1");
		
		/* [{"name":"idHabilidad","value":"2","code":"007","type":"I"}] */
		String jSon = getJsonFromService(jsCont.toString(), URI_PERSONA_HABILIDAD+URI_DELETE );
		return jSon;
	}
	
	
	
	/* ************************  POSICION   ******************************** */
	/**
	 * Verifica funcionamiento de servicio para agregar Habilidad a posición: POSITION.C
	 * @throws Exception
	 */
	public static String posicionHabilidadInsert() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDSELEX );
		jsCont.put("idPosicion", "11");
		
//		jsCont.put("idHabilidadpos", "2");	//2.Alemán | 8.Word
//		jsCont.put("idDominio", "1");
//		
		/* [{"name":"idPoliticaMHabilidad","value":"61","code":"004","type":"I"}] */
		String jSon = getJsonFromService(jsCont.toString(), URI_POSICION_HABILIDAD+URI_CREATE );	
		return jSon; 
	}
	
	/**
	 * Verifica funcionamiento de servicio Update de Habilidad en Persona  POSITION.U
	 * @throws Exception
	 */
	public static String posicionHabilidadUpdate() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDSELEX );
		jsCont.put("idPoliticaMHabilidad", "46");
		
		jsCont.put("idHabilidadpos", "2");	//2.Alemán | 8.Word
		jsCont.put("idDominio", "1");
		
		/* [] */
		String jSon = getJsonFromService(jsCont.toString(), URI_POSICION_HABILIDAD+URI_UPDATE );
		return jSon;
	}
	
	/**
	 * Verifica funcionamiento de borrado de Habilidad Persona POSITION.D
	 * @throws Exception
	 */
	public static String posicionHabilidadDelete() throws Exception {
		
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDSELEX );
		jsCont.put("idPoliticaMHabilidad", "36");
		
		/* [{"name":"idHabilidad","value":"2","code":"007","type":"I"}] */
		String jSon = getJsonFromService(jsCont.toString(), URI_POSICION_HABILIDAD+URI_DELETE );
		return jSon;
	}
	
	
	/**
	 * Verifica funcionamiento de borrado de Habilidad Persona POSITION.D
	 * @throws Exception
	 */
	public static String posicionHabilidadGet() throws Exception {
		
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDSELEX );
		jsCont.put("idPosicion", "1");
		
		/* [{"name":"idHabilidad","value":"2","code":"007","type":"I"}] */
		String jSon = getJsonFromService(jsCont.toString(), URI_POSICION_HABILIDAD+URI_GET );
		return jSon;
	}
	
}
