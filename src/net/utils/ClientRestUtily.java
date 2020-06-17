package net.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
//import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class ClientRestUtily {
	
	public final static String PCHARSET = "UTF-8";
	public final static String CHARSET=";charset=UTF-8";
	public final static String[] repArticulos = {"en", "de"};
	
	static Logger log4j = Logger.getLogger( ClientRestUtily.class );
	
	public static void testTrim(String Str){	
		if(Str==null){
			Str = new String("   Welcome to Tutorialspoint .com   ");
		}

	      System.out.print("Return Value :" );
	      System.out.println(">"+Str.trim()+"<" );
	   }
	
	/**
	 * Realiza un formateo para identar una Cadena Json
	 * @param jsonString
	 * @return
	 */
	public static String formtJsonByGoogle(String jsonString){
		JsonParser parser = new JsonParser();
		JsonElement el = parser.parse(jsonString);
		
		Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
		jsonString = gson.toJson(el);
		
		return jsonString;
	}
	
	/**
	 * Obtiene el json contenido en un archivo plano .json desde el directorio designado
	 * @param jsonFileName [i.e. read-1.json]
	 * @param jsonDir [i.e. /home/user/dir/]
	 * @return
	 */
	public static String getJsonFile(String jsonFileName, String jsonDir) {
		String jsonPath = null;
		jsonPath = jsonDir.concat(jsonFileName);
//		log4j.debug("path de busqueda: [".concat(jsonPath).concat("]"));
		String jsonFile = "[]";
		try {
			jsonFile = getBuilderNoTabsFile(jsonPath, PCHARSET).toString();
		} catch (IOException e) {			
			log4j.fatal("Excepción al generar Json desde archivo: [".concat(jsonPath).concat("]: "), e);
			jsonFile = "[]";
		}
		return jsonFile;
	}
	
	/**
	 * Lee un archivo planto, generando un Builder substituyendo Tabuladores por un espacio en blanco
	 * utilizando el charset Definido
	 * @param fullPath
	 * @param stCharset
	 * @return
	 */
	public static StringBuilder getBuilderNoTabsFile( String fullPath, String stCharset) throws IOException{
		StringBuilder sb = new StringBuilder();
		if(stCharset==null || stCharset.trim().equals("")){
			stCharset="UTF8";
		}
		//log4j.debug("**************  USANDO FileInputStream con "+stCharset+" *****************");
	        BufferedReader infile = new BufferedReader(
	        		 new InputStreamReader(
	                         new FileInputStream(fullPath), stCharset));
	        String strLine;
	        while ((strLine = infile.readLine()) != null) 
	        {
	        	String unTabedLine = removeTabs(strLine).concat(" ");
	            sb.append(unTabedLine).append("\n");
	        }
	        infile.close();
	    return sb;
	}
	
	public static StringBuilder getBuilderFile( String fullPath, String stCharset) throws IOException{
		StringBuilder sb = new StringBuilder();
		if(stCharset==null || stCharset.trim().equals("")){
			stCharset="UTF8";
		}
		//log4j.debug("**************  USANDO FileInputStream con "+stCharset+" *****************");
	        BufferedReader infile = new BufferedReader(
	        		 new InputStreamReader(
	                         new FileInputStream(fullPath), stCharset));
	        String strLine;
	        while ((strLine = infile.readLine()) != null) 
	        {
	        	
	            sb.append(strLine).append("\n");
	        }
	        infile.close();
	    return sb;
	}
	
	/**
	 * Lee y Despliega en pantalla linea por linea de un archivo
	 * @param fullPath
	 */
	public static void readFile( String fullPath){
		int nLines = 0;
		try 
	    {
	        BufferedReader infile = new BufferedReader(new FileReader(fullPath));
	        String strLine;
	        while ((strLine = infile.readLine()) != null) 
	        {
	            System.out.println(strLine);    
	            nLines++;
	        }
	        infile.close();	        
	    } 
	    catch (IOException e) 
	    {	//System.err.println("IOException "+e);
	    	e.printStackTrace();
	    }
	    if(nLines!=0)
	    	System.out.println("<Fin>Numero de Lineas Leidas: " + nLines + "</fin>");
	    else
	    	System.out.println("Archivo Vacio: " + nLines + " lineas");	    
	}
	
	/**
     * Remueve los espacios en blanco al principio y final de una cadena, 
     * sin afectar los que se encuentran entre caracteres 
     * @param tabulada
     * @return
     */
    public static String removeTabs(String tabulada){
    	String subCadena = tabulada.replace("\t", "");
		boolean val = true;
		do{
			if(subCadena!=null && subCadena.length()>0){
				String prim = subCadena.substring(0,1);
				if(prim.equals(" ")){
					subCadena = subCadena.substring(1,subCadena.length());
				}else{
					//val = false;
					if(subCadena.length()>0){
						String last = subCadena.substring(subCadena.length()-1,subCadena.length());
						if(last.equals(" ")){
							subCadena = subCadena.substring(0,subCadena.length()-1);
						}else{
							val = false;
						}
					}
				}
			}else{
				if(subCadena==null){
					subCadena = "";
				}
				val = false;
			}
			
		}while(val);
		return subCadena;
    }

    /**
	 * Muestra una lista de archivos contenidos dentro de un directorio
	 * @param dirPath
	 */
	public static List<String> listFilesInDir(String dirPath){
		List<String> lsFiles = null;
		File dir = new File(dirPath);
		FileFilter fileFilter = new FileFilter() {
	        public boolean accept(File file) {
	            return file.isFile();
	        }
	    };
	    
	    File[] files = dir.listFiles(fileFilter);
		
		if(files!=null){
			lsFiles = new ArrayList<String>();
			for(int x=0;x<files.length;x++){				
				lsFiles.add(x, files[x].getName());
//				System.out.println(files[x].getName());
			}
		}else{
			System.out.println("El directorio esta vacio");
		}
		return lsFiles;
	}
	
	
	/**
	 * Escribe una cadena de texto en un archivo
	 * @param filePath ruta, nombre y extension  del archivo
	 * @param texto Cadena a agregar
	 * @param append agrega (true) o reemplaza (false)
	 * @throws IOException 
	 */
	public static void writeStringInFile(String filePath, String texto, boolean append ) {
		
		BufferedWriter bufferedWriter;
		if(null == texto){
			return;
		}
		try {
			//bufferedWriter = new BufferedWriter(new FileWriter(filePath, append));
			bufferedWriter = new BufferedWriter (new OutputStreamWriter(new FileOutputStream(filePath, append),"UTF-8"));
			bufferedWriter.write(texto);
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
//			throw e;
		}
		
	}
	

	/**
	 * Escribe una cadena de texto en un archivo
	 * @param filePath ruta, nombre y extension  del archivo
	 * @param texto Cadena a agregar
	 * @param append agrega (true) o reemplaza (false)
	 */
	public static void writeStringInFile(String filePath, String texto, String encode, boolean append ){
		
		BufferedWriter bufferedWriter;
		if(null == texto){
			return;
		}
		try {
			//bufferedWriter = new BufferedWriter(new FileWriter(filePath, append));
			bufferedWriter = new BufferedWriter (new OutputStreamWriter(new FileOutputStream(filePath, append),encode));
			bufferedWriter.write(texto);
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Obtiene la extensión de la ruta de archivo
	 * @param fullPath
	 * @return
	 */
	public static String getFileExt(String fullPath){
		String fileName = "", fileExt="";
		
		if(fullPath!=null && !(fullPath.trim().equals("")) ){
			
				fileName = fullPath.substring(fullPath.lastIndexOf("/")+1,fullPath.length());
				
				if(fileName.indexOf(".")!=-1){
					fileExt = fullPath.substring(fullPath.lastIndexOf(".")+1,fullPath.length());
				}
		}
		
		return fileExt;
	}
	
	
	/**
     * Obtiene un arreglo de cadenas desde una cadena a separar por token
     * @param strDatos
     * @param delim
     * @return
     */
    public static String[] getTokenizerData(String strDatos, String delim){
    	StringTokenizer tokens=new StringTokenizer(strDatos, delim);
    	int iDatos = tokens.countTokens();
    	System.out.println("iDatos: " + iDatos );
    	String[] datos = new String[iDatos];    	
    	int i=0;
    	 while(tokens.hasMoreTokens()){
             String str=tokens.nextToken();
             datos[i]=str;
             i++;
         }    	
    	return datos;
    }
    
    /**
	 * Elimina un archivo, regresa booleano con resultado
	 * @param fullPath
	 * @return
	 */
	public static boolean delFile( String fullPath ){
		boolean deleted = false;
		try {
			File file = new File(fullPath);
			 deleted = file.delete();			 
		}catch (Exception e) 
	    {       e.printStackTrace();	    }
		if(!deleted){
		 	System.out.println("No se pudo eliminar el archivo " + fullPath);
		 	}
		return deleted;
	}
	
	

	/**
	 * Mueve archivo a nuevo directorio, si existe lo elimina y lo vuelve a escribir
	 * @param pathToMove
	 * @param file
	 * @return
	 */
	public static boolean moveRenameFile(String filePath, String newFilePath) throws Exception{
		log4j.debug("filePath: " + filePath);
		log4j.debug("newFilePath: " + newFilePath);
		
		boolean moved = false;
		try{
			File file = new File(filePath);
			File nfile = new File(newFilePath);
			if(nfile.exists()){
				if(nfile.delete()){
					moved = file.renameTo(nfile);
				}
			}else{
				moved = file.renameTo(nfile);
			}
		}catch (Exception e) {
			//e.printStackTrace();
			log4j.fatal("Excepcion: ", e);
			throw e;
		}
		return moved;
	}
	
	/**
	 * Realiza una copia de archivo de un origen a una copia
	 * @param pathOrigen
	 * @param pathDestino
	 * @return
	 * @throws Exception
	 */
	public static boolean copyFile(String pathOrigen, String pathDestino) throws Exception {
		boolean copied = false;
		log4j.debug("pathOrigen: " + pathOrigen);
		log4j.debug("pathDestino: " + pathDestino);
		InputStream is = null;
	    OutputStream os = null;
		
		try{
			File fileI = new File(pathOrigen);
			File fileO = new File(pathDestino);
			if(fileI.exists()){				
				is = new FileInputStream(fileI);
		        os = new FileOutputStream(fileO);
		        byte[] buffer = new byte[1024];
		        int length;
		        while ((length = is.read(buffer)) > 0) {
		            os.write(buffer, 0, length);
		        }
		        copied = true;
			}else{
				log4j.error("El archivo Origen no existe");
			}
		}catch (Exception e) {
			//e.printStackTrace();
			log4j.fatal("Excepcion: ", e);
			copied = false;
			throw e;
		}finally {
			if(is!=null){
				is.close();
			}
	        if(os!=null){
	        	os.close();
	        }
	    }
		
		return copied;
	}
	/**
	 * emula al StopWords
	 * @param cadena
	 * @param tokens
	 * @return
	 */
	public static String stopWordsTce(String cadena, String[] tokens){
		for(int x=0;x<tokens.length;x++){
			cadena = cadena.replace(tokens[x], "");
		}
		for(int y=0;y<repArticulos.length;y++){
			cadena = cadena.replace(repArticulos[y], "");
		}
		
		return cadena;
	} 
    
    
	public static void main(String[] args) {
		/*List lista = listFilesInDir("/home/tce/Documents/TCE/Selex-Curricula-Clasificada/JSONBack/");
		log4j.debug("Lista:\n" + lista );//*/
//		String fullPath = "/home/dothr/JsonUI/createdJson/MatrizPermisos.csv";
		//System.out.println(getFileExt(fullPathT));
//		readFile(fullPath);
		
		String linea = "54,COMPANY.DIS,/module/curriculumCompany/disassociate,,true,true,URI para dessociar una persona de una empresa,1";
		String[] datos = getTokenizerData(linea, ",");
		for(int x=0;x<datos.length;x++){
			System.out.println(datos[x]);
		}
		
	}
	
	
	
	
	/**
	 * Lee un archivo y genera un List de cadenas read-*
	 * <b> Unicamente para leer el archivo de lista de CurriculumManagement </b>
	 * @param fullPath
	 */
	public static List<String> readListaTxt(String listaTxtPath){
		List<String> lsRead = new ArrayList<String>();
		try 
	    {
	        BufferedReader infile = new BufferedReader(new FileReader(listaTxtPath));
	        String strLine;
	        while ((strLine = infile.readLine()) != null) 
	        {
	        	lsRead.add(strLine);
	        }
	        infile.close();	        
	    } 
	    catch (IOException e) 
	    {	//System.err.println("IOException "+e);
	    	e.printStackTrace();
	    	log4j.fatal("Excepcion de lectura ", e);
	    }
	    
	    return lsRead;
	}
	
	public static List<String> getTokens(String input, String sep) {
		List<String> items = Arrays.asList(input.split("\\s*"+sep+"\\s*"));
		return items;
	}
	
	/**
	 * Lee un archivo y genera una Lista de cadenas por cada linea en el archivo
	 * @param fullPath
	 */
	public static List<String> getLinesFile(String fullPath){
		List<String> lsRead = new ArrayList<String>();
		try 
	    {
	        BufferedReader infile = new BufferedReader(new FileReader(fullPath));
	        String strLine;
	        while ((strLine = infile.readLine()) != null) 
	        {
	        	lsRead.add(strLine);
	        }
	        infile.close();	        
	    } 
	    catch (IOException e) 
	    {	//System.err.println("IOException "+e);
	    	e.printStackTrace();
	    	log4j.fatal("Excepcion de lectura ", e);
	    }
	    
	    return lsRead;
	}
	
	
	/* ********** metodos de JSON  ************** */
	/**
	 * Obtiene un objeto de JSONArray a partir de un archivo plano
	 * @param jsonFileName [i.e. read-1.json]
	 * @param jsonDir [i.e. /home/user/dir/]
	 * @return
	 * @throws Exception
	 */
	public static JSONArray getJsonArray(String jsonFileName, String jsonDir) throws Exception {
		String stJson = getJsonFile(jsonFileName, jsonDir);
		JSONArray jsArray = new JSONArray(stJson);
		
		return jsArray;
	}
	/**
	 * Obtiene un objeto de JSONObject a partir de un archivo plano
	 * @param jsonFileName [i.e. read-1.json]
	 * @param jsonDir [i.e. /home/user/dir/]
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getJsonObj(String jsonFileName, String jsonDir) throws Exception {
		String stJson = getJsonFile(jsonFileName, jsonDir);
		JSONObject json = new JSONObject(stJson);
		
		return json;
	}
	
	/**
	 * Obtiene el json contenido en un archivo plano .json
	 * @param jsonPath [i.e. /home/user/dir/read-1.json]
	 * @return
	 */
	public static String getJsonFile(String jsonPath) {		
		String jsonFile = "[]";
		try {
			jsonFile = getBuilderNoTabsFile(jsonPath, PCHARSET).toString();
		} catch (IOException e) {			
			log4j.fatal("Excepción al generar Json desde archivo: [".concat(jsonPath).concat("]: "), e);
			jsonFile = "[]";
		}
		return jsonFile;
	}
	
	/**
	 * Crea la estructura de directorios si no existe
	 * @param fullPath
	 * @param crear
	 * @return
	 */
	public static boolean createDirIfNotExist(String fullPath, boolean crear) {
		//BACKUP_JSON_DIR
		File directory = new File(fullPath);
		
		if (!directory.exists()){
			log4j.debug("NO existe ruta: " + fullPath );
			if(crear) {
				log4j.debug("Se crea ruta");
				directory.mkdirs();
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return true;
		}
		
	}
	
	/**
	 * Escribe una cadena de texto en un archivo
	 * @param filePath ruta, nombre y extension  del archivo
	 * @param texto Cadena a agregar
	 * @param append agrega (true) o reemplaza (false)
	 * @throws IOException 
	 */
	public static void writeFile(String filePath, String texto, boolean append ) {
		
		BufferedWriter bufferedWriter;
		if(null == texto){
			return;
		}
		try {
			//bufferedWriter = new BufferedWriter(new FileWriter(filePath, append));
			bufferedWriter = new BufferedWriter (new OutputStreamWriter(new FileOutputStream(filePath, append),"UTF-8"));
			bufferedWriter.write(texto);
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
//			throw e;
		}
		
	}
}
