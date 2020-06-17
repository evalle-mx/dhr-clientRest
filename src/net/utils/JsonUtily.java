package net.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class JsonUtily {
		
	@SuppressWarnings("rawtypes")
	public static void jsonFromMap(Map mapa){
		System.out.println("jsonFromMap");
		if(mapa!=null){
			System.out.println("Mapa(".concat(String.valueOf(mapa.size())).concat(")").concat(mapa.toString()) );
			//imprimeMapa(mapa);
			System.out.println();
			JSONObject jsono = new JSONObject(mapa);//al crear, se pierde cualquier orden
			System.out.println("jsono:");
			System.out.println(jsono.toString());
		}
	}
	
	/**
	 * Convierte un JSONObject a un java.util.Map, con esto se evita excepcion 
	 * al buscar un objeto que no exista en el Json
	 * @param json
	 * @return
	 */
	public static Map<String, Object> mapFromJson(JSONObject json){
		String[] nombres = JSONObject.getNames(json);
//		System.out.println("total de valores en el Json:" + nombres.length + "\n");
		Map<String, Object> mapa = new TreeMap<String, Object>();
				
		for(String key : nombres){
			try {
				Object value = json.get(key);
				mapa.put(key, value);
//				System.out.println(key + ": "+value);
			} catch (JSONException e) {
				System.err.println("error al obtener:".concat(key));
				e.printStackTrace();
			}
		}
			
		return mapa;
	}

	/**
	 * Obtiene Cadena dentro de un Json, debe recibir un json con estructura {"key":valor, "key2":valor2}
	 * <ul>
	 * <li>En caso de no existir la llave, regresa null</li>
	 * <li>En caso de recibir un arreglo o tener algun error,
	 * reporta en log la excepcion interna y regresa null</li>
	 * <li>En caso de que el objeto sea encontrado, pero no sea convertible a cadena, reporta en log y regresa null</li>
	 * </ul>
	 * @param jsonString
	 * @param key
	 * @return
	 */
	public static String getStringInJson(String jsonString, String key){
		String cadena = null;
		Object objeto = getObjInJson(jsonString, key);
		if(objeto!=null){
			try{
				cadena = String.valueOf(objeto);
			}catch (Exception e) {
				System.err.println("Se obtuvo Objeto, pero este no se puede convertir en cadena");
				e.printStackTrace();
			}
		}
		
		return cadena; 
	}
	/**
	 * Obtiene Objeto (Object) dentro de un Json, debe recibir un Json con estructura {"key":valor, "key2":valor2}
	 * <ul><li>En caso de no existir la llave, regresa null</li>
	 * <li>En caso de recibir un arreglo o tener algun error,
	 * reporta en log la excepcion interna y regresa null</li></ul>
	 * @param jsonString
	 * @param key
	 * @return
	 */
	public static Object getObjInJson(String jsonString, String key){
		Object objeto = null;
		if(null!=jsonString && null!=key){
			try {
				JSONObject json = new JSONObject(jsonString);
				objeto = json.get(key);
			} catch (JSONException e) {
//				System.err.println("Error al Convertir datos en Json");
//				
			}
		}
		return objeto;
	}
	
	
	
	public static Object getJsonObjFromString(String json){
		Object jsonObject = null;
		if(null !=json && !json.trim().equals("")){
			System.out.println("json: ".concat(json));
			try {
				if(json.startsWith("{")){ 
					System.out.println("es JSONObject");
					JSONObject jsObj = new JSONObject(json);
					return jsObj;
				}else if(json.startsWith("[")){
					System.out.println("es JSONArray");
					JSONArray jsArr = new JSONArray(json);
					return jsArr;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return jsonObject;
	}
	
	
	public static void main(String[] args) {
//		//test_jsonFromMap
		//test_mapFromJson();
		//test_getObjInJson();
		/*
		String jsFile = "Tce/experienciasLaborales";  //"Tce/experiencia-41"; //"phoneJson/sanyo-zio";
		String json = getJsonStringFromFile(jsFile);  //sanyo-zio");
		Object obj = getJsonObjFromString(json);
		System.out.println("\n\n objeto json: \n");
		System.out.println(obj); //*/
		
		test_ResponseArea();
	}
	
	protected static void test_getObjInJson(){
		String jsonStOk = "{\"code\":\"005\",\"type\":\"I\",\"message\":\"Fue modificado satisfactoriamente \"}";
		String jsonStFail = "{\"code\":\"002\",\"type\":\"F\",\"message\":\"La construcci�n del message no es correcta, favor de verificar la especificaci�n correspondiente\"}";
		String jsonStDetail = "[ {\"email\":\"mimail@pati@to.com\",\"code\":\"006\",\"type\":\"E\",\"message\":\" Pattern(email)\"}, {\"anioNacimiento\":\"dhhdfhdfhfdhdf\",\"code\":\"006\",\"type\":\"E\",\"message\":\"Range [1927,1995]\"}, {\"sexo\":\"5\",\"code\":\"006\",\"type\":\"E\",\"message\":\"ValueSet(0,1)\"}, {\"numeroDependientesEconomicos\":\"holatyuu\",\"code\":\"006\",\"type\":\"E\",\"message\":\" Restriction(numbers)\"} ]";
		
		//1.jsonStOk debe mostrar I (type = I)
		String type = getStringInJson(jsonStOk, "type");  //(String) getObjInJson(jsonStOk, "type");
		System.out.println("jsonStOk: " + (type!=null?type:"type es null") );
		
		//2.jsonStFail debe mostrar F (type = F)
		type = (String) getObjInJson(jsonStFail, "type");
		System.out.println("jsonStFail: " + (type!=null?type:"type es null") );
		
		//2.jsonStFail debe mostrar null, porque genera excepcion al intentar convertir arreglo en Json (type = null)
		type = getStringInJson(jsonStDetail, "type");
		System.out.println("jsonStFail: " + (type!=null?type:"type es null") );
		
	}
//	
//	/**
//	 * Muestra como obtener un Mapa a partir de un Json, el cual se genera y demuestra tambien que
//	 *  los valores que sean null no son almacenados en el JSONObject
//	 * 
//	 */
//	protected static void test_mapFromJson(){
//		//el siguiente objeto se declara como null o con valor para probar que no se agrega al json siendo null
//		String nuleable = "noNUll";
//		try{
//			JSONObject json = new JSONObject();
//			json.put("idPersona", "147");
//			json.put("nombre", "paquito");
//			json.put("apellidoPaterno", "Perez");
//			json.put("apellidoMaterno", "Gomez");
//			json.put("correo", "PerezG@mail.com");
//			json.put("sexo", new Long(0));
//			json.put("fechaNacimiento", "01/02/03");
//			json.put("idEstatusInscripcion", "1");
//			json.put("urlImgPerfil", "http://localhost/imagen/uno.jpg");
//			json.put("idContenidoImgPerfil", "987");
//			json.put("nuleable", nuleable);
//			json.put("nuleable2", nuleable);
//			
//			Map mapa = mapFromJson(json);
//			
//			System.out.println("\n>>Tama�o del mapa: "+mapa.size());	
//			System.out.println(mapa.toString());
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	
	protected static void test_jsonFromMap(){
		//Map<String, Object> mapaObj = new java.util.HashMap<String, Object>();
		Map<String, Object> mapaObj = new java.util.TreeMap<String, Object>();

		mapaObj.put("Cadena", "Aania");
		mapaObj.put("Long", new Long(5));
		mapaObj.put("Cadena2", "Acathan");
		mapaObj.put("Booleano", false);
		mapaObj.put("Fecha", new java.util.Date());
		
		jsonFromMap(mapaObj);
	}
	
	protected static void test_ResponseArea(){
		JSONArray jarrResponse = new JSONArray();
		try{
			for(int p=0;p<5;p++){
				JSONObject joPadre = new JSONObject();
				joPadre.put("id", (p+1) );
				joPadre.put("nombre", "padre-"+(p+1) );
				JSONArray jarrHijos = new JSONArray();
				for(int h=0;h<2;h++){
					JSONObject joHijo = new JSONObject();
					joHijo.put("id", (p+1)+""+(h+1) );
					joHijo.put("nombre", "hijo-"+(p+1)+(h+1) );
					jarrHijos.put(joHijo);
				}
				System.out.println("hijos?"+jarrHijos.length());
				joPadre.put("hijos", jarrHijos);
				jarrResponse.put(joPadre);
			}
		}catch (JSONException e) {
			e.printStackTrace();
		}
		
		System.out.println(jarrResponse);
	}
	
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected static void imprimeMapa(Map mapa){
		Iterator<Map.Entry> i = mapa.entrySet().iterator(); 
		while(i.hasNext()){
		    String key = (String)i.next().getKey();
		    System.out.println(key+", "+mapa.get(key));
		}
	}
	
	
	/**
	 * Convierte un arreglo de Json (De igual estructura) en un texto estilo matriz 
	 * con el nombre de Parametro en el encabezado
	 * @param jsArr
	 * @return
	 * @throws Exception
	 */
	protected static StringBuilder convertJsonToText(JSONArray jsArr) throws Exception {
		StringBuilder sbFile = new StringBuilder();
		
		if(jsArr!=null && jsArr.length()>0){
			JSONObject jsonSys = jsArr.getJSONObject(0); /* se obtiene para extraer nombres de campos*/
			
			String[] names = JSONObject.getNames(jsonSys);
			
			//System.out.println("names: " + names );
			for(int j=0; j<names.length;j++){
				sbFile.append(names[j]).append( (j==names.length-1?"\n":",") );
			}
			for(int x=0; x<jsArr.length();x++){
				jsonSys = jsArr.getJSONObject(x);
				Object objValue = null;
				for(int j=0; j<names.length;j++){
					if(jsonSys.has(names[j])){ //Valida si existe el parametro en el Json
						objValue = jsonSys.get(names[j]);
					}
					//System.out.println(objValue);
					sbFile.append(objValue).append( (j==names.length-1?"":",") );
				}
				sbFile.append("\n");
			}
		}
		return sbFile;
	}
	
	/**
	 * Realiza un formateo para identar una Cadena Json
	 * @param jsonString
	 * @return
	 */
	public static String formtJsonByGoogle(String jsonString){
		JsonParser parser = new JsonParser();
		JsonElement el = parser.parse(jsonString);
		
		Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
		jsonString = gson.toJson(el);
		
		return jsonString;
	}
}

