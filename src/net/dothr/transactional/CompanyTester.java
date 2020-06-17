package net.dothr.transactional;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import net.dothr.MainAppTester;

@SuppressWarnings("unused")
public class CompanyTester extends MainAppTester {
	private static final String IDCONF = IDCONF_DOTHR;

	protected static Logger log4j = Logger.getLogger( CompanyTester.class );
	
	private final static String URI_PUBLICATE = "/setEnterpriseResumePublication";
	
	private final static String URI_GET_REQUESTS="/getRequests";
	private final static String URI_GET_ASSOCIATES="/getAssociates";
	private final static String URI_REQUEST_ASSOCIATION = "/requestAssociation";
	private final static String URI_UPD_ASSOCIATION = "/updateAssociation";
	private final static String URI_DISASSOCIATE="/disassociate";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
//			pruebaGetEmpresa();
//			pruebaCreateEmpresa();
//			pruebaReadEmpresa();
//			pruebaUpdateEmpresa();
//			pruebaPublicate();
			
			pruebaGetAssociates();
//			pruebaRequestAssociate();
//			pruebaUpdAssociate();
//			pruebaDisAssociate();	//HASTA FUNCIONAR REQUEST
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Verifica funcionamiento del uriCode COMPANY.G
	 * Obtiene las empresas relacionadas a una persona determinada, se pueden añadir parametros y filtros
	 * @throws Exception
	 */
	public static void pruebaGetEmpresa() throws Exception {
		
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDSELEX);
		json.put(P_JSON_PERSONA, "7");
		
		/* opcionales */
//		json.put("idTipoRelacion", "9");
		json.put("idEstatusInscripcion", "3");
//		json.put("historico", "true");  ///  1&true | 0,false,x,.... (Incluye aquellos q tienen fin de relacion-inactiva-)
		
		
		String jSon = getJsonFromService(json.toString(), URI_COMPANY+URI_GET );
	}

	/**
	 * Verifica funcionamiento del uriCode COMPANY.C
	 * @throws Exception
	 */
	public static void pruebaCreateEmpresa() throws Exception{
		//TODO verificar idEstatus por default si es creado o Activo
		final String idPersona = "7";
		
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDCONF);
		json.put(P_JSON_PERSONA, idPersona); //Se debe enviar persona existente, sino envia error(TODO generar error y documentar)
		json.put("idTipoRelacion", "9");
		json.put("razonSocial", "STARK Industries S.A."); //TODO poner como opcional
		json.put("rfc", "ABC010101E5C"); // ABC010101ABC | STA910101RK1 | TEL721214GK6
		
		/* opcionales  */
//		json.put("nombre", "Industrias MINION Cinco ");
//		json.put("descripcion", "Seguidores dedicados a servir a su amo sin descanso");
		
//		json.put("nombre", "STARK CORPORATION");
//		json.put("razonSocial", "STARK Industries S.A.");
//		json.put("descripcion", "Desarrollo de nuevas tecnologias electricas, de ingenieria, aeronautica, robotica y tecnologia de microciencia");
//		json.put("numeroEmpleados", "200"); 
//		json.put("clientes", "USA NAVY, USA ARMY, SHIELD, MARVEL"); 
//		json.put("socios", "MARVEL, BANNER LABS, CAPCOM AC");
//		json.put("anioInicio", "2010");
//		json.put("mesInicio", "10");
//		json.put("diaInicio", "3");
		
//		System.out.println(json.toString());
		String jSon = getJsonFromService(json.toString(), URI_COMPANY+URI_CREATE );
	}
	
	/**
	 * Verifica funcionamiento del uriCode COMPANY.R
	 * @throws Exception 
	 */
	public static void pruebaReadEmpresa() throws Exception{
		final String idEmpresa = "1";
		
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDCONF);
		json.put(P_JSON_EMPRESA, idEmpresa);
		
		String jSon = getJsonFromService(json.toString(), URI_COMPANY+URI_READ );
	}
	
	/**
	 * Verifica funcionamiento del servicio COMPANY.U
	 * @throws Exception 
	 */
	public static void pruebaUpdateEmpresa() throws Exception{
		final String idEmpresa = "22";
		
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDCONF);
		json.put(P_JSON_EMPRESA, idEmpresa);
		
		
//		json.put("nombre", "STARK ASOCIATES");
//		json.put("razonSocial", "STARK Asociates S.C.");
		json.put("descripcion", "Desarrollo de nuevas tecnologías, de ingeniería, aeronáutica, robótica y tecnología de microciencia");
//		json.put("numeroEmpleados", "50"); 
//		json.put("clientes", "USA NAVY, USA ARMY, SHIELD, MARVEL"); 
//		json.put("socios", "BANNER LABS, CAPCOM");
//		json.put("anioInicio", "1955");
//		json.put("mesInicio", "10");
//		json.put("diaInicio", "3");
		
		/* Admin */
//		json.put("idPersona", "1500");
//		json.put("estaVerificado", "1");
//		json.put("rfc", "STA012389RK2");
//		json.put("idEstatusInscripcion", "2");
		
		//json.put("idTipoRelacion", "5"); //relacion_empresa_persona
		
		
		//clientes USA NAVY, USA ARMY, SHIELD, MARVEL
		//fund 1945
		//webPage: http://www.starkexpo2010.com/
		
		String jSon = getJsonFromService(json.toString(), URI_COMPANY.concat(URI_UPDATE) );
	}
	
	/**
	 * Verifica funcionamiento de publicacion COMPANY.P
	 * @throws Exception
	 */
	public static void pruebaPublicate() throws Exception {
		final String idEmpresa = "22";
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDCONF);
		json.put(P_JSON_EMPRESA, idEmpresa);
		
		String jSon = getJsonFromService(json.toString(), URI_COMPANY+URI_PUBLICATE );
	}
	
	
	/* ***************************** SERVICIOS DE ASOCIACION A EMPRESA   ********************** */
	/**
	 * Realiza prueba de la solicitud de anexión a empresa
	 * @throws Exception
	 */
	public static void pruebaGetAssociates() throws Exception {
		//ASSOCIATE.G =>	 [getRequests]		
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDSELEX);
		json.put(P_JSON_EMPRESA, "1");
//		json.put("idPersonaEjecutor", "1" ); //Propuesto para bitacora
		//Opcionales:
//		json.put("idTipoRelacion", "9");
//		json.put("idEstatusInscripcion", "2");
		
		String jSon = getJsonFromService(json.toString(), URI_COMPANY+URI_GET_ASSOCIATES );
	}
	
	/**
	 * Realiza prueba de la solicitud de anexión a empresa ASSOCIATE.C
	 * @throws Exception
	 */
	public static void pruebaRequestAssociate() throws Exception{
		// ASSOCIATE.C => requestAssociation 
		final String idEmpresa = "1";
		final String idPersona = "156";//asociado
		
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDCONF);
		json.put(P_JSON_EMPRESA, idEmpresa);
		json.put(P_JSON_PERSONA, idPersona ); //Persona que solicita anexion
		
		json.put("idTipoRelacion", "9" );
		
		String jSon = getJsonFromService(json.toString(), URI_COMPANY+URI_REQUEST_ASSOCIATION);
		
		//FIXME ERROR: insert or update on table "relacion_empresa_persona" violates foreign key constraint "rel_ep_per_fk"
	}
	
	
	/**
	 * Realiza modificación (Aprobacion/rechazo) de relacion empresa-persona
	 * @throws Exception
	 */
	public static void pruebaUpdAssociate() throws Exception{
		//ASSOCIATE.U => updateAssociation
		final String idEmpresa = "1";
		final String idPersona = "156";//asociado
//		final String idPersonaEjecutor = "5000";//adm
		final String idRelacionEmpresaPersona = "160";
//		final String operacionRelacion = "ACEPTA"; // CANCELA | ACEPTA | RECHAZA
		final String idTipoRelacion = "9"; // 1-Empleado, 2-proveedor, 4-rechazado, 6-exempleado
		
		final boolean representante = true;
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDCONF);
		json.put(P_JSON_EMPRESA, idEmpresa);
		json.put(P_JSON_PERSONA, idPersona );
//		json.put("idPersonaEjecutor", idPersonaEjecutor);
		
		json.put("idTipoRelacion", idTipoRelacion );
		json.put("idRelacionEmpresaPersona", idRelacionEmpresaPersona);
		json.put("representante", representante);
		
		String jSon = getJsonFromService(json.toString(), URI_COMPANY+URI_UPD_ASSOCIATION);
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public static void pruebaDisAssociate() throws Exception{
		//disassociatePerson
		final String idEmpresa = "2";
		final String idPersona = "3";//asociado
		final String idPersonaEjecutor = "5000";//adm
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDCONF);
		json.put(P_JSON_EMPRESA, idEmpresa);
		json.put(P_JSON_PERSONA, idPersona );
		json.put("idPersonaEjecutor", idPersonaEjecutor);
		
		String jSon = getJsonFromService(json.toString(), URI_COMPANY+URI_DISASSOCIATE );
	}
}
