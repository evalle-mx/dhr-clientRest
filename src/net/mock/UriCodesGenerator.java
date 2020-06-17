package net.mock;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import net.db.DataGenerator;
import net.tce.dto.PermisoInsertDto;
import net.utils.ClientRestUtily;
import net.utils.ConnectionBD;

public class UriCodesGenerator extends DataGenerator {
	
	private static final String OUTPUT_GENERATED_FILE_DIR = "/home/dothr/JsonUI/createdJson/";
	private static final String DB_MANAGER = "PSG";
	private static final String INPUT_CSV_SVN_DIR = "/home/dothr/Documents/TCE_CODE/Data/loads/csv/";
	
	private static final String JSON_URICODE_FILENAME = "uricodes.json";
	private static final String JSON_MATRIZPERMISO_FILENAME = "JsonMatrizPermisos.txt";
	private static final String CVS_MATRIZPERMISO_FILENAME = "MatrizPermisos.csv";
	private static final String CSV_SCRIPT_FILENAME = "tipo_relacion_permiso.csv";
	
	private static final String TABLE_PERMISO = "permiso";
	private static final String CSV_SEPARATOR = ";"; 
	
	public static final String TIPO_SOLICITANTE = "8";
	public static final String TIPO_CONTRATANTE = "11";
	public static final String TIPO_ADMINISTRADOR = "9";
	
	private static boolean incluyeDescripcion = false;
	
	private static String keyOtorgado = "true";
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		if(conn==null){
			conn = new ConnectionBD(); 
		}
		
		showUricodes();
//		exportUricodesJson();
		
		//reportePermisosPorTipo(TIPO_CONTRATANTE);
		
//		generaMatrizPermisos(getListaTipoRelacion());
//		generaInsertsDeMatriz();
		
		
//		generaPermisoInsertsFromCSV();
		/* StringBuilder sbOut = getUricodes();
		System.out.println(sbOut); */
		
		/*
		 * Metodologia para Implementar URICODES
		 * 
		 * 1. ejecutar showUricodes() para mostrar la lista de permisos(uricodes) disponibles
		 * 2. comprobar que este completa y ordenada la lista, sino modificar en Postgre y volver a paso 1
		 * 3. Ejecutar generaMatrizPermisos() para obtener la  matriz de permisos/tipoPersona [json y csv]
		 * 4. Abrir archivo MatrizPermisos.csv en Editor de hoja de calculo
		 * 5. Añadir/quitar permisos (true) en la columna correspondiente al tipo de Relacion de persona
		 * 6. Ejecutar generaInsertsDeMatriz() para obtener un archivo Inserts-tipo_relacion_permiso.sql que contiene los inserts a la tabla tipo_relacion_permiso
		 * 7. Ejecutar el archivo Inserts-tipo_relacion_permiso.sql en postgreSql
		 * 
		 */
		
		conn.closeConnection();
	}

	/**
	 * Genera lista de Tipos Relacion existentes
	 * @return
	 */
	protected static List<String> getListaTipoRelacion(){
		List<String> lsIdTipoRelacion = new ArrayList<String>();
		lsIdTipoRelacion.add(0, TIPO_SOLICITANTE);
		lsIdTipoRelacion.add(1, TIPO_CONTRATANTE);
		lsIdTipoRelacion.add(2, TIPO_ADMINISTRADOR);
		return lsIdTipoRelacion;
	}
	
	/**
	 * Obtiene etiqueta/Descripcion del tipoRelacion (para este proceso)
	 * @param idTipoRelacion
	 * @return
	 */
	private static String getDescript(String idTipoRelacion){
		String desc = "";
		switch (idTipoRelacion) {
			case TIPO_SOLICITANTE:
				desc = "Solicitante";
            break;
			case TIPO_CONTRATANTE:
				desc = "Contratante";
            break;
			case TIPO_ADMINISTRADOR:
				desc = "Administrador";
            break;
			default:
				desc = "??";
		}
        return desc;
	}
	
	private static String exBol(String tipo){
		return tipo.equals("1")?"true":"";
	}
	
	
	/**
	 * Realiza una consulta a la tabla uriCodes y regresa el resultado en un StringBuilder 
	 * (texto plano), y posteriormente procesaro o exportarlo a un txt o un csv
	 * @return
	 */
	public static void showUricodes(){
		System.out.println("Mostrando el contenido de la tabla: "+TABLE_PERMISO+" en la base de XE");
		StringBuilder sbUricode = getTableContent(TABLE_PERMISO);
		System.out.println(sbUricode);
	}
	
	/**
	 * Ejecuta el proceso de lectura y obtiene arreglo de Json
	 * emulando el obtenido por el servicio en Transactional
	 * (Contiene todos los endPoints dados de alta en sistema, sin importar el tipoRelacion de persona)
	 * @throws Exception 
	 */
	public static JSONArray getJsonUricodesFromDB() throws Exception{
		StringBuilder sbFile = new StringBuilder();
		JSONArray jsArr = new JSONArray();
		JSONObject json = null;
		
		try{
			conn.getDbConn();
			sbFile.append("SELECT p.id_permiso AS idPermiso, p.contexto AS contexto, p.valor AS valor")
					.append(", p.descripcion AS descripcion, p.id_tipo_permiso AS idTipoPermiso")
					.append(" FROM permiso AS p ")
					.append(" ORDER BY p.contexto ");
			
				ResultSet rs = conn.getQuerySet(sbFile.toString());
				if(rs!= null ){
					sbFile = new StringBuilder();
					while (rs.next()){
						json = new JSONObject();
						json.put("idTipoPermiso", rs.getString("idTipoPermiso"));
						json.put("valor", rs.getString("valor"));
						json.put("contexto", rs.getString("contexto"));
						/*  => No estan en el json del servicio: */
						json.put("idPermiso", rs.getString("idPermiso") );
						json.put("descripcion", rs.getString("descripcion"));
						jsArr.put(json);
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
				jsArr = null;
				throw e;
			}
		return jsArr;
	}
	
	/**
	 * Ejecuta el proceso de lectura y genera el archivo <b>uriCodes.json</b>
	 * emulando el obtenido por el servicio en Transactional
	 * (Contiene todos los endPoints dados de alta en sistema, sin importar el tipoRelacion de persona)<br>
	 * <br>
	 * <i>[este json es el q emplea transactionalMock para obtener endPoints]
	 */
	public static void exportUricodesJson(){
		StringBuilder sbFile = new StringBuilder();
		boolean success = false;
		try{
			
			JSONArray jsArr = getJsonUricodesFromDB();
			sbFile.append("[ { \""+ TABLE_PERMISO + "\":").append(jsArr).append("} ]");
			ClientRestUtily.writeStringInFile(OUTPUT_GENERATED_FILE_DIR+JSON_URICODE_FILENAME, sbFile.toString(), false);
			success=true;
		}catch (Exception e) {
			e.printStackTrace();
			sbFile.append("<Exception> ").append(e.getMessage());
		}
		
		if(success){
			System.out.println("PROCESO TERMINADO SIN ERRORES\n"+sbFile.toString());
		}
		else
			System.out.println("PROCESO TERMINADO CON ERRORES ");
	}
		
	
	/**
	 * Ejecuta el proceso de lectura y obtiene arreglo de Json
	 * emulando el obtenido por el servicio en Transactional
	 * (Contiene todos los endPoints dados de alta en sistema, sin importar el tipoRelacion de persona)
	 * @throws Exception 
	 */
	public static JSONArray getJsonUricodesByTypeDB(String idTipoRelacion) throws Exception{
		StringBuilder sbFile = new StringBuilder();
		JSONArray jsArr = new JSONArray();
		JSONObject json = null;
		
		try{
			conn.getDbConn();
			sbFile.append("SELECT trp.id_tipo_relacion as idTipoRelacion, p.id_permiso AS idPermiso")
			.append(", p.contexto AS contexto, p.valor AS valor, p.descripcion AS descripcion, p.id_tipo_permiso AS idTipoPermiso")
			.append(" FROM TIPO_RELACION_PERMISO trp, Permiso p")
			.append(" WHERE trp.id_permiso = p.id_permiso")
			.append(" AND id_tipo_relacion = ").append(idTipoRelacion)
			.append(" ORDER BY p.contexto ");
			
				ResultSet rs = conn.getQuerySet(sbFile.toString());
				if(rs!= null ){
					sbFile = new StringBuilder();
					while (rs.next()){
						json = new JSONObject();
						json.put("idPermiso", rs.getString("idPermiso") );
						json.put("contexto", rs.getString("contexto"));
						json.put("valor", rs.getString("valor"));
						
						json.put("idTipoPermiso", rs.getString("idTipoPermiso"));
						json.put("descripcion", rs.getString("descripcion"));
						json.put("idTipoRelacion", rs.getString("idTipoRelacion"));
						jsArr.put(json);
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
				jsArr = null;
				throw e;
			}
		return jsArr;
	}
	
	
	/**
	 * Genera una MATRIZ relacional entre URICODES y los tipo Relacion existentes 
	 * (listaIdTIpoRelacion)
	 */
	protected static void generaMatrizPermisos(List<String> lsIdTipoRelacion){
		boolean success = false;
		StringBuilder sbFile = new StringBuilder();
		
		String idPermiso;
		
		try{
			JSONArray jsArrPermisos = getJsonUricodesFromDB();
			
			Iterator<String> itTipoRelacion = lsIdTipoRelacion.iterator();
			String idTipoRelacion, pTipo;
			JSONArray jsArrTmp;
			while(itTipoRelacion.hasNext()){
				idTipoRelacion = itTipoRelacion.next();
				System.out.println("Evaluando tipoRelacion: " + idTipoRelacion);
				pTipo = "p"+getDescript(idTipoRelacion);
				jsArrTmp = getJsonUricodesByTypeDB(idTipoRelacion);
				
				if(jsArrTmp!=null && jsArrTmp.length()>0){
					JSONObject jsonPermiso, jsonSys;
					for(int x=0; x<jsArrPermisos.length();x++){
						jsonSys = jsArrPermisos.getJSONObject(x);
						
						idPermiso = jsonSys.getString("idPermiso");
						boolean tienePermiso = false;
						for(int y=0; y<jsArrTmp.length();y++){
							jsonPermiso = jsArrTmp.getJSONObject(y);
							if(idPermiso.equals(jsonPermiso.getString("idPermiso"))){
								tienePermiso = true;
								break;
							}
						}
						jsonSys.put(pTipo, (tienePermiso?"1":"0") );
					}
				}
			}
			sbFile.append(jsArrPermisos); /* En este punto se tiene el Json con los valores matriciales, y es posible exportarlo */
			ClientRestUtily.writeStringInFile(OUTPUT_GENERATED_FILE_DIR+JSON_MATRIZPERMISO_FILENAME, sbFile.toString(), false);
			
			sbFile = convierteJsonTextoFormat(jsArrPermisos);	//convertJsonToText(jsArrPermisos);
			ClientRestUtily.writeStringInFile(OUTPUT_GENERATED_FILE_DIR+CVS_MATRIZPERMISO_FILENAME, sbFile.toString(), false);
			
			success = true;
		}catch (Exception e) {
			e.printStackTrace();
			sbFile.append("<Exception> ").append(e.getMessage());
		}
		
		if(success){
			System.out.println("PROCESO TERMINADO SIN ERRORES");
		}
		else
			System.out.println("PROCESO TERMINADO CON ERRORES ");
		
	}
	
	/**
	 * Convierte un arreglo de Json en un texto personalizado, estilo matriz 
	 * con el nombre de Parametro en el encabezado
	 * 
	 * @param jsArr
	 * @return
	 * @throws Exception
	 */
	protected static StringBuilder convierteJsonTextoFormat(JSONArray jsArr) throws Exception {
		StringBuilder sbFile = new StringBuilder();
		String separador = ";";
		
		if(jsArr!=null && jsArr.length()>0){
			//	pSolicitante	pContratante	pAdministrador	
			JSONObject jsonSys;
			sbFile.append("idPermiso").append(separador)
				.append("contexto").append(separador)
				.append("valor").append(separador)
				.append("Solicitante").append(separador)
				.append("Contratante").append(separador)
				.append("Administrador").append(separador)
				.append("descripcion").append(separador)
				.append("idTipoPermiso").append("\n");
			
			for(int x=0; x<jsArr.length();x++){
				jsonSys = jsArr.getJSONObject(x);
				sbFile.append(jsonSys.get("idPermiso")).append(separador)
				.append(jsonSys.get("contexto")).append(separador)
				.append(jsonSys.get("valor")).append(separador)
				.append(exBol(jsonSys.has("pSolicitante")?jsonSys.getString("pSolicitante"):"")).append(separador)
				.append(exBol(jsonSys.has("pContratante")?jsonSys.getString("pContratante"):"")).append(separador)
				.append(exBol(jsonSys.has("pAdministrador")?jsonSys.getString("pAdministrador"):"")).append(separador)
				.append(jsonSys.get("descripcion")).append(separador)
				.append(jsonSys.get("idTipoPermiso")).append("\n");
			}
		}
		return sbFile;
	}
	
	/**
	 * Genera un archivo txt con el reporte de Permisos otorgados y denegados por tipo de Relacion (usuario)
	 */
	protected static void reportePermisosPorTipo(String idTipoRelacion){
		StringBuilder sbFile = new StringBuilder();
		boolean success = false;
		String idPermiso, contexto, valor, descripcion;
		System.out.println("\t Reporte para " + getDescript(idTipoRelacion));
		String sqlRemove = "DELETE FROM tipo_relacion_permiso WHERE id_permiso = ? AND id_tipo_relacion = "+idTipoRelacion ;
		String sqlAdd = "Insert into TIPO_RELACION_PERMISO (ID_TIPO_RELACION_PERMISO,ID_TIPO_RELACION,ID_PERMISO) values (<consecutivo>,"+idTipoRelacion +",<idPermiso>)";
		
		try{
			StringBuilder sbGranted = new StringBuilder("Permisos OTORGADOS:\t[").append(sqlRemove).append("]\n");
			StringBuilder sbDeny = new StringBuilder("Permisos DENEGADOS:\t[").append(sqlAdd).append("]\n");
			JSONArray jsArrAll = getJsonUricodesFromDB();
			JSONArray jsArrTipoSolicitante = getJsonUricodesByTypeDB(idTipoRelacion);
			
			if(jsArrAll.length()>0 && jsArrTipoSolicitante.length()>0){
				
				JSONObject jsonPermiso, jsonSys;
				
				for(int x=0; x<jsArrTipoSolicitante.length();x++){
					jsonPermiso = jsArrTipoSolicitante.getJSONObject(x);
					idPermiso = jsonPermiso.getString("idPermiso");
					contexto = jsonPermiso.getString("contexto");
					valor = jsonPermiso.getString("valor");
					descripcion = jsonPermiso.getString("descripcion");
					
					System.out.println("tamaño de permisos sistema a evaluar " + jsArrAll.length() );
					for(int y=0; y<jsArrAll.length();y++){
						jsonSys = jsArrAll.getJSONObject(y);
						if(idPermiso.equals(jsonSys.getString("idPermiso"))){
							jsArrAll.remove(y);
							break;
						}
					}
					sbGranted.append(" ").append(contexto).append(",").append(idPermiso).append(",").append(valor)
					.append(incluyeDescripcion?","+descripcion:"").append("\n");
				}
			}	
			System.out.println("# de Permisos negados a usuario " + idTipoRelacion +": "+jsArrAll.length() );
			if(jsArrAll.length()>0){
				
				JSONObject jsonSys;
				for(int y=0; y<jsArrAll.length();y++){
					jsonSys = jsArrAll.getJSONObject(y);
					idPermiso = jsonSys.getString("idPermiso");
					contexto = jsonSys.getString("contexto");
					valor = jsonSys.getString("valor");
					descripcion = jsonSys.getString("descripcion");
					sbDeny.append(" ").append(contexto).append(",").append(idPermiso).append(",").append(valor)
					.append(incluyeDescripcion?","+descripcion:"").append("\n");
				}
			}
			sbFile = new StringBuilder();
			sbFile.append("**************  Reporte para ").append(getDescript(idTipoRelacion)).append("  **************** \n")
			.append(sbGranted).append("###############\n").append(sbDeny);
			
			ClientRestUtily.writeStringInFile(OUTPUT_GENERATED_FILE_DIR+"ReportePermiso"+idTipoRelacion+".txt", sbFile.toString(), false);
			success = true;
			
		}catch (Exception e) {
			e.printStackTrace();
			sbFile.append("<Exception> ").append(e.getMessage());
		}
		
		if(success){
			System.out.println("PROCESO TERMINADO SIN ERRORES\n"+sbFile.toString());
		}
		else
			System.out.println("PROCESO TERMINADO CON ERRORES ");
	}
	
	/**
	 * Metodo que lee el archivo MATRIZ.csv para generar los inserts en tipo_relacion_permiso 
	 */
	protected static void generaInsertsDeMatriz(){
		System.out.println("PROCESO: Generando inserts en tipo_relacion_permiso para usuarios ....");
		StringBuilder sbInserts = new StringBuilder("/* INSERTS A PARTIR DE "+CVS_MATRIZPERMISO_FILENAME+".csv */\n\n");
		StringBuilder sbCsv = new StringBuilder();
		String insertClause = "INSERT INTO tipo_relacion_permiso (id_tipo_relacion_permiso,id_tipo_relacion,id_permiso) " +
				"VALUES (<consecutivo>,<tipoRelacion>,<idPermiso>);";
		int nLines = 0;
		List<PermisoInsertDto> lsInsertsDto = new ArrayList<PermisoInsertDto>();
		try{
			BufferedReader infile = new BufferedReader(
	        		 new InputStreamReader(
	                         new FileInputStream(OUTPUT_GENERATED_FILE_DIR+CVS_MATRIZPERMISO_FILENAME), "UTF8"));
	        String strLine;
	        List<String> items;
//	        String[] datos;
	        while ((strLine = infile.readLine()) != null) 
	        {   
	        	if(nLines==0){
	        		System.out.println("[Encabezado: "+strLine + "]" );
	        	}
	            if(nLines>0){
	            	items = java.util.Arrays.asList(strLine.split("\\s*;\\s*"));
	            	System.out.println(items);
	            	PermisoInsertDto dto = new PermisoInsertDto(items);
	            	lsInsertsDto.add(dto);
	            }
	            nLines++;
	            
	        }
	        infile.close();	
		}catch (Exception e){
			System.out.println("Excepcion al leer archivo Matriz.csv");
			e.printStackTrace();
		}
		
		//2. a partir de la lista se generan 1,2,3 inserts dependiendo del valor en dto
		
		if(!lsInsertsDto.isEmpty()){
			sbInserts.append("DELETE FROM tipo_relacion_permiso; \n\n");
			Iterator<PermisoInsertDto> itPermisoDto = lsInsertsDto.iterator();
			PermisoInsertDto tmpDto;
			String insertCl;
			int consecutivo = 1;
			while(itPermisoDto.hasNext()){
				tmpDto = itPermisoDto.next();
				//valua permiso para SOlicitante
				if(tmpDto.getSolicitante()!=null && tmpDto.getSolicitante().equals(keyOtorgado)){
					insertCl = insertClause.replace("<consecutivo>", String.valueOf(consecutivo))
							.replace("<tipoRelacion>", TIPO_SOLICITANTE)
							.replace("<idPermiso>", tmpDto.getIdPermiso());
					sbCsv.append(String.valueOf(consecutivo)).append(CSV_SEPARATOR).append(TIPO_SOLICITANTE).append(CSV_SEPARATOR).append(tmpDto.getIdPermiso()).append("\n");
					sbInserts.append( insertCl ).append("\n");
					consecutivo++;
				}
				//valua permiso para CONTRATANTE
				if(tmpDto.getContratante()!=null && tmpDto.getContratante().equals(keyOtorgado)){
					insertCl = insertClause.replace("<consecutivo>", String.valueOf(consecutivo))
							.replace("<tipoRelacion>", TIPO_CONTRATANTE)
							.replace("<idPermiso>", tmpDto.getIdPermiso());
					sbCsv.append(String.valueOf(consecutivo)).append(CSV_SEPARATOR).append(TIPO_CONTRATANTE).append(CSV_SEPARATOR).append(tmpDto.getIdPermiso()).append("\n");
					sbInserts.append( insertCl ).append("\n");
					consecutivo++;
				}
				//valua permiso para ADMINISTRADOR
				if(tmpDto.getAdministrador()!=null && tmpDto.getAdministrador().equals(keyOtorgado)){
					insertCl = insertClause.replace("<consecutivo>", String.valueOf(consecutivo))
							.replace("<tipoRelacion>", TIPO_ADMINISTRADOR)
							.replace("<idPermiso>", tmpDto.getIdPermiso());
					sbCsv.append(String.valueOf(consecutivo)).append(CSV_SEPARATOR).append(TIPO_ADMINISTRADOR).append(CSV_SEPARATOR).append(tmpDto.getIdPermiso()).append("\n");
					sbInserts.append( insertCl ).append("\n");
					consecutivo++;
				}
			}
		}
		ClientRestUtily.writeStringInFile(OUTPUT_GENERATED_FILE_DIR+"Inserts-tipo_relacion_permiso.sql", sbInserts.toString(), false);	
		ClientRestUtily.writeStringInFile(OUTPUT_GENERATED_FILE_DIR+CSV_SCRIPT_FILENAME, sbCsv.toString(), false);
	}
	
	private static String convertData(String data, int type){
		if(type==1){  //numero, en teoria no debe ser ni null ni blank
			return data;
		}else if(type==2){ //Character
			if(data==null || data.trim().equals("") || data.length()==0){
				return "null";
			}else{
				return "'"+data+"'";
			}
		}else if(type==3){ //Boolean
			if(data!=null && !data.trim().equals("") && (data.equals("true") || data.equals("1"))){
				return DB_MANAGER.equals("PSG")?"true":"1";
			}else{
				return DB_MANAGER.equals("PSG")?"false":"0";
			}
		}
		else{
			return data;
		}
	}
	
	/**
	 * Lee el archivo de carga (load) csv para generar Inserts
	 */
	protected static void generaPermisoInsertsFromCSV(){
		boolean success = false;
		String fileName = "permiso.csv";		//uricodes.csv
		StringBuilder sbInserts = new StringBuilder("--/* INSERTS A PARTIR DE ").append(fileName).append(".csv */\n\n");
		String InsertClause = "INSERT INTO permiso (id_permiso,contexto,valor,descripcion,id_tipo_permiso) VALUES ("
				+ "<1>,<2>,<3>,<4>,<5>);";
		//values (1,'ACADBACK.C','/module/academicBackground/create','URI para crear un nuevo registro de trayectoria Académica',1);
		
		try{
			BufferedReader br = Files.newBufferedReader(Paths.get(INPUT_CSV_SVN_DIR+fileName), StandardCharsets.UTF_8);
			 List<String> items;
//			 int nLines = 0;
			for (String line = null; (line = br.readLine()) != null;) {
					items = java.util.Arrays.asList(line.split("\\s*,\\s*"));
					String insertCl = InsertClause.replace("<1>", convertData(items.get(0),1) )
							.replace("<2>", convertData(items.get(1),2) )
							.replace("<3>", convertData(items.get(2),2) )
							.replace("<4>", convertData(items.get(3),2) )
							.replace("<5>", convertData(items.get(4),2) );
					sbInserts.append( insertCl ).append("\n");
//				nLines++;
			}
			ClientRestUtily.writeStringInFile(OUTPUT_GENERATED_FILE_DIR+fileName+".sql", sbInserts.toString(), false);
			success = true;
		}catch (Exception e){
			System.out.println("Excepcion al leer archivo CSV " + fileName );
			e.printStackTrace();
		}
		
		if(success){
			System.out.println("PROCESO TERMINADO SIN ERRORES");
		}
		else
			System.out.println("PROCESO TERMINADO CON ERRORES ");
	}
}
