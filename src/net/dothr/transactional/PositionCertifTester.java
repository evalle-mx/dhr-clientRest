package net.dothr.transactional;

import net.dothr.MainAppTester;

import org.json.JSONObject;

public class PositionCertifTester extends MainAppTester {
	
	public final static String URI_POSICION_CERTIFICAT="/module/positionCert";

	public static void main(String[] args) {
		try {
//			posicionCertifInsert();
//			posicionCertifUpdate();
			posicionCertifGet();
//			posicionCertifDelete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Verifica funcionamiento de servicio para agregar Habilidad a posición: POSITION.C
	 * @throws Exception
	 */
	public static String posicionCertifInsert() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDSELEX );
		jsCont.put("idPosicion", "5");
		
		jsCont.put("tituloCert", "Certificacion Gestión Internacional de Recursos Humanos (RH Manager)");
		jsCont.put("idGrado", "5");
		
		/* [{"name":"idCertificacion","value":"61","code":"004","type":"I"}] */
		String jSon = getJsonFromService(jsCont.toString(), URI_POSICION_CERTIFICAT+URI_CREATE );	
		return jSon; 
	}
	
	/**
	 * Verifica funcionamiento de servicio Update de Habilidad en Persona  POSITION.U
	 * @throws Exception
	 */
	public static String posicionCertifUpdate() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDSELEX );
		jsCont.put(P_JSON_POSICION, "5" );
		jsCont.put("idCertificacion", "36");
		
		jsCont.put("tituloCert", "Apache WEB");
		jsCont.put("idGrado", "2");
		
		/* [] */
		String jSon = getJsonFromService(jsCont.toString(), URI_POSICION_CERTIFICAT+URI_UPDATE );
		return jSon;
	}
	/**
	 * Verifica funcionamiento de servicio para agregar Habilidad a posición: POSITION.C
	 * @throws Exception
	 */
	public static String posicionCertifGet() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDSELEX );
		jsCont.put("idPosicion", "5");
		
		String jSon = getJsonFromService(jsCont.toString(), URI_POSICION_CERTIFICAT+URI_GET );	
		return jSon; 
	}
	
	/**
	 * Verifica funcionamiento de borrado de Habilidad Persona POSITION.D
	 * @throws Exception
	 */
	public static String posicionCertifDelete() throws Exception {
		
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDSELEX );
		jsCont.put("idCertificacion", "11");	//idPoliticaMValor
		
		/* [{"name":"idCertificacion","value":"61","code":"007","type":"I"}] */
		String jSon = getJsonFromService(jsCont.toString(), URI_POSICION_CERTIFICAT+URI_DELETE );
		return jSon;
	}
	
}
