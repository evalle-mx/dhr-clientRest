package net.mock.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import net.dothr.MainAppTester;
import net.utils.ClientRestUtily;
import net.utils.ConstantesREST;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class ProcessDataJson extends MainAppTester {

	
	private static Logger log4j = Logger.getLogger( ProcessDataJson.class );
	private static String pathJsons = "/home/dothr/JsonUI/PersonaRecreate/curriculumManagement/99.Demo/"; 
//			"/home/dothr/JsonUI/PersonaRecreate/curriculumManagement/TMP/";
	protected static final String REPORTE_PATH = ConstantesREST.JSON_HOME+"Report/";
	
	
	public static void main(String[] args) {
		try {
			replaceEmailPass(pathJsons);
//			addIdiomaArray();
//			renameJson();
//			replacePwd();
//			addImageToJson("/home/dothr/JsonUI/PersonaRecreate/curriculumManagement/5.SelexP42017/");
//			evaluaJson();
			
//			replaceTipoGenero(pathJsons);
//			convertHabIdioma(pathJsons);
//			removeTextoClass(pathJsons);
			
//			replaceRol(pathJsons);
			
//			mail2UserNs("/home/dothr/JsonUI/PersonaRecreate/curriculumManagement/TMP/mails.txt");
		} catch (Exception e) {
			log4j.fatal(e);
		}
	}
	
	/** 
	 * Valida si en la lista de archivos hay correos (cuentas) repetidas
	 */
	@SuppressWarnings("unused")
	private static void checkRepetido(){
		String dir = ConstantesREST.JSON_HOME+
				"PersonaRecreate/curriculumManagement/"+"0.pirh/";
		log4j.debug("<checkRepetido> ");
		Iterator<String> itArchivo = listFromFile(dir,"listaRead.txt").iterator();
		HashMap<String, String> mapaPersona = new HashMap<String, String>();
		StringBuilder sb = new StringBuilder();
		try{
			String fileName;
			while(itArchivo.hasNext()){
				fileName = itArchivo.next();
				JSONObject jsonPersona = getJsonObject(fileName, dir);
				Object obj = mapaPersona.get(jsonPersona.getString("email"));
				if(obj==null){
					//log4j.debug("No existe en mapa, se agrega");
					mapaPersona.put(jsonPersona.getString("email"), fileName );
				}
				else{
					log4j.debug("\n Persona (correo) duplicado: " + fileName );
					sb.append("\n Persona (correo) duplicado: " + fileName 
							+ " con " + mapaPersona.get(jsonPersona.getString("email")));
				}
			}
			
			log4j.debug("Usuarios en mapa:\n "+ mapaPersona.size() );
			log4j.debug("Usuarios en mapa:\n "+ mapaPersona );
			log4j.debug("Repetidos: \n" + sb.toString() );
		}catch (Exception e){
			log4j.fatal("Error al iterar. ", e);
		}
	}
	
	/**
	 * Reemplaza el correo electrónico y la contraseña, para generar usuarios en ambiente de pruebas
	 * @param pathJsons
	 * @throws Exception
	 */
	protected static void replaceEmailPass(String pathJsons) throws Exception {
		log4j.debug("<replaceEmail> ");
		ArrayList<String> lsName = new ArrayList<String>();
		Iterator<String> itFile = listFromFile(pathJsons, "listaRead.txt").iterator();
		String fileName, nFileName, email;
		JSONObject jsonPersona;
		StringBuilder sb = new StringBuilder();
		while(itFile.hasNext()){
			fileName = itFile.next();
			jsonPersona = getJsonObject(fileName, pathJsons);
			
			email = jsonPersona.getString("email");
			System.out.println("email: "+email);
			sb.append("idPersona: ").append(jsonPersona.getString("idPersona")).append(", ");			
			jsonPersona.put("email", replaceDomain(email));
			jsonPersona.remove("password");
			jsonPersona.put("password", PWD_123456789);	//reemplaza la contraseña por 1-9
			sb.append("email:").append(email).append(" ==>").append(replaceDomain(email)).append("\n");
					
			ClientRestUtily.writeStringInFile(pathJsons+fileName, 
					ClientRestUtily.formtJsonByGoogle("["+jsonPersona.toString()+"]"), false);			
		}
		System.out.println(sb);		
	}
	
	public static void renameJson(String pathJsons) throws Exception {
		log4j.debug("<renameJson> ");
		ArrayList<String> lsName = new ArrayList<String>();
		Iterator<String> itFile = listFromFile(pathJsons, "listaRead.txt").iterator();
		String fileName, nFileName, email;
		JSONObject jsonPersona;
		StringBuilder sb = new StringBuilder();
		String repetidos = "";
		while(itFile.hasNext()){
			fileName = itFile.next();
			jsonPersona = getJsonObject(fileName, pathJsons);
			
			if(jsonPersona.has("emailOriginal")){
				email = jsonPersona.getString("emailOriginal");
				jsonPersona.remove("emailOriginal");
				jsonPersona.put("email", email);
			}else{
				email = jsonPersona.getString("email");
			}
			nFileName = getUserName(email);
			
			for(int i=0; i<lsName.size();i++){
				if(nFileName.equals(lsName.get(i))){			
					repetidos+=nFileName + " ";
					nFileName+="-2";
				}
			}
			lsName.add(nFileName);
			//System.out.println(email);
			sb.append(nFileName).append(".json\n");
			log4j.debug("<renameJson> Se renombra archivo de " + fileName +" a " + nFileName );
			ClientRestUtily.writeStringInFile(pathJsons+"renamed/"+nFileName+".json", formatJsonPersona(jsonPersona, true), false);
			
		}
		System.out.println(sb);
		
		System.out.println("Repetidos: "+ repetidos);
		sb.append("\n\n#Repetidos: ").append(repetidos).append("\n");
		ClientRestUtily.writeStringInFile(pathJsons+"renamed/listaRead.txt", sb.toString(), false);
	}
	
	/*
	public static void replacePwd() throws Exception {		
		String path781 = pathCvs + "6.Selex/781-Publicados/", pathOrig = "/home/dothr/JsonUI/PersonaRecreate/curriculumManagement/8.Selex2016/renamed/";
		Iterator<String> itFile = listFromFile(path781,"listaRead.txt").iterator();
		List<String> lsEmailPwd = listFromFile("/home/dothr/JsonUI/PersonaRecreate/curriculumManagement/6.Selex/781-Publicados/", "pwds.txt");
		String fileName, emailPwd;
		String pwd781, pwdAnt;
		JSONObject jsonPersona;
		List<String> items;
		while(itFile.hasNext()){
			fileName = itFile.next();
			jsonPersona = getJsonObject(fileName, path781);
			pwd781 = jsonPersona.getString("password");
			pwdAnt=null;
			for(int x=0;x<lsEmailPwd.size();x++){
				emailPwd = lsEmailPwd.get(x);
				
				if(emailPwd.startsWith("#")){
					items = Arrays.asList(emailPwd.split("\\s*;\\s*"));
					if(items.size()>1){
						if(items.get(0).equals(jsonPersona.getString("email"))){
							pwdAnt = items.get(1);
							if(pwd781.equals(pwdAnt)){
								pwdAnt = null;
							}
						}
					}else{
						log4j.debug("ITEMS INVALIDO "+items);
					}
				}
			}			
			if(pwdAnt!=null){
				jsonPersona.put("password", pwdAnt);
				log4j.debug("<replacePwd> Se sobrescribe archivo: " + fileName );
				ClientRestUtily.writeStringInFile("/home/dothr/JsonUI/PersonaRecreate/curriculumManagement/"+fileName, formatJsonPersona(jsonPersona, true), false);
			}
		}
	}//*/
	
	public static void replaceTipoGenero(String pathJsons) throws Exception {	
		Iterator<String> itFile = listFromFile(pathJsons,"listaRead.txt").iterator();
		StringBuilder sb = new StringBuilder(" /* Reporte de proceso */\n>> pathJsons: ").append(pathJsons).append(" <<< \n\n");
		String fileName, idTipoGenero;
		JSONObject jsonPersona;
		int count = 0;
		while(itFile.hasNext()){
			fileName = itFile.next();
			jsonPersona = getJsonObject(fileName, pathJsons);
			if(jsonPersona.has("idTipoGenero")){
				idTipoGenero = jsonPersona.getString("idTipoGenero");
				log4j.debug("tipoGenero: " + idTipoGenero);
				if(idTipoGenero.equals("0")){
					jsonPersona.put("idTipoGenero", "1");
				}else{
					jsonPersona.put("idTipoGenero", "2");
				}
				log4j.debug(fileName+" Se sobreEscribe !!");
				sb.append(fileName).append(" Se sobreEscribe !!").append(" \n");
				ClientRestUtily.writeStringInFile(pathJsons+fileName, formatJsonPersona(jsonPersona, true), false);
			}else{
				log4j.debug(fileName+" No tiene idTipoGenero !!");
				sb.append(fileName).append(" No tiene idTipoGenero !!").append(" \n");
			}
			count++;
		}
		sb.append("\n").append("SE procesaron ").append(count).append(" archivos json");
		ClientRestUtily.writeStringInFile(REPORTE_PATH+"ReporteReplaceTipoGenero.log", sb.toString(), false);
		log4j.debug("\n" + "SE procesaron " + count + " archivos json");
	}
	
	
	public static void evaluaJson(String pathJsons) throws Exception {
		StringBuilder sb = new StringBuilder();
		String filePath = "/home/dothr/JsonUI/PersonaRecreate/curriculumManagement/6.Selex/";
		Iterator<String> itFile = listFromFile(filePath,"listaRead(Todos).txt").iterator();
		String fileName, datos;
		JSONObject jsonPersona;
		boolean hayDatos = false;
		while(itFile.hasNext()){
			fileName = itFile.next();
			jsonPersona = getJsonObject(fileName, pathJsons);
			hayDatos = false;
			datos = "";
//			idPersona = jsonPersona.getString("idPersona");
			
//			if(jsonPersona.has("escolaridad")){
//				hayDatos = true;
//				datos+="escolaridad ";
//			}
//			if(jsonPersona.has("experienciaLaboral")){
//				hayDatos = true;
//				datos+="experienciaLaboral ";
//			}
//			if(jsonPersona.has("habilidad")){
//				hayDatos = true;
//				datos+="habilidad ";
//			}
//			
//			if(hayDatos){
//				//System.out.println("archivo: " + fileName + " tiene datos");
//				sb.append("archivo: ").append(fileName).append(" tiene datos: ").append(datos).append("\n");
//			}
			sb
			.append(jsonPersona.getString("email")).append(";")
			.append(jsonPersona.getString("password"))
			.append("\n");
			
		}
		System.out.println(sb.toString());
		ClientRestUtily.writeStringInFile("/home/dothr/JsonUI/PersonaRecreate/curriculumManagement/relXe.csv", sb.toString(), false);
		
	}
	
	/**
	 * Agrega idioma a Json, usando un arreglo aleatorio de datos
	 * @throws Exception
	 */
	public static void addIdiomaArray(String pathJsons) throws Exception {
		log4j.debug("<addIdiomaArray> ");
		Iterator<String> itFile = listFromFile(pathJsons,"listaRead.txt").iterator();
		String fileName, idPersona;
		JSONObject jsonPersona;
		boolean sobreEscribe = false;
		while(itFile.hasNext()){
			fileName = itFile.next();
			jsonPersona = getJsonObject(fileName, pathJsons);
			sobreEscribe = false;
			idPersona = jsonPersona.getString("idPersona");
//			System.out.println("jsonPersona:"+jsonPersona);
						
			//IDIOMAS
			if(!jsonPersona.has("idioma")){
				log4j.debug(idPersona+" : No tiene idiomas, se agrega aleatorio" );
				jsonPersona.put("idioma", getArray(1));
				sobreEscribe = true;
			}
			
			//Habilidades
			if(!jsonPersona.has("habilidad")){
				log4j.debug(idPersona+" : No tiene habilidades, se agrega aleatorio" );
				jsonPersona.put("habilidad", getArray(2));
				sobreEscribe = true;
			}
			
			if(sobreEscribe){
				log4j.debug("<addIdiomaArray> Se sobrescribe archivo: " + fileName );
				ClientRestUtily.writeStringInFile(pathJsons+fileName, formatJsonPersona(jsonPersona, true), false);
			}
		}
	}
	
	
	
	public static void addImageToJson(String pathJsons) throws Exception {
		JSONObject json = new JSONObject();
		json.put("fileDescripcion", "Profile");
		json.put("idTipoContenido", "1");
		StringBuilder sb = new StringBuilder();
		String imgfileName, fileName;
		JSONObject  jsonPersona; 
		Iterator<String> itFileImg = listFromFile(pathJsons+"img/","listaImg.txt").iterator();
		while(itFileImg.hasNext()){
			imgfileName = itFileImg.next();
			
//			System.out.println(imgfileName);
			fileName = imgfileName.substring(2, imgfileName.lastIndexOf("."))+".json";
			
//			System.out.println(fileName);
			try{
				jsonPersona = getJsonObject(fileName, pathJsons);
				json.put("idContenido", "1"+jsonPersona.getString("idPersona"));
				json.put("url", imgfileName);
				jsonPersona.put("imgPerfil", json);
				ClientRestUtily.writeStringInFile(pathJsons+fileName, formatJsonPersona(jsonPersona, true), false);
			}catch (Exception e){
				log4j.fatal("No existe " + fileName);
				sb.append(imgfileName).append(", ").append(fileName).append(", ").append("No Existe \n");
			}
			
		}
		System.out.println(sb);
		
	}
	
	/**
	 * Obtiene arreglo de datos aleatorio, en base a su tipo<br>
	 * <b>1</b> idioma <br> <b>2</b> habilidad
	 * @param tipo
	 * @return
	 */
	private static JSONArray getArray(int tipo){
		String idiom1 = "[{\"idIdioma\": \"1\",\"idDominio\": \"3\",\"idPersonaIdioma\": \"71\"}, {\"idIdioma\": \"2\",\"idDominio\": \"4\",\"idPersonaIdioma\": \"61\"}]";
		String idiom2 = "[{\"idIdioma\": \"3\",\"idDominio\": \"4\",\"idPersonaIdioma\": \"62\"}, {\"idIdioma\": \"4\",\"idDominio\": \"2\",\"idPersonaIdioma\": \"72\"}, {\"idIdioma\": \"1\",\"idDominio\": \"3\",\"idPersonaIdioma\": \"82\"}]";
		String idiom3 = "[{\"idIdioma\": \"6\",\"idDominio\": \"5\",\"idPersonaIdioma\": \"63\"}]";
		String[] idiomas = {idiom1, idiom2, idiom3 }; 
		
		String hab1 = "[{\"idHabilidad\": \"7\",\"descripcion\": \"Linux\",\"idDominio\": \"4\",\"lbDominio\": \"Avanzado\"}]";
		String hab2 = "[{\"idHabilidad\": \"7\",\"descripcion\": \"Linux\",\"idDominio\": \"4\",\"lbDominio\": \"Avanzado\"}, {\"idHabilidad\": \"8\",\"descripcion\": \"Office\",\"idDominio\": \"2\",\"lbDominio\": \"Regular\"}]";
		String hab3 = "[{\"idHabilidad\": \"9\",\"descripcion\": \"Marketing\",\"idDominio\": \"1\",\"lbDominio\": \"Superficial\"}, {\"idHabilidad\": \"7\",\"descripcion\": \"Linux\",\"idDominio\": \"4\",\"lbDominio\": \"Avanzado\"}, {\"idHabilidad\": \"7\",\"descripcion\": \"Office\",\"idDominio\": \"4\",\"lbDominio\": \"Avanzado\"}]";
		String[] habils = {hab1, hab2, hab3 };
		
		JSONArray jsArr = null;
		try{
			int aleat = 0;
			if(tipo == 1){ //Idioma
				aleat = getRdm(idiomas.length-1);
				jsArr = new JSONArray(idiomas[aleat]);
			}else if(tipo == 2){ //Idioma
				aleat = getRdm(habils.length-1);
				jsArr = new JSONArray(habils[aleat]);
			}
		}catch (Exception e){
			log4j.fatal("Error: ", e);
		}
		if(jsArr == null){
			log4j.fatal("Excepcion");			
		}
		
		return jsArr;
	}
	
	private static int getRdm(int maximo){
		int numeroAleatorio = (int) (Math.random()*maximo+1);
		return numeroAleatorio;
	}
	
	private static void mail2UserNs(String mailsTxtPath) throws Exception {
		String pathOut = "/home/dothr/JsonUI/PersonaRecreate/curriculumManagement/TMP/usernames.txt";
		System.out.println("mail2UserNs");
		StringBuilder sb = new StringBuilder();
		String email, username;
		Iterator<String> itFile = ClientRestUtily.readListaTxt(mailsTxtPath).iterator();
		while(itFile.hasNext()){
			email = itFile.next();
			username = email.substring(0, email.indexOf("@"));
			System.out.println(username);
			sb.append(username).append(".json").append("\n");
		}
		ClientRestUtily.writeStringInFile(pathOut, sb.toString(), false);
		System.out.println("archivo generado "+pathOut);
	}
	
	
	public static void removeTextoClass(String pathJsons) throws Exception {	
		Iterator<String> itFile = listFromFile(pathJsons,"listaRead.txt").iterator();
		StringBuilder sb = new StringBuilder(" /* Reporte de proceso */\n>> pathJsons: ").append(pathJsons).append(" <<< \n\n");
		String fileName, idTipoGenero;
		JSONObject jsonPersona;
		int count = 0;
		while(itFile.hasNext()){
			fileName = itFile.next();
			jsonPersona = getJsonObject(fileName, pathJsons);
			if(jsonPersona.has("textoClasificacion")){
				jsonPersona.remove("textoClasificacion");
				log4j.debug(fileName+" Se sobreEscribe !!");
				sb.append(fileName).append(" Se sobreEscribe !!").append(" \n");
				ClientRestUtily.writeStringInFile(pathJsons+fileName, formatJsonPersona(jsonPersona, true), false);
			}else{
				log4j.debug(fileName+" No tiene textoClasificacion !!");
				sb.append(fileName).append(" No tiene textoClasificacion !!").append(" \n");
			}
			count++;
		}
		sb.append("\n").append("SE procesaron ").append(count).append(" archivos json");
		ClientRestUtily.writeStringInFile(pathJsons+"ReporteReplaceTipoGenero.log", sb.toString(), false);
		log4j.debug("\n" + "SE procesaron " + count + " archivos json");
	}
	
	
	/**
	 * Agrega idioma a Json, usando un arreglo aleatorio de datos
	 * @throws Exception
	 */
	public static void convertHabIdioma(String pathCvs) throws Exception {
		log4j.debug("<convertHabIdioma> ");
		StringBuilder sb = new StringBuilder();
		
		Iterator<String> itFile = listFromFile(pathCvs,"listaRead.txt").iterator();
		String fileName, idPersona;
		JSONObject jsonPersona, jsonHab, jsonIdioma;
		String desc = "", idIdioma="";
		JSONArray jsHabilidad=null, jsIdiomas;
		boolean sobreEscribe = false;
		while(itFile.hasNext()){
			fileName = itFile.next();
			jsIdiomas = new JSONArray();
			jsonPersona = getJsonObject(fileName, pathCvs);
			sobreEscribe = false;
			idPersona = jsonPersona.getString("idPersona");
						
			//IDIOMAS
			if(jsonPersona.has("idioma")){
				log4j.debug(idPersona+" : Tiene idiomas, se inicia" );
				jsIdiomas = jsonPersona.getJSONArray("idioma");
				jsonPersona.remove("idioma");
			}
			
			//Habilidades
			if(!jsonPersona.has("habilidad")){
				log4j.debug(idPersona+" : No tiene habilidades" );				
			}else{
				jsHabilidad = jsonPersona.getJSONArray("habilidad");
				jsonPersona.remove("habilidad");
				//for(int x=0; x<jsHabilidad.length(); x++){
				for(int x=jsHabilidad.length(); x>0; x--){
					log4j.debug("X "+x);
					idIdioma = null;
					jsonHab = jsHabilidad.getJSONObject(x-1);
					
				
					if( jsonHab.has("descripcion") ){
						String descripcion =jsonHab.getString("descripcion"); 
						desc = descripcion.replace(" ", "");
						if( desc.toLowerCase().equals("inglés") || desc.toLowerCase().equals("ingles") 
							|| desc.toLowerCase().equals("igles")
							|| desc.toLowerCase().equals("ingloes")
							|| desc.toLowerCase().equals("english")
							|| desc.toLowerCase().equals("inles")
							|| desc.toLowerCase().equals("inges")
							|| desc.toLowerCase().equals("idiomainglés")
							|| desc.toLowerCase().equals("igles")
							|| desc.toLowerCase().equals("nngles")|| desc.toLowerCase().equals("ingled")
							|| desc.toLowerCase().equals("inlges")
							|| desc.toLowerCase().equals("idiomaingles")
							|| desc.toLowerCase().equals("ingés")
							|| desc.toLowerCase().equals("intermedioavanzado,nivelb2ref.cefr")
							|| desc.toLowerCase().equals("inglés.")){	//ingloes, english, inles, inges, idiomainglés, igles, nngles
							idIdioma = "1";
							desc="Inglés";				
						}						
						else if( desc.toLowerCase().equals("alemán") || desc.toLowerCase().equals("aleman") ){
							idIdioma = "2";
							desc="Alemán";
						}
						else if( desc.toLowerCase().equals("frances") || desc.toLowerCase().equals("francés") ){
							idIdioma = "3";
							desc="Frances";
						}
						else if( desc.toLowerCase().equals("italiano") ){
							idIdioma = "4";
							desc="Italiano";
						}
						else if( desc.toLowerCase().equals("portugués") || desc.toLowerCase().equals("portugues") 
								|| desc.toLowerCase().equals("portugés")
								|| desc.toLowerCase().equals("portuges")
								|| desc.toLowerCase().equals("portugües")){	//portuges,  portugües
							idIdioma = "5";
							desc="Portugués";
						}
						else if( desc.toLowerCase().equals("japonés") || desc.toLowerCase().equals("japones") ){
							idIdioma = "6";
							desc="Japonés";
						}
						else if( desc.toLowerCase().indexOf("chino")!=-1 || desc.toLowerCase().indexOf("mandarín")!=-1 || desc.toLowerCase().indexOf("mandarin")!=-1){
							idIdioma = "7";
							desc="Chino Mandarín";
						}
						
						if(idIdioma!=null){
							sobreEscribe = true;
							jsonIdioma= new JSONObject();
							jsonIdioma.put("idIdioma", idIdioma);
							jsonIdioma.put("lbIdioma", desc);
							jsonIdioma.put("idDominio", jsonHab.has("idDominio")?jsonHab.getString("idDominio"):"1" ) ;
							jsonIdioma.put("idPersona", idPersona);
							jsonIdioma.put("idPersonaIdioma", jsonHab.has("idHabilidad")?jsonHab.getString("idHabilidad"):""+x );
							jsIdiomas.put(jsonIdioma);
							jsHabilidad.remove(x-1);
						}
						
						sb.append(descripcion).append(idIdioma!=null?"*"+idIdioma:"").append("\n");
					}else{
						jsHabilidad.remove(x-1);
						sobreEscribe=true;
					}				
				}
			}
			
			if(sobreEscribe){
				
				if(jsIdiomas!=null && jsIdiomas.length()>0){
					log4j.debug("agrega Idiomas: ");
					jsonPersona.put("idioma", jsIdiomas);
				}
				if(jsHabilidad!=null && jsHabilidad.length()>0){
					jsonPersona.put("habilidad", jsHabilidad);
				}
				log4j.debug("<addIdiomaArray> Se sobrescribe archivo: " + fileName );
				ClientRestUtily.writeStringInFile(pathCvs+fileName, formatJsonPersona(jsonPersona, true), false);
			}
			
		}
		log4j.debug("\n Habilidades encontradas: \n"+ sb );
		
	}
	
	
	
	public static void replaceRol(String pathJsons) throws Exception {	
		Iterator<String> itFile = listFromFile(pathJsons,"listaRead.txt").iterator();
		StringBuilder sb = new StringBuilder(" /* Reporte de proceso */\n>> pathJsons: ").append(pathJsons).append(" <<< \n\n");
		String fileName, role, idRol;
		JSONObject jsonPersona;
		int count = 0;
		while(itFile.hasNext()){
			fileName = itFile.next();
			jsonPersona = getJsonObject(fileName, pathJsons);
			if(jsonPersona.has("role")){
				role = jsonPersona.getString("role");
				log4j.debug("role: " + role);
				if(role.equals("9")){
					jsonPersona.put("idRol", "1");
				}else{
					jsonPersona.put("idRol", "3");
				}
				log4j.debug(fileName+" Se sobreEscribe !!");
				sb.append(fileName).append(" Se sobreEscribe !!").append(" \n");
				ClientRestUtily.writeStringInFile(pathJsons+fileName, formatJsonPersona(jsonPersona, true), false);
			}else{
				log4j.debug(fileName+" No tiene role !!");
				sb.append(fileName).append(" No tiene role !!").append(" \n");
			}
			count++;
		}
		sb.append("\n").append("SE procesaron ").append(count).append(" archivos json");
		ClientRestUtily.writeStringInFile(REPORTE_PATH+"ReporteReplaceRol.log", sb.toString(), false);
		log4j.debug("\n" + "SE procesaron " + count + " archivos json");
	}
	
	
	
	
	/* ************************************************************ */
	/**
	 * Convierte un arreglo de Json (De igual estructura) en un texto estilo matriz 
	 * con el nombre de Parametro en el encabezado
	 * @param jsArr
	 * @return
	 * @throws Exception
	 */
	protected static StringBuilder convertJsonToTextDD(JSONArray jsArr) throws Exception {
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
}
