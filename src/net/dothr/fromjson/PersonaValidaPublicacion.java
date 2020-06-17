package net.dothr.fromjson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.utils.ClientRestUtily;
import net.utils.ConstantesREST;
import net.utils.DateUtily;

/**
 * Esta clase hace una "preverificacion" de datos, 
 * realiza un analisis de elementos y Si determina que estos son suficientes para el calculo de Indices,
 * complementa los faltantes para realizar la publicación por sistema (Reemplaza archivo .json original)
 * @author dothr
 *
 */
public class PersonaValidaPublicacion extends PersonaFromJson {

	
	static Logger log4j = Logger.getLogger( PersonaValidaPublicacion.class );
	private static final String DIR_REPORTE_PUBLICACION = LOCAL_HOME+"/JsonUI/Report/";
	private static final String NOMBRE_ARCHIVO = "PrePublicacionMultiple"+fechaArchivo()+".txt";
	
	private static List<String> lsReemplazados, lsIncompletos, lsActivoNull, lsInscrito;
	
	private static final String DIRECTORIO_JSON = ConstantesREST.JSON_HOME+
			"PersonaRecreate/curriculumManagement/3.aws(P.RH)NoP/";
			//"PersonaRecreate/curriculumManagement/";
	
	
	private static String[] repTokens = {"Licenciatura ","Ingenieria ", };
		
	public static void main(String[] args) {
		try {
//			ping(null);
//			prePublicaSt("read-672.json");
			prePublicaMultiple(lsFilesPersona());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void prePublicaSt(String archivoPersona){
		try{

			lsReemplazados = new ArrayList<String>();
			lsActivoNull = new ArrayList<String>();
			lsIncompletos = new ArrayList<String>();
			lsInscrito = new ArrayList<String>();
//			String asterisco = "";
			
			JSONObject jsonPersona = getJsonObject(archivoPersona, DIRECTORIO_JSON);
			jsonPersona.put("fileName", archivoPersona);
			prePublica(jsonPersona);
		}catch (Exception e){
			log4j.debug("<Exception>", e);
		}
	}
	
	/**
	 * Realiza el proceso con la lista de Archivos
	 * @param lsArchivos
	 */
	public static void prePublicaMultiple(List<String> lsArchivos){
		log4j.debug("<prePublicaMultiple>");
		StringBuilder sbMultiple = new StringBuilder("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n")
		 .append("++++++++++++++++++++++++++++++++++++  PersonaValidaPublicacion.prePublicaMultiple  +++++++++++++++++++++++++++++++++ \n")
		 .append("+ DIRECTORIO_JSON= ").append(DIRECTORIO_JSON).append("\n")
		 .append("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n\n");
		
		Iterator<String> itArchivos = lsArchivos.iterator();
		String archivoPersona = "";
		JSONObject jsonPersona;
		lsReemplazados = new ArrayList<String>();
		lsActivoNull = new ArrayList<String>();
		lsIncompletos = new ArrayList<String>();
		lsInscrito = new ArrayList<String>();
		
		while(itArchivos.hasNext()){
			try{
				archivoPersona = itArchivos.next();
				jsonPersona = getJsonObject(archivoPersona, DIRECTORIO_JSON);
				jsonPersona.put("fileName", archivoPersona);
				sbMultiple.append(prePublica(jsonPersona)).append("\n");
			}catch (Exception e){
				log4j.fatal("<Excepcion> Error al procesar: " + archivoPersona, e);
				sbMultiple.append("<Excepcion> Error al procesar: " + archivoPersona).append(e.getMessage()).append("\n");
			}			
		}
		
		sbMultiple.append("\n\n***************************************\n")
			.append("[S] Success (completo-publicable), [F] Fail (inCompleto), [I] Inscribed (no Validado) \n")
			.append("Reemplazados/Completados [").append(lsReemplazados.size()).append("]: \n").append(lsReemplazados).append("\n") 
			.append("\n Incompletos [").append(lsIncompletos.size()).append("]: \n").append(lsIncompletos).append("\n")
			.append("\n ActivosNull [").append(lsActivoNull.size()).append("]: \n").append(lsActivoNull).append("\n")
			.append("\n Inscritos [").append(lsInscrito.size()).append("]: \n").append(lsInscrito).append("\n");
		ClientRestUtily.writeStringInFile(DIR_REPORTE_PUBLICACION+NOMBRE_ARCHIVO, sbMultiple.toString(), false );
		log4j.debug("<prePublicaMultiple> Fin de Proceso.\n Reporte se archiva en "+DIR_REPORTE_PUBLICACION+NOMBRE_ARCHIVO);
	}
	
	/**
	 * Proceso que realiza una "preVerificacion" de los requeridos
	 * si estos son suficientes, corrige las deficiencias con cadenas estandar
	 * Si no lo son, no realiza ningun cambio para publicación
	 * @param jsonPersona
	 * @return
	 * @throws Exception
	 */
	private static StringBuilder prePublica(JSONObject jsonPersona) throws Exception{
		log4j.debug("<prePublica>");
		log4j.debug("<prePublica> fileName: "+jsonPersona.getString("fileName"));
		StringBuilder sbProcess = new StringBuilder();

		if(WEB_RESOURCE.equals(ConstantesREST.WEB_TRANSACT_AWS) 
				&& !PERMITIR_CARGA_EN_AWS)
				{
				log4j.fatal("Esta apuntando a AWS y no esta permitido "+WEB_RESOURCE, new NullPointerException() );
				sbProcess.append(" Esta apuntando a AWS y no esta permitido: "+WEB_RESOURCE).append("\n");
		}else{
			//Valida Existencia de datos:
			String idPersona = jsonPersona.getString("email");//jsonPersona.getString("idPersona");
			String email = jsonPersona.getString("email");
			sbProcess.append("*************** >> PRE-Validacion PARA PERSONA ").append(idPersona).append(" << *************** \n");
			
			JSONArray jsonArrDomicilio = null, jsonArrContactos = null, 
					jsonArrExperiencias = null, jsonArrEscolaridades = null, jsonArrHabilidades = null;
			
			sbProcess.append("Email: ").append(email)
			.append(", IdPersona: ").append(idPersona)
			.append(", idEstatusInscripcion: ").append(jsonPersona.getString("idEstatusInscripcion"))
			.append(" \n");
			log4j.debug("<procesaJsonPersona> Se obtienen campos satelite (domicilio, contacto, experienciaLaboral, escolaridad)");
			if(jsonPersona.has("localizacion")){
				jsonArrDomicilio = jsonPersona.getJSONArray("localizacion");
			}
			if(jsonPersona.has("contacto")){
				jsonArrContactos = jsonPersona.getJSONArray("contacto");
			}
			if(jsonPersona.has("escolaridad")){
				jsonArrEscolaridades = jsonPersona.getJSONArray("escolaridad");
			}
			if(jsonPersona.has("experienciaLaboral")){
				jsonArrExperiencias = jsonPersona.getJSONArray("experienciaLaboral");
			}
			if(jsonPersona.has("habilidad")){
				jsonArrHabilidades = jsonPersona.getJSONArray("habilidad");
			}
			
			log4j.debug("Procesar arreglos :");
			/* PROCESA ARREGLOS REQUERIDOS */
			jsonArrEscolaridades= validaEscolaridad(jsonArrEscolaridades);
//			log4j.debug("jsonArrEscolaridades: \n" + jsonArrEscolaridades );
			sbProcess.append(" >jsonArrEscolaridades (Procesados): ").append(
					jsonArrEscolaridades!=null?jsonArrEscolaridades.length():0
					).append("\n");
			
			jsonArrExperiencias = validaExperienciaLabo(jsonArrExperiencias);
//			log4j.debug("jsonArrExperiencias: \n" + jsonArrExperiencias );
			sbProcess.append(" >jsonArrExperiencias (Procesados): ").append(
					jsonArrExperiencias!=null?jsonArrExperiencias.length():0
					).append("\n");
			
			
			/* SI TIENE LOS REQUERIDOS POR INDICES DE APEGO A PERFIL (IAP) SE COMPLETA */
			if(jsonArrEscolaridades!=null || jsonArrExperiencias !=null){
				log4j.debug("<prePublica> CV SI tiene los requeridos por indices de apego a perfil (iap) se completa");
				sbProcess.append("[S] CV ID ").append(idPersona).append(" SI tiene los requeridos por indices de apego a perfil (iap) se completa \n");
				jsonPersona.put("localizacion", completaDomicilio(jsonArrDomicilio)); //jsonArrDomicilio = completaDomicilio(jsonArrDomicilio);
				jsonPersona.put("contacto", completaContacto(jsonArrContactos)); //jsonArrContactos = completaContacto(jsonArrContactos);
				jsonPersona.put("escolaridad", jsonArrEscolaridades );
				jsonPersona.put("experienciaLaboral", jsonArrExperiencias );
				jsonPersona.put("habilidad", validaHabilidad(jsonArrHabilidades)); //jsonArrHabilidades = validaHabilidad(jsonArrHabilidades);
//				log4j.debug("jsonArrHabilidades: \n" + jsonArrHabilidades );
				
				/* Si tiene datos para indices, se completan datos requeridos de persona */
				if(!jsonPersona.has("idEstadoCivil")){ jsonPersona.put("idEstadoCivil", "1"); /*Default Soltero */ }
				if(!jsonPersona.has("idTipoDispViajar")){ jsonPersona.put("idTipoDispViajar", "2"); /*Default ocasionalmente */ }
				if(!jsonPersona.has("nombre")){ jsonPersona.put("nombre", "NoCapturado"); }//email.substring(0, email.indexOf("@")) ); /*Default email */ }
				if(!jsonPersona.has("apellidoPaterno")){ jsonPersona.put("apellidoPaterno", "NO CAPTURADO" ); /*Default  */ }
				if(!jsonPersona.has("idTipoGenero")){ jsonPersona.put("idTipoGenero", "0" ); /*Default Femenino */ }
				if(!jsonPersona.has("permisoTrabajo")){ jsonPersona.put("permisoTrabajo", "1" ); /*Default Si */ }
				if(!jsonPersona.has("cambioDomicilio")){ jsonPersona.put("cambioDomicilio", "0" ); /*Default No */ }
				if(!jsonPersona.has("disponibilidadHorario")){ jsonPersona.put("disponibilidadHorario", "1" ); /*Default Si */ }
				if(!jsonPersona.has("anioNacimiento")){	jsonPersona.put("anioNacimiento", "1995"); /*Default 1995*/ }
				if(!jsonPersona.has("mesNacimiento")){	jsonPersona.put("mesNacimiento", "1"); /*Default Enero*/ }
				
				if(!jsonPersona.has("salarioMin")){
					if(jsonPersona.has("salarioMax")){
						Long salMax = Long.parseLong(jsonPersona.getString("salarioMax"));
						jsonPersona.put("salarioMin", String.valueOf(salMax-10) );
					}else{
						jsonPersona.put("salarioMin", String.valueOf(1000) );/*Default $1000 */
					}
				}
				if(!jsonPersona.has("salarioMax")){
//					if(jsonPersona.has("salarioMin")){
						Long salMin = Long.parseLong(jsonPersona.getString("salarioMin"));
						jsonPersona.put("salarioMax", String.valueOf(salMin+10) );
//					}					
				}
				
				//UNA Vez completado se exporta a Archivo JSON Original
				sbProcess.append("se exporta a Archivo JSON Original: ").append(jsonPersona.getString("fileName")).append("\n");
				log4j.debug("<prePublica> se exporta a Archivo JSON Original: " + jsonPersona.getString("fileName"));
//				ClientRestUtily.writeStringInFile(DIRECTORIO_JSON+jsonPersona.getString("fileName"), "["+ jsonPersona.toString()+"]", false);
				
				ClientRestUtily.writeStringInFile(DIRECTORIO_JSON+jsonPersona.getString("fileName"), 
						formatJsonPersona(jsonPersona, true)
						, false);
				lsReemplazados.add(jsonPersona.getString("fileName"));
				
				
			}else{
				log4j.debug("<prePublica> EL curriculum "+ jsonPersona.getString("fileName") + " NO TIENE suficientes datos para validar");
				if(jsonPersona.getString("idEstatusInscripcion").equals("1")){
					sbProcess.append("[I] El CV NO SE HA VALIDADO (estatus: Inscrito) \n");
					lsInscrito.add(jsonPersona.getString("fileName"));
				}
				if(jsonPersona.getString("idEstatusInscripcion").equals("2")){
					if(isActiveNotNull(jsonPersona)){
						sbProcess.append("[F] CAPTURO DATOS, PERO NO TIENE LOS REQUERIDOS POR INDICES DE APEGO A PERFIL \nSe OMITE REEMPLAZO: ").append(jsonPersona.getString("fileName")).append("\n");
						lsIncompletos.add(jsonPersona.getString("fileName"));
					}else{
						sbProcess.append("[F] El Usuario no ha Ingresado al Sistema (estatus: Activo) \nse Omite Reemplazo: ").append(jsonPersona.getString("fileName")).append("\n");
						lsActivoNull.add(jsonPersona.getString("fileName"));
					}
					
				}
				/* Descomentar para formatear todos */
//				ClientRestUtily.writeStringInFile(DIRECTORIO_JSON+jsonPersona.getString("fileName"), 
//						formatJsonPersona(jsonPersona, true)
//						, false);				
			}
		}
		
		return sbProcess;				
	}
	
	/**
	 * Valida si ha capturado al menos información personal
	 * <br><b>false</b> SIgnifica que es ActiveNull (Sin accesar ningun dato)
	 * <br><b>true</b> significa que entro al sistema
	 * @param jsonPersona
	 * @return
	 */
	private static boolean isActiveNotNull(JSONObject jsonPersona){
		boolean notNull = false;
		/* Si tiene alguna de las propiedades, significa que entro al sistema */
		if(jsonPersona.has("nombre")){ notNull=true;}
		if(jsonPersona.has("apellidoPaterno")){ notNull=true;}
		if(jsonPersona.has("anioNacimiento")){ notNull=true;}
		if(jsonPersona.has("mesNacimiento")){ notNull=true;}
		if(jsonPersona.has("cambioDomicilio")){ notNull=true;}
		if(jsonPersona.has("salarioMin")){ notNull=true;}
		if(jsonPersona.has("salarioMax")){ notNull=true;}
		
		if(jsonPersona.has("idTipoDispViajar")){ notNull=true;}
		if(jsonPersona.has("disponibilidadHorario")){ notNull=true;}
		
		if(jsonPersona.has("localizacion")){ notNull=true;}
		if(jsonPersona.has("experienciaLaboral")){ notNull=true;}
		if(jsonPersona.has("escolaridad")){ notNull=true;}
		if(jsonPersona.has("contacto")){ notNull=true;}
		if(jsonPersona.has("habilidad")){ notNull=true;}
		
		return notNull;
	}
	
	/**
	 * Complementa o genera la información de contacto
	 * @param jsonArrContactos
	 * @return
	 * @throws JSONException
	 */
	private static JSONArray completaContacto(JSONArray jsonArrContactos) throws JSONException{
		JSONArray newJsContactos = new JSONArray();
		
		if(jsonArrContactos==null){
			JSONObject contacto = new JSONObject();
			contacto.put("idContacto", "9999");
			contacto.put("idTipoContacto", "11");
			contacto.put("numero", "99999999");
			newJsContactos.put(contacto);
		}else{
			newJsContactos = jsonArrContactos;
		}
		
		return newJsContactos;
	}
	/**
	 * Completa o genera nuevo domicilio
	 * (este metodo debe llamarse despues de validar los requeridos para IAP)
	 * @param jsonArrDomicilio
	 * @return
	 * @throws JSONException 
	 */
	private static JSONArray completaDomicilio(JSONArray jsonArrDomicilio) throws JSONException{
		JSONArray newJsDomicilios = new JSONArray();
		JSONObject domicilio;
		if(jsonArrDomicilio!=null && jsonArrDomicilio.length()>0){
			//valida y completa el primero
			domicilio = jsonArrDomicilio.getJSONObject(0);
		}else{
			domicilio = new JSONObject();
			//TODO generar de Arreglo de DOmicilios aleatorios
		}
		if(!domicilio.has("idDomicilio") ){ domicilio.put("idDomicilio","124"); }
		if(!domicilio.has("codigoPostal") ){ domicilio.put("codigoPostal","14400"); }
		if(!domicilio.has("idPais") ){ domicilio.put("idPais","1"); }
		if(!domicilio.has("idEstado") ){ domicilio.put("idEstado","9"); }
		if(!domicilio.has("idMunicipio") ){ domicilio.put("idMunicipio","276"); }
		if(!domicilio.has("idTipoDomicilio") ){ domicilio.put("idTipoDomicilio","1"); }
		if(!domicilio.has("googleLatitude") || !domicilio.has("googleLongitude") || !domicilio.has("idAsentamiento")){ 
			domicilio.put("googleLatitude","19.2535822"); 
			domicilio.put("googleLongitude","-99.17264929999999"); 
			domicilio.put("idAsentamiento","27026");
		}
		if(!domicilio.has("calle") ){ domicilio.put("calle","Dom. conocido"); }
		if(!domicilio.has("numeroExterior") ){ domicilio.put("numeroExterior","1"); }
		
		newJsDomicilios.put(domicilio);
		
		return newJsDomicilios;
	}
	
	/**
	 * Valida Arreglo de Habilidades, si no tiene Descripcion, lo elimina del arreglo
	 * @param jsonArrHabilidades
	 * @return
	 * @throws JSONException
	 */
	private static JSONArray validaHabilidad(JSONArray jsonArrHabilidades) throws JSONException{
		JSONArray newJsHability = null;
		/* Para Doc.Class (Solr) , usa Descripcion */
		if(jsonArrHabilidades!=null && jsonArrHabilidades.length()>0){
			newJsHability = new JSONArray();
			JSONObject jsHab;
			for(int z=0; z<jsonArrHabilidades.length();z++){
				jsHab = jsonArrHabilidades.getJSONObject(z);
				if(jsHab.has("descripcion")){
					if(!jsHab.has("idDominio")){
						jsHab.put("idDominio", "3"); //Default Bueno
					}
					newJsHability.put(jsHab);
				}
			}
			if(newJsHability.length()<1){
				newJsHability = null;
			}
		}
		return newJsHability;
	}
	
	/**
	 * Valida Arreglo de escolaridades, valida si es Diferente de null
	 * y si tiene elementos, los completa o elimina vacios 
	 * @param jsonArrEscolaridades
	 * @return
	 * @throws JSONException 
	 */
	private static JSONArray validaEscolaridad(JSONArray jsonArrEscolaridades) throws JSONException{
		JSONArray newJsEscolaridades = null;
		/* Para Doc.Class (Solr) , usa Titulo y Texto */
		if(jsonArrEscolaridades!=null && jsonArrEscolaridades.length()>0){
			newJsEscolaridades = new JSONArray();
			//Itera para completar/eliminar
			JSONObject jsEsc = null;
			String titulo, texto, stGrado = "";
			Long anioAux;
			for(int x=0;x<jsonArrEscolaridades.length();x++){
				jsEsc = jsonArrEscolaridades.getJSONObject(x);	
				//Según los parametros SOlr-Requeridos, se validan/completan
				if((jsEsc.has("titulo") && !jsEsc.getString("titulo").trim().equals("") )
						|| (jsEsc.has("texto") && !jsEsc.getString("texto").trim().equals("") )){
					
					//Completar si alguno requerido es Vacio
					if(!jsEsc.has("titulo") || jsEsc.getString("titulo").trim().equals("") ){
						if(jsEsc.has("idGradoAcademico")){
							stGrado = lbCatalogo(jsEsc.getString("idGradoAcademico"), "Grado");
						}
						titulo = stGrado+jsEsc.getString("texto");
						jsEsc.put("titulo", (titulo.length()>60? titulo.substring(0, 60):titulo) );//Reducir a Texto
					}else if(!jsEsc.has("texto") || jsEsc.getString("texto").trim().equals("")){
							texto = jsEsc.getString("titulo");//Titulo es menor que Texto
							jsEsc.put("texto", ClientRestUtily.stopWordsTce(texto, repTokens) ); 
					}
					//Completar los siguientes unitariamente
					if(!jsEsc.has("idGradoAcademico")){
						jsEsc.put("idGradoAcademico", "2"); //Default Carrera tecnica
					}
					if(!jsEsc.has("idEstatusEscolar")){
						jsEsc.put("idEstatusEscolar", "1"); //Default titulado
					}
					if(!jsEsc.has("nombreInstitucion")){
						jsEsc.put("nombreInstitucion", "No Registrada"); //Default 
					}
					if(!jsEsc.has("idPais")){
						jsEsc.put("idPais", "1"); //Default Mexico 
					}
					/*Verificación y re-Calculo de rango de fechas */
					//a) Fecha Inicio incompleta o vacia
					if(!jsEsc.has("anioInicio") || !jsEsc.has("mesInicio")){
						if(!jsEsc.has("mesInicio")){ //Si tiene mes Inicio, el mes por defaul es Enero (1)
							jsEsc.put("mesInicio", "1");
						}
						
						if(!jsEsc.has("anioInicio") && jsEsc.has("anioFin")){// Si no tiene año inicio, se calcula uno menos de añoFin
							anioAux= Long.parseLong(jsEsc.getString("anioFin"));
							jsEsc.put("anioInicio", String.valueOf(anioAux-1) );
						}else {
							//Si no tiene año fin, Año inicio se calcula en base al numero de Escolaridades (nExp) y el indice (x)
							int nExp = jsonArrEscolaridades.length();
							anioAux = new Long(2016 -(nExp*x));
							
							jsEsc.put("anioInicio", String.valueOf(anioAux-1) );
							jsEsc.put("anioFin", String.valueOf(anioAux) );
						}						
					}					
					if(!jsEsc.has("fechaInicio") ){
						//A este punto ya hay Año y Mes inicio
						jsEsc.put("fechaInicio", jsEsc.getString("anioInicio")+"-"
								+(jsEsc.getString("mesInicio").length()<2?"0"+jsEsc.getString("mesInicio"):jsEsc.getString("mesInicio"))
								+"-01 00:00:00.0");	//1966-04-07 00:00:00.0
					}
					
					//b) Fecha Fin incompleta o vacia
					if(!jsEsc.has("anioFin") || !jsEsc.has("mesFin")){
						//Si explicitamente se indica Escolaridad en curso, se elimina año y mes Fin
						if(jsEsc.has("idEstatusEscolar") && jsEsc.getString("idEstatusEscolar").equals("3")){ //Solo siendo estudiante
							jsEsc.remove("anioFin");
							jsEsc.remove("mesFin");
						}
						
						else{//Si no es trabajo actual se calcula
							//Si NO tiene mes Fin, tiene AñoFin y no es actual, el mes por defaul es Diciembre (12)
							if(!jsEsc.has("anioFin")){ //No tiene año fin, por default es uno mayor al inicio
								anioAux= Long.parseLong(jsEsc.getString("anioInicio"));
								jsEsc.put("anioFin", String.valueOf(anioAux+1) );
							}
							
							if(!jsEsc.has("mesFin")){
								if(!jsEsc.getString("anioFin").equals("2016")){
									jsEsc.put("mesFin", "12");
								}else {
									jsEsc.put("mesFin", getThisMonth());
								}
							}
						}
					}
					if(!jsEsc.has("fechaFin") && jsEsc.has("anioFin") && jsEsc.has("mesFin")){						
						//A este punto ya hay Año y Mes (O es estudiante)
						jsEsc.put("fechaFin", jsEsc.getString("anioFin")+"-"
								+(jsEsc.getString("mesFin").length()<2?"0"+jsEsc.getString("mesFin"):jsEsc.getString("mesFin"))
								+"-28 00:00:00.0");	//1966-04-07 00:00:00.0
						log4j.debug("SE AGREGA FECHA FIN a escolaridad " + x);
					}
					
					
					/* Segmento si tiene alguna fecha *
					if(!jsEsc.has("fechaInicio") && jsEsc.has("fechaFin")){						
						//mesFin= Long.parseLong("mesFin");
						if(!jsEsc.has("anioInicio") ){
							anioAux= Long.parseLong(jsEsc.getString("anioFin"));
							jsEsc.put("anioInicio", String.valueOf(anioAux-2) );
						}
						if(!jsEsc.has("mesInicio") ){
							jsEsc.put("mesInicio", "1");
						}
					}
					if(!jsEsc.has("fechaFin") && jsEsc.has("fechaInicio")){						
						//mesFin= Long.parseLong("mesFin");
						if(!jsEsc.has("anioFin") ){
							anioAux= Long.parseLong(jsEsc.getString("anioInicio"));
							jsEsc.put("anioFin", String.valueOf(anioAux+1) );
						}
						if(!jsEsc.has("mesFin") ){
							jsEsc.put("mesFin", "11");
						}
					}					
					/ * Segmento si no tiene ninguna fecha, se ponen aleatorias * / //TODO hacerlas secuenciales si hay mas de una					
					if(!jsEsc.has("fechaInicio") && !jsEsc.has("fechaFin")){
						jsEsc.put("anioInicio", "2010" );
						jsEsc.put("mesInicio", "1" );
						
						jsEsc.put("anioFin", "2015" );
						jsEsc.put("mesFin", "11" );
					}
					/* Si al final alguno de fecha es null * /
					if(!jsEsc.has("anioInicio") || !jsEsc.has("mesInicio")){
						jsEsc.put("anioInicio", extractYear(jsEsc.getString("fechaInicio")) );
						jsEsc.put("mesInicio", extractMonth(jsEsc.getString("fechaInicio")) );
					}
					if(!jsEsc.has("anioFin") || !jsEsc.has("mesFin")){
						if(jsEsc.has("idEstatusEscolar") && jsEsc.getString("idEstatusEscolar").equals("3")){ //Solo siendo estudiante
							log4j.debug("Es estudiante actualmente, no es requerida fecha fin");
						}else{
							jsEsc.put("anioFin", extractYear(jsEsc.getString("fechaFin")) );
							jsEsc.put("mesFin", extractMonth(jsEsc.getString("fechaFin")) );
						}
					} */
					
					/* PARCHE para quitar ceros a la izquierda en mes*/
					if(jsEsc.has("mesInicio") && jsEsc.getString("mesInicio").startsWith("0")){ jsEsc.put("mesInicio", extractMonth(jsEsc.getString("fechaInicio")) ); }
					if(jsEsc.has("mesFin") && jsEsc.getString("mesFin").startsWith("0")){ jsEsc.put("mesFin", extractMonth(jsEsc.getString("fechaFin")) ); }
					//esValido=true;
					newJsEscolaridades.put(jsEsc);
				}
				else{ //SIno tiene ninguno de los anteriores, se elimina
					log4j.debug("<validaEscolaridad> se elimina esta Escolaridad "+x);
				}
			}//fin de FOr
			if(newJsEscolaridades.length()<1){
				newJsEscolaridades = null;
			}
		}
		return newJsEscolaridades;
	}
	
	/**
	 * Valida Arreglo de escolaridades, valida si es Diferente de null
	 * y si tiene elementos, los completa o elimina vacios 
	 * @param jsonArrEscolaridades
	 * @return
	 * @throws JSONException 
	 */
	private static JSONArray validaExperienciaLabo(JSONArray jsonArrExperiencias) throws JSONException{
		JSONArray newJsWorkExps = null;
		/* Para Doc.Class (Solr) , usa Puesto y Texto */
		if(jsonArrExperiencias!=null && jsonArrExperiencias.length()>0){
			newJsWorkExps = new JSONArray();
			//Itera para completar/eliminar
			JSONObject jsWorkExp = null;
			String puesto, texto, stLvJerarquico = "";
			Long anioAux;
			for(int x=0;x<jsonArrExperiencias.length();x++){
				jsWorkExp = jsonArrExperiencias.getJSONObject(x);
//				log4j.debug("jsWorkExp: \n" + ClientRestUtily.formtJsonByGoogle(jsWorkExp.toString()));
				//Según los parametros SOlr-Requeridos, se validan/completan
				if((jsWorkExp.has("puesto") && !jsWorkExp.getString("puesto").trim().equals("") )
						|| (jsWorkExp.has("texto") && !jsWorkExp.getString("texto").trim().equals("") )){
					
					/* Completar si alguno requerido es Vacio*/
					//Puesto es vacio:
					if(!jsWorkExp.has("puesto") || jsWorkExp.getString("puesto").trim().equals("") ){ 
						if(jsWorkExp.has("idNivelJerarquico")){
							stLvJerarquico = lbCatalogo(jsWorkExp.getString("idNivelJerarquico"), "Jerarquico");
						}
						puesto = stLvJerarquico+": "+jsWorkExp.getString("texto");
						jsWorkExp.put("puesto", (puesto.length()>60? puesto.substring(0, 60):puesto) );//Reducir a Texto
					} //Texto (Descripcion labores) es vacio
					else if(!jsWorkExp.has("texto") || jsWorkExp.getString("texto").trim().equals("")){
							texto = jsWorkExp.getString("puesto");//puesto es menor que Texto
							jsWorkExp.put("texto", ClientRestUtily.stopWordsTce(texto, repTokens) ); 
					}
					//Completar los siguientes unitariamente
					if(!jsWorkExp.has("idNivelJerarquico")){
						jsWorkExp.put("idNivelJerarquico", "1"); //Default Operario
					}
					if(!jsWorkExp.has("genteACargo")){
						jsWorkExp.put("genteACargo", "0"); //Default No
					}

					if(!jsWorkExp.has("idTipoJornada")){
						jsWorkExp.put("idTipoJornada", "1"); //Default Tiempo Completo
					}
					if(!jsWorkExp.has("nombreEmpresa")){
						jsWorkExp.put("nombreEmpresa", "No Registrada"); //Default 
					}
					if(!jsWorkExp.has("idPais")){
						jsWorkExp.put("idPais", "1"); //Default Mexico 
					}
					
					/*Verificación y re-Calculo de rango de fechas */
					//a) Fecha Inicio incompleta o vacia
					if(!jsWorkExp.has("anioInicio") || !jsWorkExp.has("mesInicio")){
						if(!jsWorkExp.has("mesInicio")){ //Si tiene mes Inicio, el mes por defaul es Enero (1)
							jsWorkExp.put("mesInicio", "1");
						}
						
						if(!jsWorkExp.has("anioInicio") && jsWorkExp.has("anioFin")){// Si no tiene año inicio, se calcula uno menos de añoFin
							anioAux= Long.parseLong(jsWorkExp.getString("anioFin"));
							jsWorkExp.put("anioInicio", String.valueOf(anioAux-1) );
						}else {
							//Si no tiene año fin, Año inicio se calcula en base al numero de Experiencias (nExp) y el indice de Experiencia (x)
							int nExp = jsonArrExperiencias.length();
							anioAux = new Long(2016 -(nExp*x));
							
							jsWorkExp.put("anioInicio", String.valueOf(anioAux-1) );
							jsWorkExp.put("anioFin", String.valueOf(anioAux) );
						}						
					}					
					if(!jsWorkExp.has("fechaInicio") ){
						//A este punto ya hay Año y Mes inicio
						jsWorkExp.put("fechaInicio", jsWorkExp.getString("anioInicio")+"-"
								+(jsWorkExp.getString("mesInicio").length()<2?"0"+jsWorkExp.getString("mesInicio"):jsWorkExp.getString("mesInicio"))
								+"-01 00:00:00.0");	//1966-04-07 00:00:00.0
					}
					/*
					if(!jsWorkExp.has("fechaInicio") && jsWorkExp.has("fechaFin")){
						if(jsWorkExp.has("trabajoActual") && jsWorkExp.getString("trabajoActual").equals("1") ){
							jsWorkExp.remove("anioFin");
							jsWorkExp.remove("mesFin");
						}else{
							//mesFin= Long.parseLong("mesFin");
							if(!jsWorkExp.has("anioInicio") ){
								anioAux= Long.parseLong(jsWorkExp.getString("anioFin"));
								jsWorkExp.put("anioInicio", String.valueOf(anioAux-1) );
							}
							if(!jsWorkExp.has("mesInicio") ){
								jsWorkExp.put("mesInicio", "1");
							}
							jsWorkExp.put("trabajoActual", "0");
						}
					}
					*/
					
					//b) Fecha Fin incompleta o vacia
					if(!jsWorkExp.has("anioFin") || !jsWorkExp.has("mesFin")){
						//Si explicitamente se indica trabajo actual, se elimina año y mes Fin
						if(jsWorkExp.has("trabajoActual") && jsWorkExp.getString("trabajoActual").equals("1") ){ 
							jsWorkExp.remove("anioFin");
							jsWorkExp.remove("mesFin");
						}
						
						else{//Si no es trabajo actual se calcula
							//Si NO tiene mes Fin, tiene AñoFin y no es actual, el mes por defaul es Diciembre (12)
							if(!jsWorkExp.has("anioFin")){ //No tiene año fin, por default es uno mayor al inicio
								anioAux= Long.parseLong(jsWorkExp.getString("anioInicio"));
								jsWorkExp.put("anioFin", String.valueOf(anioAux+1) );
							}
							
							if(!jsWorkExp.has("mesFin")){
								if(!jsWorkExp.getString("anioFin").equals("2016")){
									jsWorkExp.put("mesFin", "12");
								}else {
									jsWorkExp.put("mesFin", getThisMonth());
								}
							}
							
							jsWorkExp.put("trabajoActual", "0");//Al final se pone false trabajo actual
						}
					}

					if(!jsWorkExp.has("fechaFin") && (jsWorkExp.has("anioFin") && jsWorkExp.has("mesFin"))){						
						//A este punto ya hay Año y Mes 
						jsWorkExp.put("fechaFin", jsWorkExp.getString("anioFin")+"-"
								+(jsWorkExp.getString("mesFin").length()<2?"0"+jsWorkExp.getString("mesFin"):jsWorkExp.getString("mesFin"))
								+"-01 00:00:00.0");	//1966-04-07 00:00:00.0
						log4j.debug("SE AGREGA FECHA FIN a Experiencia "+x);
					}
					/*					
					if(!jsWorkExp.has("fechaFin") && jsWorkExp.has("fechaInicio")){						
						//mesFin= Long.parseLong("mesFin");
						if(!jsWorkExp.has("anioFin") ){
							anioAux= Long.parseLong(jsWorkExp.getString("anioInicio"));
							jsWorkExp.put("anioFin", String.valueOf(anioAux+1) );
						}
						if(!jsWorkExp.has("mesFin") ){
							jsWorkExp.put("mesFin", "11");
						}
					}
										
					if(!jsWorkExp.has("fechaInicio") && !jsWorkExp.has("fechaFin")){
						log4j.debug("<validaEscolaridad> Escolaridad no tiene fechas, se agregan aleatoria");
						jsWorkExp.put("anioInicio", "2010" );
						jsWorkExp.put("mesInicio", "1" );
						
						jsWorkExp.put("anioFin", "2015" );
						jsWorkExp.put("mesFin", "11" );
						
						jsWorkExp.put("trabajoActual", "0");
					}
					
					/* Si al final alguno de fecha es null * /
					if(!jsWorkExp.has("anioInicio") || !jsWorkExp.has("mesInicio")){
						jsWorkExp.put("anioInicio", extractYear(jsWorkExp.getString("fechaInicio")) );
						jsWorkExp.put("mesInicio", extractMonth(jsWorkExp.getString("fechaInicio")) );
					}
					if(!jsWorkExp.has("anioFin") || !jsWorkExp.has("mesFin")){
						if(jsWorkExp.has("trabajoActual") && jsWorkExp.getString("trabajoActual").equals("1")){
							log4j.debug("Es trabajo actual NO es requerido fechaFin");
						}else{
							jsWorkExp.put("anioFin", extractYear(jsWorkExp.getString("fechaFin")) );
							jsWorkExp.put("mesFin", extractMonth(jsWorkExp.getString("fechaFin")) );
						}
					}*/
					
					/* Parche para eliminar ceros */
					if(jsWorkExp.has("mesInicio") && jsWorkExp.getString("mesInicio").startsWith("0")){ jsWorkExp.put("mesInicio", extractMonth(jsWorkExp.getString("fechaInicio")) ); }
					if(jsWorkExp.has("mesFin") && jsWorkExp.getString("mesFin").startsWith("0")){ jsWorkExp.put("mesFin", extractMonth(jsWorkExp.getString("fechaFin")) ); }
					newJsWorkExps.put(jsWorkExp);
				}
				else{ //SIno tiene ninguno de los anteriores, se elimina
					log4j.debug("<validaEscolaridad> se elimina esta Experiencia " + x);
				}
			}//fin de FOr
			if(newJsWorkExps.length()<1){
				newJsWorkExps = null;
			}
		}
		return newJsWorkExps;
	}
	

	/**
	 * Lista de archivos para esta clase
	 * @return
	 */
	protected static List<String> lsFilesPersona(){
		List<String> lsIdPersona = 
				//getListaArchivosPersona(); 
				new ArrayList<String>();
//		lsIdPersona.add("read-1000.json");		
		
		lsIdPersona.add("jorgespra.json");
		
		
		return lsIdPersona;
	}
	
	
	
	/* *********************************************************************** */
	/* ************************    AUXILIARES (TODO crear utileria ******************************* */
	/* *********************************************************************** */
	
	/**
	 * Obtiene 
	 * @param stIdCat
	 * @param catalogo
	 * @return
	 */
	private static String lbCatalogo(String stIdCat, String catalogo){
		Integer idCat = Integer.parseInt(stIdCat);
		String label = "";
		if(catalogo.equals("Grado")){
			switch (idCat) {
			case 1:
				label = "";//"Secundaria / Preparatoria";
				break;
			case 2:
				label = "Técnico ";//"Carrera técnica / comercial";
				break;
			case 3:
				label = "";//"Diplomado / Cursos";
				break;
			case 4:
				label = "Lic. ";//"Licenciatura / ingeniería";
				break;
			case 5:
				label = "";//"Certificación";
				break;
			case 6:
				label = "Maestría ";
				break;
			case 7:
				label = "Doctorado ";
				break;
			}
		}
		else if(catalogo.equals("Jerarquico")){
			switch (idCat) {
			case 1:
				label = "Operario-obrero ";//"Secundaria / Preparatoria";
				break;
			case 2:
				label = "Vendedor- servicio al cliente ";//"Carrera técnica / comercial";
				break;
			case 3:
				label = "Asistente ";//"Diplomado / Cursos";
				break;
			case 4:
				label = "Analista-Consultor Jr ";//"Licenciatura / ingeniería";
				break;
			case 5:
				label = "Supervisor-Coordinador ";//"Certificación";
				break;
			case 6:
				label = "Líder de proyecto-Jefe ";
				break;
			case 7:
				label = "Gerente-Jefe de área ";
				break;
			case 8:
				label = "Director-Alta Gerencia ";
				break;
			}
		}
		return label;
	}	
	
	/**Obtiene año de fecha con formato 2006-11-15 06:00:00.0 */
	private static String extractYear(String fecha){
		return fecha.substring(0,4);
	}
	/**Obtiene mes de fecha con formato 2006-11-15 06:00:00.0 */
	private static String extractMonth(String fecha){
		String mes=fecha.substring(5,7);
		
		return mes.startsWith("0")?mes.substring(1):mes;
	}
	
	private static String getThisMonth(){
	 String fecha = DateUtily.date2String(new java.util.Date(), "YYYY-MM-DD");	
	 return extractMonth(fecha+" 00:00:00.0");
	}
	
}
