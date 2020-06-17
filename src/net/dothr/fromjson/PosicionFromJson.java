package net.dothr.fromjson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import net.dothr.tojson.PosicionToJson;
import net.utils.ClientRestUtily;
import net.utils.ConstantesREST;

public class PosicionFromJson extends PosicionToJson {
	
	private static Logger log4j = Logger.getLogger( PosicionFromJson.class );
//	private static String idPersonaPosicion = "1";
	private static final String IDCONF = ConstantesREST.IDEMPRESA_CONF;
	public final static String URI_POSICION_HABILIDAD = "/module/positionSkill";
	public final static String PATH_REPORTE = "/home/dothr/workspace/ClientRest/files/out/Rep.PositionFromJson.txt";
	
	
	public static void main(String[] args) {
		try {
			StringBuilder sbProc = 
//					procesaMultiplePosicion(); 
					procesaPosicionFile("read-5.json");
			log4j.debug("\n REPORTE DE PROCESO: en ruta "+ PATH_REPORTE );
			ClientRestUtily.writeStringInFile(PATH_REPORTE, sbProc.toString(), false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	 /** Procesa una lista de Archivos de Posicion, para reCrear datos en la aplicación
	 * @return
	 * @throws Exception
	 */
	private static StringBuilder procesaMultiplePosicion() throws Exception {
		StringBuilder sbMain = new StringBuilder();
		List<String> lsPosiciones = getLsPosiciones();
		Iterator<String> itPosicion = lsPosiciones.iterator();
		String archivoPosicion = null;
		int iFiles = 0;
		while(itPosicion.hasNext()){
			archivoPosicion = itPosicion.next();
			sbMain.append("\n/* >>>>>>>>>>>>>>>>>>>   PROCESO PARA " + archivoPosicion + " <<<<<<<<<<<<<<<<<< */\n");
			sbMain.append(procesaPosicionFile(archivoPosicion)).append("\n");
			iFiles++;
		}
		sbMain.append("\n Fin de Proceso sin Errores, ").append(iFiles).append(" Procesados.\n ");
		return sbMain;
	}
	
	/**
	 * Obtiene el archivo plano .json para enviar al proceso de JSONObject para persistir datos
	 * de posición
	 * @param archivoPosicion
	 * @return
	 * @throws Exception
	 */
	public static StringBuilder procesaPosicionFile(String archivoPosicion) throws Exception{
		StringBuilder stBuilder = new StringBuilder(">>>>   procesaJSONPosicion   <<<<\n");
		if(WEB_RESOURCE.equals(ConstantesREST.WEB_TRANSACT_AWS) 
				//|| WEB_RESOURCE.indexOf("localhost")== -1
				){
			log4j.fatal("No esta apuntando a DESARROLLO "+WEB_RESOURCE, new NullPointerException() );
			stBuilder.append("No esta apuntando a DESARROLLO "+WEB_RESOURCE).append("\n");
		}else{
			try{
				log4j.debug("paso A) cadena de vacante(posición): ");
				stBuilder.append("A) String de Posición:");
				String stVacancy = ClientRestUtily.getJsonFile(archivoPosicion, POSITION_JSON_DIR);	
				stBuilder.append(stVacancy).append("\n");
				if(stVacancy.equals("[]") || (stVacancy.indexOf("\"idPosicion\"")==-1)) {
					log4j.debug("NO EXISTE ARCHIVO JSON (posicion) o es invalido");
					stBuilder.append("NO EXISTE ARCHIVO JSON (posicion) o es invalido \n");
				}else{
					log4j.debug("paso B) Vacante (cadena) Se convierte a objeto json ");
					stBuilder.append("B) Vacante (cadena) Se convierte a objeto json \n");
					JSONArray jsonArrayVacancy = new JSONArray(stVacancy);					
					JSONObject jsonVacancy = jsonArrayVacancy.getJSONObject(0);
					String idPosicion = jsonVacancy.getString("idPosicion");
					log4j.debug("idPosicion en archivo json " + idPosicion );
//					jsonVacancy.put("idPersona", idPersona);
					stBuilder.append(procesaJSONPosicion(jsonVacancy)).append("\n");
					
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
	 * @param jsPosicion
	 * @return
	 * @throws Exception
	 */
	public static StringBuilder procesaJSONPosicion(JSONObject jsPosicion) throws Exception{
		log4j.debug("<procesaJSONPosicion> jsPosicion: "+jsPosicion.toString() );
		StringBuilder sbProcesoPosicion = new StringBuilder();
		String idPersona = jsPosicion.getString("idPersona");
		sbProcesoPosicion.append("1) jsPosicion: ").append(jsPosicion).append("\n");
		JSONArray jsArrDomicilio = null, jsArrPerfiles = null, jsArrHabilidades = null, jsArrIdioma = null, jsArrCertif = null;
		JSONObject jsonArea = null;
		
		if(jsPosicion.has("domicilios") ){
			jsArrDomicilio = jsPosicion.getJSONArray("domicilios");
		}
		if(jsPosicion.has("perfiles") ){
			jsArrPerfiles = jsPosicion.getJSONArray("perfiles");
		}
		if(jsPosicion.has("habilidades") ){
			jsArrHabilidades = jsPosicion.getJSONArray("habilidades");
		}
		
		//idioma []
		if(jsPosicion.has("idioma") ){
			jsArrIdioma = jsPosicion.getJSONArray("idioma");
		}
		//competencias[]
		if(jsPosicion.has("certificacion") ){
			jsArrCertif = jsPosicion.getJSONArray("certificacion");
		}
		//area {}
		if(jsPosicion.has("area")){
			jsonArea = jsPosicion.getJSONObject("area");
		}
		
		if(idPersona!=null && !idPersona.equals("")) {
			/* EN ESTE CASO, LA EMPRESA A DIFERENCIA DE PERSONA, SE DEBE CREAR CON DATOS PROCESADOS EN ESTE PUNTO */
			JSONObject jsonRequest = new JSONObject();
			jsonRequest.put(P_JSON_IDCONF, IDCONF );
			jsonRequest.put("idPersona", idPersona);
			
			log4j.debug("jsonRequest (Create): " + jsonRequest );
			sbProcesoPosicion.append("\n VACANCY.C:\n==>Json: ").append(jsonRequest).append("\n uri: ").append(URI_VACANCY.concat(URI_CREATE)).append("\n");
			// 1]Create (se crea posición)
			String jsVacCreated = 
					getJsonFromService(jsonRequest.toString(), URI_VACANCY.concat(URI_CREATE) );
			sbProcesoPosicion.append("\n<==Response: ").append(jsVacCreated);

			if(jsVacCreated.indexOf("\"idPosicion\"")!=-1){//[{"name":"idPosicion","value":"17","code":"004","type":"I"}]
				JSONArray jsArrResponse = new JSONArray(jsVacCreated);
				JSONObject jsCreateResp = jsArrResponse.getJSONObject(0);
				String idPosicion = jsCreateResp.getString("value");
				//2] se debe realizar un Read para obtener idPerfil
				jsonRequest = new JSONObject();
				jsonRequest.put(P_JSON_IDCONF, IDCONF );
				jsonRequest.put("idPosicion", idPosicion);
				
				String jsVacRead =  
						getJsonFromService(jsonRequest.toString(), URI_VACANCY.concat(URI_READ) );
						//"[{\"idPosicion\":\"53\",\"idEstatusPosicion\":\"1\",\"fechaCreacion\":\"2014-05-27 14:07:46.0\",\"idPersona\":\"18\",\"idPerfil\":\"64\",\"perfilesInternos\":[{\"ponderacion\":\"0.5\",\"idPerfil\":\"64\",\"nombre\":\"Interno\",\"textos\":[]}],\"perfilesExternos\":[],\"domicilios\":[],\"politicaEscolaridads\":[]}]";
				
				if(jsVacRead.indexOf("\"idPerfil\"")!=-1){ 
					jsArrResponse = new JSONArray(jsVacRead);
					JSONObject jsCreateRead = jsArrResponse.getJSONObject(0);
					String idPerfil = jsCreateRead.getString("idPerfil");
					
					//3] Se actualizan los datos de primer nivel
					jsonRequest = new JSONObject();
					jsonRequest.put(P_JSON_IDCONF, IDCONF );
					jsonRequest.put("idPosicion", idPosicion);
					jsonRequest.put("idPerfil", idPerfil);
					
					
					//1er update (datos sin error de Politicas)
//					jsonRequest.put("nombre", (jsPosicion.has("nombre")?jsPosicion.getString("nombre"):null) );
					if(jsPosicion.has("puesto")){
						jsonRequest.put("puesto", (jsPosicion.has("puesto")?jsPosicion.getString("puesto"):jsPosicion.has("nombre")?jsPosicion.getString("nombre"):null) );
					}else if(jsPosicion.has("nombre")){
						jsonRequest.put("puesto", (jsPosicion.has("nombre")?jsPosicion.getString("nombre"):null) );
					}
//					jsonRequest.put("puesto", (jsPosicion.has("nombre")?jsPosicion.getString("nombre"):null) );
//					jsonRequest.put("puesto", (jsPosicion.has("puesto")?jsPosicion.getString("puesto"):null) );
					jsonRequest.put("idTipoJornada", (jsPosicion.has("idTipoJornada")?jsPosicion.get("idTipoJornada"):null));
					jsonRequest.put("idTipoContrato", (jsPosicion.has("idTipoContrato")?jsPosicion.get("idTipoContrato"):null));
					jsonRequest.put("idAmbitoGeografico", (jsPosicion.has("idAmbitoGeografico")?jsPosicion.get("idAmbitoGeografico"):null));
					jsonRequest.put("esConfidencial", (jsPosicion.has("esConfidencial")?jsPosicion.get("esConfidencial"):null));
					jsonRequest.put("salarioMin", (jsPosicion.has("salarioMin")?jsPosicion.get("salarioMin"):null));
					jsonRequest.put("salarioMax", (jsPosicion.has("salarioMax")?jsPosicion.get("salarioMax"):null));
					jsonRequest.put("sueldoNegociable", (jsPosicion.has("sueldoNegociable")?jsPosicion.get("sueldoNegociable"):null));
					jsonRequest.put("idPeriodicidadPago", (jsPosicion.has("idPeriodicidadPago")?jsPosicion.get("idPeriodicidadPago"):null));
					jsonRequest.put("idTipoPrestacion", (jsPosicion.has("idTipoPrestacion")?jsPosicion.get("idTipoPrestacion"):null));
					jsonRequest.put("otros", (jsPosicion.has("otros")?jsPosicion.get("otros"):null));
					
					
					String jsVacUpdate =
							getJsonFromService(jsonRequest.toString(), URI_VACANCY.concat(URI_UPDATE) );
							//"[]"; 
					sbProcesoPosicion.append("\n VACANCY.U:\n==>Json: ").append(jsonRequest.toString())
					.append("\n<==Response: ").append(jsVacUpdate);
					if(!jsVacUpdate.equals("[]")){
						sbProcesoPosicion.append("<Error> No se pudo actualizar dato en la posición "+idPosicion);
						return sbProcesoPosicion;
					}
					
					//2o Update (datos unitarios por politicas
					List<String> lsUnitarios = getUnitarios();
					Iterator<String> itUnitario = lsUnitarios.iterator();
					String unitPropiedad = null;
					while(itUnitario.hasNext()){
						jsonRequest = new JSONObject();
						jsonRequest.put(P_JSON_IDCONF, IDCONF );
						jsonRequest.put("idPosicion", idPosicion);
						jsonRequest.put("idPerfil", idPerfil);
						unitPropiedad = itUnitario.next();
						jsonRequest.put(unitPropiedad, (jsPosicion.has(unitPropiedad)?jsPosicion.get(unitPropiedad):null));
						jsVacUpdate =
								getJsonFromService(jsonRequest.toString(), URI_VACANCY.concat(URI_UPDATE) );
								//"[]"; 
						sbProcesoPosicion.append("\n VACANCY.U:\n==>Json: ").append(jsonRequest.toString())
						.append("\n<==Response: ").append(jsVacUpdate);
						if(!jsVacUpdate.equals("[]")){
							sbProcesoPosicion.append("<Error> No se pudo actualizar dato en la posición "+idPosicion);
						}
					}
					
					
					if(jsVacUpdate.equals("[]") ){
						/* Se procesan campos satelite*/
						log4j.debug("Se procesan campos satelite (ubicacion, perfiles [tNgram], Habilidades, idiomas, " +
								"certificaciones, area) y asignacion para idPosicion: "+ idPosicion  );
						
						/* [4.1] Se insertan direcciones */
						if(jsArrDomicilio!=null){
							sbProcesoPosicion.append("\n\n ============ Domicilios ============ ");
							for(int x=0;x<jsArrDomicilio.length();x++){
								JSONObject jsDom = jsArrDomicilio.getJSONObject(x);
								jsDom.put("idPosicion", idPosicion);
								jsDom.put("idPerfil", idPerfil);
								jsDom.put("idPersona", idPersona);
								jsDom.put(P_JSON_IDCONF, IDCONF );
								jsDom.remove("idDomicilio");
								
								if(jsDom.has("idPais")){
									jsDom.put("tempPais", jsDom.getString("idPais"));
									jsDom.remove("idPais");
								}
								if(jsDom.has("idAsentamiento")){
									jsDom.put("tempAsentamiento", jsDom.getString("idAsentamiento"));
									jsDom.remove("idAsentamiento");
								}
								if(jsDom.has("idMunicipio")){
									jsDom.put("tempMunicipio", jsDom.getString("idMunicipio"));
									jsDom.remove("idMunicipio");
								}
								if(jsDom.has("idEstado")){
									jsDom.put("tempEstado", jsDom.getString("idEstado"));
									jsDom.remove("idEstado");
								}
								if(jsDom.has("codigoPostal")){
									jsDom.put("tempCodigoPostal", jsDom.getString("codigoPostal"));
									jsDom.remove("codigoPostal");
								}
								String jsResponseSettlement = getJsonFromService(jsDom.toString(),URI_SETTLEMENT_C); //"idDomicilio=?";//
								sbProcesoPosicion.append("\n[SETTLEMENT.C]: \n==>").append(jsDom).append("\n<== ").append(jsResponseSettlement);
								if(jsResponseSettlement.indexOf("\"idDomicilio\"")==-1){
									log4j.equals("Error al insertar domicilio "+x );
									sbProcesoPosicion.append("\nError al insertar domicilio: ").append(x);
								}
							}
						}					
						
						/* [4.2] Se insertan perfiles (funciones) internos */
						if(jsArrPerfiles!=null){
							sbProcesoPosicion.append("\n\n ============ PERFILES ============ ");
							for(int x=0;x<jsArrPerfiles.length();x++){
								JSONObject jsPerfil = jsArrPerfiles.getJSONObject(x);
								String idTipoPerfilI = jsPerfil.getString("idTipoPerfil");
								
								if(idTipoPerfilI.equals("4")) {		/* Aplica para perfil Interno */
									if(jsPerfil.has("textos") ){
										JSONArray jsArrTextos = jsPerfil.getJSONArray("textos");
										for(int h=0;h<jsArrTextos.length();h++){
											JSONObject jsTextoPerfil = jsArrTextos.getJSONObject(h);
											String desTexto = "";	//textos[?].texto
											if(jsTextoPerfil.has("texto") ){
												desTexto = jsTextoPerfil.getString("texto"); 
											}
											String ponderacionTexto = jsTextoPerfil.has("ponderacion")?jsTextoPerfil.getString("ponderacion"):"11";
											
											//a) create
											jsonRequest = new JSONObject();
											jsonRequest.put(P_JSON_IDCONF, IDCONF );
											jsonRequest.put("idPerfil", idPerfil);
											jsonRequest.put("idPersona", idPersona);//Para cuestiones de permiso de modificar/Eliminar
											jsonRequest.put("idPosicion", idPosicion);											
											String textCreate = getJsonFromService(jsonRequest.toString(), URI_PROFILE.concat(URI_TEXT_C));
											sbProcesoPosicion.append("\n[PROFILETEX.C]: \n==>").append(jsonRequest).append("\n<== ").append(textCreate);
											if(textCreate.indexOf("\"idPerfilTextoNgram\"")!=-1){
												//b) update
												JSONArray jsArrCreateText = new JSONArray(textCreate);
												JSONObject jsCreate = jsArrCreateText.getJSONObject(0);
												String idPerfilTextoNgram = jsCreate.getString("value");
												jsonRequest = new JSONObject();
												jsonRequest.put(P_JSON_IDCONF, IDCONF );
												jsonRequest.put("idPosicion", idPosicion);
												jsonRequest.put("idPerfil", idPerfil);					
												jsonRequest.put("idPersona", idPersona);												
												jsonRequest.put("idPerfilTextoNgram", idPerfilTextoNgram );
												jsonRequest.put("texto", desTexto);
												jsonRequest.put("ponderacion", ponderacionTexto);
												
												String textUpd = getJsonFromService(jsonRequest.toString(), URI_PROFILE.concat(URI_TEXT_U));
												sbProcesoPosicion.append("\n[PROFILETEX.U]: \n==>").append(jsonRequest).append("\n<== ").append(textUpd);
												if(!textUpd.equals("[]")){
													log4j.debug("<Error> al actualizar texto PerfilNgram " + textUpd);
													sbProcesoPosicion.append("<Error> al actualizar texto PerfilNgram " + textUpd);
												}
											}else{
												log4j.debug("<Error> no se pudo crear texto PerfilNgram");
												sbProcesoPosicion.append("<Error> no se pudo crear texto PerfilNgram");
											}
										}
									}
								}else{
									log4j.debug("Perfil no Interno, se debe agregar la relacion ");
									
								}
							}//fin de for jsonArrPerfiles
							log4j.debug("<OK>Se han procesado los perfiles");
						}// fin de jsonArrPerfiles
//						/* [4.3] Se insertan Habilidades */
						if(jsArrHabilidades != null){
							sbProcesoPosicion.append("\n\n ============ Habilidades ============ ");
							JSONArray jsArrResp = null;
							for(int x=0;x<jsArrHabilidades.length();x++){
								JSONObject jsHab = jsArrHabilidades.getJSONObject(x);
								jsHab.put(P_JSON_IDCONF, IDCONF );
								jsHab.put(P_JSON_PERSONA, idPersona);
								jsHab.remove("idPoliticaMHabilidad");
								jsHab.remove("descripcion");
								jsHab.put("idPosicion", idPosicion);
								
								String jsMainUpdate = getJsonFromService(jsHab.toString(), URI_POSICION_HABILIDAD+URI_CREATE);
								sbProcesoPosicion.append("\n[HAB.C]: \n==>").append(jsHab).append("\n<== ").append(jsMainUpdate);
								if(jsMainUpdate.indexOf("idPoliticaMHabilidad")==-1){
									log4j.debug("Error al insertar Habilidad "+x );
									sbProcesoPosicion.append("\nError al insertar Habilidad: ").append(x);
								}else{
									jsArrResp = new JSONArray(jsMainUpdate);
									String idPoliticaMHabilidadRes = jsArrResp.getJSONObject(0).getString("value");
									jsHab.put("idPoliticaMHabilidad", idPoliticaMHabilidadRes);
									jsMainUpdate = getJsonFromService(jsHab.toString(), URI_POSICION_HABILIDAD+URI_UPDATE);
									if(!jsMainUpdate.equals("[]")){
										log4j.debug("Error al actualizar Habilidad "+x );
										sbProcesoPosicion.append("\nError al actualizar Habilidad: ").append(x);
									}
								}
							}
						}
//						/* [4.4] Se insertan idiomas */
						if(jsArrIdioma != null){
							sbProcesoPosicion.append("\n\n ============ IDIOMA ============ ");
							for(int x=0;x<jsArrIdioma.length();x++){
								JSONObject jsIdiom = jsArrIdioma.getJSONObject(x);
								jsIdiom.put(P_JSON_IDCONF, IDCONF );
//								jsIdiom.put(P_JSON_PERSONA, idPersona);
								jsIdiom.put(P_JSON_POSICION, idPosicion);
								jsIdiom.remove("idPoliticaValor");
								jsIdiom.remove("idPosicionIdioma");								
//								jsIdiom.put("idDominio", "2");	//"Bueno"
//								jsIdiom.put("idIdioma", "3");	//"Inglés" 
								
								String jsMainUpdate = getJsonFromService(jsIdiom.toString(), URI_IDIOMA+URI_CREATE);
								sbProcesoPosicion.append("\n[LANG.C]: \n==>").append(jsIdiom).append("\n<== ").append(jsMainUpdate);
								if(jsMainUpdate.indexOf("idPosicionIdioma")==-1){
									log4j.debug("Error al insertar IDIOMA "+x );
									sbProcesoPosicion.append("\nError al insertar IDIOMA: ").append(x);
								}else{
									sbProcesoPosicion.append("\n Se inserto Idioma correctamente"); //idPosicionIdioma
								}
							}
						}
						/* [4.5] Se insertan certificaciones */
						if(jsArrCertif!= null){
							sbProcesoPosicion.append("\n\n ============ CERTIFICACION ============ ");
							for(int x=0;x<jsArrCertif.length();x++){
								JSONObject jsCert = jsArrCertif.getJSONObject(x);
								jsCert.put(P_JSON_IDCONF, IDCONF );
								jsCert.put(P_JSON_POSICION, idPosicion);
								jsCert.remove("idCertificacion");
//								jsCert.put("tituloCert", "Certificacion Gestión Internacional de Recursos Humanos (RH Manager)");
//								jsCert.put("idGrado", "5");
								
								String jsMainUpdate = getJsonFromService(jsCert.toString(), URI_CERTIFICACION+URI_CREATE);
								sbProcesoPosicion.append("\n[CERT.C]: \n==>").append(jsCert).append("\n<== ").append(jsMainUpdate);
								if(jsMainUpdate.indexOf("idCertificacion")==-1){
									log4j.debug("Error al insertar CERTIFICACION "+x );
									sbProcesoPosicion.append("\nError al insertar CERTIFICACION: ").append(x);
								}else{
									sbProcesoPosicion.append("\n Se inserto CERTIFICACION correctamente"); //idCertificacion
								}
							}
						}
						
						
						if(jsonArea!=null){
							sbProcesoPosicion.append("\n\n ============ AREA ============ ");
							String idArea = jsonArea.getString("idArea");
							jsonArea = new JSONObject();
							jsonArea.put(P_JSON_IDCONF, IDCONF );
							jsonArea.put(P_JSON_POSICION, idPosicion);
							jsonArea.put(P_JSON_PERSONA, idPersona);
							JSONObject jsonA = new JSONObject();jsonA.put("idArea", idArea);
							jsonArea.put("area", jsonA);
							String jsMainUpdate = getJsonFromService(jsonArea.toString(), URI_VACANCY+URI_UPDATE);
							sbProcesoPosicion.append("\n[VACANCY.U]: \n==>").append(jsonArea).append("\n<== ").append(jsMainUpdate);
						}
						
						
						sbProcesoPosicion.append("\n\n ============ ASIGNACION ============ ");
						jsonRequest = new JSONObject();
						jsonRequest.put(P_JSON_IDCONF, IDCONF );
						jsonRequest.put(P_JSON_POSICION, idPosicion);
						jsonRequest.put(P_JSON_PERFIL, idPerfil);
						jsonRequest.put(P_JSON_EMPRESA, "1");
						
						jsVacUpdate =
								getJsonFromService(jsonRequest.toString(), URI_VACANCY.concat(URI_UPDATE) );
								//"[]"; 
						sbProcesoPosicion.append("\n VACANCY.U:\n==>Json: ").append(jsonRequest.toString())
						.append("\n<==Response: ").append(jsVacUpdate);
						if(!jsVacUpdate.equals("[]")){
							sbProcesoPosicion.append("<Error> No se pudo ASIGNAR la posición "+idPosicion);
						}
						
						
					}else{
						log4j.debug("<Error>No se pudo actualizar la posición "+idPosicion);
						sbProcesoPosicion.append("<Error> No se pudo actualizar la posición "+idPosicion);
					}						
				}
				else{
					log4j.debug("<Error>No se pudo leer en BD la posición "+idPosicion);
					sbProcesoPosicion.append("<Error> No se pudo leer en BD la posición "+idPosicion);
				}
				
			}else{
				log4j.debug("<Error>No se pudo crear la posición");
				sbProcesoPosicion.append("<Error> No se pudo crear la posición");
			}
		}else{
			log4j.debug("<Error> idPersona es invalido");
			sbProcesoPosicion.append("<Error> idPersona es invalido");
		}
		
		return sbProcesoPosicion;
	}
	
	
	/**
	 * Obtiene listado de elementos raiz secundarios (No permite capturar hasta completar los anteriores)
	 * @return
	 */
	private static List<String> getUnitarios(){
		List<String> lsUni = new ArrayList<String>();
		lsUni.add("edadMinima");
		lsUni.add("edadMaxima");		
		lsUni.add("experienciaLaboralMinima");
		lsUni.add("idGradoAcademicoMin");
		lsUni.add("idGradoAcademicoMax");		
		lsUni.add("idEstatusEscolarMin");
		lsUni.add("idEstatusEscolarMax");
		lsUni.add("idTipoGeneroRequerido");
		lsUni.add("idEstadoCivilRequerido");
		
		return lsUni;
	}
	
	
	
	

	
	/**
	 * Genera la lista de archivos Json-Posicion a persistir en la Aplicación
	 * @return
	 */
	private static List<String> getLsPosiciones(){
		List<String> lsPos = new ArrayList<String>();
		
		lsPos.add("read-5.json");
		lsPos.add("read-6.json");
		
		return lsPos;
	}
}
