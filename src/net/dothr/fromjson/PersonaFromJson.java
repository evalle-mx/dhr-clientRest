package net.dothr.fromjson;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import net.dothr.recreate.PersonaFullToJson;
import net.utils.ClientRestUtily;
import net.utils.ConstantesREST;

/**
 * Clase para Carga (actualizar) de datos de Persona, con base en archivos .json
 * para esto, se debe haber hecho los procesos indicados en PersonaFullToJson
 * <i> Se debe Correr primero PersonaInsertsFromJson, para generar persona (inicial) en el sistema</i><br>
 * <b> Opcional</b> Correr primero <i>PersonaValidaPublicacion</i> para completar la información faltante
 * @author dothr
 *
 */
public class PersonaFromJson extends PersonaFullToJson {

	private static Logger log4j = Logger.getLogger( PersonaFromJson.class );
	protected static final String DIRECTORIO_JSON = PERSONA_JSON_DIR+"0.pirh/"
			//"8.Selex2016.2/";
			//+"9.Selex-2016/"
			;
	protected static final String CV_PERS_INITIAL ="/module/curriculumManagement/initial";
	protected static final String PERSON_U = URI_CURRICULUM+URI_UPDATE;		//"/module/curriculumManagement/update";
	protected static final String CONTACT_C = URI_CONTACT+URI_CREATE;			//"/module/contact/create";
	protected static final String WORKEXP_C = URI_EXP_LABORAL+URI_CREATE;		//"/module/workExperience/create";
	protected static final String ACADBACK_C = URI_ESCOLARIDAD+URI_CREATE;	//"/module/academicBackground/create";
	protected static final String SKILL_C = URI_HABILIDAD+URI_CREATE;	//"/module/academicBackground/create";
	protected static final String LANG_C = URI_IDIOMA+URI_CREATE;	//"/module/language/create";
	protected static final String CERT_C = URI_CERTIFICACION+URI_CREATE;	//"/module/language/create";
	
	private static final String REPORTE_PATH = ConstantesREST.JSON_HOME+"Report/Rep.PersonaFromJson"+fechaArchivo()+".txt";
		
	private static String IDCONF = ConstantesREST.IDEMPRESA_CONF;;
	
	private static boolean escribeJsPersonaBitacora = false;  //BANDERA QUE INDICA SI SE DEBE ESCRIBIR EL JSON DE ENTRADA EN REPORTE
	
	private static JSONObject jsonReq;
	private static JSONArray jsArrResp;
	
	private static StringBuilder sbError;
	
	/*procedimiento
	 * 1. Obtener json string
	 * 2. Convertir a JSON
	 * 3. Verificar que exista en Sistema (via Servicio Initial, para validad existencia y corroborar idPersona)
	 * 4. Inyectar idPersona obtenido en Servicio (Se asegura que no se cargue información en persona incorrecta)
	 * 5. Continuar proceso anterior de Carga
	 *  
	 */
	
	public static void main(String[] args) {
		
		try {
//			ping(null);
			
			StringBuilder sbProceso = 
//					multiLoadPersona();
					procesaPersona("read-7.selex1.json");
			ClientRestUtily.writeStringInFile(REPORTE_PATH, sbProceso.toString(), false);
//			
			log4j.debug("Reporte en: \n" + REPORTE_PATH );
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Procedimmiento para cargar los datos a partir de un archivo .json
	 * <i> se debe ubicar en la carpeta Recreate, para disminuir errores</i>
	 * <b> REQUIERE AppTransactional arriba</b>
	 * @param archivoPersona
	 * @return
	 */
	public static StringBuilder procesaPersona(String archivoPersona){
		log4j.debug("<procesaPersona> ");
		StringBuilder sbMain = new StringBuilder("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n")
			.append("++++++++++++++++++++++++++++++++++++  PROCESAPERSONA  +++++++++++++++++++++++++++++++++ \n")
			.append("+ WEB_RESOURCE= ").append(WEB_RESOURCE).append("\n")
			.append("+ DIRECTORIO_JSON= ").append(DIRECTORIO_JSON).append("\n")
			.append("+ archivoPersona= ").append(archivoPersona).append("\n")
			.append("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n\n");;
		
		if(WEB_RESOURCE.equals(ConstantesREST.WEB_TRANSACT_AWS) 
				//|| WEB_RESOURCE.indexOf("localhost")== -1
				){
				log4j.fatal("No esta apuntando a DESARROLLO "+WEB_RESOURCE, new NullPointerException() );
				sbMain.append(" No esta apuntando a DESARROLLO "+WEB_RESOURCE).append("\n");
		}else{
			try{
				sbError = new StringBuilder();
				
				if(archivoPersona.startsWith("read")){
					//1. Obtener json string & 2. Convertir a JSON
					log4j.debug("<procesaPersona> Se obtiene Json a partir de archivoPersona ");
					JSONObject jsonPersona = getJsonObject(archivoPersona, DIRECTORIO_JSON);
					if(escribeJsPersonaBitacora){
						sbMain.append("jsonPersona: ").append(jsonPersona).append("\n");
					}else{
						sbMain.append("jsonPersona: ").append(jsonPersona.toString().substring(0, 25)).append(" ..... ] \n");
					}
					
					
					//3. Verificar que exista en Sistema (via Servicio Initial)
					jsonReq = new JSONObject();
					jsonReq.put("email", jsonPersona.getString("email"));
					jsonReq.put("idEmpresaConf", jsonPersona.has("idEmpresaConf")?jsonPersona.getString("idEmpresaConf"):IDCONF);
					sbMain.append("Se valida existencia via servicio INITIAL:\n ==> jsonReq: ").append(jsonReq).append("\n");
					String stResponse  = getJsonFromService(jsonReq.toString(), CV_PERS_INITIAL );
					// [{"idPersona":"1","role":"9"}]
					sbMain.append("<== Response: ").append(stResponse).append("\n");
					if(stResponse.indexOf("idPersona")!=-1 && stResponse.indexOf("role")!=-1){
						jsArrResp = new JSONArray(stResponse);						
						jsonReq = jsArrResp.getJSONObject(0); //Temporalmente se usa para transformar respuesta en JSON
						
						jsonPersona.put("idPersona", jsonReq.getString("idPersona"));
						sbMain.append(procesaJsonPersona(jsonPersona));
						sbError.append("\n Archivo: ").append(archivoPersona).append("\n");
						ClientRestUtily.writeStringInFile("/home/dothr/logs/ErrorCargaDomicilio.txt", sbError.toString(), true);
					
					}else{
						log4j.debug("<procesaPersona> Error: No existe persona coincidente con email & idEmpresaConf " + jsonReq.toString() );
						sbMain.append("Error, No existe persona coincidente con email & idEmpresaConf: ")
							.append(jsonReq).append("\n");
					}
					
				}else{
					log4j.debug("<procesaPersona> Error: el nombre de archivo no es el esperado [read-IDPERSONA]: " + archivoPersona );
					sbMain.append("el nombre de archivo no es el esperado [read-IDPERSONA]").append("\n");
				}
			}catch (Exception e){
				log4j.fatal("<procesaPersona> Fatal: ", e);
				sbMain.append("<Exception> error al procesar: ").append(e.getMessage()).append("\n");
			}
		}
		sbMain.append("\n++++++++++++++++++++++++++++++++ FIN DE PROCESO ++++++++++++++++++++++++++++++++");
		log4j.debug("+++++ FIN DE PROCESO (procesaPersona) +++++++");
		
		return sbMain;
	}
	
	
	
	/**
	 * Proceso para insertar datos de un Objeto JSON, por medio del proceso normal de la aplicación,
	 * enviando paquetes y recibiendo Id's, para comprobar funcionamiento y carga de datos 
	 * @param jsonPersona
	 * @return
	 * @throws Exception
	 */
	protected static StringBuilder procesaJsonPersona(JSONObject jsonPersona) throws Exception {
		log4j.debug("<procesaJsonPersona>");
		StringBuilder sbProcesoPersona = new StringBuilder();
		String jsMainUpdate = 	"[]";
		String idPersona, email;
		
		if(WEB_RESOURCE.equals(ConstantesREST.WEB_TRANSACT_AWS) 
				&& !PERMITIR_CARGA_EN_AWS)
				{
				log4j.fatal("Esta apuntando a AWS y no esta permitido "+WEB_RESOURCE, new NullPointerException() );
				sbProcesoPersona.append(" Esta apuntando a AWS y no esta permitido: "+WEB_RESOURCE).append("\n");
		}else{
			idPersona = jsonPersona.getString("idPersona");
			email = jsonPersona.getString("email");
			sbProcesoPersona.append("*************** >> PROCESO DE CARGA PARA PERSONA ").append(idPersona).append(" << *************** \n");
			JSONArray jsonArrDomicilio = null, jsonArrContactos = null,
					jsonArrExperiencias = null, jsonArrEscolaridades = null, jsonArrHabilidades = null, jsonArrIdiomas = null, jsonArrCertifics=null;
			
			log4j.debug("<procesaJsonPersona> Se obtienen campos satelite (domicilio, contacto, experienciaLaboral, escolaridad, idiomas, habilidades)");
			if(jsonPersona.has("localizacion")){
				jsonArrDomicilio = jsonPersona.getJSONArray("localizacion");
			}
			if(jsonPersona.has("contacto")){
				jsonArrContactos = jsonPersona.getJSONArray("contacto");
			}
			if(jsonPersona.has("experienciaLaboral")){
				jsonArrExperiencias = jsonPersona.getJSONArray("experienciaLaboral");
			}
			if(jsonPersona.has("escolaridad")){
				jsonArrEscolaridades = jsonPersona.getJSONArray("escolaridad");
			}
			if(jsonPersona.has("habilidad")){
				jsonArrHabilidades = jsonPersona.getJSONArray("habilidad");
			}
			//areaPersona
			if(jsonPersona.has("idioma")){
				jsonArrIdiomas = jsonPersona.getJSONArray("idioma");
			}
			if(jsonPersona.has("certificacion")){
				jsonArrCertifics = jsonPersona.getJSONArray("certificacion");
			}
			
//			if(jsonPersona.has("areaPersona")){
//				jsonArrAreas = jsonPersona.getJSONArray("areaPersona");
//			}
			
			jsonPersona.remove("localizacion");
			jsonPersona.remove("contacto");
			jsonPersona.remove("experienciaLaboral");
			jsonPersona.remove("escolaridad");
			jsonPersona.remove("habilidad");
			jsonPersona.remove("idioma");
			jsonPersona.remove("certificacion");
			
			jsonPersona.put("idEmpresaConf", IDCONF );	//Reemplaza si existe uno
			
			jsonPersona.remove("email");
			jsonPersona.remove("idEstatusInscripcion");
			jsonPersona.remove("password");
			jsonPersona.remove("textoClasificacion");
			jsonPersona.remove("role");
			jsonPersona.remove("idEmpresa");
			jsonPersona.remove("estatus");
			jsonPersona.remove("edad");
			jsonPersona.remove("fechaNacimiento");
			
			jsonPersona.remove("lbDispViajar");
			jsonPersona.remove("lbEstadoCivil");
			jsonPersona.remove("lbGenero");
			jsonPersona.remove("nombreCompleto");
			
			jsonPersona.remove("areaPersona");
			
			jsMainUpdate = getJsonFromService(jsonPersona.toString(), PERSON_U);
			sbProcesoPersona.append("\n PERSON.U:\n==>Json: ").append(jsonPersona.toString())
					.append("<==Response: ").append(jsMainUpdate);
			
			if(jsMainUpdate.equals("[]")){
				/* DOmicilio */
				if(jsonArrDomicilio!=null){
					sbProcesoPersona.append("\n\n ============ Domicilios ============ ");
					for(int x=0;x<jsonArrDomicilio.length();x++){
						JSONObject jsDom = jsonArrDomicilio.getJSONObject(x);
						jsDom.put("idPersona", idPersona);
						jsDom.put(P_JSON_IDCONF, IDCONF );
						jsDom.remove("idDomicilio");
						if(jsDom.has("idAsentamiento")){
							jsDom.put("tempAsentamiento", jsDom.getString("idAsentamiento"));
							jsDom.remove("idAsentamiento");
						}
						if(jsDom.has("idMunicipio")){
							jsDom.put("tempMunicipio", jsDom.getString("idMunicipio"));
							jsDom.remove("idMunicipio");
						}
						if(jsDom.has("idEstado")){
							jsDom.put("tempEstado", jsDom.getString("idEstado"));
							jsDom.remove("idEstado");
						}
						if(jsDom.has("codigoPostal")){
							jsDom.put("tempCodigoPostal", jsDom.getString("codigoPostal"));
							jsDom.remove("codigoPostal");
						}
						jsMainUpdate = getJsonFromService(jsDom.toString(),URI_SETTLEMENT_C);
						sbProcesoPersona.append("\n[SETTLEMENT.C]: \n==>").append(jsDom).append("\n<== ").append(jsMainUpdate);
						if(jsMainUpdate.indexOf("\"idDomicilio\"")==-1){
							log4j.debug("<procesaJsonPersona> ERROR AL INSERTAR DOMICILIO "+x );
							sbProcesoPersona.append("\nERROR AL INSERTAR DOMICILIO: ").append(x)
							.append( jsDom.has("tempAsentamiento")?"\nAsent:"+jsDom.getString("tempAsentamiento"):"" )
							.append( jsDom.has("googleLatitude")?"\ng.Lat:"+jsDom.getString("googleLatitude"):"" )
							.append( jsDom.has("googleLongitude")?"\ng.Long:"+jsDom.getString("googleLongitude"):"" )
							.append( "\n se agrega temporal para continuar la secuencia: ");
							
							jsMainUpdate = getJsonFromService(getDomTemporal(idPersona).toString(),URI_SETTLEMENT_C);
							if(jsMainUpdate.indexOf("\"idDomicilio\"")==-1){
								log4j.debug("<procesaJsonPersona> ERROR AL INSERTAR DOMICILIO TEMPORAL " );
							}
							
							sbError.append( "idPersona:" ).append(idPersona).append("\n email: ").append( email )
							.append( jsDom.has("tempAsentamiento")?"\nAsent:"+jsDom.getString("tempAsentamiento"):"" )
							.append( jsDom.has("googleLatitude")?"\ng.Lat:"+jsDom.getString("googleLatitude"):"" )
							.append( jsDom.has("googleLongitude")?"\ng.Long:"+jsDom.getString("googleLongitude"):"" );
						}
					}
				}
				/* Contactos */
				if(jsonArrContactos!=null){
					sbProcesoPersona.append("\n\n ============ Contactos ============ ");
					for(int x=0;x<jsonArrContactos.length();x++){
						JSONObject jsCont = jsonArrContactos.getJSONObject(x);
						jsCont.put("idPersona", idPersona);
						jsCont.put("idEmpresaConf", IDCONF );
						jsCont.remove("idContacto");
						jsMainUpdate = getJsonFromService(jsCont.toString(),CONTACT_C);
						sbProcesoPersona.append("\n[CONTACT.C]: \n==>").append(jsCont).append("\n<== ").append(jsMainUpdate);
						if(jsMainUpdate.indexOf("\"idContacto\"")==-1){
							log4j.debug("<procesaJsonPersona> ERROR AL INSERTAR CONTACTO "+x );
							sbProcesoPersona.append("\nERROR AL INSERTAR CONTACTO: ").append(x);
						}
					}
				}
				/* Experiencia Laboral */
				if(jsonArrExperiencias!=null){
					sbProcesoPersona.append("\n\n ============ Experiencias Laborales ============ ");
					for(int x=0;x<jsonArrExperiencias.length();x++){
						JSONObject jsExp = jsonArrExperiencias.getJSONObject(x);
						jsExp.put("idPersona", idPersona);
						jsExp.put("idEmpresaConf", IDCONF );
						jsExp.remove("idExperienciaLaboral");
						jsExp.remove("textoFiltrado");
						jsExp.remove("fechaInicio");
						jsExp.remove("fechaFin");
						jsMainUpdate = getJsonFromService(jsExp.toString(),WORKEXP_C);
						sbProcesoPersona.append("\n[WORKEXP.C]: \n==>").append(jsExp).append("\n<== ").append(jsMainUpdate);
						if(jsMainUpdate.indexOf("idExperienciaLaboral")==-1){
							log4j.debug("<procesaJsonPersona> ERROR AL INSERTAR EXPERIENCIA LABORAL "+x );
							sbProcesoPersona.append("\nERROR AL INSERTAR EXPERIENCIA LABORAL: ").append(x);
						}
					}
				}
				/* Escolaridad */
				if(jsonArrEscolaridades!=null){
					sbProcesoPersona.append("\n\n ============ Escolaridad ============ ");
					for(int x=0;x<jsonArrEscolaridades.length();x++){
						JSONObject jsAcad = jsonArrEscolaridades.getJSONObject(x);
						jsAcad.put("idPersona", idPersona);
						jsAcad.put("idEmpresaConf", IDCONF );
						jsAcad.remove("idEscolaridad");
						jsAcad.remove("fechaInicio");
						jsAcad.remove("fechaFin");
						jsMainUpdate = getJsonFromService(jsAcad.toString(),ACADBACK_C);
						sbProcesoPersona.append("\n[ACADBACK.C]: \n==>").append(jsAcad).append("\n<== ").append(jsMainUpdate);
						if(jsMainUpdate.indexOf("idEscolaridad")==-1){
							log4j.debug("<procesaJsonPersona> ERROR AL INSERTAR HISTORIAL ACADEMICO "+x );
							sbProcesoPersona.append("\nERROR AL INSERTAR HISTORIAL ACADEMICO: ").append(x);
						}
					}
				}
				/* Habilidades */
				if(jsonArrHabilidades!=null){
					sbProcesoPersona.append("\n\n ============ Habilidades ============ ");
					for(int x=0;x<jsonArrHabilidades.length();x++){
						JSONObject jsHab = jsonArrHabilidades.getJSONObject(x);
						jsHab.put("idPersona", idPersona);
						jsHab.put("idEmpresaConf", IDCONF );
						jsHab.remove("idHabilidad");
						jsMainUpdate = getJsonFromService(jsHab.toString(),SKILL_C);
						sbProcesoPersona.append("\n[PERSONSKILL.C]: \n==>").append(jsHab).append("\n<== ").append(jsMainUpdate);
						if(jsMainUpdate.indexOf("idHabilidad")==-1){
							log4j.debug("<procesaJsonPersona> ERROR AL INSERTAR HABILIDAD "+x );
							sbProcesoPersona.append("\nERROR AL INSERTAR HABILIDAD: ").append(x);
						}
						sbProcesoPersona.append("\n<<**** Fin de Carga Habilidad "+x+"\n");
					}
				}
				/* Idiomas */
				if(jsonArrIdiomas!=null){
					sbProcesoPersona.append("\n\n ============ Idiomas ============ ");
					for(int x=0;x<jsonArrIdiomas.length();x++){
						JSONObject jsIdiom = jsonArrIdiomas.getJSONObject(x);
						jsIdiom.put("idPersona", idPersona);
						jsIdiom.put("idEmpresaConf", IDCONF );
						jsIdiom.remove("idCertificacion");
						jsMainUpdate = getJsonFromService(jsIdiom.toString(), LANG_C);
						sbProcesoPersona.append("\n[LANGUAGE.C]: \n==>").append(jsIdiom).append("\n<== ").append(jsMainUpdate);
						if(jsMainUpdate.indexOf("idPersonaIdioma")==-1){
							log4j.debug("<procesaJsonPersona> ERROR AL INSERTAR IDIOMA "+x );
							sbProcesoPersona.append("\nERROR AL INSERTAR IDIOMA: ").append(x);
						}
						sbProcesoPersona.append("\n<<**** Fin de Carga Idioma "+x+"\n");
					}
				}
				/* Certificaciones */
				if(jsonArrCertifics!=null){
					sbProcesoPersona.append("\n\n ============ Certificaciones ============ ");
					for(int x=0;x<jsonArrCertifics.length();x++){
						JSONObject jsCertif = jsonArrCertifics.getJSONObject(x);
						jsCertif.put("idPersona", idPersona);
						jsCertif.put("idEmpresaConf", IDCONF );
						jsCertif.remove("idCertificacion");
						jsMainUpdate = getJsonFromService(jsCertif.toString(), CERT_C);
						sbProcesoPersona.append("\n[PERSCERT.C]: \n==>").append(jsCertif).append("\n<== ").append(jsMainUpdate);
						if(jsMainUpdate.indexOf("idCertificacion")==-1){
							log4j.debug("<procesaJsonPersona> ERROR AL INSERTAR CERTIFICACIÓN "+x );
							sbProcesoPersona.append("\nERROR AL INSERTAR CERTIFICACIÓN: ").append(x);
						}
						sbProcesoPersona.append("\n<<**** Fin de Carga Certificación "+x+" \n");
					}
				}
				
				/* AREAS  */
				//Este servicio recibe todas las areas en una solicitud
				// ==> Json: {"idPersona":"2","areaPersona":[{"principal":true,"idArea":"18"},{"idArea":"11"}],"idEmpresaConf":"1"}
				// ==> URI: /module/curriculumManagement/update
				
				/* **********  TEMPORAL PARA ASIGNAR TODAS LAS PERSONAS A LA MISMA AREA ************/
//				JSONObject jsonReq = new JSONObject(), jsonArea = new JSONObject();
//				JSONArray jsArrAreas = new JSONArray();
//				jsonReq.put("idEmpresaConf", IDCONF );
//				jsonReq.put("idPersona", idPersona);
//				//Por cada area
//					jsonArea.put("principal", "true");
//					jsonArea.put("idArea", "18");
//					jsArrAreas.put(jsonArea);
//				jsonReq.put("areaPersona", jsArrAreas );
				
				
				/* ********************** */
				
			}else{
				log4j.error("<procesaJsonPersona> Error al actualizar persona ");
				sbProcesoPersona.append("\nError al actualizar persona ");
			}
		}
		return sbProcesoPersona; 
	}
	
	private static JSONObject getDomTemporal(String idPersona) throws Exception {
		JSONObject json = new JSONObject();
		String direccion = "\"codigoPostal\": \"14210\",  \"idMunicipio\": \"276\",  \"stEstado\": \"Distrito Federal\",  \"stColonia\": \"Jardines en la Montaña\",  \"googleLatitude\": \"19.298875\",  \"stMunicipio\": \"Tlalpan\",  \"idTipoDomicilio\": \"1\",  \"numeroExterior\": \"154\",  \"idAsentamiento\": \"26949\",  \"idPais\": \"1\",  \"stPais\": \"México\",  \"idEstado\": \"9\",  \"googleLongitude\": \"-99.20947209999997\",  \"calle\": \"Carretera Picacho Ajusco\"";
		json = new JSONObject(direccion);
		json.put("idPersona", idPersona);
		json.put(P_JSON_IDCONF, IDCONF );
		
		return json;
	}
	
	/**
	 * 
	 * @return
	 */
	public static StringBuilder multiLoadPersona(){
		List<String> lista = getListaArchivosPersona();
		StringBuilder mainReporte = new StringBuilder(" /* *****************  REPORTE GENERAL DE PROCESO DE CARGA DE ARCHIVOS *********** */ \n");

		log4j.debug("VERIFICA QUE NO SEA PRODUCCION:....");
		if(WEB_RESOURCE.equals(ConstantesREST.WEB_TRANSACT_AWS) 
				//|| WEB_RESOURCE.indexOf("localhost")== -1
				){
			log4j.fatal("No esta apuntando a DESARROLLO "+WEB_RESOURCE, new NullPointerException() );
			//stMainBuilder.append("No esta apuntando a DESARROLLO "+WEB_RESOURCE);
		}else{
			try{
				JSONArray jsResponse;
				String pingRes = ping(null);
				jsResponse = new JSONArray(pingRes);
				JSONObject jsonAux = jsResponse.getJSONObject(0);
				if(
					(jsonAux.has("code") && jsonAux.has("type")) 
						&& (jsonAux.getString("type").equals("I"))){
					if(lista!=null && lista.size()>0){
						for(String element: lista){
							System.out.println(element);
							if(element.startsWith("read")){
								StringBuilder stBuilder = procesaPersona(element);
								mainReporte.append(stBuilder).append("\n\n ********************************************************************************************************** \n");
							}
						}
					}
				}else{
					log4j.error("NO hay conexión con App "+ (jsonAux.has("message")?jsonAux.has("message"):""));
					mainReporte.append("<Excepcion> Error: ").append("NO hay conexión con App ")
						.append( (jsonAux.has("message")?jsonAux.has("message"):""));
				}
			}catch (Exception e){
				log4j.fatal("<multiLoadPersona> Fatal: ", e);
				mainReporte.append("<Exception> Error en conexión/iteracción.").append("\n");
			}			
		}
		mainReporte.append("/* ***************** +++ fin de multiLoadPersona +++ *********************** */ \n");
		return mainReporte;
	}
	
	
	
//	/**
//	 * Lista los archivos incluidos en el archivo lista en cada carpeta <b>listaRead.txt</b>
//	 * <br><i> Si no existe llega vacio</i>
//	 * @return
//	 */
//	protected static List<String> listFromFile(String path, String fileName){		
//		//return ClientRestUtily.readListaTxt(CV_JSON_DIR+"listaRead.txt");
//		return ClientRestUtily.readListaTxt(path+fileName);
//	}
	
	/**
	 * Lista de archivos en Directorio de Persona a procesar
	 * @return
	 */
	public static List<String> getListaArchivosPersona(){
		List<String> lsPersona = new ArrayList<String>();
		/*lsPersona.add("read-3.json");
	     lsPersona.add("read-4.json");
	     lsPersona.add("read-5.json");
	     lsPersona.add("read-6.json");
	     lsPersona.add("read-7.json");
	     lsPersona.add("read-8.json");
	     lsPersona.add("read-10.json");
	     lsPersona.add("read-11.json");
	     lsPersona.add("read-12.json");
	     lsPersona.add("read-14.json");
	     lsPersona.add("read-15.json");
	     lsPersona.add("read-16.json");
	     lsPersona.add("read-18.json");
	     lsPersona.add("read-20.json");
	     lsPersona.add("read-23.json");
	     lsPersona.add("read-24.json");
	     lsPersona.add("read-26.json");
	     lsPersona.add("read-27.json");
	     lsPersona.add("read-36.json");
	     lsPersona.add("read-37.json");
	     lsPersona.add("read-44.json");
	     lsPersona.add("read-46.json");
	     lsPersona.add("read-48.json");
	     lsPersona.add("read-51.json");
	     lsPersona.add("read-52.json");
	     lsPersona.add("read-53.json");
	     lsPersona.add("read-54.json");
	     lsPersona.add("read-57.json");
	     lsPersona.add("read-60.json");
	     lsPersona.add("read-63.json");
	     lsPersona.add("read-64.json");
	     lsPersona.add("read-65.json");
	     lsPersona.add("read-66.json");
	     lsPersona.add("read-67.json");
	     lsPersona.add("read-68.json");
	     lsPersona.add("read-70.json");
	     lsPersona.add("read-75.json");
	     lsPersona.add("read-76.json");
	     lsPersona.add("read-79.json");
	     lsPersona.add("read-80.json");
	     lsPersona.add("read-81.json");
	     lsPersona.add("read-82.json");
	     lsPersona.add("read-84.json");
	     lsPersona.add("read-87.json");
	     lsPersona.add("read-88.json");
	     lsPersona.add("read-89.json");
	     lsPersona.add("read-90.json");
	     lsPersona.add("read-91.json");
	     lsPersona.add("read-93.json");
	     lsPersona.add("read-97.json");
	     lsPersona.add("read-98.json");
	     lsPersona.add("read-99.json");
	     lsPersona.add("read-100.json");
	     lsPersona.add("read-101.json");
	     lsPersona.add("read-103.json");
	     lsPersona.add("read-104.json");
	     lsPersona.add("read-107.json");
	     lsPersona.add("read-111.json");
	     lsPersona.add("read-112.json");
	     lsPersona.add("read-116.json");
	     lsPersona.add("read-117.json");
	     lsPersona.add("read-119.json");
	     lsPersona.add("read-120.json");
	     lsPersona.add("read-125.json");
	     lsPersona.add("read-126.json");
	     lsPersona.add("read-127.json");
	     lsPersona.add("read-128.json");
	     lsPersona.add("read-129.json");
	     lsPersona.add("read-131.json");
	     lsPersona.add("read-132.json");
	     lsPersona.add("read-134.json");
	     lsPersona.add("read-137.json");
	     lsPersona.add("read-138.json");
	     lsPersona.add("read-139.json");
	     lsPersona.add("read-141.json");
	     lsPersona.add("read-143.json");
	     lsPersona.add("read-144.json");
	     lsPersona.add("read-145.json");
	     lsPersona.add("read-146.json");
	     lsPersona.add("read-148.json");
	     lsPersona.add("read-154.json");
	     lsPersona.add("read-156.json");
	     lsPersona.add("read-163.json");
	     lsPersona.add("read-164.json");
	     lsPersona.add("read-165.json");
	     lsPersona.add("read-166.json");
	     lsPersona.add("read-168.json");
	     lsPersona.add("read-169.json");
	     lsPersona.add("read-170.json");
	     lsPersona.add("read-172.json");
	     lsPersona.add("read-173.json");
	     lsPersona.add("read-175.json");
	     lsPersona.add("read-179.json");
	     lsPersona.add("read-180.json");
	     lsPersona.add("read-183.json");
	     lsPersona.add("read-184.json");
	     lsPersona.add("read-185.json");
	     lsPersona.add("read-186.json");
	     lsPersona.add("read-188.json");
	     lsPersona.add("read-190.json");
	     lsPersona.add("read-191.json");
	     lsPersona.add("read-192.json");
	     lsPersona.add("read-193.json");
	     lsPersona.add("read-194.json");
	     lsPersona.add("read-196.json");
	     lsPersona.add("read-197.json");
	     lsPersona.add("read-198.json");
	     lsPersona.add("read-199.json");
	     lsPersona.add("read-202.json");
	     lsPersona.add("read-203.json");
	     lsPersona.add("read-204.json");
	     lsPersona.add("read-205.json");
	     lsPersona.add("read-213.json");
	     lsPersona.add("read-214.json");
	     lsPersona.add("read-215.json");
	     lsPersona.add("read-216.json");
	     lsPersona.add("read-218.json");
	     lsPersona.add("read-227.json");
	     lsPersona.add("read-231.json");
	     lsPersona.add("read-232.json");
	     lsPersona.add("read-233.json");
	     lsPersona.add("read-237.json");
	     lsPersona.add("read-239.json");
	     lsPersona.add("read-240.json");
	     lsPersona.add("read-242.json");
	     lsPersona.add("read-243.json");
	     lsPersona.add("read-244.json");
	     lsPersona.add("read-246.json");
	     lsPersona.add("read-248.json");
	     lsPersona.add("read-251.json");
	     lsPersona.add("read-254.json");
	     lsPersona.add("read-255.json");
	     lsPersona.add("read-256.json");
	     lsPersona.add("read-258.json");
	     lsPersona.add("read-259.json");
	     lsPersona.add("read-262.json");
	     lsPersona.add("read-263.json");
	     lsPersona.add("read-265.json");
	     lsPersona.add("read-268.json");
	     lsPersona.add("read-269.json");
	     lsPersona.add("read-273.json");
	     lsPersona.add("read-277.json");
	     lsPersona.add("read-279.json");
	     lsPersona.add("read-283.json");
	     lsPersona.add("read-284.json");
	     lsPersona.add("read-285.json");
	     lsPersona.add("read-286.json");
	     lsPersona.add("read-288.json");
	     lsPersona.add("read-292.json");
	     lsPersona.add("read-294.json");
	     lsPersona.add("read-295.json");
	     lsPersona.add("read-297.json");
	     lsPersona.add("read-298.json");
	     lsPersona.add("read-302.json");
	     lsPersona.add("read-303.json");
	     lsPersona.add("read-308.json");
	     lsPersona.add("read-310.json");
	     lsPersona.add("read-311.json");
	     lsPersona.add("read-312.json");
	     lsPersona.add("read-315.json");
	     lsPersona.add("read-316.json");
	     lsPersona.add("read-317.json");
	     lsPersona.add("read-318.json");
	     lsPersona.add("read-320.json");
	     lsPersona.add("read-321.json");
	     lsPersona.add("read-325.json");
	     lsPersona.add("read-326.json");
	     lsPersona.add("read-327.json");
	     lsPersona.add("read-328.json");
	     lsPersona.add("read-329.json");
	     lsPersona.add("read-331.json");
	     lsPersona.add("read-332.json");
	     lsPersona.add("read-335.json");
	     lsPersona.add("read-337.json");
	     lsPersona.add("read-338.json");
	     lsPersona.add("read-339.json");
	     lsPersona.add("read-340.json");
	     lsPersona.add("read-342.json");
	     lsPersona.add("read-343.json");
	     lsPersona.add("read-345.json");
	     lsPersona.add("read-347.json");
	     lsPersona.add("read-351.json");
	     lsPersona.add("read-352.json");
	     lsPersona.add("read-353.json");
	     lsPersona.add("read-354.json");
	     lsPersona.add("read-356.json");
	     lsPersona.add("read-357.json");
	     lsPersona.add("read-358.json");
	     lsPersona.add("read-359.json");
	     lsPersona.add("read-360.json");
	     lsPersona.add("read-361.json");
	     lsPersona.add("read-365.json");
	     lsPersona.add("read-366.json");
	     lsPersona.add("read-367.json");
	     lsPersona.add("read-372.json");
	     lsPersona.add("read-373.json");
	     lsPersona.add("read-376.json");
	     lsPersona.add("read-377.json");
	     lsPersona.add("read-380.json");
	     lsPersona.add("read-381.json");
	     lsPersona.add("read-382.json");
	     lsPersona.add("read-383.json");
	     lsPersona.add("read-384.json");
	     lsPersona.add("read-385.json");
	     lsPersona.add("read-386.json");
	     lsPersona.add("read-388.json");
	     lsPersona.add("read-389.json");
	     lsPersona.add("read-392.json");
	     lsPersona.add("read-393.json");
	     lsPersona.add("read-394.json");
	     lsPersona.add("read-395.json");
	     lsPersona.add("read-397.json");
	     lsPersona.add("read-398.json");
	     lsPersona.add("read-403.json");
	     lsPersona.add("read-407.json");
	     lsPersona.add("read-410.json");
	     lsPersona.add("read-415.json");
	     lsPersona.add("read-417.json");
	     lsPersona.add("read-418.json");
	     lsPersona.add("read-419.json");
	     lsPersona.add("read-420.json");
	     lsPersona.add("read-423.json");
	     lsPersona.add("read-426.json");
	     lsPersona.add("read-427.json");
	     lsPersona.add("read-428.json");
	     lsPersona.add("read-430.json");
	     lsPersona.add("read-432.json");
	     lsPersona.add("read-433.json");
	     lsPersona.add("read-434.json");
	     lsPersona.add("read-435.json");
	     lsPersona.add("read-437.json");
	     lsPersona.add("read-438.json");
	     lsPersona.add("read-439.json");
	     lsPersona.add("read-442.json");
	     lsPersona.add("read-446.json");
	     lsPersona.add("read-452.json");
	     lsPersona.add("read-455.json");
	     lsPersona.add("read-457.json");
	     lsPersona.add("read-459.json");
	     lsPersona.add("read-460.json");
	     lsPersona.add("read-463.json");
	     lsPersona.add("read-467.json");
	     lsPersona.add("read-468.json");
	     lsPersona.add("read-471.json");
	     lsPersona.add("read-473.json");
	     lsPersona.add("read-476.json");
	     lsPersona.add("read-477.json");
	     lsPersona.add("read-478.json");
	     lsPersona.add("read-479.json");
	     lsPersona.add("read-480.json");
	     lsPersona.add("read-481.json");
	     lsPersona.add("read-482.json");
	     lsPersona.add("read-484.json");
	     lsPersona.add("read-486.json");
	     lsPersona.add("read-490.json");
	     lsPersona.add("read-492.json");
	     lsPersona.add("read-493.json");
	     lsPersona.add("read-494.json");
	     lsPersona.add("read-495.json");
	     lsPersona.add("read-497.json");
	     lsPersona.add("read-498.json");
	     lsPersona.add("read-500.json");
	     lsPersona.add("read-501.json");
	     lsPersona.add("read-502.json");
	     lsPersona.add("read-505.json");
	     lsPersona.add("read-506.json");
	     lsPersona.add("read-509.json");
	     lsPersona.add("read-510.json");
	     lsPersona.add("read-511.json");
	     lsPersona.add("read-512.json");
	     lsPersona.add("read-513.json");
	     lsPersona.add("read-514.json");
	     lsPersona.add("read-515.json");
	     lsPersona.add("read-517.json");
	     lsPersona.add("read-519.json");
	     lsPersona.add("read-521.json");
	     lsPersona.add("read-522.json");
	     lsPersona.add("read-525.json");
	     lsPersona.add("read-526.json");
	     lsPersona.add("read-527.json");
	     lsPersona.add("read-528.json");
	     lsPersona.add("read-529.json");
	     lsPersona.add("read-530.json");
	     lsPersona.add("read-531.json");
	     lsPersona.add("read-534.json");
	     lsPersona.add("read-536.json");
	     lsPersona.add("read-538.json");
	     lsPersona.add("read-539.json");
	     lsPersona.add("read-540.json");
	     lsPersona.add("read-543.json");
	     lsPersona.add("read-544.json");
	     lsPersona.add("read-545.json");
	     lsPersona.add("read-546.json");
	     lsPersona.add("read-547.json");
	     lsPersona.add("read-548.json");
	     lsPersona.add("read-549.json");
	     lsPersona.add("read-551.json");
	     lsPersona.add("read-554.json");
	     lsPersona.add("read-555.json");
	     lsPersona.add("read-556.json");
	     lsPersona.add("read-560.json");
	     lsPersona.add("read-561.json");
	     lsPersona.add("read-562.json");
	     lsPersona.add("read-563.json");
	     lsPersona.add("read-564.json");
	     lsPersona.add("read-566.json");
	     lsPersona.add("read-567.json");
	     lsPersona.add("read-568.json");
	     lsPersona.add("read-569.json");
	     lsPersona.add("read-571.json");
	     lsPersona.add("read-572.json");
	     lsPersona.add("read-573.json");
	     lsPersona.add("read-575.json");
	     lsPersona.add("read-576.json");
	     lsPersona.add("read-579.json");
	     lsPersona.add("read-580.json");
	     lsPersona.add("read-581.json");
	     lsPersona.add("read-582.json");
	     lsPersona.add("read-583.json");
	     lsPersona.add("read-584.json");
	     lsPersona.add("read-585.json");
	     lsPersona.add("read-586.json");
	     lsPersona.add("read-587.json");
	     lsPersona.add("read-589.json");
	     lsPersona.add("read-591.json");
	     lsPersona.add("read-592.json");
	     lsPersona.add("read-593.json");
	     lsPersona.add("read-594.json");
	     lsPersona.add("read-596.json");
	     lsPersona.add("read-598.json");
	     lsPersona.add("read-599.json");
	     lsPersona.add("read-600.json");
	     lsPersona.add("read-601.json");
	     lsPersona.add("read-604.json");
	     lsPersona.add("read-605.json");
	     lsPersona.add("read-607.json");
	     lsPersona.add("read-608.json");
	     lsPersona.add("read-610.json");
	     lsPersona.add("read-611.json");
	     lsPersona.add("read-613.json");
	     lsPersona.add("read-615.json");
	     lsPersona.add("read-617.json");
	     lsPersona.add("read-618.json");
	     lsPersona.add("read-619.json");
	     lsPersona.add("read-621.json");
	     lsPersona.add("read-622.json");
	     lsPersona.add("read-624.json");
	     lsPersona.add("read-625.json");
	     lsPersona.add("read-626.json");
	     lsPersona.add("read-627.json");
	     lsPersona.add("read-629.json");
	     lsPersona.add("read-631.json");
	     lsPersona.add("read-633.json");
	     lsPersona.add("read-634.json");
	     lsPersona.add("read-636.json");
	     lsPersona.add("read-639.json");
	     lsPersona.add("read-640.json");
	     lsPersona.add("read-642.json");
	     lsPersona.add("read-643.json");
	     lsPersona.add("read-644.json");
	     lsPersona.add("read-645.json");
	     lsPersona.add("read-646.json");
	     lsPersona.add("read-648.json");
	     lsPersona.add("read-649.json");
	     lsPersona.add("read-650.json");
	     lsPersona.add("read-652.json");
	     lsPersona.add("read-653.json");
	     lsPersona.add("read-654.json");
	     lsPersona.add("read-655.json");
	     lsPersona.add("read-656.json");
	     lsPersona.add("read-658.json");
	     lsPersona.add("read-660.json");
	     lsPersona.add("read-661.json");
	     lsPersona.add("read-662.json");
	     lsPersona.add("read-663.json");
	     lsPersona.add("read-665.json");
	     lsPersona.add("read-666.json");
	     lsPersona.add("read-669.json");
	     lsPersona.add("read-671.json");
	     lsPersona.add("read-673.json");
	     lsPersona.add("read-675.json");
	     lsPersona.add("read-676.json");
	     lsPersona.add("read-677.json");
	     lsPersona.add("read-678.json");
	     lsPersona.add("read-679.json");
	     lsPersona.add("read-680.json");
	     lsPersona.add("read-681.json");
	     lsPersona.add("read-682.json");
	     lsPersona.add("read-683.json");
	     lsPersona.add("read-684.json");
	     lsPersona.add("read-685.json");
	     lsPersona.add("read-686.json");
	     lsPersona.add("read-688.json");
	     lsPersona.add("read-689.json");
	     lsPersona.add("read-691.json");
	     lsPersona.add("read-692.json");
	     lsPersona.add("read-695.json");
	     lsPersona.add("read-697.json");
	     lsPersona.add("read-698.json");
	     lsPersona.add("read-700.json");
	     lsPersona.add("read-701.json");
	     lsPersona.add("read-703.json");
	     lsPersona.add("read-704.json");
	     lsPersona.add("read-706.json");
	     lsPersona.add("read-708.json");
	     lsPersona.add("read-709.json");
	     lsPersona.add("read-711.json");
	     lsPersona.add("read-712.json");
	     lsPersona.add("read-713.json");
	     lsPersona.add("read-715.json");
	     lsPersona.add("read-716.json");
	     lsPersona.add("read-720.json");
	     lsPersona.add("read-723.json");
	     lsPersona.add("read-725.json");
	     lsPersona.add("read-726.json");
	     lsPersona.add("read-727.json");
	     lsPersona.add("read-728.json");
	     lsPersona.add("read-729.json");
	     lsPersona.add("read-730.json");
	     lsPersona.add("read-731.json");
	     lsPersona.add("read-732.json");
	     lsPersona.add("read-733.json");
	     lsPersona.add("read-735.json");
	     lsPersona.add("read-736.json");
	     lsPersona.add("read-737.json");
	     lsPersona.add("read-739.json");
	     lsPersona.add("read-740.json");
	     lsPersona.add("read-741.json");
	     lsPersona.add("read-742.json");
	     lsPersona.add("read-745.json");
	     lsPersona.add("read-746.json");
	     lsPersona.add("read-747.json");
	     lsPersona.add("read-748.json");
	     lsPersona.add("read-749.json");
	     lsPersona.add("read-752.json");
	     lsPersona.add("read-753.json");
	     lsPersona.add("read-754.json");
	     lsPersona.add("read-759.json");
	     lsPersona.add("read-762.json");
	     lsPersona.add("read-763.json");
	     lsPersona.add("read-765.json");
	     lsPersona.add("read-766.json");
	     lsPersona.add("read-767.json");
	     lsPersona.add("read-768.json");
	     lsPersona.add("read-769.json");
	     lsPersona.add("read-772.json");
	     lsPersona.add("read-773.json");
	     lsPersona.add("read-776.json");
	     lsPersona.add("read-777.json");
	     lsPersona.add("read-779.json");
	     lsPersona.add("read-780.json");
	     lsPersona.add("read-783.json");
	     lsPersona.add("read-784.json");
	     lsPersona.add("read-787.json");
	     lsPersona.add("read-789.json");
	     lsPersona.add("read-790.json");
	     lsPersona.add("read-791.json");
	     lsPersona.add("read-793.json");
	     lsPersona.add("read-796.json");
	     lsPersona.add("read-798.json");
	     lsPersona.add("read-799.json");
	     lsPersona.add("read-802.json");
	     lsPersona.add("read-803.json");
	     lsPersona.add("read-805.json");
	     lsPersona.add("read-806.json");
	     lsPersona.add("read-808.json");
	     lsPersona.add("read-809.json");
	     lsPersona.add("read-810.json");
	     lsPersona.add("read-811.json");
	     lsPersona.add("read-812.json");
	     lsPersona.add("read-813.json");
	     lsPersona.add("read-819.json");
	     lsPersona.add("read-820.json");
	     lsPersona.add("read-821.json");
	     lsPersona.add("read-822.json");
	     lsPersona.add("read-824.json");
	     lsPersona.add("read-827.json");
	     lsPersona.add("read-829.json");
	     lsPersona.add("read-832.json");
	     lsPersona.add("read-833.json");
	     lsPersona.add("read-836.json");
	     lsPersona.add("read-838.json");
	     lsPersona.add("read-839.json");
	     lsPersona.add("read-842.json");
	     lsPersona.add("read-844.json");
	     lsPersona.add("read-845.json");
	     lsPersona.add("read-846.json");
	     lsPersona.add("read-848.json");
	     lsPersona.add("read-851.json");
	     lsPersona.add("read-852.json");
	     lsPersona.add("read-853.json");
	     lsPersona.add("read-854.json");
	     lsPersona.add("read-856.json");
	     lsPersona.add("read-857.json");
	     lsPersona.add("read-860.json");
	     lsPersona.add("read-861.json");
	     lsPersona.add("read-862.json");
	     lsPersona.add("read-863.json");
	     lsPersona.add("read-864.json");
	     lsPersona.add("read-865.json");
	     lsPersona.add("read-869.json");
	     lsPersona.add("read-870.json");
	     lsPersona.add("read-871.json");
	     lsPersona.add("read-873.json");
	     lsPersona.add("read-874.json");
	     lsPersona.add("read-877.json");
	     lsPersona.add("read-879.json");
	     lsPersona.add("read-882.json");
	     lsPersona.add("read-884.json");
	     lsPersona.add("read-885.json");
	     lsPersona.add("read-887.json");
	     lsPersona.add("read-888.json");
	     lsPersona.add("read-889.json");
	     lsPersona.add("read-890.json");
	     lsPersona.add("read-892.json");
	     lsPersona.add("read-893.json");
	     lsPersona.add("read-900.json");
	     lsPersona.add("read-901.json");
	     lsPersona.add("read-902.json");
	     lsPersona.add("read-903.json");
	     lsPersona.add("read-904.json");
	     lsPersona.add("read-907.json");
	     lsPersona.add("read-908.json");
	     lsPersona.add("read-909.json");
	     lsPersona.add("read-913.json");
	     lsPersona.add("read-914.json");
	     lsPersona.add("read-915.json");
	     lsPersona.add("read-916.json");
	     lsPersona.add("read-917.json");
	     lsPersona.add("read-918.json");
	     lsPersona.add("read-920.json");
	     lsPersona.add("read-921.json");
	     lsPersona.add("read-923.json");
	     lsPersona.add("read-924.json");
	     lsPersona.add("read-926.json");
	     lsPersona.add("read-927.json");
	     lsPersona.add("read-930.json");
	     lsPersona.add("read-931.json");
	     lsPersona.add("read-932.json");
	     lsPersona.add("read-933.json");
	     lsPersona.add("read-934.json");
	     lsPersona.add("read-936.json");
	     lsPersona.add("read-937.json");
	     lsPersona.add("read-938.json");
	     lsPersona.add("read-940.json");
	     lsPersona.add("read-941.json");
	     lsPersona.add("read-943.json");
	     lsPersona.add("read-944.json");
	     lsPersona.add("read-946.json");
	     lsPersona.add("read-949.json");
	     lsPersona.add("read-950.json");
	     lsPersona.add("read-953.json");
	     lsPersona.add("read-955.json");
	     lsPersona.add("read-957.json");
	     lsPersona.add("read-959.json");
	     lsPersona.add("read-960.json");
	     lsPersona.add("read-965.json");
	     lsPersona.add("read-966.json");
	     lsPersona.add("read-967.json");
	     lsPersona.add("read-968.json");
	     lsPersona.add("read-971.json");
	     lsPersona.add("read-972.json");
	     lsPersona.add("read-973.json");
	     lsPersona.add("read-974.json");
	     lsPersona.add("read-975.json");
	     lsPersona.add("read-976.json");
	     lsPersona.add("read-977.json");
	     lsPersona.add("read-979.json");
	     lsPersona.add("read-982.json");
	     lsPersona.add("read-984.json");
	     lsPersona.add("read-988.json");
	     lsPersona.add("read-989.json");
	     lsPersona.add("read-990.json");
	     lsPersona.add("read-992.json");
	     lsPersona.add("read-996.json");
	     lsPersona.add("read-998.json");
	     lsPersona.add("read-999.json");
	     lsPersona.add("read-1000.json");
	     lsPersona.add("read-1003.json");
	     lsPersona.add("read-1004.json");
	     lsPersona.add("read-1006.json");
	     lsPersona.add("read-1007.json");
	     lsPersona.add("read-1010.json");
	     lsPersona.add("read-1016.json");
	     lsPersona.add("read-1018.json");
	     lsPersona.add("read-1020.json");
	     lsPersona.add("read-1021.json");
	     lsPersona.add("read-1023.json");
	     lsPersona.add("read-1024.json");
	     lsPersona.add("read-1026.json");
	     lsPersona.add("read-1028.json");
	     lsPersona.add("read-1029.json");
	     lsPersona.add("read-1030.json");
	     lsPersona.add("read-1032.json");
	     lsPersona.add("read-1033.json");
	     lsPersona.add("read-1034.json");
	     lsPersona.add("read-1036.json");
	     lsPersona.add("read-1037.json");
	     lsPersona.add("read-1043.json");
	     lsPersona.add("read-1045.json");
	     lsPersona.add("read-1047.json");
	     lsPersona.add("read-1050.json");
	     lsPersona.add("read-1053.json");
	     lsPersona.add("read-1054.json");
	     lsPersona.add("read-1058.json");
	     lsPersona.add("read-1059.json");
	     lsPersona.add("read-1060.json");
	     lsPersona.add("read-1061.json");
	     lsPersona.add("read-1063.json");
	     lsPersona.add("read-1064.json");
	     lsPersona.add("read-1065.json");
	     lsPersona.add("read-1067.json");
	     lsPersona.add("read-1069.json");
	     lsPersona.add("read-1071.json");
	     lsPersona.add("read-1072.json");
	     lsPersona.add("read-1075.json");
	     lsPersona.add("read-1076.json");
	     lsPersona.add("read-1077.json");
	     lsPersona.add("read-1079.json");
	     lsPersona.add("read-1080.json");
	     lsPersona.add("read-1081.json");
	     lsPersona.add("read-1082.json");
	     lsPersona.add("read-1083.json");
	     lsPersona.add("read-1085.json");
	     lsPersona.add("read-1086.json");
	     lsPersona.add("read-1087.json");
	     lsPersona.add("read-1089.json");
	     lsPersona.add("read-1090.json");
	     lsPersona.add("read-1095.json");
	     lsPersona.add("read-1096.json");
	     lsPersona.add("read-1098.json");
	     lsPersona.add("read-1099.json");
	     lsPersona.add("read-1100.json");
	     lsPersona.add("read-1102.json");
	     lsPersona.add("read-1103.json");
	     lsPersona.add("read-1104.json");
	     lsPersona.add("read-1105.json");
	     lsPersona.add("read-1107.json");
	     lsPersona.add("read-1110.json");
	     lsPersona.add("read-1111.json");
	     lsPersona.add("read-1114.json");
	     lsPersona.add("read-1115.json");
	     lsPersona.add("read-1116.json");
	     lsPersona.add("read-1119.json");
	     lsPersona.add("read-1123.json");
	     lsPersona.add("read-1124.json");
	     lsPersona.add("read-1125.json");
	     lsPersona.add("read-1126.json");
	     lsPersona.add("read-1129.json");
	     lsPersona.add("read-1130.json");
	     lsPersona.add("read-1132.json");
	     lsPersona.add("read-1133.json");
	     lsPersona.add("read-1135.json");
	     lsPersona.add("read-1136.json");
	     lsPersona.add("read-1139.json");
	     lsPersona.add("read-1142.json");
	     lsPersona.add("read-1148.json");
	     lsPersona.add("read-1151.json");
	     lsPersona.add("read-1152.json");
	     lsPersona.add("read-1154.json");
	     lsPersona.add("read-1158.json");
	     lsPersona.add("read-1161.json");
	     lsPersona.add("read-1163.json");
	     lsPersona.add("read-1164.json");
	     lsPersona.add("read-1165.json");
	     lsPersona.add("read-1166.json");
	     lsPersona.add("read-1167.json");
	     lsPersona.add("read-1169.json");
	     lsPersona.add("read-1171.json");
	     lsPersona.add("read-1175.json");
	     lsPersona.add("read-1181.json");
	     lsPersona.add("read-1184.json");
	     lsPersona.add("read-1185.json");
	     lsPersona.add("read-1186.json");
	     lsPersona.add("read-1189.json");
	     lsPersona.add("read-1190.json");
	     lsPersona.add("read-1191.json");
	     lsPersona.add("read-1192.json");
	     lsPersona.add("read-1193.json");
	     lsPersona.add("read-1194.json");
	     lsPersona.add("read-1195.json");
	     lsPersona.add("read-1196.json");
	     lsPersona.add("read-1197.json");
	     lsPersona.add("read-1199.json");
	     lsPersona.add("read-1200.json");
	     lsPersona.add("read-1201.json");
	     lsPersona.add("read-1202.json");
	     lsPersona.add("read-1203.json");
	     lsPersona.add("read-1204.json");
	     lsPersona.add("read-1205.json");
	     lsPersona.add("read-1206.json");
	     lsPersona.add("read-1209.json");
	     lsPersona.add("read-1210.json");
	     lsPersona.add("read-1213.json");
	     lsPersona.add("read-1215.json");
	     lsPersona.add("read-1216.json");
	     lsPersona.add("read-1217.json");
	     lsPersona.add("read-1218.json");
	     lsPersona.add("read-1219.json");
	     lsPersona.add("read-1221.json");
	     lsPersona.add("read-1224.json");
	     lsPersona.add("read-1225.json");
	     lsPersona.add("read-1229.json");
	     lsPersona.add("read-1233.json");
	     lsPersona.add("read-1237.json");
	     lsPersona.add("read-1238.json");
	     lsPersona.add("read-1240.json");
	     lsPersona.add("read-1242.json");
	     lsPersona.add("read-1243.json");
	     lsPersona.add("read-1244.json");
	     lsPersona.add("read-1247.json");
	     lsPersona.add("read-1250.json");
	     lsPersona.add("read-1251.json");
	     lsPersona.add("read-1252.json");
	     lsPersona.add("read-1254.json");
	     lsPersona.add("read-1256.json");
	     lsPersona.add("read-1258.json");
	     lsPersona.add("read-1259.json");
	     lsPersona.add("read-1260.json");
	     lsPersona.add("read-1262.json");
	     lsPersona.add("read-1263.json");
	     lsPersona.add("read-1266.json");
	     lsPersona.add("read-1267.json");
	     lsPersona.add("read-1268.json");
	     lsPersona.add("read-1269.json");
	     lsPersona.add("read-1271.json");
	     lsPersona.add("read-1272.json");
	     lsPersona.add("read-1273.json");
	     lsPersona.add("read-1274.json");
	     lsPersona.add("read-1276.json");
	     lsPersona.add("read-1277.json");
	     lsPersona.add("read-1279.json");
	     lsPersona.add("read-1282.json");
	     lsPersona.add("read-1284.json");
	     lsPersona.add("read-1285.json");
	     lsPersona.add("read-1287.json");
	     lsPersona.add("read-1288.json");
	     lsPersona.add("read-1289.json");
	     lsPersona.add("read-1291.json");
	     lsPersona.add("read-1292.json");
	     lsPersona.add("read-1295.json");
	     lsPersona.add("read-1299.json");
	     lsPersona.add("read-1300.json");
	     lsPersona.add("read-1301.json");
	     lsPersona.add("read-1303.json");
	     lsPersona.add("read-1305.json");
	     lsPersona.add("read-1307.json");
	     lsPersona.add("read-1308.json");
	     lsPersona.add("read-1309.json");
	     lsPersona.add("read-1310.json");
	     lsPersona.add("read-1311.json");
	     lsPersona.add("read-1315.json");
	     lsPersona.add("read-1316.json");
	     lsPersona.add("read-1318.json");
	     lsPersona.add("read-1319.json");
	     lsPersona.add("read-1320.json");
	     lsPersona.add("read-1322.json");
	     lsPersona.add("read-1323.json");
	     lsPersona.add("read-1327.json");
	     lsPersona.add("read-1329.json");
	     lsPersona.add("read-1330.json");
	     lsPersona.add("read-1331.json");
	     lsPersona.add("read-1332.json");
	     lsPersona.add("read-1333.json");
	     lsPersona.add("read-1334.json");
	     lsPersona.add("read-1335.json");
	     lsPersona.add("read-1336.json");
	     lsPersona.add("read-1337.json");
	     lsPersona.add("read-1340.json");
	     lsPersona.add("read-1342.json");
	     lsPersona.add("read-1343.json");
	     lsPersona.add("read-1346.json");
	     lsPersona.add("read-1349.json");
	     lsPersona.add("read-1350.json");
	     lsPersona.add("read-1354.json");
	     lsPersona.add("read-1362.json");
	     lsPersona.add("read-1364.json");
	     lsPersona.add("read-1368.json");
	     lsPersona.add("read-1369.json");
	     lsPersona.add("read-1370.json");
	     lsPersona.add("read-1371.json");*/
		
		lsPersona.add("read-Angelica.json");
		lsPersona.add("read-Esther.json");
		lsPersona.add("read-Isabel.json");
		lsPersona.add("read-Laura.json");
		lsPersona.add("read-monica.json");
		
		return lsPersona;
		
	}
	
	
}
