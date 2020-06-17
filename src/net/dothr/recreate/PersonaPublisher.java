package net.dothr.recreate;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import net.dothr.fromjson.PersonaFromJson;
import net.utils.ClientRestUtily;

public class PersonaPublisher extends PersonaFromJson {
	
	static Logger log4j = Logger.getLogger( PersonaPublisher.class );
	private static final String DIR_REPORTE_PUBLICACION = "/home/dothr/workspace/ClientRest/files/out/";
	private static final String NOMBRE_ARCHIVO = "multiPublicacion-"+fechaArchivo()+".txt";
	private static final String NOPUBLICADO = "NoPublicados-"+fechaArchivo()+".txt";
	
	
	public static void main(String[] args) {
		try {
//			ping(null);
			
			//*
			List<String> lsIdPersona = 
					getIdsPersona(10, 2371) ; // 10,45 | 46,161 | 162,184  | 185, 1265
//					getIdsPersona();			
			multiplePublicacion(lsIdPersona); //*/
//			publicaPersona(3);
			
//			updateEmails();
//			System.out.println(getIdsFromListaArchivos());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Lee un archivo con idPersona;email para reemplazar el dominio del correo por un 
	 * dominio propio [@dhrtest.net], y genera un archivo SQL con update's para ejecutar en Base de Datos
	 */
	public static void updateEmails(){
		String outPath = "/home/dothr/workspace/ClientRest/files/out/updateEmail.sql";
		String inPath = "/home/dothr/workspace/ClientRest/files/emailsUser.txt";
		StringBuilder sbSql = new StringBuilder("/*  UPDATE de correos electrónicos para prueba */\n\n");
		
		
		List<String> lsLinea = ClientRestUtily.getLinesFile(inPath), lsTokens;
		System.out.println("lsLinea.size: " + lsLinea.size());
		Iterator<String> itLinea = lsLinea.iterator();
		String linea, idPersona, email, emailTest;
		
		while(itLinea.hasNext()){
			linea = itLinea.next();
			lsTokens = Arrays.asList(linea.split("\\s*;\\s*"));
			idPersona = lsTokens.get(0);
			email = lsTokens.get(1);
			
			emailTest = replaceDomain(email);
			sbSql.append("UPDATE persona SET email = '").append(emailTest).append("' WHERE id_persona=")
			.append(idPersona).append(";").append(" -- ").append(email).append("\n");
//			System.out.println("idPersona: " + idPersona + ", email: " + email + ", emailTest: "+emailTest);
		}
		System.out.println(sbSql);
		
		ClientRestUtily.writeStringInFile(outPath, 
				sbSql.toString(), false);
	}
	
	/**
	 * Procesa una lista de id's de persona en sistema, para solicitar publicación 
	 * y envia el resultado a un reporte
	 */
	protected static void multiplePublicacion(List<String> lsIdPersona){
		StringBuilder sbProcesoPublica = new StringBuilder("/* ************************** REPORTE PROCESO PUBLICACION  ******************** */ \n")
												   .append("/* ***>>>  WEB_RESOURCE: ").append(WEB_RESOURCE).append("  <<<***************** */ \n");
		StringBuilder sbNoPublicados = new StringBuilder(" /* No publicados: */ \n");
		StringBuilder sbIdNoPub = new StringBuilder();
		String idPersona = null, jsonResponse;
		int publicados = 0, noPublicados = 0;
		try{
			 /* De listado manual */
			Iterator<String> itIdPersona = lsIdPersona.iterator();
			while(itIdPersona.hasNext()){
				idPersona = itIdPersona.next();
				sbProcesoPublica.append("\n Publicando Persona: ").append(idPersona).append(". \n");
				jsonResponse = cvpPublication(idPersona); 
				
				sbProcesoPublica.append(" <== ").append(jsonResponse).append("\n =============================== \n");
				if(jsonResponse.equals("[]")){
					publicados++;
				}else{
					noPublicados++;
					sbNoPublicados.append("\n>> idPersona: ").append(idPersona).append("\n").append(jsonResponse).append("\n");
					sbIdNoPub.append(idPersona).append(itIdPersona.hasNext()?",":"");
				}
			}
			sbNoPublicados.append("\n ID-Persona no publicados: \n").append(sbIdNoPub).append("\n");
			
		}catch (ConnectException je){
			log4j.fatal("ConnectExcepcion al procesar dato de Persona " + idPersona, je);
			sbProcesoPublica.append("\n =============================== \n ERROR DE CONEXION::: ").append(je.getMessage());
		}
		catch (Exception e){
			log4j.fatal("Excepcion al procesar dato de Persona " + idPersona, e);
			sbProcesoPublica.append("\n =============================== \n ERROR DE PROCESO::: ").append(e.getMessage());
		}
		
		sbProcesoPublica.append("\n ::: \n Publicados:    ").append(publicados).append(" \n No Publicados: ").append(noPublicados);
		ClientRestUtily.writeStringInFile(DIR_REPORTE_PUBLICACION+NOMBRE_ARCHIVO, 
				sbProcesoPublica.toString(), false);
		ClientRestUtily.writeStringInFile(DIR_REPORTE_PUBLICACION+NOPUBLICADO, 
				sbNoPublicados.toString(), false);
		log4j.debug("Reporte de multi Publicación: \n"+DIR_REPORTE_PUBLICACION+NOMBRE_ARCHIVO );
	}
	
	/**
	 * Verifica funcionamiento de servicio de Publicacion PERSON.P
	 * @throws Exception
	 */
	public static String cvpPublication(String idPersona) throws ConnectException, Exception{
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDSELEX);
		json.put(P_JSON_PERSONA, idPersona);
		
		String jSon = getJsonFromService(json.toString(), CV_PERS_PUBLICATE );
		return jSon;
	}
	
	/**
	 * Listado de personas a publicar
	 * (Obtenido de Lista en Clase padre PersonaFromJson)
	 * @return
	 */
	protected static List<String> getIdsFromListaArchivos(){
		List<String> lsIdPersona = new ArrayList<String>();
		//Obtiene lista de read de PersonaFromJson
		Iterator<String> itPersona = getListaArchivosPersona().iterator();
		String fileName;
		while(itPersona.hasNext()){
			fileName = itPersona.next();	//"read-3.json"
			lsIdPersona.add( fileName.replace("read-", "").replace(".json", "") );
		}
		return lsIdPersona;
	}

	/**
	 * Listado de IdPersona a Publicar
	 * (Obtenido con Query en la BD)
	 * @return
	 */
	protected static List<String> getIdsPersona(){
		List<String> lsIdPersona;
		String sep = ","; 
		String idsPersona = "10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127,128,129,130,131,132,133,134,135,136,137,138,139,140,141,142,143,144,145,146,147,148,149,150,151,152,153,154,155,156,157,158,159,160,161";
				
		lsIdPersona = 
				Arrays.asList(idsPersona.split("\\s*"+sep+"\\s*"));
		
		return lsIdPersona;
	}
	protected static List<String> getIdsPersona(int ini, int fin){
		List<String> lsIdPersona = new ArrayList<String>();
		for(int x=ini;x<=fin;x++){
			lsIdPersona.add( String.valueOf(x));
		}
		return lsIdPersona;
	}
}
