package net.mock;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import net.dothr.ClientJersey;
import net.utils.ClientRestUtily;

public class AbstractImporter {
	
	protected static final String PATH_BACKUP = "/home/netto/Documents/Selex/TMP/";
	protected static final String WEB_RESOURCE = "http://localhost:8080/AppTransactionalStructured";
	
	
	protected static final String IDCONF = "1";
	
	//Methods
	public final static String URI_URICODES="/uricodes";
	public final static String URI_UPDATE="/update";
	public final static String URI_CREATE="/create";
	public final static String URI_READ="/read";
	public final static String URI_DELETE="/delete";
	public final static String URI_GET="/get";
	public final static String URI_DATA_CONF="/dataconf";
	public final static String URI_EXISTS="/exists";
	public final static String URI_INITIAL="/initial";
	
	//EndPoints
	protected final static String URI_VACANCY="/module/vacancy";
	protected final static String URI_ROL = "/admin/rol";
	protected final static String URI_MODRSC="/module/modeloRsc";
	protected final static String URI_MODRSCPOS="/module/modeloRscPos";
	protected final static String URI_MODRSCPOS_FASE = "/module/modeloRscPosFase";
	protected final static String URI_MONITORPOS = "/module/monitor";
	
	static Logger log4j = Logger.getLogger( AbstractImporter.class);

	
	/**
	 * Metodo comun para utilizar servicio
	 * @param outJson i.e. {}
	 * @param uriService  i.e. / 
	 * @return
	 * @throws Exception
	 */
	protected static String getJsonFromService(String inputJson, String uriService) throws Exception{
//		System.out.println("\n **************************************************************************************************************");
		String jsonResponse= null;
		Response response = null;
		log4j.debug("\n Probando ["+ uriService +"] > " + WEB_RESOURCE.concat(uriService) +"\n" );
		
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
//		log4j.debug("\n==> Json: "+inputJson + "\n==> URI: "+uriService + "\n<== response:\n " + jsonResponse +"\n");
		return jsonResponse;
	}
	
	
	/* ************************************************************************************************ */
	/* *************************************    UTILERIAS   ******************************************* */
	/* ************************************************************************************************ */
	
	/**
	 * Devuelve una cadena con el JSON formateado 
	 * @param stJson
	 * @return
	 * @throws Exception
	 */
	protected static String formatJson(String stJson) throws Exception {
		return ClientRestUtily.formtJsonByGoogle(stJson);
	}
	/**
	 * Escribe un archivo plano (Sobreescribe si existe)
	 * @param filePath
	 * @param texto
	 * @throws Exception
	 */
	protected static void writeFile(String filePath, String texto) throws Exception {
		ClientRestUtily.writeFile(filePath, texto, false);
	}
	
	/**
	 * Obtiene un objeto JSON desde un archivo plano, si el archivo contiene un arreglo, 
	 * regresa el primer elemento
	 * @param rutaArchivoJson
	 * @return
	 * @throws Exception
	 */
	protected static JSONObject getFromFile(String rutaArchivoJson) throws Exception {
		String stJson = ClientRestUtily.getBuilderNoTabsFile(rutaArchivoJson, null).toString();
		JSONObject json = null;
		if(stJson.startsWith("{")) {
			json = new JSONObject(stJson);
		}
		else if(stJson.startsWith("[")) {
			JSONArray jsArr = new JSONArray(stJson);
			json = jsArr.getJSONObject(0);
		}
		else {
			log4j.error("NO es cadena valida ", new NullPointerException());
			throw new NullPointerException("CADENA INVALIDA");
		}
		return json;
	}
}
