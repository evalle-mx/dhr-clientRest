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

public class PosicionToJson extends MainAppTester {

	static Logger log4j = Logger.getLogger( PosicionToJson.class );
	protected static final String POSITION_JSON_DIR = ConstantesREST.JSON_HOME+"PersonaRecreate/vacancy/";
	protected static final String IDCONF = ConstantesREST.IDEMPRESA_CONF;
	
	protected static final String REPORTE_PATH = ConstantesREST.JSON_HOME+"Rep.PositionsToJson.txt";
	
	public static void main(String[] args) {
		multiplePosicionToFile();
	}
	
	
	/**
	 * Obtiene el JSON (Sencillo) de posicion de Servicios Rest y crea un archivo .json con la informacion
	 * @param idPersona
	 * @throws Exception
	 */
	public static boolean positionToFile(int idPosicion, Long nuevoIdPosicion) throws Exception {
		log4j.debug("<positionToFile>");
		boolean reOrdenamiento = true, resp = false;
		if(nuevoIdPosicion == null){
			nuevoIdPosicion = new Long(idPosicion);
			reOrdenamiento = false;
		}
		
		String filePath = POSITION_JSON_DIR+"read-"+nuevoIdPosicion+".json";
		log4j.debug("Posición "+idPosicion+" se respaldara en .json ...  ");
		
		JSONObject jsonReq = new JSONObject();
		jsonReq.put(P_JSON_IDCONF, IDCONF);
		jsonReq.put(P_JSON_POSICION, String.valueOf(idPosicion) );
		
		String stPosicionApp = getJsonFromService(jsonReq.toString(), URI_VACANCY.concat(URI_READ) );
		log4j.debug("> Posición: \n"+stPosicionApp);
		
		log4j.debug("Convirtiendo a JSONObject (Validá estructura Json Respuesta correcta");
		JSONArray jsArrResponse = new JSONArray(stPosicionApp);
		JSONObject jsonPosicion = jsArrResponse.getJSONObject(0);
			
		if( jsonPosicion.has("idPosicion") ){//stPersonaApp.indexOf("email")!=-1){
			jsonPosicion.put("idPosicion", String.valueOf(nuevoIdPosicion) );
			if(reOrdenamiento){ jsonPosicion.put("idPosicionAnt", String.valueOf(idPosicion) ); }
			log4j.debug("Creando el archivo de respaldo: \n"+filePath );
			jsArrResponse = new JSONArray();
			jsArrResponse.put(jsonPosicion);
			ClientRestUtily.writeStringInFile(filePath, jsArrResponse.toString(), false);
			resp = true;
		}else{
			log4j.debug("No existe Posición id:"+idPosicion);
			if( jsonPosicion.has("code") && jsonPosicion.has("type") && jsonPosicion.has("message")){
				log4j.error(jsonPosicion.get("message"));
			}
			resp = false;
		}
		return resp;
	}
	
	/**
	 * Realiza el procedimiento de guardado multiple
	 * @return
	 * @throws Exception
	 */
	public static StringBuilder multiplePosicionToFile() {
		log4j.info("<multiplePosicionToFile>");
		StringBuilder sbProcess = new StringBuilder(">>> multiplePosicionToFile <<< \n");
		List<String> lsPosicion = listaPosiciones();
		
		Iterator<String> itPosicion = lsPosicion.iterator();
		
		boolean reOrdenar = false;
		int nPosi = 0;
		Long idPosicion = null;
		sbProcess.append("lsPosicion: ").append(lsPosicion).append("\n")
		.append("reOrdenar: ").append(reOrdenar).append("\n\n ************************************");
		try{
			if(reOrdenar){
				// PROCEDIMIENTO PARA GENERAR NUEVO ORDENAMIENTO SIN SALTOS: 
				Long cons = new Long(1);
				while(itPosicion.hasNext()){
					idPosicion = Long.parseLong(itPosicion.next());
					sbProcess.append("idPosicion: ").append(idPosicion).append(":\n").append(positionToFile(idPosicion.intValue(),cons) ).append("\n");
					cons++;nPosi++;
				}
			}else {
				// PROCEDIMIENTO PARA COPIAR EN REPLICA	(mismos ID's)
				while(itPosicion.hasNext()){
					idPosicion = Long.parseLong(itPosicion.next());
					//positionToFile(idPosicion.intValue(),null);
					sbProcess.append("idPosicion: ").append(idPosicion).append(":\n").append(positionToFile(idPosicion.intValue(),null) ).append("\n");
					nPosi++;
				}
			}
			log4j.info("SE respaldaron " + nPosi + " Posiciones de la Aplicacion");
			sbProcess.append("SE procesaron ").append(nPosi).append(" Posiciones de la Aplicacion");
		}catch (Exception e){
			log4j.fatal("<Exception> Se tuvo un error: ", e);
			sbProcess.append("<Exception> Se tuvo un error: ").append(e.getMessage());
		}
		
		
		return sbProcess;
	}
	
	
	
	/** 
	 * Genera la lista de ID's para posición en App
	 * @return
	 */
	private static List<String> listaPosiciones(){
		List<String> lsPosicion = new ArrayList<String>();
		
//		lsPosicion.add("5");
//		lsPosicion.add("6");
//		lsPosicion.add("7");
		lsPosicion.add("8");
		
		return lsPosicion;
	}
}
