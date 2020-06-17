package net.dothr.recreate;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import net.db.QueryXe;
import net.dothr.fromjson.PersonaFromJson;
import net.utils.ClientRestUtily;
import net.utils.ConnectionBD;
import net.utils.ConstantesREST;

/**
 * USO EXCLUSIVO PARA SISTEMA CON TODOS LOS USUARIOS (XE_ALL)
 * Clase para procesar los archivos .json e insertar sus datos a manera
 * de evitar el proceso de alta con correo electrónico y validación<br>
 * 1. <b> INSERTA PERSONA CON NUEVA SECUENCIA</b> <br> 
 * 2. <b> Carga Datos por medio de Transactional</b> <br>
 * 3. <i> No inserta datos de Solr ni area_persona </i>
 * @author Netto
 *
 */
public class PersonaFullFromJson extends PersonaFromJson{
	/*
	 * Es requerido tener direccionada y preCargada la BD-xe, y 
	 * corriendo la aplicación TransactionalStructured
	 */
	
	private static Logger log4j = Logger.getLogger( PersonaFullFromJson.class );
	private static final String ROL_INICIAL ="3";
	
	private static Map<String, Long> mapSecuencia = null;
	
	private static final String CV_DIR = 
//			"0.pirh/" // USARLA CUANDO SE CREÓ BD POR Scripts.sh */ 
//			"1.jul-2015/"	//>Test CV's de repositorio (Word,pdf)
//			"2.AWS(P.RH)/"		//>> RDS
//			"3.Selex(P.Disen)/"	//>> RDS 
			"4.SelexP32016/"	//>> RDS
//			"5.SelexP42017/"	//>> RDS
//			"6.SelexP52017/"	//>> RDS

			/* **********TEST */
			//"99.Demo/"			// >> DEMO-parcial
//			"xeAll/"	//  >> DEMO.TOtal
//			"x.ClasificadosGab/781-Publicados/" //>Test/Octavio
//			"x.ClasificadosGab/preClasificados/" //>Octavio
			;
	
	/* ERROR: ERROR AL INSERTAR / verificar la especificación correspondiente / Error de sistema */
	/* Insertar area_persona con AreaPersonaTester para cada persona */
	
		
	private static final String CV_JSON_DIR = ConstantesREST.JSON_HOME+
			"PersonaRecreate/curriculumManagement/"+CV_DIR;

	protected static String REPORTE_PATH = 
			"/home/dothr/workspace/ClientRest/files/out/"
			+"R.InsPersonas_"+fechaArchivo()+"<SUBF>.txt";
	private static String fechaCreacionPps;
	
	private static void setProps(){
		REPORTE_PATH = REPORTE_PATH.replace("<SUBF>", CV_DIR.replace("/", "_") );
		if(CV_DIR.equals("0.pirh/")){
			fechaCreacionPps = "2015-11-07 00:00:00.295";
		}
		else if(CV_DIR.equals("1.jul-2015/")){
			fechaCreacionPps = "2015-11-07 00:00:00.295";
		}else if(CV_DIR.equals("2.AWS(P.RH)/")){
			fechaCreacionPps = "2016-02-01 11:00:00.295";
		}else  if(CV_DIR.equals("3.Selex(P.Disen)/")){
			fechaCreacionPps = "2016-05-12 11:39:00.295";
		}else  if(CV_DIR.equals("4.SelexP32016/")){
			fechaCreacionPps = "2016-09-05 11:39:00.295";
		}else  if(CV_DIR.equals("5.SelexP42017/")){
			fechaCreacionPps = "2016-09-05 11:39:00.295";
		}else  if(CV_DIR.equals("6.SelexP52017/")){
			fechaCreacionPps = "2016-09-05 11:39:00.295";
		}
		else {	//DEFAULT
			fechaCreacionPps =  getToday4Insert() ;
		}
	}
	
	public static void main(String[] args) {
		setProps();
		
		if(WEB_RESOURCE.equals(ConstantesREST.WEB_TRANSACT_AWS) 
				&& !PERMITIR_CARGA_EN_AWS)
		{
				log4j.fatal("<updXeBase> Esta apuntando a AWS y no esta permitido "+WEB_RESOURCE, new NullPointerException() );
				 return;
		}
		else{
			try{	
				
				if(CV_DIR.equals("0.pirh/") || CV_DIR.equals("99.Demo/")){
					updXeBase();
					log4j.debug("<createPersonas> Se cargan datos de Administradores!!");
				}
				StringBuilder pCreate = createPersonas();
				ClientRestUtily.writeStringInFile(REPORTE_PATH, pCreate.toString(), false);
				log4j.info(">>>\n Se Reporta proceso en: \n" + REPORTE_PATH);
				
				log4j.info("\n Para Cargar Imagenes, se debe utilizar FileJAXWSTest en ClientFileTCE, " +
						"modificando el PATH_JSON a: "+CV_DIR);
				

			}catch (Exception e){
				e.printStackTrace();
			}
		}
		
		
	}
	
	/**
	 * Carga los datos de Administradores (1 y 2) del Sistema
	 * @return
	 * @throws Exception
	 */
	protected static boolean updXeBase() throws Exception{
		log4j.debug("<updXeBase> Inicio.");
		boolean res = false;

		JSONArray jsResponse;
		String pingRes = ping(null);
		jsResponse = new JSONArray(pingRes);
		JSONObject jsonAux = jsResponse.getJSONObject(0);
		if( (jsonAux.has("code") && jsonAux.has("type")) && (jsonAux.getString("type").equals("I"))){
			log4j.debug("<updXeBase> Se tiene conexión con AppTransactional \n");
			
			JSONObject jsonPersona = getJsonObject("read-1.octavio.linares.json", 
					ConstantesREST.JSON_HOME+"PersonaRecreate/curriculumManagement/0.pirh/");
			String idPersona = jsonPersona.getString("idPersona");	//"2";
			String email = jsonPersona.getString("email");
			
			log4j.debug("<updXeBase> procesando idPersona: "+idPersona +", email: "+email);
			log4j.debug(procesaJsonPersona(jsonPersona));
			
			jsonPersona = getJsonObject("read-2.ernesto.valle.json", 
					ConstantesREST.JSON_HOME+"PersonaRecreate/curriculumManagement/0.pirh/");
			idPersona = jsonPersona.getString("idPersona");	//"2";
			email = jsonPersona.getString("email");
			
			log4j.debug("<updXeBase> procesando idPersona: "+idPersona +", email: "+email);
			log4j.debug(procesaJsonPersona(jsonPersona));
			
			res = true;
			
		}
		else{
			log4j.error("<updXeBase> NO hay conexión con App "+ (jsonAux.has("message")?jsonAux.has("message"):""));
		}
		log4j.debug("<updXeBase> Fin.");
		return res;
	}
	
	/**
	 * Metodo integral que realiza la lectura de archivos read-?.json:<br> 
	 * 1. Inserta datos Iniciales en BD (Persona, HistoricoPassword, Rel-Emp-Persona)<br>
	 * 2. Carga datos a partir del JSONObject generado del archivo (en App Transactional)	 *  
	 * @return
	 * @throws Exception
	 */
	protected static StringBuilder createPersonas() throws Exception {
		log4j.debug("<createPersonas> Inicio.");
		
		Long secPersona = null;
		Long secHistPwd = null;
		Long secRelEmpPer = null;
//		Long secAreaPer = null;
		
		
		StringBuilder sbProcess = new StringBuilder("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n")
		 .append("++++++++++++++++++++++++++++++++++++  PersonaFullFromJson.createPersonas  +++++++++++++++++++++++++++++++++ \n")
		 .append("+ CV_JSON_DIR= ").append(CV_JSON_DIR).append("\n")
		 .append("+ WEB_RESOURCE= ").append(WEB_RESOURCE).append("\n")
		 .append("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n\n");
		

		try {	
			log4j.debug("<createPersonas> Conectando a Base de datos...");
			conn = new ConnectionBD(WEB_RESOURCE);
			conn.getDbConn();
			if(mapSecuencia==null){
				log4j.debug("<createPersonas> Se genera mapa de secuencias ");
				sbProcess.append("Se genera mapa de secuencias\n");
				mapSecuencia = getMapaSecuencias();
				secPersona = mapSecuencia.get("SEQ_PERSONA");
				secHistPwd = mapSecuencia.get("SEQ_HISTORICO_PASSWORD");
				secRelEmpPer = mapSecuencia.get("SEQ_RELACION_EMPRESA_PERSONA");
			}
			sbProcess.append("Hay conexión con BD.\n Comprobando Conexión con AppTransactional (Ping) ... \n");
			log4j.debug("<createPersonas> Comprobando Conexión con AppTransactional (Ping) \n");
			
			JSONArray jsResponse;
			String pingRes = ping(null);
			jsResponse = new JSONArray(pingRes);
			JSONObject jsonAux = jsResponse.getJSONObject(0);
			if( (jsonAux.has("code") && jsonAux.has("type")) && (jsonAux.getString("type").equals("I"))){
				log4j.debug("<createPersonas> Se tiene conexión con AppTransactional \n");
				String archivoPersona = null;
				JSONObject jsonPersona;
				StringBuilder sbQuery = null;
						
				sbProcess.append("Se obtiene listado de archivos a procesar\n");
				Iterator<String> itFiles = listFromFile(CV_JSON_DIR,"listaRead.txt").iterator();
						//getLsArchivosPersona(origenFileRead).iterator();
				sbProcess.append("Se Iteran archivos Json para Inserción y Carga::::::: \n\n");					
				
				while(itFiles.hasNext()){
					archivoPersona = itFiles.next();
					
					////sdafasdf
					if(!archivoPersona.startsWith("#")) {
						log4j.debug("<createPersonas> Se obtiene Json a partir de archivoPersona " + archivoPersona );
						try{
							jsonPersona = getJsonObject(archivoPersona, CV_JSON_DIR);
							log4j.debug("<createPersonas> jsonPersona: " + jsonPersona );
							String email = jsonPersona.getString("email");
							
							//Datos que no existian en Json ANteriores
							jsonPersona.put("idEmpresa", "1");
							jsonPersona.put("idEmpresaConf", "1");
							if(!jsonPersona.has("role")){ jsonPersona.put("role", "8"); }
							
							
							/* ******* INSERTS PREVIOS A CARGA (Persona, HistoricoPwd, RelEmpPersona) ***** */
							sbProcess.append("\n\n  *************** >> PROCESO DE INSERTS PARA Archivo ").append(archivoPersona).append(" << *************** \n");
							Long idPersona = secPersona;
							
							/* Insert de Persona */
							String idEstatusInscripcion = (jsonPersona.has("idEstatusInscripcion")? jsonPersona.getString("idEstatusInscripcion"):"1");
							sbQuery = new StringBuilder(QueryXe.sqlInsPersona)
								.append("(")
								.append(idPersona).append(",'")
								.append( (jsonPersona.has("email")? jsonPersona.getString("email"):"") ).append("',")
								.append(idEstatusInscripcion.equals("3")?"2":idEstatusInscripcion) //Si es 3, manda msg de contexto: Se cambió una propiedad en un contexto de Publicación
								.append(",true,")
								.append( (jsonPersona.has("tokenInicio")? "'"+jsonPersona.getString("tokenInicio")+"'":"null") )
//								.append(",").append("null")//.append( (jsonPersona.has("textoClasificacion")? "'"+jsonPersona.getString("textoClasificacion")+"'":"null") )
								.append(",").append( (jsonPersona.has("textoClasificacion")? "true":"false") ) //clasificado?
								.append(",'").append(jsonPersona.has("fechaCreacion")?jsonPersona.getString("fechaCreacion"):fechaCreacionPps)
								.append("','").append(jsonPersona.has("fechaCreacion")?jsonPersona.getString("fechaCreacion"):fechaCreacionPps)
								.append("'); \n");		
							log4j.debug("<createPersonas> Se inserta Persona en BD: " + sbQuery );
							sbProcess.append("Insert-Persona: ").append(sbQuery).append("\n");
							conn.updateDataBaseNoCommit(sbQuery.toString());
							
							/* Inserts de Historico Password */
							sbQuery = new StringBuilder(QueryXe.sqlInsHistPwd).append("(").append(secHistPwd).append(",")
								.append(idPersona).append(",'")
								.append( (jsonPersona.has("password")? jsonPersona.getString("password"):PWD_123456789) )
								.append("','").append(jsonPersona.has("fechaCreacion")?jsonPersona.getString("fechaCreacion"):fechaCreacionPps)
								.append("');\n");
							log4j.debug("<createPersonas> Se inserta Historico PWD en BD: " + sbQuery );
							sbProcess.append("Historico PWD: ").append(sbQuery).append("\n");
							conn.updateDataBaseNoCommit(sbQuery.toString());
							
							/* Inserts de Relacion Empresa Persona */
							sbQuery = new StringBuilder(QueryXe.sqlInsRelEmpPers).append("(")
									.append(secRelEmpPer)
									.append(",").append(ROL_INICIAL)//jsonPersona.getString("role"))
									.append(",").append(jsonPersona.getString("idEmpresa")).append(",").append(idPersona)
									.append(",'").append(jsonPersona.has("fechaCreacion")?jsonPersona.getString("fechaCreacion"):fechaCreacionPps)
									.append("',false,true,null")
									.append(");\n");
							log4j.debug("<createPersonas> Se inserta Rel_empresa_persona en BD: " + sbQuery );
							sbProcess.append("Rel_empresa_persona: ").append(sbQuery).append("\n");
							conn.updateDataBaseNoCommit(sbQuery.toString());
							/* Confirmar los tres inserts */
							conn.commitQuery();
							
							log4j.debug("<createPersonas> Fin de Inserts, se procede a carga con Json.");
							
							// Verificar que exista en Sistema (via Servicio Initial)
							JSONObject jsonReq = new JSONObject();
							jsonReq.put("email", email);
							jsonReq.put("idEmpresaConf", jsonPersona.has("idEmpresaConf")?jsonPersona.getString("idEmpresaConf"):IDCONF_DOTHR);
							log4j.debug("<createPersonas> Se valida existencia via servicio INITIAL:\n ==> jsonReq: " + jsonReq);
							
							sbProcess.append("Se valida existencia via servicio INITIAL:\n ==> jsonReq: ").append(jsonReq).append("\n");
							String stResponse  = getJsonFromService(jsonReq.toString(), CV_PERS_INITIAL );
							
							
							log4j.debug("<createPersonas> <==: " + stResponse);
							if(stResponse.indexOf("idPersona")!=-1 && stResponse.indexOf("role")!=-1){
								/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ */
								/* ++++++++++++++++++++ CARGA (LOAD) DE DATOS POR AppTransactional ++++++++++++++ */
								/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ */
								
								jsonPersona.put("idPersona", String.valueOf(idPersona) );
								sbProcess.append(procesaJsonPersona(jsonPersona));
								
								//TODO AREA PERSONA
								
								
								//Reescribiendo con el nuevo ID
								jsonPersona = getJsonObject(archivoPersona, CV_JSON_DIR);
								if(jsonPersona.has("idPersona") && !jsonPersona.has("idPersonaAnt") ){
									jsonPersona.put("idPersonaAnt", jsonPersona.getString("idPersona") );
								}
								jsonPersona.put("idPersona", String.valueOf(idPersona) );
								
								ClientRestUtily.writeStringInFile(CV_JSON_DIR+archivoPersona, formatJsonPersona(jsonPersona, true), false);
							}else{
								log4j.error("<createPersonas> Error: No existe persona coincidente con email & idEmpresaConf ");
								sbProcess.append("Error, No existe persona coincidente con email & idEmpresaConf: ")
									.append(jsonReq).append("\n");
							}							
							secPersona++;
							secHistPwd++;
							secRelEmpPer++;	
							
						}catch (Exception ef){
							log4j.fatal("<createPersonas> Error al procesar archivoPersona", ef);
							sbProcess.append("<Exception> en procesar archivoPersona: ").append(ef.getMessage()).append("\n");
						}
					}
					
					///////sdffasdf
				}//Fin de WHile
				sbProcess.append("\n:::::: Fin de Iteración de archivos \n");
				
				sbProcess.append("\n Se actualizan las secuencias \n");
				
				StringBuilder sbSetSecuencia = new StringBuilder("\n /* Actualizar Secuencias */ \n");
				
				sbSetSecuencia
					.append("--SELECT last_value FROM SEQ_PERSONA; \n").append(" ALTER SEQUENCE SEQ_PERSONA RESTART WITH ").append(secPersona).append(";\n")
					.append("--SELECT last_value FROM SEQ_HISTORICO_PASSWORD; \n").append(" ALTER SEQUENCE SEQ_HISTORICO_PASSWORD RESTART WITH ").append(secHistPwd).append(";\n")
					.append("--SELECT last_value FROM SEQ_RELACION_EMPRESA_PERSONA; \n").append(" ALTER SEQUENCE SEQ_RELACION_EMPRESA_PERSONA RESTART WITH ").append(secRelEmpPer).append(";\n");
				log4j.debug(sbSetSecuencia.toString());
				sbProcess.append(sbSetSecuencia);
				conn.updateDataBase(sbSetSecuencia.toString());
				
			}else{
				log4j.error("<createPersonas> NO hay conexión con App "+ (jsonAux.has("message")?jsonAux.has("message"):""));
				sbProcess.append("<Excepcion> Error: NO hay conexión con App ")
					.append( (jsonAux.has("message")?jsonAux.has("message"):""));
			}
		} catch (Exception e) {
			log4j.fatal("<createPersonas> Error GENERAL en createPersona: ", e);
			sbProcess.append("<Exception> en createPersona: ").append(e.getMessage()).append("\n");
		}
		
		conn.closeConnection();
		
		sbProcess
		    .append("\n ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ \n")
			.append(" +++++++++++++++++++  FIN de Proceso PersonaFullFromJson.createPersona   ++++++++++++++++ \n")
			.append(" ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		return sbProcess;
	}
	
	
	
	/* *********************  SECUENCIAS DESDE BASE DE DATOS   ***************************** */
	/**
	 * Obtiene un mapa de secuencias para los Inserts Correspondientes
	 * @return
	 */
	private static Map<String, Long> getMapaSecuencias() throws Exception{
		Map<String, Long> mapa = new HashMap<String, Long>();
		
		mapa.put("SEQ_PERSONA", getLastValue("SEQ_PERSONA"));
		mapa.put("SEQ_HISTORICO_PASSWORD", getLastValue("SEQ_HISTORICO_PASSWORD"));
		mapa.put("SEQ_RELACION_EMPRESA_PERSONA", getLastValue("SEQ_RELACION_EMPRESA_PERSONA"));
		mapa.put("SEQ_AREA_PERSONA", getLastValue("SEQ_AREA_PERSONA"));
		
		return mapa;
	}
	/**
	 * Obtiene el entero del valor actual de la secuencia en parametro
	 * @param sequenceName
	 * @return
	 */
	private static Long getLastValue(String sequenceName) {
		Long idLast = null;
		try {
			ResultSet rs = conn.getQuerySet("SELECT last_value FROM " + sequenceName + ";");
			if(rs.next()){
				idLast = rs.getLong(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log4j.fatal("<error> Obtener el ultimo valor de la secuencia " + sequenceName );
			idLast= null;
		}
		
		return idLast;
	}
	
}