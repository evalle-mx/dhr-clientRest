package net.dothr.temp;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.dothr.MainAppTester;
import net.dothr.tojson.DBdataToJson;
import net.utils.ClientRestUtily;
import net.utils.ConnectionBD;
import net.utils.ConstantesREST;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Elabora los Json de Control propios de Transactional Mock
 * @author dothr
 *
 */
public class TransMockRecreate extends MainAppTester {
	
	static Logger log4j = Logger.getLogger( TransMockRecreate.class );
	
	private static ConnectionBD connect;
	
	private static final String PERSONA_JSON_DIR = ConstantesREST.JSON_HOME+"module/curriculumManagement/";
	
	private static final String USERDATA_JSON_DIR = ConstantesREST.JSON_HOME+"module/curriculumManagement/";
	private static final String USERDATA_FILENAME = "usersdata.json";
	private static final String URICODE_JSON_DIR = ConstantesREST.JSON_HOME+"module/curriculumManagement/";
	private static final String URICODE_FILENAME = "uricodes.json";
	
	private static final String REPORTE_PATH = ConstantesREST.JSON_HOME+"Report/";
		
	/**
	 * Genera los archivos necesarios para el ambiente Demo
	 *  (TransactionalMock)
	 * 
	 * @throws Exception
	 */
	public static void setEnviroment() throws Exception {
		StringBuilder mainSb = new StringBuilder("==============  PersonaFullToJson.Report  =============== \n");
		mainSb.append("===  Fecha: ").append(date2String(new Date(), "dd/MM/yy mm:ss"))
			.append("  ===\n==========================================\n");
		log4j.debug("<setEnviroment> Crear archivo de usersdata.json ("+USERDATA_JSON_DIR+USERDATA_FILENAME+")");
		mainSb.append("\n\n I. USERSDATA:\n").append(createUsersDataFile());
		log4j.debug("<setEnviroment> Crear archivo uricodes.json ("+URICODE_JSON_DIR+URICODE_FILENAME+")");
		mainSb.append("\n\n II. URICODES: \n").append(createUriCodesFile());
		log4j.debug("<setEnviroment> Genera el reporte en " + REPORTE_PATH );
		ClientRestUtily.writeStringInFile(REPORTE_PATH+"setEnviromentMock.txt", mainSb.toString(), false);
		
		log4j.debug("<setEnviroment> FIN");
	}
	
	public static void main(String[] args) {
		try{
			setEnviroment();
		}catch (Exception e){
			log4j.fatal("setEnviroment Fail ", e);
		}
	}
	
	/**
	 * Genera archivo de control de usuarios (usersdata) con base a los archivos de Persona existentes en lista
	 * [uso exclusivo de TransactionalMock] 
	 * @return
	 */
	private static StringBuilder createUsersDataFile() {
		log4j.info("<processPersonas>");
		StringBuilder sbUserData = new StringBuilder("***********************  createUsersDataFile *********************** "
				).append("\n")
		.append(" > USERDATA_JSON_DIR=").append(USERDATA_JSON_DIR).append("\n")
		.append(" > PERSONA_JSON_DIR=").append(PERSONA_JSON_DIR).append("\n")
		;
		List<String> lsPersona = lsPersonasRead();
		sbUserData.append(" > lsPersona=").append(lsPersona.toString()).append("\n\n");
		Iterator<String> itPersona = lsPersona.iterator();
		String filePersona, idPersona;
		JSONArray jsUserData = new JSONArray();
		JSONObject jsPersona, jsUser;
		int iUser = 1;
		while(itPersona.hasNext()){
			try{
				filePersona = itPersona.next();
				jsPersona = getJsonObject(filePersona, PERSONA_JSON_DIR);	//jsResp.getJSONObject(0);
				idPersona = jsPersona.getString("idPersona");
				sbUserData.append("#").append(iUser++).append(" >> idPersona ").append(idPersona).append(" a insertar en userData \n");
				
				/* Generar json con datos requeridos de user-System */
				jsUser = new JSONObject();

				jsUser.put("idPersona", idPersona );
				jsUser.put("nombre", jsPersona.has("nombre")?jsPersona.getString("nombre"):"");
				jsUser.put("apellidoPaterno", jsPersona.has("apellidoPaterno")?jsPersona.getString("apellidoPaterno"):"");
				jsUser.put("apellidoMaterno", jsPersona.has("apellidoMaterno")?jsPersona.getString("apellidoMaterno"):"");
				jsUser.put("idEmpresa", jsPersona.has("idEmpresa")?jsPersona.getString("idEmpresa"):IDSELEX);
				jsUser.put("idEmpresaExterno", jsPersona.has("idEmpresa")?jsPersona.getString("idEmpresa"):IDSELEX); //Para metodo Initial solamente
				jsUser.put("idEmpresaConf", jsPersona.has("idEmpresaConf")?jsPersona.getString("idEmpresaConf"):IDCONF_DOTHR);
				jsUser.put("email", jsPersona.getString("email"));
				jsUser.put("role", jsPersona.has("role")?jsPersona.getString("role"):"8");
				jsUser.put("password", jsPersona.has("password")?jsPersona.getString("password"):PWD_123456789 );
				jsUser.put("idEstatusInscripcion", jsPersona.has("idEstatusInscripcion")?jsPersona.getString("idEstatusInscripcion"):"2");
				
				jsUser.put("publicate", (jsPersona.has("textoClasificacion")?"publicateSuccess":"publicateError1") ) ;
				/* Agregar objeto a lista de usuarios sistema */
				jsUserData.put(jsUser);
				sbUserData
					.append("idPersona: ").append(jsUser.getString("idPersona")).append(", ")
					.append("email: ").append(jsUser.getString("email")).append(", ")
					.append("role: ").append(jsUser.getString("role")).append(", ")
					.append("publicate: ").append(jsUser.getString("publicate")).append(", ")
					.append("Se inserto correctamente <<## \n ");
			}catch (Exception e){
				log4j.fatal("<Exception> Fatal al generar archivo "+USERDATA_FILENAME, e);
				sbUserData.append("<Exception> Fatal: ").append(e.getMessage());
			}
		}
			
		ClientRestUtily.writeStringInFile(USERDATA_JSON_DIR+USERDATA_FILENAME, ClientRestUtily.formtJsonByGoogle(jsUserData.toString()), false);
		log4j.debug("<Success> Se genero archivo de Usuarios en "+USERDATA_JSON_DIR+USERDATA_FILENAME);
		sbUserData.append("<Success> SE GENERO ARCHIVO DE USUARIOS EN ").append(USERDATA_JSON_DIR+USERDATA_FILENAME).append("\n");
		
		sbUserData.append(" *********************** FIN DE PROCESOcreateUsersDataFile *********************** \n ");
		return sbUserData;
	}
	
	
	/**
	 * Genera archivo de uricodes necesario para TransactionalMock
	 * @return
	 */
	public static StringBuilder createUriCodesFile(){
		log4j.info("<createUriCodesFile>");
		StringBuilder sbUricodes = new StringBuilder("*********************** createUriCodesFile *********************** \n")
		.append("\n > WEB_RESOURCE=").append(WEB_RESOURCE).append("\n")
		.append(" > URICODE_JSON_DIR=").append(URICODE_JSON_DIR).append("\n")
		;
		JSONArray jsUricodes, jsFile = new JSONArray();
		sbUricodes.append(" \n \n");
		try{
			log4j.debug("<createUriCodesFile> connectando a BD (WEB_RESOURCE = " + WEB_RESOURCE + ")");
			connect = new ConnectionBD(WEB_RESOURCE);
			connect.getDbConn();
			
			sbUricodes.append("==> Obteniendo datos de DBdataToJson.getUriCodePermiso... \n");
			jsUricodes = DBdataToJson.getUriCodePermiso(connect);
			sbUricodes.append("jsUricodes:").append(jsUricodes).append("\n");
			if(jsUricodes!=null && jsUricodes.length()>0){
				JSONObject jsObj = new JSONObject();
				jsObj.put("permiso", jsUricodes);
				
				jsFile.put(jsObj);
			}
			ClientRestUtily.writeStringInFile(URICODE_JSON_DIR+URICODE_FILENAME, ClientRestUtily.formtJsonByGoogle(jsFile.toString()), false);
			log4j.debug("<Success> Se genero correctamente el archivo en " + URICODE_JSON_DIR+URICODE_FILENAME );
			sbUricodes.append("\n<Success> Se genero archivo de uricodes en ").append(URICODE_JSON_DIR+URICODE_FILENAME)
				.append("\n");
			
		}catch(Exception e){
			log4j.fatal("<Exception>Error al generar archivo "+URICODE_FILENAME, e);
			sbUricodes.append("<Exception> Fatal: ").append(e.getMessage());
		}
		finally{
			connect.closeConnection();
		}
		sbUricodes.append("\n *************************** FIN DE PROCESO createUriCodesFile ****************************** \n ");
		return sbUricodes;
	}

	/**
	 * Lista de Archivos read de persona
	 * @return
	 */
	private static List<String> lsPersonasRead(){
		List<String> lsPersonaRead =
				ClientRestUtily.readListaTxt(PERSONA_JSON_DIR+"listaRead.txt");
				//new ArrayList<String>();
		
		return lsPersonaRead;
	}
}
