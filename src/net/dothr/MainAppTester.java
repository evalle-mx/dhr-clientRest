package net.dothr;

import java.lang.reflect.Type;
import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

import net.utils.ClientRestUtily;
import net.utils.ConstantesREST;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

@SuppressWarnings("unused")
public abstract class MainAppTester {

	protected static final String SEPARADOR = "\n **************************************************************************************************************";
	public static final String LOCAL_HOME =  System.getProperty("user.home"); //Tested on UNIX
		
	private static Logger log4j = Logger.getLogger( MainAppTester.class );
	
	protected static Gson gson; //Para convertir objetos en Json igual que en Transactional
	
	private static JSONArray jsArrRsp;
	private static JSONObject jsRsp;
	
	protected static final String PWD_123456789 = "15e2b0d3c33891ebb0f1ef609ec419420c20e320ce94c65fbc8c3312448eb225";
	
	protected static String WEB_RESOURCE =
//			ConstantesREST.HPSERVER_APP;
//			ConstantesREST.OCTAVIO_APP;
//			ConstantesREST.WEB_TRANSACT_MOCK;
			ConstantesREST.WEB_TRANSACT_LOCAL;
//			ConstantesREST.WEB_TRANSACT_AWS;
//			ConstantesREST.WEB_OPERAT_LOCAL;
//			"http://localhost:8080/AgroTransactional";
	
	public static boolean PERMITIR_CARGA_EN_AWS = false; //Confirma si se cargan los datos a AWS (Produccion)
	protected static final String CONTENT_TYPE = ConstantesREST.CONTENT_TYPE;
	
	protected static String IDCONF_DOTHR = "1";
	protected static String IDSELEX = "1";//"3";
	
	
	/*  URI-SERVICES */	
//	protected final static String URI_ENTERPRISE_PARAMETERS ="/module/enterpriseParameter";	//Deprecado, los uricodes estan en Permiso
	protected final static String URI_LOCATION = "/module/location";
	protected final static String URI_CURRICULUM="/module/curriculumManagement";
	protected final static String URI_COMPANY="/module/curriculumCompany";
	protected final static String URI_ESCOLARIDAD ="/module/academicBackground";
	protected final static String URI_HABILIDAD ="/module/personSkill";
	protected final static String URI_EXP_LABORAL ="/module/workExperience";
	protected final static String URI_IDIOMA ="/module/language";
	protected final static String URI_CERTIFICACION ="/module/personCert";
	protected final static String URI_VACANCY="/module/vacancy";
	protected static final String URI_CONTACT = "/module/contact";
	protected static final String URI_CATALOGO = "/admin/catalogue/";
	protected static final String URI_FILE ="/module/file";
	protected static final String URI_LOGOUT = "/logout/delete";
	public static final String URI_SETTLEMENT ="/module/settlement";
	protected static final String CV_PERS_PUBLICATE ="/module/applicant/setResumePublication";
	protected final static String URI_ADMIN="/admin/management";
		
	/* OPERACIONES / METODOS */
	public final static String URI_URICODES="/uricodes";
	public final static String URI_UPDATE="/update";
	public final static String URI_CREATE="/create";
	public final static String URI_READ="/read";
	public final static String URI_DELETE="/delete";
	public final static String URI_GET="/get";
	public final static String URI_DATA_CONF="/dataconf";
	public final static String URI_EXISTS="/exists";
	public final static String URI_CLONE="/clone";
	public final static String URI_FIND_SIMILAR="/findSimilar";
	public final static String URI_PING="/ping";
	public final static String URI_ENABLE_EDITION="/enableEdition";
	
	public final static String URI_CREATECOMPLETE = "/createcomplete";
	public final static String URI_CREATEMASIVE = "/createMasive";	
	public final static String URI_MERGE = "/merge";
	
	public final static String URI_TEXT_C = "/textcreate";
	public final static String URI_TEXT_U = "/textupdate";
	public final static String URI_TEXT_D = "/textdelete";

	protected final static String URI_UPD_PWD="/updpwd";
	protected final static String URI_MENU = "/menu";
	
	/*  PARAMETROS JSON */
	public final static String P_JSON_IDCONF= "idEmpresaConf"; // idConf
	public final static String P_JSON_PERSONA= "idPersona";
	public final static String P_JSON_EMPRESA= "idEmpresa";
	public final static String P_JSON_POSICION= "idPosicion";
	public final static String P_JSON_PERFIL= "idPerfil";
	public final static String P_JSON_EMAIL= "email";
	
	public static final String URI_SETTLEMENT_C =URI_SETTLEMENT+URI_CREATE;	//"/module/settlement/create";
	public final static String URI_PROFILE ="/module/profile";
//	public final static String URI_DISPONIBILIDAD = "/availability";
	
	private static Map<String, String> hmUriCodes = ConstantesREST.hmUriCodes;
	
	/* directorios y rutas */
	private static final String dirJson = "/home/dothr/JsonUI/module/enterpriseParameter/";
	
	
	/**
	 * Metodo comun para utilizar servicio
	 * @param outJson i.e. {}
	 * @param uriService  i.e. / 
	 * @return
	 * @throws Exception
	 */
	protected static String getJsonFromService(String inputJson, String uriService) throws ConnectException, Exception{
		System.out.println(SEPARADOR);
		String jsonResponse= null;
		Response response = null;
		if(WEB_RESOURCE.equals(ConstantesREST.WEB_TRANSACT_MOCK)){
			log4j.debug("\n\n ?????????  APUNTANDO A AMBIENTE EMULADO MOCK "+ ConstantesREST.WEB_TRANSACT_MOCK +" ?????????????????????\n"); 
		}
		log4j.debug("\n Probando ["+ transUriCode(uriService) +"] > " + WEB_RESOURCE.concat(uriService) +"\n" );
		
		ClientJersey.init(WEB_RESOURCE);
		response = ClientJersey.request(inputJson, uriService );
		if (response.getStatusInfo().getFamily() == Family.SUCCESSFUL) {
			jsonResponse =  response.readEntity(String.class);
		} else {
			 int cve = response.getStatus();
			 log4j.error("Rest.ERROR --> estatus: " + cve );
			 jsonResponse =  response.readEntity(String.class);
			 
			 if(jsonResponse.startsWith("<!DOCTYPE html")){
				 log4j.error("Error de comunicación con: "+WEB_RESOURCE +", \n 1. verificar conectividad. \n 2.Verificar servicio "+ uriService, new NullPointerException());
			 }
		}
		log4j.debug("\n==> Json: "+inputJson + "\n==> URI: "+uriService + "\n<== response:\n " + jsonResponse +"\n");
		return jsonResponse;
	}
	
	
	public static void main(String[] args) {
		try {
			ping(null);
//			pruebaFileDataConf();
					////empresaParametros(); /*DEPRECADO*/
//			getUricodesByUser();
//			imprimeMapaUricodesEnDB();
			/*
			Map<String,String> mapa = getMapaUris();
			log4j.debug("mapa:");
			log4j.debug(mapa); //*/
			
//			comparaPermisos();
			
//			logout();
			
//			for(int x= 0;x<900; x++){
//				try{
//						String test = getUricodesByUser();
//					  Thread.sleep(1000);
//					}catch(InterruptedException ex){
//					  //do stuff
//					}
//			}
			
//			System.out.println(getToday4Insert());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*  ***************  METODOS DE PRUEBA/OBTENCION DE DATOS *****************************************************  */
	/**
	 * Metodo para probar conectividad con APP y el AdapterRest solicitado, 
	 * pues todos heredan ruta de servicio "ping" desde ErrorMessageAdapterRest
	 * @param uriRoot   /module/service
	 */
	public static String ping(String uriRoot) throws Exception {
		if(uriRoot==null || uriRoot.trim().equals("")){
			uriRoot = "/module/curriculumManagement";
		}
		String stjSon = getJsonFromService("{}", uriRoot+"/ping");
		return stjSon;
	}
	
	/**
	 * Obtiene los uriCodes por usuario (Persona), que tendra disponible la aplicación 
	 * [anteriormente era servicio getParameters]
	 * @return
	 * @throws Exception
	 */
	public static String getUricodesByUser() throws Exception {
		
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDCONF_DOTHR);
		json.put("email", "ernesto.valle@dothr.net");
		
		String stjSon = getJsonFromService(json.toString(), URI_CURRICULUM+"/uricodes");
		return stjSon;
	}
	
	/**
	 * OBtiene los uricodes por usuario, y los regresa en forma de Arreglo de Json <br>
	 * [{"contexto":"ACADBACK.C","valor":"/m/a/c","idTipoPermiso":"1"},{..}]
	 * @return
	 * @throws Exception
	 */
	public static JSONArray getArrPermiso() throws Exception {
		String stJsonUricodes = getUricodesByUser();	
		JSONArray jsArr1 = null, jsArrPermiso=null;
		//  [{"permiso":[{"contexto":"ACADBACK.C","valor":"/m/a/c","idTipoPermiso":"1"},{..}]}]
		try{
			jsArr1 = new JSONArray(stJsonUricodes);
			if(jsArr1!=null && jsArr1.length()>0){
				JSONObject jsonPermisos = jsArr1.getJSONObject(0);
				jsArrPermiso = jsonPermisos.getJSONArray("permiso");				
			}
		}catch (JSONException je){
			log4j.debug("error de conversion ", je );
		}
		return jsArrPermiso;
	}
	
	/**
	 * Verifica el servicio de LogOut
	 * @throws Exception
	 */
	protected static String logout() throws Exception {
		
		JSONObject jsono = new JSONObject();
		jsono.put(P_JSON_IDCONF, IDCONF_DOTHR );		
		jsono.put(P_JSON_PERSONA, "2");
		
		String jSon = getJsonFromService(jsono.toString(), URI_LOGOUT ); 
		return jSon;
	}
	
	/**
	 * Metodo heredado para verificar funcionamiento de listado de archivos/Contenido FILE.G
	 * @param intTipoContenido
	 * @param P_Json
	 * @param idEntidad
	 * @throws Exception
	 */
	protected static String pruebaFileGet(int intTipoContenido, String P_Json, 
			String idEntidad) throws Exception{		
		
		String tipoContenido = String.valueOf(intTipoContenido);
		JSONObject jsono = new JSONObject();
		jsono.put(P_JSON_IDCONF, IDCONF_DOTHR );
		jsono.put(P_Json, idEntidad);
		jsono.put("idTipoContenido", tipoContenido);

		String jSon = getJsonFromService(jsono.toString(), URI_FILE.concat(URI_GET) ); //
		return jSon;
	}
	
	/**
	 * Obtiene los datos de configuración de archivo
	 * @return
	 * @throws Exception
	 */
	protected static String pruebaFileDataConf() throws Exception {
		JSONObject jsono = new JSONObject();
		jsono.put(P_JSON_IDCONF, IDCONF_DOTHR );	
		String jSon = getJsonFromService(jsono.toString(), URI_FILE.concat(URI_DATA_CONF) );
		return jSon;
	}
	
	
	/* **********************  metodos privativos ***************************************************** */
	/**
	 * INTERNO: muestra los uricode's Reales registrados en la base de datos, 
	 * y los imprime en formato JSONObject
	 * put( UriCode, UriRest );
	 * @throws Exception
	 */
	private static void imprimeMapaUricodesEnDB() throws Exception{
		JSONArray jsArrPermiso = getArrPermiso();
		StringBuilder sb = new StringBuilder();
		if(jsArrPermiso.length()>0){
			log4j.debug("uricodes:" + jsArrPermiso.length() );
			JSONObject jsPermiso = null;
			for(int x=0; x<jsArrPermiso.length();x++){
				jsPermiso = jsArrPermiso.getJSONObject(x);
				sb.append(jsPermiso).append("\n");  //Directo
			}
		}
		log4j.debug("\n" + sb );		
	}
	
	protected static void printJson(String stJson){
		StringBuilder sb = new StringBuilder("\n");
		if(stJson!=null && (stJson.startsWith("[") || stJson.startsWith("{"))){
			try{
				if(stJson.startsWith("[")){//JSONArray
					jsArrRsp = new JSONArray(stJson);
					sb.append("[\n");
					for(int x=0; x<jsArrRsp.length(); x++){
						jsRsp = jsArrRsp.getJSONObject(x);
						sb.append(jsRsp.toString()).append(x==jsArrRsp.length()-1?"":",").append("\n");
					}
					sb.append("]");
					sb.append("\n# de elementos en Arreglo: ").append(jsArrRsp.length()).append("\n");
				}else{
					jsRsp = new JSONObject(stJson);
					sb.append("JSONObject:\n").append(jsRsp.toString()).append("\n");
				}
			}catch (Exception e){
				log4j.error("No es un Objeto JSON valido. ", e);
				e.printStackTrace();
			}
		}else{
			log4j.debug("No es un Objeto JSON valido. ");
			sb.append("Cadena invalida, no es Objeto JSON\n");
		}
		System.out.println(sb);
	}
	
	/**
	 * Utiliza el servicio de Empresa-parametro, y convierte el resultado en mapa de valores <String,String>
	 * @return
	 */
	public static Map<String, String> getMapaUris() {
		Map<String, String> mapa = new HashMap<String, String>();
		
		try {
			//String stEmpresaParametro = empresaParametros();
			JSONArray jsArray = getArrPermiso(); //new JSONArray(stEmpresaParametro);
			for(int x = 0; x<jsArray.length();x++){
				try{
					JSONObject jsonEmpParam = jsArray.getJSONObject(x);
					mapa.put(jsonEmpParam.getString("contexto"), jsonEmpParam.getString("valor"));//CVE | URI					
				}catch (Exception e) {
					log4j.debug("Error al obtener contexto y/o valor de Permiso en ".concat(String.valueOf(x)));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * Antes comparaEmpresaParametro.
	 * Realiza una comparación entre el resultado de la consulta de uricodes 
	 * en BD y el archivo Json para el Mock (SImulador) 
	 * @throws Exception
	 */
	public static void comparaPermisos() throws Exception {
		log4j.debug("<comparaPermisos>");		
		log4j.debug("paso 1) Json Empresa parametro Local (File): ");
		String stEmpParamFile = ClientRestUtily.getJsonFile("get.json", dirJson);
		
		JSONArray jsArrEmpFile = new JSONArray(stEmpParamFile);
		
		log4j.debug("jsArrEmpFIle.length() "+ jsArrEmpFile.length() );
		
		List<String> lsRequeridos = new ArrayList<String>();
		StringBuilder stDiferencias = new StringBuilder();
		
		Map<String, String> mapaCoreApp = getMapaUris();
		
		log4j.debug("iterar jsArr: ");
		for(int x=0; x<jsArrEmpFile.length();x++){
			JSONObject jsObj = jsArrEmpFile.getJSONObject(x);
			String contexto = jsObj.getString("contexto");
			//stbRequeridos.append(contexto).append(x==jsArrEmpFile.length()-1?"":"|");
			lsRequeridos.add(contexto);
			String valorEnCore = mapaCoreApp.get(contexto);
			if(valorEnCore==null || valorEnCore.equals("")){
				log4j.debug("No se encontro "+ contexto + " en resultado del servicio Empresa-parametro");
				stDiferencias.append(contexto).append(" ");
			}
		}
		log4j.debug("Requeridos (JsonDev):");
		Collections.sort(lsRequeridos);
		String stRequeridos = lsRequeridos.toString();
		log4j.debug(stRequeridos.replace(", ", "|").replace("[", "").replace("]", ""));
		log4j.debug("Faltantes (CORE-App):");
		log4j.debug(stDiferencias);
	}
	/*    UTILERIAS PARA DE FORMATO E INFORMACIÓN PARA ESTA CLASE */
	/**
	 * Metodo que muestra la traducción de UriService - UriCode en sistema
	 * @param uriService
	 * @return
	 */
	protected static String transUriCode(String uriService){
		String res = "???.? (No esta registrado el UriCode para Ese Servicio)";
		if(uriService.indexOf("/ping")!=-1){
			res = "PING";
		}else{
			for (Map.Entry<String, String> entry : hmUriCodes.entrySet()) {
				if(entry.getValue().equals(uriService)){
					res = entry.getKey();
				}
			}
		}
		return res;
	}
	
	/**
	 * Cuando el usuario no tiene contraseña (Insertado por SQL),
	 * reemplaza el null por la contraseña de default
	 * @param pwdBD
	 * @return
	 */
	public static String setPassword(String pwdBD){
		if(pwdBD == null || pwdBD.trim().equals("")){
			return ConstantesREST.PWD_DEF_123456789;
		}
		return pwdBD;
	}
	
	/**
	 * Genera fecha con patron, (DataUtil.java)
	 * @param fecha
	 * @param pattern
	 * @return
	 */
	public static String date2String(Date fecha, String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(fecha);
	}
	
	/**
	 * Obtiene fecha para inserts de SQL
	 * @return
	 */
	public static String getToday4Insert(){
		String fDate =date2String( new Date(), "yyyy-MM-dd 00:00:00.295");	//Media noche
		//String fDate =date2String( new Date(), "yyyy-MM-dd hh:mm:ss:mmm"); //Hora-segundo Actual
		return fDate;
	}
	
	/**
	 * Genera una cadena con la fecha para nombre de archivo (formato: yyMMdd)
	 * @return
	 */
	public static String fechaArchivo()	{
		return date2String( new Date(), "yyMMdd");}
	
	/**
	 * Metodo común para obtener directamente el (1er) Objeto JSON a partir de un String.json, 
	 * <br> <b> el Formato del archivo .json debe ser [{}]</b>
	 * <br> utilizado por: <ul><li>Persona</li></ul>
	 * @param fileName
	 * @param fileDir
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getJsonObject(String fileName, String fileDir) throws Exception {
		String stPersona = ClientRestUtily.getJsonFile(fileName, fileDir);
		JSONArray jsResp = new JSONArray(stPersona);
//		JSONObject jsPersona = jsResp.getJSONObject(0);		
		return jsResp.getJSONObject(0);
	}
	
	/**
	 * Regresa un correo Dummy con el mismo username pero dominio <b>dhrtest.net</b>
	 * @param email
	 * @return
	 */
	protected static String replaceDomain(String email){
		String username = email.substring(0, email.indexOf("@"));		
		return username+"@dhrtest.net";
	}
	/**
	 * Regresa el userName de un correo Electrónico
	 * @param email
	 * @return
	 */
	protected static String getUserName(String email){
		String username = email.substring(0, email.indexOf("@"));
		return username;
	}
	/**
	 * Realiza el ordenamiento e identación de un Json de Persona
	 * @param jsonPersona
	 * @param toArray (true: el archivo se escribe [{...}], false {...} )
	 * @return
	 * @throws JSONException
	 */
	public static String formatJsonPersona(JSONObject jsonPersona, boolean toArray) throws JSONException {
		StringBuilder sbJson = new StringBuilder();
		String defRole = "8";
		String defRol = "3";
		if(jsonPersona.has("role")){ 
			defRole = jsonPersona.getString("role"); 
		}
		
		/* >>>>  Inicio  <<<< */
		sbJson.append(toArray?"[{":"{").append("\n");
		//Internos & control
		if(jsonPersona.has("idPersona")){ sbJson.append("\t").append("  \"idPersona\":\"").append( jsonPersona.getString("idPersona") ).append("\", \n"); }
		if(jsonPersona.has("email")){ sbJson.append("\t").append("  \"email\":\"").append( jsonPersona.getString("email") ).append("\", \n"); }		
		if(jsonPersona.has("idEmpresa")){ sbJson.append("\t").append("  \"idEmpresa\":\"").append( jsonPersona.getString("idEmpresa") ).append("\", \n"); }
		if(jsonPersona.has("idEmpresaConf")){ sbJson.append("\t").append("  \"idEmpresaConf\":\"").append( jsonPersona.getString("idEmpresaConf") ).append("\", \n"); }
		if(jsonPersona.has("idEstatusInscripcion")){ sbJson.append("\t").append("  \"idEstatusInscripcion\":\"").append( jsonPersona.getString("idEstatusInscripcion") ).append("\", \n"); }
		if(jsonPersona.has("estatus")){ sbJson.append("\t").append("  \"estatus\":\"").append( jsonPersona.getString("estatus") ).append("\", \n"); }
		if(jsonPersona.has("tokenInicio")){ sbJson.append("\t").append("  \"tokenInicio\":\"").append( jsonPersona.getString("tokenInicio") ).append("\", \n"); }
		if(jsonPersona.has("textoClasificacion")){ sbJson.append("\t").append("  \"textoClasificacion\":\"").append( jsonPersona.getString("textoClasificacion") ).append("\", \n"); }
		if(jsonPersona.has("fechaCreacion")){ sbJson.append("\t").append("  \"fechaCreacion\":\"").append( jsonPersona.getString("fechaCreacion") ).append("\", \n"); }
		if(jsonPersona.has("fechaModificacion")){ sbJson.append("\t").append("  \"fechaModificacion\":\"").append( jsonPersona.getString("fechaModificacion") ).append("\", \n"); }
				
		//Formulario
		if(jsonPersona.has("nombre")){ sbJson.append("\t").append("  \"nombre\":\"").append( jsonPersona.getString("nombre") ).append("\", \n"); }
		if(jsonPersona.has("apellidoPaterno")){ sbJson.append("\t").append("  \"apellidoPaterno\":\"").append( jsonPersona.getString("apellidoPaterno") ).append("\", \n"); }
		if(jsonPersona.has("apellidoMaterno")){ sbJson.append("\t").append("  \"apellidoMaterno\":\"").append( jsonPersona.getString("apellidoMaterno") ).append("\", \n"); }		
		if(jsonPersona.has("anioNacimiento")){ sbJson.append("\t").append("  \"anioNacimiento\":\"").append( jsonPersona.getString("anioNacimiento") ).append("\", \n"); }
		if(jsonPersona.has("mesNacimiento")){ sbJson.append("\t").append("  \"mesNacimiento\":\"").append( jsonPersona.getString("mesNacimiento") ).append("\", \n"); }
		if(jsonPersona.has("diaNacimiento")){ sbJson.append("\t").append("  \"diaNacimiento\":\"").append( jsonPersona.getString("diaNacimiento") ).append("\", \n"); }
		if(jsonPersona.has("fechaNacimiento")){ sbJson.append("\t").append("  \"fechaNacimiento\":\"").append( jsonPersona.getString("fechaNacimiento") ).append("\", \n"); }
		if(jsonPersona.has("edad")){ sbJson.append("\t").append("  \"edad\":\"").append( jsonPersona.getString("edad") ).append("\", \n"); }
		
		if(jsonPersona.has("idTipoGenero")){ sbJson.append("\t").append("  \"idTipoGenero\":\"").append( jsonPersona.getString("idTipoGenero") ).append("\", \n"); }
		if(jsonPersona.has("idEstadoCivil")){ sbJson.append("\t").append("  \"idEstadoCivil\":\"").append( jsonPersona.getString("idEstadoCivil") ).append("\", \n"); }
		if(jsonPersona.has("idPais")){ sbJson.append("\t").append("  \"idPais\":\"").append( jsonPersona.getString("idPais") ).append("\", \n"); }
		if(jsonPersona.has("permisoTrabajo")){ sbJson.append("\t").append("  \"permisoTrabajo\":\"").append( jsonPersona.getString("permisoTrabajo") ).append("\", \n"); }
		if(jsonPersona.has("cambioDomicilio")){ sbJson.append("\t").append("  \"cambioDomicilio\":\"").append( jsonPersona.getString("cambioDomicilio") ).append("\", \n"); }
		if(jsonPersona.has("idTipoDispViajar")){ sbJson.append("\t").append("  \"idTipoDispViajar\":\"").append( jsonPersona.getString("idTipoDispViajar") ).append("\", \n"); }
		if(jsonPersona.has("disponibilidadHorario")){ sbJson.append("\t").append("  \"disponibilidadHorario\":\"").append( jsonPersona.getString("disponibilidadHorario") ).append("\", \n"); }
		if(jsonPersona.has("salarioMin")){ sbJson.append("\t").append("  \"salarioMin\":\"").append( jsonPersona.getString("salarioMin") ).append("\", \n"); }
		if(jsonPersona.has("salarioMax")){ sbJson.append("\t").append("  \"salarioMax\":\"").append( jsonPersona.getString("salarioMax") ).append("\", \n"); }
		
		//Arreglos (Domicilio, contacto, escolaridad, experiencia, habilidad, idiomas)
		if(jsonPersona.has("contacto")){ sbJson.append("\t").append("  \"contacto\":").append( jsonPersona.getJSONArray("contacto").toString() ).append(", \n"); }		
		if(jsonPersona.has("localizacion")){ sbJson.append("\t").append("  \"localizacion\":").append( jsonPersona.getJSONArray("localizacion").toString() ).append(", \n"); }
		if(jsonPersona.has("escolaridad")){ sbJson.append("\t").append("  \"escolaridad\":").append( jsonPersona.getJSONArray("escolaridad").toString() ).append(", \n"); }
		if(jsonPersona.has("experienciaLaboral")){ sbJson.append("\t").append("  \"experienciaLaboral\":").append( jsonPersona.getJSONArray("experienciaLaboral").toString() ).append(", \n"); }
		if(jsonPersona.has("idioma")){ sbJson.append("\t").append("  \"idioma\":").append( jsonPersona.getJSONArray("idioma").toString() ).append(", \n"); }
		if(jsonPersona.has("habilidad")){ sbJson.append("\t").append("  \"habilidad\":").append( jsonPersona.getJSONArray("habilidad").toString() ).append(", \n"); }
		if(jsonPersona.has("certificacion")){ sbJson.append("\t").append("  \"certificacion\":").append( jsonPersona.getJSONArray("certificacion").toString() ).append(", \n"); }
		
		
		//EXTRA: parametros para control y recreación de personas
		if(jsonPersona.has("nombreCompleto")){ sbJson.append("\t").append("  \"nombreCompleto\":\"").append( jsonPersona.getString("nombreCompleto") ).append("\", \n"); }
		if(jsonPersona.has("password")){ sbJson.append("\t").append("  \"password\":\"").append( jsonPersona.getString("password") ).append("\", \n"); }
		if(jsonPersona.has("emailOriginal")){ sbJson.append("\t").append("  \"emailOriginal\":\"").append( jsonPersona.getString("emailOriginal") ).append("\", \n"); }
		if(jsonPersona.has("areaPersona")){ sbJson.append("\t").append("  \"areaPersona\":").append( jsonPersona.getJSONArray("areaPersona").toString() ).append(", \n"); }
		if(jsonPersona.has("idPersonaAnt")){ sbJson.append("\t").append("  \"idPersonaAnt\":\"").append( jsonPersona.getString("idPersonaAnt") ).append("\", \n"); }
				
		//TODO incluir historico_password para contraseñas anteriores
		if(jsonPersona.has("historicoPassword")){ sbJson.append("\t").append("  \"historicoPassword\":").append( jsonPersona.getJSONArray("historicoPassword").toString() ).append(", \n"); }
		
		//Nuevos & etiquetas
		if(jsonPersona.has("lbGenero")){ sbJson.append("\t").append("  \"lbGenero\":\"").append( jsonPersona.getString("lbGenero") ).append("\", \n"); }
		if(jsonPersona.has("lbEstadoCivil")){ sbJson.append("\t").append("  \"lbEstadoCivil\":\"").append( jsonPersona.getString("lbEstadoCivil") ).append("\", \n"); }
		if(jsonPersona.has("stPais")){ sbJson.append("\t").append("  \"stPais\":\"").append( jsonPersona.getString("stPais") ).append("\", \n"); }
		if(jsonPersona.has("lbDispViajar")){ sbJson.append("\t").append("  \"lbDispViajar\":\"").append( jsonPersona.getString("lbDispViajar") ).append("\", \n"); }
		
		if(jsonPersona.has("lbTitulo")){ sbJson.append("\t").append("  \"lbTitulo\":\"").append( jsonPersona.getString("lbTitulo") ).append("\", \n"); }
		if(jsonPersona.has("lbPuesto")){ sbJson.append("\t").append("  \"lbPuesto\":\"").append( jsonPersona.getString("lbPuesto") ).append("\", \n"); }
		if(jsonPersona.has("personalDesc")){ sbJson.append("\t").append("  \"personalDesc\":\"").append( jsonPersona.getString("personalDesc") ).append("\", \n"); }
		if(jsonPersona.has("imgPerfil")){ sbJson.append("\t").append("  \"imgPerfil\":").append( jsonPersona.getJSONObject("imgPerfil").toString() ).append(", \n"); }
		if(jsonPersona.has("escandidato")){ sbJson.append("\t").append("  \"escandidato\":\"").append( jsonPersona.getString("escandidato") ).append("\", \n"); }
		if(jsonPersona.has("bcompensacion")){ sbJson.append("\t").append("  \"bcompensacion\":\"").append( jsonPersona.getString("bcompensacion") ).append("\", \n"); }
		
		//Interno permanente, se pone al final para evitar la coma de mas
		if(jsonPersona.has("idRol")){ sbJson.append("\t").append("  \"idRol\":\"").append( jsonPersona.getString("idRol") ).append("\", \n"); }
		sbJson.append("\t").append("  \"role\":\"").append( defRole ).append("\"");
		
		/* >>>> Fin  <<<< */
		sbJson.append("\n").append(toArray?" }]":" }");
		return ClientRestUtily.formtJsonByGoogle(sbJson.toString());
	}
	
	
	/**
	 * Lista los archivos incluidos en el archivo lista en cada carpeta <b>listaRead.txt</b>
	 * <br><i> Si no existe llega vacio</i>
	 * @return
	 */
	protected static List<String> listFromFile(String path, String fileName){
		return ClientRestUtily.readListaTxt(path+fileName);
	}
}

