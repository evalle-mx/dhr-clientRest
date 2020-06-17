package netto.dhr;

import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

import oneclick.twise.util.AppUtily;
import oneclick.twise.util.Constante;
import oneclick.twise.wserv.ClientJersey;

public abstract class DotHRTester {
	
	private static Logger log4j = Logger.getLogger( DotHRTester.class );
	private static JSONArray jsArrResp;
	private static JSONObject jsonResp;
	
	protected static final String IDCONF = "1";
	protected final static String URI_TEST = "/module/test";
	
	protected static String WEB_RESOURCE = "http://localhost:8089/DotHRApp";
	
	protected static Gson gson; //Convertidor de objetos en Json
	protected static final String CONTENT_TYPE = Constante.CONTENT_TYPE;

	//CRUD
	public final static String M_GET="/get";
	public final static String M_CREATE="/create";
	public final static String M_READ="/read";
	public final static String M_UPDATE="/update";
	public final static String M_DELETE="/delete";
	
	/**
	 * Metodo PRINCIPAL para Consumir servicios REST
	 * @param outJson i.e. {}
	 * @param uriService  i.e. / 
	 * @return
	 * @throws Exception
	 */
	protected static String getJsonFromService(String inputJson, String uriService) throws ConnectException, Exception{
		String SEPARADOR = "\n **************************************************************************************************************";
		System.out.println(SEPARADOR);
		String jsonResponse= null;
		Response response = null;
		
		log4j.debug("\n Probando " + WEB_RESOURCE.concat(uriService) +"\n" );
		
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
		log4j.debug("\n ==> Json: "+inputJson + "\n ==> URI: "+uriService + "\n <== response:\n " + jsonResponse +"\n");
		return jsonResponse;
	}
	
	/**
	 * Genera un StringBuilder e imprime la cadena obtenida separada por elementos JSON
	 * @param stJson
	 */
	protected static void printJson(String stJson){
		StringBuilder sb = new StringBuilder("\n");
		if(stJson!=null && (stJson.startsWith("[") || stJson.startsWith("{"))){
			try{
				if(stJson.startsWith("[")){//JSONArray
					jsArrResp = new JSONArray(stJson);
					sb.append("[\n");
					for(int x=0; x<jsArrResp.length(); x++){
						jsonResp = jsArrResp.getJSONObject(x);
						sb.append(jsonResp.toString()).append(x==jsArrResp.length()-1?"":",").append("\n");
					}
					sb.append("]");
					sb.append("\n# de elementos en Arreglo: ").append(jsArrResp.length()).append("\n");
				}else{
					jsonResp = new JSONObject(stJson);
					sb.append("JSONObject:\n").append(jsonResp.toString()).append("\n");
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
		String stPersona = AppUtily.getJsonFile(fileName, fileDir);
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
	 * Lista los archivos incluidos en el archivo lista en cada carpeta <b>listaRead.txt</b>
	 * <br><i> Si no existe llega vacio</i>
	 * @return
	 */
	protected static List<String> listFromFile(String path, String fileName){		
		//return ClientRestUtily.readListaTxt(CV_JSON_DIR+"listaRead.txt");
		return AppUtily.readListaTxt(path+fileName);
	}
	
	
	/**
	 * Este Metodo es EXCLUSIVO Para probar la conectividad con DotHRAPP en Post
	 * @return
	 * @throws Exception
	 */
	public static String test() throws Exception {
		JSONObject json = new JSONObject();
		json.put("data", "1");
				
		String jSon = getJsonFromService(json.toString(), "/common");
		return jSon;
	}
	
	/**
	 * Metodo para probar conectividad con APP y el AdapterRest solicitado, 
	 * pues todos heredan ruta de servicio "ping" desde ErrorMessageAdapterRest
	 * @param uriRoot   /module/service
	 */
	public static String ping(String uriRoot) throws Exception {
		if(uriRoot==null || uriRoot.trim().equals("")){
			uriRoot = URI_TEST ;   // /module/tracking 
		}
		String stjSon = getJsonFromService("{}", uriRoot+"/ping");
		return stjSon;
	}
	/** Metodo para probar aplicacion por defecto */
	public static String ping() throws Exception {
		return ping(null);
	}

}
