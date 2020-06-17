package net.dothr.tojson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import net.dothr.MainAppTester;
import net.utils.ClientRestUtily;
import net.utils.ConstantesREST;

public class PersonaToJson extends MainAppTester {
	
	static Logger log4j = Logger.getLogger( PersonaToJson.class );
	private static final String PERSONA_JSON_DIR = ConstantesREST.JSON_HOME+"module/curriculumManagement/"+"test/";
	private static final String IDCONF =ConstantesREST.IDEMPRESA_CONF;
	
	
	public static void main(String[] args) {
		try {
			personaToFile(2,null);
//			multiplePersonaToFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Obtiene el JSON (Sencillo) de persona de Servicios Rest y crea un archivo .json con la informacion
	 * @param idPersona
	 * @throws Exception
	 */
	public static void personaToFile(int idPersona, Long nuevoIdPersona) throws Exception {
		log4j.debug("<personaToFile>");
		boolean reOrdenamiento = true;
		if(nuevoIdPersona == null){
			nuevoIdPersona = new Long(idPersona);
			reOrdenamiento = false;
		}
		
		String filePath = PERSONA_JSON_DIR+"read-"+nuevoIdPersona+".json";
		log4j.debug("Persona "+idPersona+" se respaldara en .json ...  ");
		
		JSONObject jsonReq = new JSONObject();
		jsonReq.put(P_JSON_IDCONF, IDCONF);
		jsonReq.put(P_JSON_PERSONA, String.valueOf(idPersona) );
		
		String stPersonaApp = getJsonFromService(jsonReq.toString(), URI_CURRICULUM+URI_READ );
		log4j.debug("> Persona: \n"+stPersonaApp);
		
		log4j.debug("Convirtiendo a JSONObject (Validá estructura Json Respuesta correcta");
		JSONArray jsArrResponse = new JSONArray(stPersonaApp);
		JSONObject jsonPersona = jsArrResponse.getJSONObject(0);
			
		if( jsonPersona.has("email") ){//stPersonaApp.indexOf("email")!=-1){
			jsonPersona.put("idPersona", String.valueOf(nuevoIdPersona) );
			if(reOrdenamiento){ jsonPersona.put("idPersonaAnt", String.valueOf(idPersona) ); }
			log4j.debug("Creando el archivo de respaldo: \n"+filePath );
			jsArrResponse = new JSONArray();
			jsArrResponse.put(jsonPersona);
			ClientRestUtily.writeStringInFile(filePath, jsArrResponse.toString(), false);
		}else{
			log4j.debug("No existe Persona id:"+idPersona);
			if( jsonPersona.has("code") && jsonPersona.has("type") && jsonPersona.has("message")){
				log4j.error(jsonPersona.get("message"));
			}
		}
	}
	
	/**
	 * Realiza la iteración del metodo personaToFile (Sencillo) para los id's 
	 * incluidos en una lista para almacenarlos en archivos .json
	 * @throws Exception
	 */
	public static void multiplePersonaToFile() throws Exception{
		boolean reOrdenar = true;
		List<String> lsIdPersona = getListaIdPersona();
		
		Iterator<String> itPersona = lsIdPersona.iterator();
		int nPers = 0;
		Long idPersona = null;
		
		if(reOrdenar){
			// PROCEDIMIENTO PARA GENERAR NUEVO ORDENAMIENTO SIN SALTOS: 
			Long cons = new Long(10); //Valor de consecutivo Inicial en los inserts de HPassword y RelacionPersona		
			while(itPersona.hasNext()){
				idPersona = Long.parseLong(itPersona.next());
				personaToFile(idPersona.intValue(),cons);
				cons++;nPers++;
			}
		}else {
			// PROCEDIMIENTO PARA COPIAR EN REPLICA	(mismos ID's)
			while(itPersona.hasNext()){
				idPersona = Long.parseLong(itPersona.next());
				personaToFile(idPersona.intValue(),null);
				nPers++;
			}
		}
		log4j.info("SE respaldaron " + nPers + " personas de la Aplicacion");
	}
	
	
	
	private static List<String> getListaIdPersona() {
		List<String> lsIdPersona = new ArrayList<String>();
		 lsIdPersona.add("1");	 //octavio.linares@dothr.net
	 	 lsIdPersona.add("2");	 //ernesto.valle@dothr.net
		
		return lsIdPersona;
	}

}
