package net.dothr.transactional;

import net.dothr.MainAppTester;
import net.tce.dto.CurriculumDto;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

@SuppressWarnings("unused")
public class CurriculumTester extends MainAppTester {
	
	private static final String IDCONF = IDCONF_DOTHR;
	
	protected static final String CV_PERS_INITIAL ="/module/curriculumManagement/initial";
	protected static final String CV_RECOVERY_MAIL="/module/curriculumManagement/recovery";
	protected final static String CV_RESTORE_PWD="/module/curriculumManagement/restore";
	protected static final String CV_PERS_URICODES="/module/curriculumManagement/uricodes";
	
	protected static final String URI_CREATEMANUAL = "/createManual";
	
	static Logger log4j = Logger.getLogger( CurriculumTester.class );

	/** ***********************************************************************************
	 *  ******************************    M A I N    **************************************
	 *  ***********************************************************************************
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			/* >>>> Flujo Login */
//			pruebaExistsPersona();
//			pruebaInitialPersona();
//			personaUricodes();
//			pruebaGetMenu(); // <<<< */
			
//			pruebaReadPersona();
			
//			pruebaGetPersonas();
			
//			pruebaReqRecovery();
//			pruebaRestorePassword();
//			pruebaUpdatePersona();
//			pruebaPublication();			
//			pruebaFileGet();
			
			/*  pruebas anexos */
			
			pruebaCreateByMail();
//			createManual();
			
		/*	pruebaCreatePersonaComplete(); //Prototipo */   

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String createManual() throws Exception {
		CurriculumDto dto = new CurriculumDto();
		gson = new Gson();
		
		dto.setIdEmpresaConf(IDCONF_DOTHR);
		dto.setEmail("netto.speed@gmail.com");
		dto.setPassword("15e2b0d3c33891ebb0f1ef609ec419420c20e320ce94c65fbc8c3312448eb225"); //YA encriptada
		dto.setIdRol("2");
		dto.setIdEstatusInscripcion("2");
		
		dto.setNombre("NEtto"); dto.setApellidoPaterno("Prueba"); dto.setApellidoMaterno("Test");
		String jSon = getJsonFromService(gson.toJson(dto), URI_CURRICULUM+URI_CREATEMANUAL );
		return jSon;
	}
	
	/**
	 * Prueba de busqueda de personas por parámetros PERSON.G
	 * @return
	 * @throws Exception
	 */
	public static String pruebaGetPersonas() throws Exception {
		JSONObject json = new JSONObject();

		json.put(P_JSON_IDCONF, IDCONF_DOTHR);		//IDCONF | 3 -> Selex
//		json.put(P_JSON_PERSONA, "-1");
//		json.put(P_JSON_EMAIL, "monica.quintero@pirh.com.mx");
//		json.put("nombre", "Monica");
//		json.put("apellidoPaterno", "Giles");
//		json.put("apellidoMaterno", "Luna");
		json.put("idRol", "5");
//		json.put("idEstatusInscripcion", "3");	
		
		
		
		System.out.println(json.toString());
		String jSon = getJsonFromService(json.toString(), URI_CURRICULUM+ URI_GET);
		JSONArray jsResponse = new JSONArray(jSon);
		System.out.println("# de elementos: "+ jsResponse.length());
		return jSon;
	}
	
	public static String pruebaCreateByMail() throws Exception {
		JSONObject json = new JSONObject();

		json.put(P_JSON_IDCONF, "3");		//IDCONF | 3 -> Selex
		json.put(P_JSON_EMAIL, "netto_speed@hotmail.com");	//ernesto.valle@dothr.net	| netto_speed@hotmail.com
		json.put("password", "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92");
		System.out.println(json.toString());
		String jSon = getJsonFromService(json.toString(), URI_CURRICULUM+ URI_CREATE);
		return jSon;
	}
	
	/**
	 * Servicio PERSON_UC
	 * @return
	 * @throws Exception
	 */
	public static String personaUricodes() throws Exception {
		JSONObject json = new JSONObject();

		json.put(P_JSON_IDCONF, IDCONF);
		json.put("email", "octavio.linares@dothr.net");
		
		String jSon = getJsonFromService(json.toString(), CV_PERS_URICODES );
		return jSon;
	}
	
	/**
	 * Prueba para verificar Persona [PERSON.E]
	 * @throws Exception 
	 */
	public static String pruebaExistsPersona() throws Exception{	
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDCONF);
		json.put("email", "ernesto.valle@dothr.net");
		
		String jSon = getJsonFromService(json.toString(), URI_CURRICULUM+URI_EXISTS );
		return jSon;
	}
	
	/**
	 * Verifica funcionamiento del uriCode PERSON.I
	 * @throws Exception
	 */
	public static String pruebaInitialPersona() throws Exception{
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, "1");
		json.put("email", "ernesto.valle@dothr.net");
		
		String jSon = getJsonFromService(json.toString(), CV_PERS_INITIAL );//[{"idPersona":"3","role":"9"}]
		return jSon;
	}
	
	/**
	 * Verifica funcionamiento para leer Persona PERSON.RM
	 * @param
	 * @throws Exception 
	 */
	public static String pruebaReqRecovery() throws Exception{
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDCONF);
		json.put("email", "netto.speed@gmail.com");
		
		String jSon = getJsonFromService(json.toString(), CV_RECOVERY_MAIL );
		/*  [{"code":"004","type":"I","message":"Se ha enviado un correo electrónico, verifique su bandeja de entrada"}] */
		return jSon;
	}
	
	/**
	 * Verifica funcionamiento para leer Persona PERSON.RP
	 * @param
	 * @throws Exception 
	 */
	public static String pruebaRestorePassword() throws Exception{
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDCONF);
		json.put("email", "netto.speed@gmail.com");
		json.put(P_JSON_PERSONA, "DA2967A94F9558919740A2BF446DFD89XIFD27XC8HF6592I");
		json.put("password", "123456789");
		
		String jSon = getJsonFromService(json.toString(), CV_RESTORE_PWD );
		return jSon;
		//http://localhost:8090/selex/index.html#/reiniciar/4FEE0C972CE1F60164A2949E057B06CALT38V0BN4HG17BDL/netto.speed@gmail.com
	}
	
	/**
	 * Verifica funcionamiento para leer Persona PERSON.R
	 * @param
	 * @throws Exception 
	 */
	public static String pruebaReadPersona() throws Exception{
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDCONF);
		json.put(P_JSON_PERSONA, "5000");	//Probar con 1 para obtener nulls
		
		String jSon = getJsonFromService(json.toString(), URI_CURRICULUM+URI_READ );
		return jSon;
	}
	
	/**
	 * Verifica funcionamiento de sevicio Update PERSON.U
	 */
	public static String pruebaUpdatePersona() throws Exception{
		
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDCONF);
		json.put(P_JSON_PERSONA, "1");
		
		json.put("nombre", "Patricia Arminda");
		json.put("apellidoPaterno", "Ruiz");
		json.put("apellidoMaterno", "Martínez");
		json.put("anioNacimiento", "1980");
		json.put("mesNacimiento", "5");
		json.put("diaNacimiento", "5");
		json.put("idTipoGenero", "0");
		json.put("idEstadoCivil", "2");
		json.put("idPais", "1");		
		json.put("cambioDomicilio", "1");
		json.put("idTipoDispViajar", "1");
		json.put("disponibilidadHorario", "1");		
		json.put("salarioMin", "15000");
		json.put("salarioMax", "20000");
		
//		json.put("idTipoJornada", "2");
//		json.put("antiguedadDomicilio", "2");
//		json.put("idTipoEstatusPadres", "4");
		
//		json.put("idTipoContrato", "1");
//		json.put("numeroHijos", "0");		
//		json.put("idTipoVivienda", "1");
		
//		json.put("idPeriodoEstadoCivil", "1");
//		json.put("idAmbitoGeografico", "3");
//		json.put("numeroDependientesEconomicos", "2");
//		json.put("idTipoPrestacion", "3");
//		json.put("permisoTrabajo", "1");
//		json.put("idTipoConvivencia", "4");
		/*
		json.put("idEstatusInscripcion", "2");
		*/
		String jSon = getJsonFromService(json.toString(), URI_CURRICULUM+URI_UPDATE );
		return jSon;
	}	
	
	/**
	 * Verifica funcionamiento de servicio de Publicacion PERSON.P
	 * @throws Exception
	 */
	public static void pruebaPublication() throws Exception {
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDCONF_DOTHR);
		json.put(P_JSON_PERSONA, "26");
		
		String jSon = getJsonFromService(json.toString(), CV_PERS_PUBLICATE );
	}

	/**
	 * Verifica funcionamiento para lista de Contenido de Persona FILE.G
	 * @throws Exception
	 */
	public static void pruebaFileGet() throws Exception {
		int tipoContenido = 1;
		pruebaFileGet(tipoContenido, P_JSON_PERSONA, "3");
	}
	
	/**
	 * Obtener menú por usuario (idPersona)
	 * @return
	 * @throws Exception
	 * 
	 * @uriService admin/management/menu
	 */
	public static String pruebaGetMenu() throws Exception {
		// ADMIN.M...
		JSONObject json = new JSONObject();

		json.put(P_JSON_IDCONF, IDSELEX);
		json.put(P_JSON_PERSONA, "2");
				
		String jSon = getJsonFromService(json.toString(), URI_ADMIN + URI_MENU);
		return jSon;
	}
}
