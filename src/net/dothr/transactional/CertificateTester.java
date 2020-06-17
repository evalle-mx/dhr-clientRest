package net.dothr.transactional;

import org.json.JSONObject;

import net.dothr.MainAppTester;

public class CertificateTester extends MainAppTester {
	
	private static final String URI_PERSONA_CERTIFICACION="/module/personCert";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			personaCertificacionInsert();
//			personaCertificacionUpdate();
//			personaCertificacionDelete();
//			personaCertificacionGet();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* ************************  PERSONA   ******************************** */
	/**
	 * Verifica funcionamiento de servicio para agregar Certificacion a persona PERSCERT.C
	 * @throws Exception
	 */
	public static String personaCertificacionInsert() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF_DOTHR );
		jsCont.put(P_JSON_PERSONA, "908");
		
		/* Full Create: */
		jsCont.put("tituloCert", "finanzas");	//HRBP
		jsCont.put("idGrado", "3");	//"Bueno" 
		
		/* [{"name":"idCertificacion","value":"714","code":"004","type":"I"}] */
		String jSon = getJsonFromService(jsCont.toString(), URI_PERSONA_CERTIFICACION+URI_CREATE );	
		return jSon; 
	}
	
	/**
	 * Verifica funcionamiento de servicio Update de Certificacion en Persona  PERSCERT.U
	 * @throws Exception
	 */
	public static String personaCertificacionUpdate() throws Exception {
		JSONObject jsCont = new JSONObject();
//		jsCont.put(P_JSON_PERSONA, "3");
		jsCont.put(P_JSON_IDCONF, IDCONF_DOTHR );
		jsCont.put("idCertificacion", "4");
		
//		jsCont.put("tituloCert", "Especialidad Java WEB Application Developer");
		
		jsCont.put("idGrado", "1");	//Avanzado
		
		/* [] */
		String jSon = getJsonFromService(jsCont.toString(), URI_PERSONA_CERTIFICACION+URI_UPDATE );
		return jSon;
	}
	
	/**
	 * Verifica funcionamiento de borrado de Certificacion Persona PERSCERT.D
	 * @throws Exception
	 */
	public static String personaCertificacionDelete() throws Exception {
		
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF_DOTHR );		
		jsCont.put("idCertificacion", "2");
		
		/* [{"name":"idCertificacion","value":"714","code":"007","type":"I"}] */
		String jSon = getJsonFromService(jsCont.toString(), URI_PERSONA_CERTIFICACION+URI_DELETE );
		return jSon;
	}

	/**
	 * Verifica funcionamiento de servicio para agregar Certificacion a persona PERSCERT.C
	 * @throws Exception
	 */
	public static String personaCertificacionGet() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF_DOTHR );
		jsCont.put(P_JSON_PERSONA, "83");
		
		/* Full Create: */
//		jsCont.put("descripcion", "Inglés");	//INGLES
//		jsCont.put("idDominio", "3");	//"Bueno" 
		
		/* [{"name":"idCertificacion","value":"714","code":"004","type":"I"}] */
		String jSon = getJsonFromService(jsCont.toString(), URI_PERSONA_CERTIFICACION+URI_GET );	
		return jSon; 
	}
}
