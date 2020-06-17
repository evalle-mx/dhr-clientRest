package net.dothr.transactional;

import org.json.JSONObject;

import net.dothr.MainAppTester;

public class IdiomaTester extends MainAppTester {
	
	private static final String IDCONF = IDCONF_DOTHR;
	
//	private static Logger log4j = Logger.getLogger( SkillTester.class );

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
//			personaIdiomaInsert();
//			personaIdiomaUpdate();
//			personaIdiomaDelete();
			
//			personaIdiomaGet();
			
//			posicionIdiomaInsert();
			posicionIdiomaUpdate();
//			posicionIdiomaDelete();
//			
//			posicionIdiomaGet();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	/**
	 * Verifica funcionamiento de servicio para agregar Idioma a persona LANGUAGE.C
	 * @throws Exception
	 */
	public static String personaIdiomaInsert() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );
		jsCont.put(P_JSON_PERSONA, "784");
		
		jsCont.put("idDominio", "3");	//"Bueno"
		jsCont.put("idIdioma", "1");	//"Inglés" 
		
		/* [{"idPersonaIdioma":"13","name":"idPersonaIdioma","value":"21","code":"004","type":"I"}] */
		String jSon = getJsonFromService(jsCont.toString(), URI_IDIOMA+URI_CREATE );	
		return jSon; 
	}
	
	/**
	 * Verifica funcionamiento de servicio para agregar Idioma a persona LANGUAGE.U
	 * @throws Exception
	 */
	public static String personaIdiomaUpdate() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );
		jsCont.put("idPersonaIdioma", "3");
		
		
		jsCont.put("idDominio", "1");	//"Bueno"
		jsCont.put("idIdioma", "1");	//"Inglés" 
		
		String jSon = getJsonFromService(jsCont.toString(), URI_IDIOMA+URI_UPDATE );	
		return jSon; 
	}

	
	/**
	 * Verifica funcionamiento de servicio para agregar Idioma a persona LANGUAGE.D
	 * @throws Exception
	 */
	public static String personaIdiomaDelete() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );
		jsCont.put("idPersonaIdioma", "3");
		
		/* [{"idPersonaIdioma":"13","name":"idPersonaIdioma","value":"21","code":"007","type":"I"}] */
		String jSon = getJsonFromService(jsCont.toString(), URI_IDIOMA+URI_DELETE );	
		return jSon; 
	}
	
	/**
	 * Verifica funcionamiento de servicio para agregar Idioma a persona LANGUAGE.G
	 * @throws Exception
	 */
	public static String personaIdiomaGet() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );
		jsCont.put(P_JSON_PERSONA, "2");
		String jSon = getJsonFromService(jsCont.toString(), URI_IDIOMA+URI_GET );
		
		return jSon; 
	}
	
	/* **************** POSICION   ********************* */
	/**
	 * Verifica funcionamiento de servicio para agregar Idioma a Posicion LANGUAGE.C
	 * @throws Exception
	 */
	public static String posicionIdiomaInsert() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );
		jsCont.put(P_JSON_POSICION, "5");
		
		jsCont.put("idDominio", "2");	//"Bueno"
		jsCont.put("idIdioma", "3");	//"Inglés" 
		
		String jSon = getJsonFromService(jsCont.toString(), URI_IDIOMA+URI_CREATE );	
		//Obtiene idPosicionIdioma == idPoliticaMValor
		return jSon; 
	}
	/**
	 * Verifica funcionamiento de servicio para agregar Idioma a Posicion LANGUAGE.U
	 * @throws Exception
	 */
	public static String posicionIdiomaUpdate() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );
		jsCont.put("idPosicionIdioma", "37");  //idPoliticaMValor
		jsCont.put("idPosicion", "5");
		
		jsCont.put("idDominio", "1");	//
		jsCont.put("idIdioma", "1");	// 
		//==> Json: {"idPerfil":"5","idPosicionIdioma":"37","idPosicion":"5","idDominio":"3","idPersona":"2","idEmpresaConf":"1"}
		String jSon = getJsonFromService(jsCont.toString(), URI_IDIOMA+URI_UPDATE );	
		return jSon; 
	}
	
	/**
	 * Verifica funcionamiento de servicio para agregar Idioma a Posicion LANGUAGE.D
	 * @throws Exception
	 */
	public static String posicionIdiomaDelete() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );
		jsCont.put("idPosicionIdioma", "18");
		
		String jSon = getJsonFromService(jsCont.toString(), URI_IDIOMA+URI_DELETE );	
		return jSon; 
	}
	
	/**
	 * Verifica funcionamiento de servicio para agregar Idioma a persona LANGUAGE.U
	 * @throws Exception
	 */
	public static String posicionIdiomaGet() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );
		jsCont.put(P_JSON_POSICION, "5");
		String jSon = getJsonFromService(jsCont.toString(), URI_IDIOMA+URI_GET );
		
		return jSon; 
	}
}
