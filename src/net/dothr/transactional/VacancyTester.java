package net.dothr.transactional;

import java.util.Date;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import net.dothr.MainAppTester;
import net.utils.ClientRestUtily;

@SuppressWarnings("unused")
public class VacancyTester extends MainAppTester {

	private static final String IDCONF = IDCONF_DOTHR;
	private static Logger log4j = Logger.getLogger( VacancyTester.class );
	
	public final static String URI_VACANCYTEXT="/module/vacancyText";
	public final static String URI_VACANCYACADEMICWEIGHING="/module/vacancyAcademicWeighing";
	public final static String URI_VACANCYPUBLICATION="/setVacancyPublication";
	
	private final static String URI_PROFILE_ASSOCIATE = "/module/profile/updateAssociation";
	
	private final static String DIR_MASIVOS = "/home/tce/Documents/TCE/JSONback/pruebaMassiveCreate/";
	
	
	private final static String URI_POSITIONCOMPETENCE = "/module/positionCompet";
	
	public static void main(String[] args) {
		try {
//			pruebaUpdateError();
//			vacancyGet();
//			vacancyRead();
			vacancyUpdate();
//			vacancyAssociation(); //Utiliza servicios en Perfil
			
			
//			vacancyCreate();//TODO verificar con productivo
			
			
//			vacancyTextCreate();
//			vacancyTextUpdate();
//			vacancyTextDelete();
//			vacancyPublication();
//			pruebaInsertLocation();
//			deleteDomicio();
			
//			//vacancyCreateComplete();
			
//			positionCompetenceGet();
//			positionCompetenceUpd();
			
		} catch (Exception e) {
			e.printStackTrace();
		}  
	}
	
	private static void pruebaUpdateError() throws Exception{
			JSONObject jsonRequest = new JSONObject();
			jsonRequest.put(P_JSON_IDCONF, IDCONF_DOTHR );
			
			jsonRequest.put("idPosicion", "19");

//			jsonRequest.put("idTipoGeneroRequerido","1");
//			jsonRequest.put("idPersona","2");
//			jsonRequest.put("experienciaLaboralMinima","1080");
			
			jsonRequest.put("idGradoAcademicoMin","4");
			jsonRequest.put("idEstatusEscolarMin","2");
			
//			jsonRequest.put("idGradoAcademicoMax","4");
//			jsonRequest.put("idEstatusEscolarMax","1");
//			jsonRequest.put("edadMaxima","42");
//			jsonRequest.put("edadMinima","25");
			
			String jSon = getJsonFromService(jsonRequest.toString(), URI_VACANCY.concat(URI_UPDATE) );
	}
	/**
	 * Consume el servicio POSITIONCOMPETENCE.U
	 * @throws Exception
	 */
	public static void positionCompetenceUpd() throws Exception {
		
		JSONObject jsono = new JSONObject();
		jsono.put(P_JSON_IDCONF, IDSELEX );
		jsono.put("idCompetenciaPosicion", "99");
		String jSon = getJsonFromService(jsono.toString(), URI_POSITIONCOMPETENCE.concat(URI_UPDATE) );
		
	}
	
	/**
	 * Consume el servicio POSITIONCOMPETENCE.G
	 * @param idPersona
	 * @param idEmpresa
	 * @throws Exception
	 */
	public static void positionCompetenceGet() throws Exception {
		
		JSONObject jsono = new JSONObject();
		jsono.put(P_JSON_IDCONF, IDSELEX );
		jsono.put(P_JSON_POSICION, "5");
		String jSon = getJsonFromService(jsono.toString(), URI_POSITIONCOMPETENCE.concat(URI_GET) );
		printJson(jSon);
	}
	
	
	
	
	
	
	/**
	 * Consume el servicio VACANCY.G
	 * @param idPersona
	 * @param idEmpresa
	 * @throws Exception
	 */
	public static void vacancyGet() throws Exception {
		String stModo = "PARTIAL"; // PARTIAL | COMPLETE
		
		JSONObject jsono = new JSONObject();
		jsono.put(P_JSON_IDCONF, IDSELEX );
//		jsono.put("modo", stModo);
//		jsono.put("idPersona", "2");		
		jsono.put("idMonitor", "2");
		
//		jsono.put("idPosicion", "5");
//		jsono.put("personal", true);
//		jsono.put("idEmpresa", "1");
		
		jsono.put("valorActivo", "1");
		String jSon = getJsonFromService(jsono.toString(), URI_VACANCY.concat(URI_GET) );
		printJson(jSon);
	}
	
	
	public static void vacancyRead() throws Exception {
		JSONObject jsono = new JSONObject();
		
		jsono.put(P_JSON_IDCONF, IDSELEX );
		jsono.put("idPersona", "2");
		jsono.put("idPosicion", "9");
		
		String jSon = getJsonFromService(jsono.toString(), URI_VACANCY.concat(URI_READ) );
	}
	
	/**
	 * Utiliza el servicio para Crear vacante completa
	 * @throws Exception
	 */
	public static void vacancyCreateComplete() throws Exception {
		String jsonRequestTxt = null;
		
		//TEST probar si es posible cargar los datos de primer nivel en el Create (Fallo, no guarda los datos, solo con update)
		
		String stPosicion = ClientRestUtily.getJsonFile("position-2.json", DIR_MASIVOS);
//		String stPosicion = ClientRestUtily.getJsonFile("position-7.json", directorio);
		jsonRequestTxt = stPosicion.substring(1,stPosicion.length()-2);
		
		String jSon = getJsonFromService(jsonRequestTxt, URI_VACANCY.concat(URI_CREATECOMPLETE) );
		
	}
	
	/**
	 * Consume el servicio de Create (basico) utilizado en VACANCY.C
	 * @throws Exception
	 */
	public static void vacancyCreate() throws Exception {
		JSONObject jsonRequest = new JSONObject();
		jsonRequest.put(P_JSON_IDCONF, IDCONF );
		jsonRequest.put("idPersona", "1");
		jsonRequest.put("puesto", "Prueba Cliente");
		jsonRequest.put("esConfidencial","0");	//0/1
		
		//{"area":{"idArea":13,"descripcion":"Humanidades y Artes"},"idPosicion":"7","idPersona":"2","idEmpresaConf":"1"}
		
		
		String jSon = getJsonFromService(jsonRequest.toString(), URI_VACANCY.concat(URI_CREATE) );
	}
	
	/**
	 * Emplea el servicio de Modificación VACANCY.U
	 * @throws Exception
	 */
	public static void vacancyUpdate() throws Exception {
		JSONObject jsonRequest = new JSONObject();
		jsonRequest.put(P_JSON_IDCONF, IDCONF_DOTHR );
		jsonRequest.put("idPersona", "1");
		jsonRequest.put("idPosicion", "5");
		
//		jsonRequest.put("idPerfil", "5");
//		jsonRequest.put("idPersonaPerfil", "2");
		
		//TEST probar si es posible cargar los datos de primer nivel en el Create
		
//		jsonRequest.put("nombre", "Ejecutivo de RH");	//(new Date()).getSeconds());
//		jsonRequest.put("idTipoJornada", notZeroNull("1"));
//		jsonRequest.put("idTipoContrato", notZeroNull("1"));
//		jsonRequest.put("idAmbitoGeografico", notZeroNull("3"));
//		jsonRequest.put("esConfidencial", "1");
//		jsonRequest.put("salarioMin", "20000");
//		jsonRequest.put("salarioMax", "6000");
//		jsonRequest.put("sueldoNegociable", "1");
//		jsonRequest.put("idPeriodicidadPago", notZeroNull("1"));
//		jsonRequest.put("idTipoPrestacion", notZeroNull("1"));
//		jsonRequest.put("otros", "Apoyo economico en base a horas laboradas y grado de estudios. Convenio atraves de CAINTRA. Trabajo en zona de Santa Catarina. Excelente ambiente de trabajo. Horario flexible.");
		
//		jsonRequest.put("edadMinima", "21");
//		jsonRequest.put("edadMaxima", "32");		
//		jsonRequest.put("experienciaLaboralMinima", "851");
		jsonRequest.put("idGradoAcademicoMin", "4");		
		jsonRequest.put("idEstatusEscolarMin", "1");
		
//		jsonRequest.put("idGradoAcademicoMax", "6");
//		jsonRequest.put("idEstatusEscolarMax", notZeroNull("3"));
//		jsonRequest.put("idTipoGeneroRequerido", "0");//0/1
//		jsonRequest.put("idEstadoCivilRequerido", notZeroNull("0")); //Debe ser diferente a cero "1"
		
//		jsonRequest.put("idEmpresa", "1");
//		jsonRequest.put("personal", "true");
		
		/*>>>>  Prueba nueva versión*/
//		jsonRequest.put("idEstatusEscolarMax","1");
//		jsonRequest.put("idEstatusEscolarMin","2");
//		jsonRequest.put("idTipoGeneroRequerido","1");
//		jsonRequest.put("idPersona","2");
//		jsonRequest.put("idGradoAcademicoMin","4");
//		jsonRequest.put("experienciaLaboralMinima","1080");
//		jsonRequest.put("idGradoAcademicoMax","6");
//		jsonRequest.put("edadMaxima","42");
//		jsonRequest.put("edadMinima","25");
		
		//<<<<</*/
		
		
		
		String jSon = getJsonFromService(jsonRequest.toString(), URI_VACANCY.concat(URI_UPDATE) );
	}
	
	/**
	 * Metodo para probar asociación entre posicion y perfil (Externo) <br>
	 * [Se encuentra en este Tester, pues es posición quien usa el servicio ]
	 * @throws Exception
	 */
	public static void vacancyAssociation() throws Exception {
		JSONObject jsonRequest = new JSONObject();
		int associate = 1;
		
		jsonRequest.put(P_JSON_IDCONF, IDCONF );
		jsonRequest.put("associate", String.valueOf(associate));	//1 asociar, 0 desasociar
		jsonRequest.put("idPosicion", "5");
		jsonRequest.put("idPerfil", "7");
		
		if(associate==1){
			jsonRequest.put("ponderacion", "0.3");
		}
		
		String jSon = getJsonFromService(jsonRequest.toString(), URI_PROFILE_ASSOCIATE );
	}
	
	
	public static void vacancyTextCreate() throws Exception {
		JSONObject jsono = new JSONObject();
		jsono.put(P_JSON_IDCONF, IDCONF );
		jsono.put("idPosicion", "13");
		jsono.put("idPerfil", "24");
		
//		jsono.put("texto","Retención de talento"); //No los toma en cuenta, se debe utilizar update
//		jsono.put("ponderacion","85");
		
		String jSon = getJsonFromService( jsono.toString(), URI_VACANCYTEXT.concat(URI_CREATE) );
		//[{"name":"idPerfilTextoNgram","value":"101","code":"004","type":"I"}]
	}
	
	public static void vacancyTextUpdate() throws Exception {
		JSONObject jsono = new JSONObject();
		jsono.put(P_JSON_IDCONF, IDCONF );
		jsono.put("idPosicion", "13");
		jsono.put("idPerfil", "24");
		jsono.put("idPerfilTextoNgram", "42");
		
		jsono.put("texto","Retención de talento"); //No los toma en cuenta, se debe utilizar update
		jsono.put("ponderacion","85");
		
		String jSon = getJsonFromService(jsono.toString(), URI_VACANCYTEXT.concat(URI_UPDATE) );
		//[]
	}
	
	public static void vacancyTextDelete() throws Exception {
		JSONObject jsono = new JSONObject();
		jsono.put(P_JSON_IDCONF, IDCONF );
		jsono.put("idPosicion", "13");
		jsono.put("idPerfil", "24");
		jsono.put("idPerfilTextoNgram", "41");
		
		String jSon = getJsonFromService(jsono.toString(), URI_VACANCYTEXT.concat(URI_DELETE) );
		//[]
	}
	
	
	public static void vacancyPublication() throws Exception {
		JSONObject jsono = new JSONObject();
		jsono.put(P_JSON_IDCONF, IDCONF );
		jsono.put(P_JSON_POSICION, "5");
		jsono.put(P_JSON_PERSONA, "2");
		
		String jSon = getJsonFromService(jsono.toString(), URI_VACANCY.concat(URI_VACANCYPUBLICATION) );
	}
	
	public static void pruebaInsertLocation() throws Exception {
		final String URI_SERVICE ="/module/settlement/create";
		
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDCONF);
		json.put("idPosicion", "13");
		
		json.put("googleLatitude", "19.3021418");	//googleLatitude
		json.put("googleLongitude", "-99.12935870000001");	//googleLongitude
		json.put("idCodigoProceso", "0");		//idCodigoProceso
		json.put("tempAsentamiento", "27002");	//idAsentamiento
		json.put("tempMunicipio", "276");		//idMunicipio
		json.put("tempEstado", "9");			//idEstado
//		json.put("tempPais", "México");
		json.put("tempCodigoPostal", "14350");	//codigoPostal
		
		String jSon = getJsonFromService(json.toString(), URI_SERVICE );
	}
	
	public static void deleteDomicio() throws Exception {
		JSONObject jsono = new JSONObject();
		jsono.put(P_JSON_IDCONF, IDCONF );
		jsono.put("idPosicion", "13");
		jsono.put("idPerfil", "24");
		jsono.put("idDomicilio", "27");
		
		String jSon = getJsonFromService(jsono.toString(), URI_LOCATION.concat(URI_DELETE) );
	}

	
	
	private static String notZeroNull(String value){
		if(value==null || value.equals("0")){
			return "1";
		}
		return value;
	}
}
