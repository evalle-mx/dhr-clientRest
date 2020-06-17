package net.dothr;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import net.utils.ClientRestUtily;

/**
 * Clase encargada de procesar archivos CSV para generar archivos json de documentación 
 * a partir de documentos o base de datos sobre endPoints/uricodes a partir de Permisos
 * @author netto
 *
 */
public class JsonDocGenerator {
	
	/** Carpeta donde se ubican los archivos para esta clase */
	private static final String jsonDocDirPath = "/home/dothr/workspace/ClientRest/files/jsonDoc/";

	/* Lista obtenida a partir del archivo RolesPermiso.xls (o Query contexto&valor en permiso)  [ uriCode;endPoint ]
	 * repositorio de uriCodes */
	private static final String csvUricode = jsonDocDirPath+"in_uriCodePoint.csv";
	
	/* Lista obtenida a partir de UriCode-Adapter.xls pestaña point-code-class [ /module/context/method ] */
	private static final String csvEndPoints = jsonDocDirPath+"In_endPoint_Temp.csv";
	
	/* Archivo de salida generado con los elementos empatados, generado para completar la columna de uricode en el
	 * archivo UriCode-Adapter.xls (point-code-class) */
	private static final String outUriCode = jsonDocDirPath+"out_uriCodes.csv";
	
	
	
	/* Lista de endPoint-UriCode-Java Class, Obtenido de UriCode-Adapter.xls (Cada linea genera un objeto Json)
	  [ /module/academicBackground/create;ACADBACK.C;AcademicBackgroundAdapterRest ]*/
	private static final String inUricodeClass = jsonDocDirPath+"/in_PointCodeClass.csv";
	
	/* Archivo de salida en jsonArray con los datos clasificados en json's que se incluira en nuevo sitio web */
	private static final String outJsonDir = jsonDocDirPath+"jsonDir.json";
	
	
	private static String archivoExtra = "extra";
	
	public static void main(String[] args) {
		try {
//			empatarInfo();
//			generaJson();
			generaArchivosModulo();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * Metodo que empata la información para asignar los uriCodes a partir del 
	 * listado de endPoints obtenido manualmente por cada Clase java (./adapter/rest/) 
	 * @throws Exception
	 */
	protected static void empatarInfo() throws Exception{		
		List<String> lsUriCodeEndPoint = ClientRestUtily.getLinesFile(csvUricode);
		List<String> lsEndPoint = ClientRestUtily.getLinesFile(csvEndPoints);
		StringBuilder sbOut;
		
		if(!lsUriCodeEndPoint.isEmpty() && !lsEndPoint.isEmpty()){
			//0.Ambas listas tienen datos
			System.out.println("total uriCodes en BD: "+lsUriCodeEndPoint.size());
			System.out.println("total EndPoints a mapear: "+lsEndPoint.size());
			
			sbOut = new StringBuilder();
			String lUriCode, lEndPoint;
			String tokensUric[];
			
			//1. Se genera el mapa de endPoints-Uricodes
			Map<String, String> mapa = new HashMap<String, String>();			
			Iterator<String> itUriCode = lsUriCodeEndPoint.iterator();
			while(itUriCode.hasNext()){
				lUriCode = itUriCode.next();
				tokensUric = lUriCode.split("\\s*;\\s*");//Arrays.asList(row.split("\\s*;\\s*"));
//				System.out.println("[Key] endPoint: "+tokensUric[1]);
//				System.out.println("[value] uriCode: "+tokensUric[0]);				
				mapa.put(tokensUric[1], tokensUric[0]);	
			}
			
			//2. Itera el listado a llenar			
			Iterator<String> itEndPoint = lsEndPoint.iterator();
			String uriCodeMap, endPoint;
			while(itEndPoint.hasNext()){				
				lEndPoint = itEndPoint.next();
				endPoint = lEndPoint.replace(";", "");// concatena: /module/context + /method
				uriCodeMap = mapa.get(endPoint);
//				System.out.println(endPoint + ": "+uriCodeTmp);
				sbOut.append(endPoint).append(";")
				.append(uriCodeMap!=null?uriCodeMap:"  ").append("\n");
			}
			ClientRestUtily.writeStringInFile(outUriCode, sbOut.toString(), false);
		}
		else{
			System.out.println("Algun archivo/listado es invalido");
		}
	}
	
	/**
	 * Genera un archivo json conteniendo el directorio ordenado los datos y usarlo en el prototipo
	 * de documentación tipo Html-web
	 * @throws Exception
	 */
	protected static void generaJson() throws Exception {
		List<String> lsUriPoints = ClientRestUtily.getLinesFile(inUricodeClass);
				
		if(!lsUriPoints.isEmpty()){
			String linea;
			String[] tokens, uriTokens;
			
			String endPoint, uriCode, javaClass, stTxt, stHtml;			
			String root,context,method; /* /module/academicBackground/create */
			
			JSONArray jsArr = new JSONArray();
			JSONObject json;
			
			Iterator<String> itLine = lsUriPoints.iterator();
			int indice = 1;
			while(itLine.hasNext()){
				linea = itLine.next();
//				System.out.println(linea);
				tokens = linea.split("\\s*;\\s*");
				endPoint = tokens[0];
				uriCode = tokens[1];
				javaClass = tokens[2];
				uriTokens = endPoint.split("\\s*/\\s*");			
				root = uriTokens[1];
				context= uriTokens[2];
				
				if(uriTokens.length==4){
					method= uriTokens[3];
					stTxt = context+".txt";
					stHtml = root.concat("_")
							.concat(context)
							.concat( method.substring(0,1).toUpperCase() )
							.concat( method.substring(1, method.length()).toLowerCase() )
							.concat(".html");
					
				}else{
					System.out.println("uriTokens.size !=4: " +endPoint);

					//root = null; context= null; 
					method= null;
					stTxt = archivoExtra+".txt";
					stHtml = root+"_"+context+".html";
				}
				
				json = new JSONObject();
				json.put("indice", indice++);
				json.put("endPoint", endPoint);
				json.put("uriCode", uriCode);
				json.put("javaClass", javaClass);
				
				json.put("root", root);
				json.put("context", context);
				json.put("method", method);
				
				json.put("stTxt", stTxt);
				json.put("stHtml", stHtml);
				
				jsArr.put(json);
			}
			ClientRestUtily.writeStringInFile(outJsonDir, 
					ClientRestUtily.formtJsonByGoogle(jsArr.toString()), false);
			System.out.println("archivo generado: "+outJsonDir);
		}
		else{
			System.out.println("No hay datos a procesar");
		}
	}
	
	/**
	 * Metodo para generar el contenido del directorio (txt) jsonDoc a partir de los datos en los archivos de 
	 * uriCodes en xls, donde se agrupan los servicios por el modulo root [ /module/academicBackground ]
	 * @throws Exception
	 */
	protected static void generaArchivosModulo() throws Exception{
		String jsonDocTxt = jsonDocDirPath+"txt/";
		String jsonDocHtml = jsonDocDirPath+"html/";
		
		//PlantillaHtml (Pagina completa, vista html)
		String plantilla = ClientRestUtily.getBuilderFile(jsonDocDirPath+"templateServ.html", null).toString(), templHtml; 
		
		StringBuilder sbTexto = new StringBuilder("\n")
		.append("		Json entrada\n		------------------------------\n		{}\n\n\n")
		.append("		Json entrada Ejemplo\n		------------------------------\n		{}\n\n\n")
		.append("		Json salida\n		------------------------------\n		   [{\"name\": \", ....}]\n\n")
		.append("		Json salida ejemplo\n		------------------------------\n")
		.append("		a) éxito.\n			[-------]\n\n		b) ....\n");
		
		//* Vacia el contenido del directorio de salida
		File dir = new File(jsonDocTxt);
		for(File file: dir.listFiles()) {
			if (!file.isDirectory()){
				file.delete();
			}
		}//*/
		/* leer el archivo de control generado para determinar cada txt requerido/elaborado */
		JSONArray jsDir = ClientRestUtily.getJsonArray("jsonDir.json", jsonDocDirPath);
		StringBuilder sb;
		String stTxt, stMethod;
		JSONObject json;
		for(int i=0;i<jsDir.length();i++){
			templHtml = plantilla;
			json = jsDir.getJSONObject(i);
			
			sb = new StringBuilder("\n ////////////////////////////////////////////////////// \n");
//			System.out.println(json);
			if(json.has("method")){ //Esta completo
				sb.append(" Método: ").append(json.getString("method")).append("\t")
				.append("uriCode: ").append(json.getString("uriCode")).append("\t")
				.append("endPoint: ").append(json.getString("endPoint")).append("\t")
				.append("javaClass: ").append(json.getString("javaClass")).append("\n");
				stMethod = json.getString("method");
				sb.append(sbTexto);
			}
			else{
				sb.append("uriCode: ").append(json.getString("uriCode")).append("\t")
				.append("endPoint: ").append(json.getString("endPoint")).append("\t")
				.append("javaClass: ").append(json.getString("javaClass")).append("\n");
				stMethod = "";
			}
			//Archivos HTML (completa o vista)
			if(json.has("stHtml") && json.getString("stHtml").length()>1 && !json.getString("stHtml").equals(archivoExtra+".html")){
				System.out.println("Creando archivo "+json.getString("stHtml"));
				templHtml = templHtml.replace("{{METHOD}}", stMethod).replace("{{URICODE}}", json.getString("uriCode")).replace("{{TITLEVIEW}}", json.getString("endPoint"));
				templHtml = templHtml.replace("{{ENDPOINT}}", json.getString("endPoint")).replace("{{JAVACLASS}}", json.getString("javaClass"));
				ClientRestUtily.writeStringInFile(jsonDocHtml+json.getString("stHtml"), 
						templHtml, false); /* Debe ser un archivo por cada servicio */
			}
			
			stTxt = json.getString("stTxt");
			ClientRestUtily.writeStringInFile(jsonDocTxt+stTxt, 
					sb.toString(), true); /* es true para que añada al archivo si existe */
		}
		
	}
}
