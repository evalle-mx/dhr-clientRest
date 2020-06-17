package netto;

import oneclick.twise.util.AppUtily;
import oneclick.twise.util.Constante;
import oneclick.twise.wserv.ClientJersey;

//import java.lang.reflect.Type;
import java.net.ConnectException;
import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Collections;
import java.util.Date;
//import java.util.HashMap;
//import java.util.Iterator;
import java.util.List;
//import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

public abstract class AppTester {
	
	private static Logger log4j = Logger.getLogger( AppTester.class );
	private static JSONArray jsArrResp;
	private static JSONObject jsonResp;
	
	protected static final String IDCONF = "1";
	protected final static String URI_TEST = "/module/test";
	
	protected static String WEB_RESOURCE = 
//			"http://127.0.0.1:8080/PrototipoApp"; 
			"http://localhost:8080/KredApp";
//			"http://localhost:8080/TicketingApp";
//			"http://192.168.0.8:8080/TicketingApp";
//			"http://ec2-34-204-5-176.compute-1.amazonaws.com:8090/KredApp"; /* EC2 - DEMO  */
//			"http://localhost:8080/EncuestaApp"; 
			//"http://localhost:8080/AgroTransactional";
	protected static Gson gson; //Para convertir objetos en Json igual que en Transactional
	protected static final String CONTENT_TYPE = Constante.CONTENT_TYPE;

	protected static final String SEPARADOR = "\n **************************************************************************************************************";
	
	
	/* ************************************** */
	//Metodos
	public final static String METHOD_PING="/ping";
	public final static String METHOD_GET="/get";
	public final static String METHOD_CREATE="/create";
	public final static String METHOD_READ="/read";
	public final static String METHOD_UPDATE="/update";	
	public final static String METHOD_DELETE="/delete";
	
	public final static String METHOD_EXISTS="/exists";
	public final static String METHOD_INITIAL="/initial";
	public final static String METHOD_URICODES="/uricodes";
	public final static String METHOD_MENU = "/menu";
	
	public final static String METHOD_QUESTIONARY ="/questionary";
	
	public final static String URI_MULTIPLE_UPDATE="/updmultiple";
	public final static String URI_RELOAD="/reload";
	/* *************************************** */
	
	/**
	 * Metodo PRINCIPAL para Consumir servicios REST
	 * @param outJson i.e. {}
	 * @param uriService  i.e. / 
	 * @return
	 * @throws Exception
	 */
	protected static String getJsonFromService(String inputJson, String uriService) throws ConnectException, Exception{
		System.out.println(SEPARADOR);
		String jsonResponse= null;
		Response response = null;
		/*if(WEB_RESOURCE.equals(ConstantesREST.WEB_TRANSACT_MOCK)){
			log4j.debug("\n\n ?????????  APUNTANDO A AMBIENTE EMULADO MOCK "+ ConstantesREST.WEB_TRANSACT_MOCK +" ?????????????????????\n"); 
		}
		log4j.debug("\n Probando ["+ transUriCode(uriService) +"] > " + WEB_RESOURCE.concat(uriService) +"\n" );//*/
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
	
	
	public static void main(String[] args) {
		try {
			ping(null);
//			demo("/module/encuesta/ping");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void demo(String uriService) throws Exception {
		String stjSon = getJsonFromService("{}", uriService);
		log4j.debug("<demo>:\n"+stjSon);
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
	
	
//	/** Metodo para probar aplicacion por defecto */
//	public static String pingGretting() throws Exception {
//		//return ping(null);
//		
//		String resp = getJsonFromService("{\"name\":\"NEtto\"}", "");
//		return resp;
//	}

	
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
}
