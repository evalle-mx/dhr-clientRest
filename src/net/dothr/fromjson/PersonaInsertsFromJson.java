package net.dothr.fromjson;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import net.db.QueryXe;
import net.utils.ClientRestUtily;
import net.utils.ConnectionBD;
import net.utils.ConstantesREST;

/**
 * USO REGULAR PARA INSERTAR USUARIOS CON SU MISMO idPersona EN UNA BD vacia
 * Clase para procesar los archivos .json e insertar sus datos a manera
 * de evitar el proceso de alta con correo electrónico y validación<br>
 * <b> INSERTA PERSONA CON EL MISMO ID QUE LA ORIGINAL</b>
 * @author Netto
 *
 */
public class PersonaInsertsFromJson extends PersonaFromJson {

	/*
	 * Procedimiento: 
	 * 0. Hacer respaldo de personas en archivos JSON [personaToJson], y 
	 * 	  ubicar directorio de entrada PERSONA_JSON_DIR (AWS, awsNoPub, RDS, etc)
	 * 1. Verificar que WEB_RESOURCE apunte hacia la base correcta (Localhost)
	 * 2. La base debe Instalarse por medio de los Scripts Sh en <%svn>/dothr_otros/Data/Postgre/
	 * 3. Los archivos (read.json) a generar Insert deben estar en PERSONA_JSON_DIR
	 * 4. Modificar-Validar la lista de jsonPersona correcta (Heredada de PersonaFromJson)
	 * 4. Ejecutar fullInsertsPersona() por cada elemento persona
	 * 5. Verificar reporte de salida
	 * 6. EJecutar PersonaFromJson.multiLoadPersona con la misma lista
	 * 
	 */
	private static Logger log4j = Logger.getLogger( PersonaInsertsFromJson.class );
	
	protected static final String UPDATE_FECHAS_PATH = ConstantesREST.JSON_HOME+"Report/SQL.UpdFechasPersona."+fechaArchivo()+".sql";
	protected static final String REPORTE_PATH = ConstantesREST.JSON_HOME+"Report/Rep.PersonaInsertsFromJson_"+fechaArchivo()+".txt";
	private static final String PATH_JSON_CONTROL = ConstantesREST.JSON_HOME+"PersonaRecreate/JSON.ControlFIleName.json";
	
	private static String idPersonaFinal = "0";
	private static Long secPersona = null;
	private static Long secHistPwd = null;
	private static Long secRelEmpPer = null;
	
	private static StringBuilder idPersonasClause = new StringBuilder();//EN caso de error, sustituir en query:
	private static StringBuilder sbSetSecuencia = new StringBuilder("/* Actualizar Secuencias */ \n");
	private static StringBuilder sbUpdateFechas = new StringBuilder("/* Querys para actualizar fechas identicas de Origen (Ejecutar después de PersonaFromJson) */ \n");
	
	private static Map<String, Long> mapSecuencia = null;
	
	private static boolean setSecuencias = true;
	private static boolean bSecPersonaDB = true; //si esta en True, obtiene idPersona de la secuencia
	
	
	public static void main(String[] args) {
		
//		simpleFullProcessPersona("read-740.json");
		multipleFullPersona();
	}
	
	/**
	 * Crea Una persona en BD y carga datos a partir de archivos .json
	 * (Solo necesita la Base de Datos XE Base )
	 * @param fileName
	 */
	public static void simpleFullProcessPersona(String fileName) {
		StringBuilder sbMain = new StringBuilder("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n")
		 .append("++++++++++++++++++++++++++++++++++++  PROCESOMVRENAME  +++++++++++++++++++++++++++++++++ \n")
		 .append("+ PATH_JSON_CONTROL= ").append(PATH_JSON_CONTROL).append("\n")
		 .append("+ DIRECTORIO_JSON= ").append(DIRECTORIO_JSON).append("\n")
//		 .append("+ PATH_NEWPERSON_DIR= ").append(PATH_NEWPERSON_DIR).append("\n")
		 .append("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n\n");
		try {
			conn = new ConnectionBD(WEB_RESOURCE);
			conn.getDbConn();
			sbMain = fullInsertsPersona(fileName);
			
			secPersona = Long.parseLong(idPersonaFinal);
			
			sbSetSecuencia = new StringBuilder("\n /* Actualizar Secuencias */ \n");
			
			sbSetSecuencia.append("--SELECT last_value FROM SEQ_PERSONA; \n")
				.append("--SELECT last_value FROM SEQ_PERSONA; \n").append(" ALTER SEQUENCE SEQ_PERSONA RESTART WITH ").append(secPersona+1).append(";\n")
				.append("--SELECT last_value FROM SEQ_HISTORICO_PASSWORD; \n").append(" ALTER SEQUENCE SEQ_HISTORICO_PASSWORD RESTART WITH ").append(secHistPwd).append(";\n")
				.append("--SELECT last_value FROM SEQ_RELACION_EMPRESA_PERSONA; \n").append(" ALTER SEQUENCE SEQ_RELACION_EMPRESA_PERSONA RESTART WITH ").append(secRelEmpPer).append(";\n");
			log4j.debug(sbSetSecuencia.toString());
			
			sbMain.append(sbSetSecuencia).append("\n").append(deleteClause());
			ClientRestUtily.writeStringInFile(REPORTE_PATH, sbMain.toString(), false );
			
			log4j.debug("Reporte Proceso: " + REPORTE_PATH );
			
			if(setSecuencias){
				conn.updateDataBase(sbSetSecuencia.toString());
			}
			
			conn.closeConnection();
			
		} catch (Exception e) {
			log4j.fatal("Error en Main: ", e);
		}
	}
	
	/**
	 * Crea personas en BD y carga datos a partir de archivos .json
	 * (Solo necesita la Base de Datos XE Base )
	 * 
	 */
	public static void multipleFullPersona() {
		log4j.debug("<multipleFullPersona>");
		List<String> lsFiles = getLsFiles();
				//getListaArchivosPersona();
		
		StringBuilder sbMultipleProccess = new StringBuilder("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n")
		 .append("++++++++++++++++++++++++++++++++++++  MULTIPLE-FULLPERSONA  +++++++++++++++++++++++++++++++++ \n")
		 .append("+ PATH_JSON_CONTROL= ").append(PATH_JSON_CONTROL).append("\n")
		 .append("+ DIRECTORIO_JSON= ").append(DIRECTORIO_JSON).append("\n")
		 .append("+ WEB_RESOURCE= ").append(WEB_RESOURCE).append("\n")
		 .append("+ # Archivos= ").append(lsFiles.size()).append("\n")
		 .append("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n\n");
		
		try {
			conn = new ConnectionBD(WEB_RESOURCE);
			conn.getDbConn();

			String fileName = null;
			secPersona = Long.parseLong(idPersonaFinal);
			
			if(mapSecuencia==null){
				log4j.debug("<fullProcessPersona> Se genera mapa de secuencias ");
				mapSecuencia = getMapaSecuencias();
				if(bSecPersonaDB){
					secPersona = mapSecuencia.get("SEQ_PERSONA");	
				}
				secHistPwd = mapSecuencia.get("SEQ_HISTORICO_PASSWORD");
				secRelEmpPer = mapSecuencia.get("SEQ_RELACION_EMPRESA_PERSONA");
			}
			
			for(int x=0; x<lsFiles.size();x++){
				fileName = lsFiles.get(x);
				sbMultipleProccess.append(fullInsertsPersona(fileName)).append("====\n");
			}
			log4j.debug("<multipleFullPersona> Fin de Multiple Proceso");
						
			
			sbSetSecuencia = new StringBuilder("\n /* Actualizar Secuencias */ \n");
			
			sbSetSecuencia
				.append("--SELECT last_value FROM SEQ_PERSONA; \n").append(" ALTER SEQUENCE SEQ_PERSONA RESTART WITH ").append(secPersona+1).append(";\n")
				.append("--SELECT last_value FROM SEQ_HISTORICO_PASSWORD; \n").append(" ALTER SEQUENCE SEQ_HISTORICO_PASSWORD RESTART WITH ").append(secHistPwd).append(";\n")
				.append("--SELECT last_value FROM SEQ_RELACION_EMPRESA_PERSONA; \n").append(" ALTER SEQUENCE SEQ_RELACION_EMPRESA_PERSONA RESTART WITH ").append(secRelEmpPer).append(";\n");
			log4j.debug(sbSetSecuencia.toString());
			sbMultipleProccess.append(sbSetSecuencia);
			
			if(setSecuencias){
				conn.updateDataBase(sbSetSecuencia.toString());
			}
			sbUpdateFechas.append("\n\n")
			.append("/* Update para estatus Publicado: */")
			.append("UPDATE persona SET id_estatus_inscripcion = 3 WHERE id_texto_clasificacion IS NOT null; \n\n ")
			.append(deleteClause());
			
			ClientRestUtily.writeStringInFile(UPDATE_FECHAS_PATH, sbUpdateFechas.toString(), false );
			sbMultipleProccess.append("\n<UPDATE.Query> Se anexa update de fechas por persona en " + UPDATE_FECHAS_PATH )
			.append("\n  ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++")
			.append("\n  ++++++++++++++++++   FIN DE MULTIPLE-FULLPERSONA   +++++++++++++++++++++++++++++++++++++ \n");
			
			conn.closeConnection();
		} catch (Exception e) {
			log4j.fatal("<Excepcion> ", e);
			sbMultipleProccess.append("<Excepcion> Error: ").append(e.getMessage());
		}
		
		log4j.debug("<multipleFullPersona> Se guarda bitacora en: \n" +
				REPORTE_PATH);
		ClientRestUtily.writeStringInFile(REPORTE_PATH, sbMultipleProccess.toString(), false );
	}
	
		
	/**
	 * Metodo que lee un archivo plano (.json) de persona 
	 * convirtiendolo en un objeto JSON para obtener datos y realizar los Inserts en el
	 * WEB_RESOURCE (DB) determinado:
	 * <ul><li>Persona</li><li>Historico_password</li><li>Relacion_empresa_persona</li></ul>
	 * 
	 * @param archivoPersona
	 * @return
	 * @throws Exception
	 */
	private static StringBuilder fullInsertsPersona(String archivoPersona) throws Exception{
		log4j.debug("<processPersona>");
		StringBuilder sbProccess = new StringBuilder("+++++++ ProcessPersona: ").append(archivoPersona).append("++++++\n");
		StringBuilder sbQuery = null;
		if(WEB_RESOURCE.equals(ConstantesREST.WEB_TRANSACT_AWS) 
				&& !PERMITIR_CARGA_EN_AWS)
				{
				log4j.fatal("Esta apuntando a AWS y no esta permitido "+WEB_RESOURCE, new NullPointerException() );
				sbProccess.append(" Esta apuntando a AWS y no esta permitido: "+WEB_RESOURCE).append("\n");
		}else{
			try{
				if(archivoPersona.startsWith("read")){
					//1. Obtener json string & 2. Convertir a JSON
					log4j.debug("<processPersona> Se obtiene Json a partir de archivoPersona ");
					JSONObject jsonPersona = getJsonObject(archivoPersona, DIRECTORIO_JSON);
					log4j.debug("<processPersona>jsonPersona: " + jsonPersona );
					String idPersona = jsonPersona.getString("idPersona");
					sbProccess.append("idPersona: ").append(idPersona).append("\n");
					//2. Se usa el idPersona como identificador a insertar en Query de Persona
					if(mapSecuencia==null){
						log4j.debug("<fullProcessPersona> Se genera mapa de secuencias ");
						mapSecuencia = getMapaSecuencias();
						if(bSecPersonaDB){
							secPersona = mapSecuencia.get("SEQ_PERSONA");
						}
						secHistPwd = mapSecuencia.get("SEQ_HISTORICO_PASSWORD");
						secRelEmpPer = mapSecuencia.get("SEQ_RELACION_EMPRESA_PERSONA");
					}
					
					if(bSecPersonaDB){
						log4j.debug("Obteniendo de secuencia: " + secPersona );
						idPersona = String.valueOf(secPersona);
					}
					
					//3 se insertan los datos previos (Emula proceso de ALta y verificación con correo)
					/* 3.a. Inserts de Persona */
					String idEstatusInscripcion = (jsonPersona.has("idEstatusInscripcion")? jsonPersona.getString("idEstatusInscripcion"):"1");
					
					sbQuery = new StringBuilder(QueryXe.sqlInsPersona)
							.append("(")
							.append(idPersona).append(",'")
							.append( (jsonPersona.has("email")? jsonPersona.getString("email"):"") ).append("',")
							.append(idEstatusInscripcion.equals("3")?"2":idEstatusInscripcion) //Si es 3, manda msg de contexto: Se cambió una propiedad en un contexto de Publicación
							.append(",true,")
							.append( (jsonPersona.has("tokenInicio")? "'"+jsonPersona.getString("tokenInicio")+"'":"null") )
							.append(",").append( (jsonPersona.has("textoClasificacion")? "'"+jsonPersona.getString("textoClasificacion")+"'":"null") )
							.append(",").append( (jsonPersona.has("textoClasificacion")? "true":"false") ) //clasificado?
							.append(",'").append(jsonPersona.has("fechaCreacion")?jsonPersona.getString("fechaCreacion"):getToday4Insert())
							.append("','").append(jsonPersona.has("fechaCreacion")?jsonPersona.getString("fechaCreacion"):getToday4Insert())
							.append("'); \n");
					/*
					 sbQuery = new StringBuilder(QueryXe.sqlInsPersona).append("(").append(idPersona)
							.append(",null,null,null,null,null,null,null,null,null,null,null,null,null,null,")
							.append(idEstatusInscripcion.equals("3")?"2":idEstatusInscripcion) //Si es 3, manda msg de contexto: Se cambió una propiedad en un contexto de Publicación 
							.append(",").append( (jsonPersona.has("textoClasificacion")? "'"+jsonPersona.getString("textoClasificacion")+"'":"null") )
							.append(",null,null,null,'")
							.append( (jsonPersona.has("email")? jsonPersona.getString("email"):"") ).append("',")
							.append("null,null,null,null,null,null,true,null,null,null,true,true,null,")
							.append(jsonPersona.has("fechaCreacion")?"'"+jsonPersona.getString("fechaCreacion")+"'":getTodaytoSQL()).append(",")
							.append(jsonPersona.has("fechaModificacion")?"'"+jsonPersona.getString("fechaModificacion")+"'":"null")
							.append(",null,false,")
							.append( (jsonPersona.has("tokenInicio")? "'"+jsonPersona.getString("tokenInicio")+"'":"null") )
							.append(",null")
							.append("); \n");					
					 */
					log4j.debug("<processPersona>Se inserta Persona en BD: " + sbQuery );
					sbProccess.append("Insert-Persona: ").append(sbQuery).append("\n");
					conn.updateDataBaseNoCommit(sbQuery.toString());
					 
					/* 3.b. Inserts de Historico Password */
					sbQuery = new StringBuilder(QueryXe.sqlInsHistPwd).append("(").append(secHistPwd).append(",")
							.append(idPersona).append(",'")
							.append( (jsonPersona.has("password")? jsonPersona.getString("password"):PWD_123456789) )
							.append("','").append(jsonPersona.has("fechaCreacion")?jsonPersona.getString("fechaCreacion"):getToday4Insert())
							.append("');\n");
					log4j.debug("<processPersona>Se inserta Historico PWD en BD: " + sbQuery );
					sbProccess.append("Historico PWD: ").append(sbQuery).append("\n");
					conn.updateDataBaseNoCommit(sbQuery.toString());
					 
					/* 3.c. Inserts de Relacion Empresa Persona */
					sbQuery = new StringBuilder(QueryXe.sqlInsRelEmpPers).append("(")
							.append(secRelEmpPer).append(",").append(jsonPersona.getString("role"))
							.append(",").append(jsonPersona.getString("idEmpresa")).append(",").append(idPersona)
							.append(",'").append(jsonPersona.has("fechaCreacion")?jsonPersona.getString("fechaCreacion"):getToday4Insert())
							.append("',false,true,null")
							.append(");\n");
					log4j.debug("<processPersona> Se inserta Rel_empresa_persona en BD: " + sbQuery );
					sbProccess.append("Rel_empresa_persona: ").append(sbQuery).append("\n");
					conn.updateDataBaseNoCommit(sbQuery.toString());
					
					idPersonasClause.append(",").append(idPersona);
					
					secHistPwd++;
					secRelEmpPer++;
					idPersonaFinal = idPersona;
					log4j.debug("secHistPwd: "+ secHistPwd + " secRelEmpPer "+ secRelEmpPer );
					conn.commitQuery();
					log4j.debug("<processPersona> Fin de proceso.");
					sbUpdateFechas.append("UPDATE persona SET ")
					
					.append("fecha_modificacion = ").append(jsonPersona.has("fechaModificacion")?"to_timestamp('"+jsonPersona.getString("fechaModificacion")+"', 'YYYY-MM-dd hh24:mi:ss.ms')":"null").append(" ")
					.append(", fecha_debe_publicar = ").append(jsonPersona.has("fechaModificacion")?"to_timestamp('"+jsonPersona.getString("fechaModificacion")+"', 'YYYY-MM-dd hh24:mi:ss.ms')":"null").append(" ")
					.append(" WHERE id_persona = " ).append(idPersona).append("; \n");
					if(bSecPersonaDB){
						secPersona++;
					}
				}else{
					log4j.debug("<procesaPersona> Error: el nombre de archivo no es el esperado [read-IDPERSONA]: " + archivoPersona );
					sbProccess.append("el nombre de archivo no es el esperado [read-IDPERSONA]").append("\n");
				}
			}catch (Exception e){
				log4j.fatal("<procesaPersona> Fatal: ", e);
				sbProccess.append("<Exception> error al procesar: ").append(e.getMessage()).append("\n");
			}
		}
		sbProccess.append("\n+++++++  FIN ").append(archivoPersona).append(" +++++++ \n");
		return sbProccess;
	}
	
	
	private static String deleteClause(){
		StringBuilder sbDel = new StringBuilder("/*  deletes requeridos para borrar persona de sistema * /").append("\n");
		
		sbDel.append(" DELETE FROM contacto_telefono WHERE id_contacto IN (SELECT id_contacto FROM contacto WHERE id_persona IN (<IdPersonas>) );").append("\n");
		sbDel.append(" DELETE FROM contenido WHERE id_persona IN (<IdPersonas>);").append("\n");
		sbDel.append(" DELETE FROM habilidad WHERE id_persona IN (<IdPersonas>);").append("\n");
		sbDel.append(" DELETE FROM contacto WHERE id_persona IN (<IdPersonas>);").append("\n");
		sbDel.append(" DELETE FROM experiencia_laboral WHERE id_persona IN (<IdPersonas>);").append("\n");
		sbDel.append(" DELETE FROM escolaridad WHERE id_persona IN (<IdPersonas>);").append("\n");
		sbDel.append(" DELETE FROM domicilio WHERE id_persona IN (<IdPersonas>);").append("\n");
		sbDel.append(" DELETE FROM persona_pais WHERE id_persona IN (<IdPersonas>);").append("\n\n");		
		
		sbDel.append(" DELETE FROM relacion_empresa_persona WHERE id_persona IN (<IdPersonas>);").append("\n");		
		sbDel.append(" DELETE FROM historico_password WHERE id_persona IN (<IdPersonas>);").append("\n");
		sbDel.append(" DELETE FROM persona WHERE id_persona IN (<IdPersonas>);").append("\n");
		
		sbDel.append(" /* <IdPersonas> => 0").append(idPersonasClause).append(" ) <= */");
		
		return sbDel.toString();
	}
	
	
	/* *********************  SECUENCIAS DESDE BASE DE DATOS   ***************************** */
	/**
	 * Obtiene un mapa de secuencias para los Inserts Correspondientes
	 * @return
	 */
	private static Map<String, Long> getMapaSecuencias() throws Exception{
		Map<String, Long> mapa = new HashMap<String, Long>();
		
		if(conn==null){
			conn = new ConnectionBD(WEB_RESOURCE); 
			conn.getDbConn();
		}
		mapa.put("SEQ_PERSONA", getLasValue("SEQ_PERSONA"));
		mapa.put("SEQ_HISTORICO_PASSWORD", getLasValue("SEQ_HISTORICO_PASSWORD"));
		mapa.put("SEQ_RELACION_EMPRESA_PERSONA", getLasValue("SEQ_RELACION_EMPRESA_PERSONA"));
		mapa.put("SEQ_AREA_PERSONA", getLasValue("SEQ_AREA_PERSONA"));
		
		//conn.closeConnection();
		return mapa;
	}
	
	/**
	 * Obtiene el entero del valor actual de la secuencia en parametro
	 * @param sequenceName
	 * @return
	 */
	private static Long getLasValue(String sequenceName) {
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
	
//	/**
//	 * Regresa el valor a insertar de fecha en Timestamp
//	 * @param fecha
//	 * @return
//	 */
//	private static String getInsertFecha(String fecha){
//		if(fecha==null){
//			return "null";
//		}else{
//			//to_timestamp('2016-07-12 17:00:50.398', 'YYYY-MM-dd hh24:mi:ss.ms')
//			return "to_timestamp('"+fecha+"', 'YYYY-MM-dd hh24:mi:ss.ms')";
//		}
//	}
	
	/**
	 * Metodo para reemplazar en caso de no usar los archivos de clase padre PersonaFromJson.getListaArchivosPersona
	 * @return
	 */
	protected static List<String> getLsFiles(){
//		List<String> lsFiles = new java.util.ArrayList<String>();
//		lsFiles.add("read-5.json");
//		return lsFiles;
		return getListaArchivosPersona();
	}
			
}
