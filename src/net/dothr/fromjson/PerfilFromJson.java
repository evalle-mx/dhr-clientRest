package net.dothr.fromjson;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import net.dothr.tojson.PerfilToJson;
import net.utils.ClientRestUtily;
import net.utils.ConstantesREST;

public class PerfilFromJson extends PerfilToJson {
	
	private static Logger log4j = Logger.getLogger( PerfilFromJson.class );
	private static String idPersonaPerfil = "2";
	private static final String IDCONF =ConstantesREST.IDEMPRESA_CONF;

	private final static String URI_CREATE_PROFPUBLIC="/createpublic";
	private final static String URI_CREATE_PROFPRIVATE="/createprivate";
	
	public final static String PATH_REPORTE = LOCAL_HOME+"/JsonUI/Rep.PerfilFromJson.txt";
	
	
	public static void main(String[] args) {
		try {
			StringBuilder sb = procesaPerfilFile("read-3.json");
			log4j.debug("\n REPORTE DE PROCESO: en ruta "+ PATH_REPORTE );
			ClientRestUtily.writeStringInFile(PATH_REPORTE, sb.toString(), false);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Obtiene el archivo plano .json para enviar al proceso de JSONObject para persistir datos
	 * de posición
	 * @param archivoPerfil
	 * @return
	 * @throws Exception
	 */
	public static StringBuilder procesaPerfilFile(String archivoPerfil) throws Exception{
		StringBuilder stBuilder = new StringBuilder(">>>>   procesaPerfilFile   <<<< \n");
		if(WEB_RESOURCE.equals(ConstantesREST.WEB_TRANSACT_AWS) || WEB_RESOURCE.indexOf("localhost")== -1){
			log4j.fatal("No esta apuntando a DESARROLLO "+ WEB_RESOURCE, new NullPointerException() );
			stBuilder.append("No esta apuntando a DESARROLLO "+WEB_RESOURCE).append("\n");
		}else{
			try{
				log4j.debug("paso A) cadena de perfil: ");
				stBuilder.append("A) Obtener String de perfil.");
				String stPerfil = ClientRestUtily.getJsonFile(archivoPerfil, PROFILE_JSON_DIR);	
//				stBuilder.append(stPerfil).append("\n");
				if(stPerfil.equals("[]") || (stPerfil.indexOf("\"idPerfil\"")==-1)) {
					log4j.debug("NO EXISTE ARCHIVO JSON (Perfil) o es invalido");
					stBuilder.append("NO EXISTE ARCHIVO JSON (Perfil) o es invalido \n");
				}else{
					log4j.debug("paso B) Perfil (cadena) Se convierte a objeto json ");
					stBuilder.append("B) Perfil (cadena) Se convierte a objeto json \n");
					JSONArray jsonArrayPerfil = new JSONArray(stPerfil);					
					JSONObject jsonPerfil = jsonArrayPerfil.getJSONObject(0);
					String idPerfil = jsonPerfil.getString("idPerfil");
					log4j.debug("idPerfil en archivo json " + idPerfil );
					jsonPerfil.put("idPersona", idPersonaPerfil);
					stBuilder.append(procesaJSONPerfil(jsonPerfil)).append("\n");
					
				}
			}catch (Exception e){
				log4j.fatal("<Exception> error: ", e);
				stBuilder.append("<Exception> error: ").append(e.getMessage()).append("\n");
			}
		}
		stBuilder.append("******************************************************");
		return stBuilder;
	}

	
	/**
	 * Procesa un elemento Json en la persistencia via sistema DotHR (AppTransactional)
	 * @param jsPerfil
	 * @return
	 * @throws Exception
	 */
	public static StringBuilder procesaJSONPerfil(JSONObject jsPerfil) throws Exception{
		log4j.debug("<procesaJSONPerfil> jsPerfil: "+jsPerfil.toString() );
		StringBuilder sbProcesoPerfil = new StringBuilder();
		sbProcesoPerfil.append("1) jsPerfil: ").append(jsPerfil).append("\n");
		String uriService = null;

		if( jsPerfil.has("idTipoPerfil") && (jsPerfil.getString("idTipoPerfil").equals("2") || jsPerfil.getString("idTipoPerfil").equals("1") ) ){
			JSONObject jsonReq = new JSONObject();
			jsonReq.put(P_JSON_IDCONF, IDCONF );
			jsonReq.put("idPersonaPerfil", idPersonaPerfil);
			
			log4j.debug("jsonReq (Create): " + jsonReq );
			
			if(jsPerfil.getString("idTipoPerfil").equals("1")){//Publico
				uriService = URI_PROFILE.concat(URI_CREATE_PROFPUBLIC);
			}else{ //Privado
				uriService = URI_PROFILE.concat(URI_CREATE_PROFPRIVATE);
			}
			sbProcesoPerfil.append("\n PROFILE.C:\n==>Json: ").append(jsonReq).append("\n uri: ").append(uriService).append("\n");
			String stResponse = 
					getJsonFromService(jsonReq.toString(), uriService );
			sbProcesoPerfil.append("\n<==Response: ").append(stResponse);
			
			if(stResponse.indexOf("\"idPerfil\"")!=-1){//[{"name":"idPerfil","value":"17","code":"004","type":"I"}]
				JSONArray jsArrResponse = new JSONArray(stResponse);
				JSONObject jsCreateResp = jsArrResponse.getJSONObject(0);
				String idPerfil = jsCreateResp.getString("value");
				//2] se debe realizar un Read para obtener idPerfil
				jsonReq = new JSONObject();
				jsonReq.put(P_JSON_IDCONF, IDCONF );
				jsonReq.put("idPerfil", idPerfil);
				
				jsonReq.put("nombre", (jsPerfil.has("nombre")?jsPerfil.getString("nombre"):null) );
				jsonReq.put("descripcion", (jsPerfil.has("descripcion")?jsPerfil.getString("descripcion"):null) ); 
				jsonReq.put("idEstatusPerfil", (jsPerfil.has("idEstatusPerfil")?jsPerfil.getString("idEstatusPerfil"):null) );
				
				stResponse = getJsonFromService(jsonReq.toString(), URI_PROFILE+URI_UPDATE );
				
				sbProcesoPerfil.append("\n PROFILE.U:\n==>Json: ").append(jsonReq).append("\n uri: ").append(URI_PROFILE+URI_UPDATE).append("\n")
				.append("<==Response: ").append(stResponse).append("\n --> Textos \n");
				
				if(stResponse.equals("[]")){
					log4j.debug("<procesaJSONPerfil> Se iteran los textos para insertarlos");
					
					if(jsPerfil.has("textos")){
						JSONArray jsArrTextos = jsPerfil.getJSONArray("textos");
						String textUpd = null;
						for(int h=0;h<jsArrTextos.length();h++){
							JSONObject jsTextoPerfil = jsArrTextos.getJSONObject(h);
							String desTexto = "";	//textos[?].texto
							if(jsTextoPerfil.has("texto") ){
								desTexto = jsTextoPerfil.getString("texto"); 
							}
							String ponderacionTexto = jsTextoPerfil.getString("ponderacion");
							
							//a) create
							jsonReq = new JSONObject();
							jsonReq.put(P_JSON_IDCONF, IDCONF );
							jsonReq.put("idPerfil", idPerfil);
							jsonReq.put("idPersonaPerfil", idPersonaPerfil);//Para cuestiones de permiso de modificar/Eliminar											
							textUpd = getJsonFromService(jsonReq.toString(), URI_PROFILE.concat(URI_TEXT_C));
							sbProcesoPerfil.append("[PROFILETEX.C]: \n==>").append(jsonReq).append("\n uri: ").append(URI_PROFILE+URI_TEXT_C)
								.append("\n<==Response: ").append(textUpd).append("\n");
							if(textUpd.indexOf("\"idPerfilTextoNgram\"")!=-1){
								//b) update
								JSONArray jsArrCreateText = new JSONArray(textUpd);
								JSONObject jsCreate = jsArrCreateText.getJSONObject(0);
								String idPerfilTextoNgram = jsCreate.getString("value");
								jsonReq = new JSONObject();
								jsonReq.put(P_JSON_IDCONF, IDCONF );
								jsonReq.put("idPerfil", idPerfil);										
								jsonReq.put("idPersonaPerfil", idPersonaPerfil);//Para cuestiones de permiso de modificar/Eliminar
								
								jsonReq.put("idPerfilTextoNgram", idPerfilTextoNgram );
								jsonReq.put("texto", desTexto);
								jsonReq.put("ponderacion", ponderacionTexto);
								
								textUpd = getJsonFromService(jsonReq.toString(), URI_PROFILE.concat(URI_TEXT_U));
								sbProcesoPerfil.append("[PROFILETEX.U]: \n==>").append(jsonReq).append("\n uri: ")
									.append(URI_PROFILE+URI_TEXT_U).append("\n<== Response: ").append(textUpd).append("\n");
								if(!textUpd.equals("[]")) {
									log4j.debug("<Error> al actualizar texto PerfilNgram " + textUpd);
									sbProcesoPerfil.append("<Error> al actualizar texto PerfilNgram " + textUpd).append("\n");
								}
							}else{
								log4j.debug("<Error> no se pudo crear texto PerfilNgram");
								sbProcesoPerfil.append("<Error> no se pudo crear texto PerfilNgram").append("\n");
							}
						}
					}else{
						log4j.debug("<procesaJSONPerfil> jsPerfil No contiene Textos-Ngram");
						sbProcesoPerfil.append("<procesaJSONPerfil> jsPerfil No contiene Textos-Ngram").append("\n");
					}
					
				}else{
					sbProcesoPerfil.append("<Error> No se pudo actualizar dato en perfil "+idPerfil);
				}
			}else{
				sbProcesoPerfil.append("<Error> No se recibio el idPerfil del servicio. ");
			}
		}else{
			log4j.error("<Error> El tipo de Perfil es invalido " + (jsPerfil.has("idTipoPerfil")?jsPerfil.getString("idTipoPerfil"):""));
			sbProcesoPerfil.append("<Error> El tipo de Perfil es invalido ")
				.append(jsPerfil.has("idTipoPerfil")?jsPerfil.getString("idTipoPerfil"):"").append("\n");
		}
		
		sbProcesoPerfil.append("<END> Proceso ");	
		
		return sbProcesoPerfil;
	}
	
	
	/**
	 * Genera la lista de archivos Json-Perfil a persistir en la Aplicación
	 * @return
	 */
	@SuppressWarnings("unused")
	private static List<String> getLsPosiciones(){
		List<String> lsPos = new ArrayList<String>();
		
		lsPos.add("read-3.json");
		
		return lsPos;
	}
}
