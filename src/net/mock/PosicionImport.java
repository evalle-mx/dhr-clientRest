package net.mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import net.utils.ClientRestUtily;
/**
 * Clase desarrollada para OBtener Json de Posicion de AppTransactional para generar archivos planos, asi como el get.json para el proyecto MOCK
 * @author netto
 *
 */
public class PosicionImport extends AbstractImporter {

	static Logger log4j = Logger.getLogger( PosicionImport.class );
	
	protected static final String BACKUP_JSON_DIR = PATH_BACKUP+"Vacancy/";
	private static final String NOM_POS_BACKUP = "ImportPosicionMock.txt";
	
	private final static String ID_2Backup = "5,6,7,8,9,11,12,13,14,15,16,17";
	
	private static Map<String, JSONObject> mapRol = null;
	
	//Auxiliares
	private static JSONArray jsOut, jsModRsc, jsMonitor, jsGet;
	private static JSONObject jsonFullPosicion, jsonAux;
	private static StringBuilder sb;
	
	private static String atributoModRscPos = "modelosRsc", atributoMonitor = "monitores";
	
	
	private static List<String> lsJsonFile;
	
	
	public static void main(String[] args) {
		try {
			
			if(ClientRestUtily.createDirIfNotExist(PATH_BACKUP, false)) {
				log4j.debug("INICIANDO PROCESO");
				processPosicion();
				log4j.debug("leyendo los jsons generados para obtener GET");
				generaGetJson();
				System.out.println("PROCESO TERMINADO");
				writeFile(BACKUP_JSON_DIR+NOM_POS_BACKUP, sb.toString());
			}
			else {
				log4j.fatal("NO EXISTE RUTA DE TRABAJO: " +PATH_BACKUP );
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
		
	protected static StringBuilder processPosicion() throws Exception {
		sb = new StringBuilder(">>>>> INICIO processPosicion :: \n")
		.append(">>> WEB_RESOURCE=").append(WEB_RESOURCE).append("\n")
		.append(">>> BACKUP_JSON_DIR=").append(BACKUP_JSON_DIR).append("\n");
		
		String[] ids = ID_2Backup.split(",");
		sb.append(">>>> [Se respaldan ").append(ids.length).append(" Posiciones en sistema] <<<<<< \n" );
		initMapaRol();//INICIANDO MAPA DE ROLES
		
		String stResponse, idPosicion, idPersona, fileName;
		
		// Validar si existe repositorio
		if(ClientRestUtily.createDirIfNotExist(BACKUP_JSON_DIR, true)) {
			lsJsonFile = new ArrayList<String>();
			/* Se ITERAN LOS ID's DE POSICION */
			for(int x=0; x<ids.length;x++){
				idPosicion = ids[x];
				sb.append("\n******************************* \n Procesando POSICIÓN Id: ").append(idPosicion).append("\n");
				//1. VACANCY.R (para validar si existe y obtener datos principales) 
				sb.append("1. Buscando Información de Posición ").append(idPosicion).append("\n");
				stResponse = readPosicion(idPosicion);
				sb.append("\t <=="+stResponse).append("\n");
				jsOut = new JSONArray(stResponse);
				jsonFullPosicion = jsOut.getJSONObject(0);
				
				if(jsonFullPosicion.has("idPerfil") && jsonFullPosicion.has("idPersona")){
					sb.append("2.Existe posición, se buscan modelos relacionados [").append(atributoModRscPos).append("] \n");
					idPersona = jsonFullPosicion.getString("idPersona");
					//2. Modelos Rsc: MODELRSCPOS.G 
					stResponse = getModRscPos(idPosicion, idPersona);
					sb.append("\t <=="+stResponse).append("\n");
					jsModRsc = new JSONArray(stResponse);				
					if(jsModRsc.length()>0){
						jsonAux = jsModRsc.getJSONObject(0);
						if(jsonAux.has("idModeloRscPos")){						
							jsonFullPosicion.put(atributoModRscPos, jsModRsc);
							sb.append("3.La posicion tiene ").append(jsModRsc.length()).append(" Modelos (participantes) Asignados, se solicitan monitores [")
							.append(atributoMonitor).append("] \n");
							
							//3.Monitores: MONITOR.G
							stResponse = getMonitoresPos(idPosicion);
							sb.append("\t <=="+stResponse).append("\n");
							jsMonitor = new JSONArray(stResponse);
							if(jsMonitor.length()>0){
								jsonAux = jsMonitor.getJSONObject(0);
								if(jsonAux.has("idModeloRscPos") && jsonAux.has("idPersona")){
									setRol2Monitors();
									jsonFullPosicion.put(atributoMonitor, jsMonitor);								
								}else{
									sb.append("Respuesta de error \n");
								}
							}else{
								sb.append("No tiene asignado ningún monitor \n");
							}
						}
						else{
							//[{"code":"003","type":"W","message":"No existen datos registrados, verifique la información proporcionada"}]
							sb.append("\t Posicion:").append(idPosicion).append(" no Tiene Modelos RSC asignados \n");
						}
					}else{
						sb.append("\t Respuesta de ModeloRSCPos-get invalida \n");
					}
					jsOut = new JSONArray();
					jsOut.put(jsonFullPosicion);
					fileName = "read-"+idPosicion+".json";
					sb.append("4. Se escribe archivo json [fileName] ").append(fileName);
					writeFile(BACKUP_JSON_DIR+fileName, formatJson(jsOut.toString()));
					lsJsonFile.add(fileName);
				}
				else{
					sb.append("\n El id:").append(idPosicion).append(" no tiene idPerfil/idPersona o no existe en Sistema \n");
				}
				
				sb.append("\n");
			}
		}else {
			sb.append("Error I/O, No se pudo realizar el proceso en la carpeta de Vacancy ");
		}		
		return sb;
	}
	
	
	private static void generaGetJson() throws Exception {
		log4j.debug("# elementos en lsJsonFile " + lsJsonFile.size() );
		if(!lsJsonFile.isEmpty()) {
			ListIterator<String> itJsonFile = lsJsonFile.listIterator();
			jsGet = new JSONArray();
			
			String jsonFileName;
			jsonAux = null;
			while(itJsonFile.hasNext()) {
				jsonFileName = itJsonFile.next();
				jsonFullPosicion = getFromFile(BACKUP_JSON_DIR+jsonFileName);
				System.out.println("JSON: " + jsonFileName );
				jsonAux = new JSONObject();
				jsonAux.put("idPosicion", jsonFullPosicion.get("idPosicion"));
				jsonAux.put("idEstatusPosicion", jsonFullPosicion.get("idEstatusPosicion"));
				jsonAux.put("fechaCreacion", jsonFullPosicion.get("fechaCreacion"));
				if(jsonFullPosicion.has("fechaModificacion")) {
					jsonAux.put("fechaModificacion", jsonFullPosicion.get("fechaModificacion"));
				}
				jsonAux.put("idEmpresa", jsonFullPosicion.get("idEmpresa"));
				jsonAux.put("idPersona", jsonFullPosicion.get("idPersona"));
				jsonAux.put("puesto", jsonFullPosicion.get("puesto"));
				jsonAux.put("valorActivo", "1");
				
				if(!jsonFullPosicion.getString("numTotalCandidatos").equals("0")) {
					jsonAux.put("numTotalCandidatos", jsonFullPosicion.get("numTotalCandidatos"));
					jsonAux.put("candidatosEnProceso", jsonFullPosicion.get("candidatosEnProceso"));
					jsonAux.put("candidatosDescartados", jsonFullPosicion.get("candidatosDescartados"));
					jsonAux.put("numTotalPreCandidatos", jsonFullPosicion.get("numTotalPreCandidatos"));
					jsonAux.put("preCandidatosConfirmados", jsonFullPosicion.get("preCandidatosConfirmados"));
					jsonAux.put("preCandidatosNoConfirmados", jsonFullPosicion.get("preCandidatosNoConfirmados"));
					jsonAux.put("preCandidatosDescartados", jsonFullPosicion.get("preCandidatosDescartados"));
				}
				//jsonAux.put("modificado*", jsonFullPosicion.get("modificado"));
				
				
				jsGet.put(jsonAux);
			}
			writeFile(BACKUP_JSON_DIR+"get.json", formatJson(jsGet.toString()));
		}
		else {
			log4j.debug("NO hay elementos a procesar");
		}
	}
	
	
	
	/**
	 * 
	 * @throws Exception
	 */
	private static void initMapaRol() throws Exception {
		int x =0;
		JSONObject jsonRol;
		if(mapRol==null){
			mapRol = new HashMap<String, JSONObject>();
			sb.append("## Creando MAPA de Roles");
			JSONObject jsonReq = new JSONObject();
			jsonReq.put("idEmpresaConf", "1");
			jsonReq.put("idPersona", "1");//ADMIN
			
			
			JSONArray jsRol = new JSONArray(getJsonFromService(jsonReq.toString(), URI_ROL+URI_GET ) );
			for(x=0; x<jsRol.length();x++){
				jsonRol = jsRol.getJSONObject(x);
				if(jsonRol.has("idRol")){
					mapRol.put(jsonRol.getString("idRol"), new JSONObject( jsonRol.toString() ));
				}
				
			}
		}
	}
	
	/**
	 * VACANCY.R
	 * @param idPosicion
	 * @return
	 * @throws Exception
	 */
	private static String readPosicion(String idPosicion) throws Exception{
		JSONObject jsonReq = new JSONObject();
		jsonReq.put("idEmpresaConf", IDCONF);
		jsonReq.put("idPosicion", idPosicion);
		
		sb.append("\t==> [VACANCY.R]: ").append(URI_VACANCY+URI_READ).append("\n")
		.append("\t==> Json:").append(jsonReq).append("\n");
		
		return getJsonFromService(jsonReq.toString(), URI_VACANCY+URI_READ );
	}
	/**
	 * MODELRSCPOS.G
	 * @param idPosicion
	 * @param idPersona
	 * @return
	 * @throws Exception
	 */
	private static String getModRscPos(String idPosicion, String idPersona) throws Exception {
		JSONObject jsonReq = new JSONObject();
		jsonReq.put("idEmpresaConf", IDCONF);
		jsonReq.put("idPosicion", idPosicion);
		jsonReq.put("idPersona", idPersona);
		jsonReq.put("modo", "1");
		jsonReq.put("activo", "1");
		// [{"code":"003","type":"W","message":"No existen datos registrados, verifique la información proporcionada"}] 
		sb.append("\t==> [MODELRSCPOS.G]: ").append(URI_MODRSCPOS+URI_GET).append("\n")
		.append("\t==> Json:").append(jsonReq).append("\n");
		
		return getJsonFromService(jsonReq.toString(), URI_MODRSCPOS+URI_GET );
	}
	
	/**
	 * MONITOR.G
	 * @param idPosicion
	 * @return
	 * @throws Exception
	 */
	private static String getMonitoresPos(String idPosicion) throws Exception {
		JSONObject jsonReq = new JSONObject();
		jsonReq.put("idEmpresaConf", IDCONF);
		jsonReq.put("idPosicion", idPosicion);
		// [] 
		sb.append("\t==> [MONITOR.G]: ").append(URI_MONITORPOS+URI_GET).append("\n")
		.append("\t==> Json:").append(jsonReq).append("\n");
		return getJsonFromService(jsonReq.toString(), URI_MONITORPOS+URI_GET );
	}
	
	private static void setRol2Monitors() throws Exception {
		int x =0;
		JSONObject jsonRol;
		if(mapRol==null){
			initMapaRol();
		}
		
		for(x=0; x<jsMonitor.length();x++){
			jsonAux = jsMonitor.getJSONObject(x);
			if(jsonAux.has("idRol")){				
				jsonRol = mapRol.get(jsonAux.getString("idRol"));
				jsonAux.put("descripcion", jsonRol.getString("descripcion"));
			}
		}
		
	}
}
