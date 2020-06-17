package net.dothr.temp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.dothr.test.CatalogoExcellUtily;
import net.utils.ClientRestUtily;
import net.utils.DateUtily;
import net.utils.JsonUtily;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CreateMasiveJson {

	private static final String DIR_JSON_PERSONA = "/home/tce/Documents/TCE/JSONback/Personas/";
	private static final String DIR_JSON_VACANCY = "/home/tce/Documents/TCE/JSONback/Vacancy/";
	private static final String DIR_JSON_OUT = "/home/tce/Documents/TCE/JSONback/pruebaMassiveCreate/";
	
	private static Logger log4j = Logger.getLogger( CreateMasiveJson.class );
	
	/**
	 * Procesa un archivo de respaldo JSON para formar un nuevo archivo de JSON.masivo
	 * @param archivoPosicion
	 * @return
	 * @throws JSONException
	 * @throws Exception
	 */
	public static StringBuilder procesaArchivoPosicion(String archivoPosicion) throws JSONException, Exception{
		StringBuilder stBuilder = new StringBuilder();
		log4j.debug("paso 1) cadena de Posicion: ");
		String stPosicion = ClientRestUtily.getJsonFile(archivoPosicion, DIR_JSON_VACANCY);
		JSONObject jsonPosicionMasivo = null;
		if(stPosicion.equals("[]") ){
			log4j.debug("NO EXISTE ARCHIVO JSON (posicion) o es invalido");
			stBuilder.append("NO EXISTE ARCHIVO JSON (posicion) o es invalido");
		}else{
			log4j.debug("paso 2) posicion Se convierte a objeto json ");
			JSONArray jsonResponse = new JSONArray(stPosicion);			
			JSONObject jsonVacancyTmp = jsonResponse.getJSONObject(0);
			
			JSONArray jsonArrDomicilio = null;
			JSONArray jsonArrPerfilesInternos = null;
			JSONArray jsonArrPerfilesExternos = null;
			JSONArray jsonArrPoliticaEscolaridads = null;
			String idPosicion = jsonVacancyTmp.getString("idPosicion");
			log4j.debug("paso 3) Se obtienen campos satelite requeridos (domicilio, perfiles internos, externos, politica Escolaridad)");
			
			if(stPosicion.indexOf("\"domicilios\"")!=-1){
				jsonArrDomicilio = jsonVacancyTmp.getJSONArray("domicilios");
			}
			if(stPosicion.indexOf("\"perfilesInternos\"")!=-1){
				jsonArrPerfilesInternos = jsonVacancyTmp.getJSONArray("perfilesInternos");
			}
			if(stPosicion.indexOf("\"perfilesExternos\"")!=-1){
				jsonArrPerfilesExternos = jsonVacancyTmp.getJSONArray("perfilesExternos");
			}
			if(stPosicion.indexOf("\"politicaEscolaridads\"")!=-1){
				jsonArrPoliticaEscolaridads = jsonVacancyTmp.getJSONArray("politicaEscolaridads");
			}
			
			/*  VALIDACION EXTRA PARA EVALUAR SI TIENE LO NECESARIO  */
			if(jsonArrDomicilio == null || jsonArrPerfilesInternos == null){
				stBuilder.append("NO EXISTE ALGUN ELEMENTO NECESARIO"); 
				return stBuilder;
			}
			log4j.debug("paso 4) se crea nuevo Json con los atributos requeridos"); 
			jsonPosicionMasivo = new JSONObject();
			
			Map<String, Object> mapJson = JsonUtily.mapFromJson(jsonVacancyTmp); //SE USA MAP PARA EVITAR EXCEPCION CON NULL ALL BUSCAR OBJETO
			
			
			/* -- Obtenidos mediante metodo */
			jsonPosicionMasivo.put("claveInterna", creaCveInterna(idPosicion, 2) );
			
			/* -- Se obtienen directo de JSON */
			jsonPosicionMasivo.put("idAmbitoGeografico", mapJson.get("idAmbitoGeografico"));
			jsonPosicionMasivo.put("idNivelJerarquico", mapJson.get("idNivelJerarquico"));
			jsonPosicionMasivo.put("idTipoJornada", mapJson.get("idTipoJornada"));
			jsonPosicionMasivo.put("idTipoContrato", mapJson.get("idTipoContrato"));
			jsonPosicionMasivo.put("idTipoPrestacion", mapJson.get("idTipoPrestacion"));
			jsonPosicionMasivo.put("idPeriodicidadPago", mapJson.get("idPeriodicidadPago"));
			jsonPosicionMasivo.put("nombre", mapJson.get("nombre"));
			jsonPosicionMasivo.put("otros", mapJson.get("otros"));
			jsonPosicionMasivo.put("sueldoNegociable", mapJson.get("sueldoNegociable"));
			jsonPosicionMasivo.put("salarioMin", mapJson.get("salarioMin"));
			jsonPosicionMasivo.put("salarioMax", mapJson.get("salarioMax"));
			jsonPosicionMasivo.put("esConfidencial", mapJson.get("esConfidencial"));
			jsonPosicionMasivo.put("nombreEmpresa", mapJson.get("nombreEmpresa"));
			jsonPosicionMasivo.put("experienciaLaboralMinima", mapJson.get("experienciaLaboralMinima"));
			jsonPosicionMasivo.put("idGradoAcademicoMin", mapJson.get("idGradoAcademicoMin"));
			jsonPosicionMasivo.put("idEstatusEscolarMin", mapJson.get("idEstatusEscolarMin"));
			jsonPosicionMasivo.put("idGradoAcademicoMax", mapJson.get("idGradoAcademicoMax"));
			jsonPosicionMasivo.put("idEstatusEscolarMax", mapJson.get("idEstatusEscolarMax"));
			jsonPosicionMasivo.put("idTipoGeneroRequerido", mapJson.get("idTipoGeneroRequerido"));
			jsonPosicionMasivo.put("idEstadoCivilRequerido", mapJson.get("idEstadoCivilRequerido"));
			jsonPosicionMasivo.put("edadMinima", mapJson.get("edadMinima"));
			jsonPosicionMasivo.put("edadMaxima", mapJson.get("edadMaxima"));
			
			/* **  Arreglos */
			jsonPosicionMasivo.put("localizacion", transFormLocalizacion(jsonArrDomicilio) );
			jsonPosicionMasivo.put("perfilesInternos", jsonArrPerfilesInternos );//TODO remover atributos no utilizados
			jsonPosicionMasivo.put("perfilesExternos", jsonArrPerfilesExternos );
			jsonPosicionMasivo.put("politicaEscolaridads", jsonArrPoliticaEscolaridads );
			
			
			JSONArray jsMasivoPosicionFinal = new JSONArray();
			jsMasivoPosicionFinal.put(jsonPosicionMasivo);
			
			log4j.debug("JSON masivo creado: " + jsMasivoPosicionFinal.toString());
			
			String filePath = DIR_JSON_OUT +"PosicionMasivo-"+idPosicion+".json";
			
			ClientRestUtily.writeStringInFile(filePath, jsMasivoPosicionFinal.toString(), false);
			stBuilder.append("<OK> Se creo el archivo " + filePath );
		}
		
		return stBuilder;
	}
	
	/**
	 * Procesa un archivo de respaldo JSON para formar un nuevo archivo de JSON.masivo
	 * @param archivoPersona
	 * @return
	 * @throws JSONException
	 * @throws Exception
	 */
	public static StringBuilder procesaArchivoPersona(String archivoPersona) throws JSONException, Exception{
		StringBuilder stBuilder = new StringBuilder();
		log4j.debug("paso 1) cadena de persona: ");
		String stPersona = ClientRestUtily.getJsonFile(archivoPersona, DIR_JSON_PERSONA);
		JSONObject jsonPersonaMasivo = null;
		
		
		log4j.debug(stPersona);
		if(stPersona.equals("[]") ){
			log4j.debug("NO EXISTE ARCHIVO JSON (persona) o es invalido");
			stBuilder.append("NO EXISTE ARCHIVO JSON (persona) o es invalido");
		}else{
			log4j.debug("paso 2) Persona Se convierte a objeto json ");
			JSONArray jsonResponse = new JSONArray(stPersona);
			
			JSONObject jsonPersonaTmp = jsonResponse.getJSONObject(0);
			
			JSONArray jsonArrDomicilio = null;
			JSONArray jsonArrHabilidades = null;
			JSONArray jsonArrExperiencias = null;
			JSONArray jsonArrEscolaridades = null;
			
			String idPersona = jsonPersonaTmp.getString("idPersona");
			
			log4j.debug("paso 3) Se obtienen campos satelite requeridos (domicilio, experienciaLaboral, escolaridad)");
			
//			if(stPersona.indexOf("\"domicilio\"")!=-1){
//				jsonArrDomicilio = jsonPersonaTmp.getJSONArray("domicilio");
//			}
			if(stPersona.indexOf("\"localizacion\"")!=-1){
				jsonArrDomicilio = jsonPersonaTmp.getJSONArray("localizacion");
			}
			if(stPersona.indexOf("\"experienciaLaboral\"")!=-1){
				jsonArrExperiencias = jsonPersonaTmp.getJSONArray("experienciaLaboral");
			}
			if(stPersona.indexOf("\"escolaridad\"")!=-1){
				jsonArrEscolaridades = jsonPersonaTmp.getJSONArray("escolaridad");
			}
			if(stPersona.indexOf("\"habilidad\"")!=-1){
				jsonArrHabilidades = jsonPersonaTmp.getJSONArray("habilidad");
			}
			
			/*  VALIDACION EXTRA PARA EVALUAR SI TIENE LO NECESARIO  */
			if(jsonArrDomicilio == null || jsonArrExperiencias == null || jsonArrEscolaridades == null){
				// || jsonArrHabilidades == null
				stBuilder.append("NO EXISTE ALGUN ELEMENTO NECESARIO"); 
				return stBuilder;
			}
			
			log4j.debug("paso 4) se crea nuevo Json con los atributos requeridos"); 
			jsonPersonaMasivo = new JSONObject();
			
			Map<String, Object> mapJson = JsonUtily.mapFromJson(jsonPersonaTmp); //SE USA MAP PARA EVITAR EXCEPCION CON NULL ALL BUSCAR OBJETO
			
			/* -- Obtenidos mediante metodo */
			jsonPersonaMasivo.put("claveInterna", creaCveInterna(idPersona, 1) );
			jsonPersonaMasivo.put("stPais", getPaisId(jsonPersonaTmp.getLong("idPais")) );
			jsonPersonaMasivo.put("edad", calculaEdad(jsonPersonaTmp.getString("anioNacimiento")+"/"+ jsonPersonaTmp.getString("mesNacimiento") ));
								
			/* -- Se obtienen directo de JSON */
			jsonPersonaMasivo.put("idEstadoCivil", mapJson.get("idEstadoCivil") );
			jsonPersonaMasivo.put("permisoTrabajo", mapJson.get("permisoTrabajo") );				
			jsonPersonaMasivo.put("cambioDomicilio", mapJson.get("cambioDomicilio") );
			jsonPersonaMasivo.put("idTipoDispViajar", mapJson.get("idTipoDispViajar") );
			jsonPersonaMasivo.put("disponibilidadHorario", mapJson.get("disponibilidadHorario") );
			jsonPersonaMasivo.put("idTipoJornada", mapJson.get("idTipoJornada") );
			jsonPersonaMasivo.put("salarioMin", mapJson.get("salarioMin") );
			jsonPersonaMasivo.put("salarioMax", mapJson.get("salarioMax") );
			jsonPersonaMasivo.put("idTipoGenero", mapJson.get("idTipoGenero") );
			jsonPersonaMasivo.put("email", mapJson.get("email") );
			
			/* **  Arreglos */
			jsonPersonaMasivo.put("experienciaLaboral", jsonArrExperiencias );//TODO remover campos no utilizados
			jsonPersonaMasivo.put("escolaridad", jsonArrEscolaridades );//TODO remover campos no utilizados
			jsonPersonaMasivo.put("localizacion", transFormLocalizacion(jsonArrDomicilio) );
			jsonPersonaMasivo.put("habilidad", jsonArrHabilidades );//TODO remover campos no utilizados
			
			JSONArray jsMasivoPersonaFinal = new JSONArray();
			jsMasivoPersonaFinal.put(jsonPersonaMasivo);
			
			log4j.debug("JSON masivo creado: " + jsMasivoPersonaFinal.toString());
			
			String filePath = DIR_JSON_OUT +"PersonaMasivo-"+idPersona+".json";
			
			ClientRestUtily.writeStringInFile(filePath, jsMasivoPersonaFinal.toString(), false);
			stBuilder.append("<OK> Se creo el archivo " + filePath );
		}
		
		return stBuilder;
	}
	
	/**
	 * Convierte id's de DOmicilio a Cadenas (Descripcion) en base a catalogo
	 * @param jsonArrDomicilio
	 * @return
	 * @throws JSONException
	 */
	private static JSONArray transFormLocalizacion(JSONArray jsonArrDomicilio) throws JSONException{
		JSONObject jsonLocal = null;
		JSONArray jsArrLocalizacion = new JSONArray();
		
		String idAsentamiento, idEstado, idMunicipio, idPais;
		
		for(int x=0; x<jsonArrDomicilio.length();x++){
			JSONObject jsTmpDom = jsonArrDomicilio.getJSONObject(x);
			jsonLocal = new JSONObject();
			if(jsTmpDom.toString().contains("idAsentamiento")){
				idAsentamiento = String.valueOf(jsTmpDom.get("idAsentamiento"));
				jsonLocal.put("stColonia", CatalogoExcellUtily.getDescription(idAsentamiento, "Asentamiento"));
			}
			if(jsTmpDom.toString().contains("idEstado")){
				idEstado = String.valueOf(jsTmpDom.get("idEstado"));
				jsonLocal.put("stEstado", CatalogoExcellUtily.getDescription(idEstado, "Estado"));
			}
			if(jsTmpDom.toString().contains("idMunicipio")){
				idMunicipio = String.valueOf(jsTmpDom.get("idMunicipio"));
				jsonLocal.put("stMunicipio", CatalogoExcellUtily.getDescription(idMunicipio, "Municipio"));
			}
			if(jsTmpDom.toString().contains("idPais")){
				idPais = String.valueOf(jsTmpDom.get("idPais"));
				jsonLocal.put("stPais", CatalogoExcellUtily.getDescription(idPais, "Pais"));
			}
			if(jsTmpDom.toString().contains("codigoPostal")){
				jsonLocal.put("codigoPostal", jsTmpDom.get("codigoPostal"));
			}
			jsArrLocalizacion.put(jsonLocal);	
		}
		
		JSONArray jsArrayLoca = new JSONArray();
		jsArrayLoca.put(jsonLocal);
		return jsArrayLoca;
	}
	
	
	/**
	 * Calcula la edad utilizando la utileria de Date, en base a fecha nacimiento
	 * @param stNacimiento
	 * @return
	 */
	protected static String calculaEdad(String stNacimiento){
		Integer edad = DateUtily.calcularEdad(stNacimiento+"/01", "yyyy/MM/dd");
		return String.valueOf(edad);
	}
	
	/**
	 * Obtiene el nombre del pais utilizando la utileria de catalogo
	 * @param idCatalogo
	 * @return
	 */
	private static String getPaisId(Long idCatalogo){
		String nmPais = "";
		if(idCatalogo!=null){
			nmPais = CatalogoExcellUtily.getDescription(String.valueOf(idCatalogo), "Pais");
		}
		return nmPais;
	}

	/**
	 * Crea una clave interna con la convención: <b>TCE?BD<b>+<i>IdObj</i>
	 * @param idObj
	 * @param tipo 1-Persona (P), 2-Vacante (V)
	 * @return
	 */
	protected static String creaCveInterna(String idObj, int tipo){
		//1 persona, 2 posicion
		if(tipo==1){
			return "TCEPDB"+idObj;
		}else{
			return "TCEVDB"+idObj;
		}
	}
	
	
	/**
	 * Procesa multiples archivos de respaldo JSON para obtener JSON-Masivo's
	 */
	public static void procesaMultiples() {
		StringBuilder sbMultiple = new StringBuilder();
		List<String> lsPersonasJs = new ArrayList<String>();
		lsPersonasJs.add("read-15.json");
		lsPersonasJs.add("read-16.json");
		lsPersonasJs.add("read-17.json");
		lsPersonasJs.add("read-18.json");
		lsPersonasJs.add("read-19.json");		
		lsPersonasJs.add("read-20.json");
		
		lsPersonasJs.add("read-21.json");
		lsPersonasJs.add("read-22.json");
		lsPersonasJs.add("read-23.json");
		lsPersonasJs.add("read-24.json");
		lsPersonasJs.add("read-25.json");
		lsPersonasJs.add("read-26.json");
		lsPersonasJs.add("read-27.json");
		lsPersonasJs.add("read-28.json");
		lsPersonasJs.add("read-29.json");
		lsPersonasJs.add("read-30.json");
		
		lsPersonasJs.add("read-31.json");
		lsPersonasJs.add("read-32.json");
		lsPersonasJs.add("read-33.json");
		lsPersonasJs.add("read-34.json");
		lsPersonasJs.add("read-35.json");
		lsPersonasJs.add("read-36.json");
		lsPersonasJs.add("read-37.json");
		lsPersonasJs.add("read-38.json");
		lsPersonasJs.add("read-39.json");
		lsPersonasJs.add("read-40.json");
		
		lsPersonasJs.add("read-41.json");
		lsPersonasJs.add("read-42.json");
		lsPersonasJs.add("read-43.json");
		lsPersonasJs.add("read-44.json");
		lsPersonasJs.add("read-45.json");
		lsPersonasJs.add("read-46.json");
		lsPersonasJs.add("read-47.json");
		lsPersonasJs.add("read-48.json");
		lsPersonasJs.add("read-49.json");
		lsPersonasJs.add("read-50.json");
		
		Iterator<String> itPersonaJs = lsPersonasJs.iterator();
		String fileName = "";
		while(itPersonaJs.hasNext()){
			try {
				fileName = itPersonaJs.next();
				sbMultiple.append(" ==> ").append(fileName).append(":\n ==>");
				sbMultiple.append( procesaArchivoPersona(fileName) ).append("\n \n");
				log4j.debug("\n ---------------------------------------------------------------------\n");
			} catch (Exception e) {
				log4j.error("Error al convertir json: " + fileName, e);
				sbMultiple.append("<Error> Error al convertir: "+e.toString()).append("\n \n");
				e.printStackTrace();
			}
		}
		log4j.debug("Resultado Masivo: \n"+sbMultiple.toString());
	}
	
	
	/* **************************************************************************************** */
	
	public static void main(String[] args) {
		try {
//			procesaArchivoPersona("read-33.json");			
//			procesaArchivoPosicion("position-11.json");
			procesaMultiples();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
