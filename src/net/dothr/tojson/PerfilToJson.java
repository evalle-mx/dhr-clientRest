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

public class PerfilToJson extends MainAppTester {
	
	static Logger log4j = Logger.getLogger( PerfilToJson.class );
	protected static final String PROFILE_JSON_DIR = ConstantesREST.JSON_HOME+"PersonaRecreate/profile/";
	private static final String IDCONF = ConstantesREST.IDEMPRESA_CONF;
	
	protected static final String REPORTE_PATH = ConstantesREST.JSON_HOME+"Rep.PerfilToJson.txt";

	
	public static void main(String[] args) {
		StringBuilder sb = multiplePerfilToFile();
		ClientRestUtily.writeStringInFile(REPORTE_PATH, sb.toString(), false);
	}
	
	
	/**
	 * Obtiene el JSON (Sencillo) de perfil de Servicios Rest y crea un archivo .json con la informacion
	 * @param idPersona
	 * @throws Exception
	 */
	public static boolean perfilToFile(int idPerfil, Long nuevoIdPerfil) throws Exception {
		log4j.debug("<perfilToFile>");
		boolean reOrdenamiento = true, resp = false;
		if(nuevoIdPerfil == null){
			nuevoIdPerfil = new Long(idPerfil);
			reOrdenamiento = false;
		}
		
		String filePath = PROFILE_JSON_DIR+"read-"+nuevoIdPerfil+".json";
		log4j.debug("Perfil "+idPerfil+" se respaldara en .json ...  ");
		
		JSONObject jsonReq = new JSONObject();
		jsonReq.put(P_JSON_IDCONF, IDCONF);
		jsonReq.put(P_JSON_PERFIL, String.valueOf(idPerfil) );
		
		String stPerfilApp = getJsonFromService(jsonReq.toString(), URI_PROFILE.concat(URI_READ) );
		log4j.debug("> Perfil: \n"+stPerfilApp);
		
		log4j.debug("Convirtiendo a JSONObject (Validá estructura Json Respuesta correcta");
		JSONArray jsArrResponse = new JSONArray(stPerfilApp);
		JSONObject jsonPerfil = jsArrResponse.getJSONObject(0);
			
		if( jsonPerfil.has("idPerfil") ){//stPersonaApp.indexOf("email")!=-1){
			jsonPerfil.put("idPerfil", String.valueOf(nuevoIdPerfil) );
			if(reOrdenamiento){ jsonPerfil.put("idPerfilAnt", String.valueOf(idPerfil) ); }
			log4j.debug("Creando el archivo de respaldo: \n"+filePath );
			jsArrResponse = new JSONArray();
			jsArrResponse.put(jsonPerfil);
			ClientRestUtily.writeStringInFile(filePath, jsArrResponse.toString(), false);
			resp = true;
		}else{
			log4j.debug("No existe Perfil id:"+idPerfil);
			if( jsonPerfil.has("code") && jsonPerfil.has("type") && jsonPerfil.has("message")){
				log4j.error(jsonPerfil.get("message"));
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
	public static StringBuilder multiplePerfilToFile() {
		log4j.info("<multiplePerfilToFile>");
		StringBuilder sbProcess = new StringBuilder(">>> multiplePerfilToFile <<< \n");
		List<String> lsPerfil = listaPerfiles();
		
		Iterator<String> itPerfil = lsPerfil.iterator();
		
		boolean reOrdenar = true;
		int nPerf = 0;
		Long idPerfil = null;
		sbProcess.append("lsPerfil: ").append(lsPerfil).append("\n")
		.append("reOrdenar: ").append(reOrdenar).append("\n\n ************************************");
		try{
			if(reOrdenar){
				// PROCEDIMIENTO PARA GENERAR NUEVO ORDENAMIENTO SIN SALTOS: 
				Long cons = new Long(1);
				while(itPerfil.hasNext()){
					idPerfil = Long.parseLong(itPerfil.next());
					sbProcess.append("idPerfil: ").append(idPerfil).append(":\n").append(perfilToFile(idPerfil.intValue(),cons) ).append("\n");
					cons++;nPerf++;
				}
			}else {
				// PROCEDIMIENTO PARA COPIAR EN REPLICA	(mismos ID's)
				while(itPerfil.hasNext()){
					idPerfil = Long.parseLong(itPerfil.next());
					//positionToFile(idPosicion.intValue(),null);
					sbProcess.append("idPerfil: ").append(idPerfil).append(":\n").append(perfilToFile(idPerfil.intValue(),null) ).append("\n");
					nPerf++;
				}
			}
			log4j.info("SE respaldaron " + nPerf + " Perfiles de la Aplicacion");
			sbProcess.append("SE respaldaron ").append(nPerf).append(" Perfiles de la Aplicacion");
		}catch (Exception e){
			log4j.fatal("<Exception> Se tuvo un error: ", e);
			sbProcess.append("<Exception> Se tuvo un error: ").append(e.getMessage());
		}
		
		return sbProcess;
	}
	
	
	
	/** 
	 * Genera la lista de ID's para posición en App
	 * [ select * from perfil where id_tipo_perfil !=4 order by id_perfil;  ]
	 * @return
	 */
	private static List<String> listaPerfiles(){
		List<String> lsPerfil = new ArrayList<String>();
		
		lsPerfil.add("5");
		lsPerfil.add("6");
		lsPerfil.add("7");
		lsPerfil.add("8");
		lsPerfil.add("10");
		lsPerfil.add("13");
		
		return lsPerfil;
	}
}
