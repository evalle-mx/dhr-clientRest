package net.mock.json;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import net.db.DataGenerator;
import net.utils.ClientRestUtily;
import net.utils.ConnectionBD;

/**
 * Clase que procesa los principales datos para procesar en archivos/Objetos tipo JSON
 * @author dothr
 *
 */
public class GeneraJSONData extends DataGenerator {

	/* CONSTANTES */
	protected static final String JSONFILES_ROOT_DIR = "/home/netto/workspDhr/JSONUI/";
	protected static final String LOAD_CSV_DIR = "/media/netto/Work320G/SVN/dothr_otros/Data/Postgre/loads/csv/";
	protected static final String MOCK_DATA_FOLDER = "/home/netto/workspDhr/JSONUI/mockData/";
	
	/* Archivos de entrada de SVN */
	private static final String PERMISO_CSV = LOAD_CSV_DIR+"permiso.csv";
	private static final String ROL_CSV = LOAD_CSV_DIR +"rol.csv";
	
	
	/* archivo json principal de salida */
	private static final String mainConfigName = "mainConfig.json";
//	private static final String pathMainConfigJson = MOCK_DATA_FOLDER+mainConfigName;
	
	
	
	
	/**
	 * METODO PRINCIPAL que Construye el NUEVO 
	 * json con información de sistema, catálogos o parámetros
	 * @throws Exception
	 */
	private static void generaMainJsonConf() throws Exception {
		System.out.println("<generaMainJsonConf> Proceso para generar el json Principal de configuración");
		JSONObject jsonMainConf = new JSONObject();
		
		/*  A.Datos de catálogos */
		//Roles (leer datos de rol.CSV)
		System.out.println("Obteniendo informacion para rol de archivo: "+ ROL_CSV );
		JSONArray jsRoles = getRolCsv();
		jsonMainConf.put("rol", jsRoles);
		
		//Se escribe el archivo en DIrectorio
		writeFile(MOCK_DATA_FOLDER+mainConfigName, ClientRestUtily.formtJsonByGoogle(jsonMainConf.toString()));
	}
	
	/**
	 * Lee el archivo de mainConfig para obtener el JSONObject getMainJsonConf
	 * @return
	 * @throws Exception
	 */
	protected static JSONObject getMainJsonConf() throws Exception {
		System.out.println("<generaMainJsonConf> Proceso para obtener el json Principal de configuración");
		JSONObject jsonMainConf = ClientRestUtily.getJsonObj(mainConfigName, MOCK_DATA_FOLDER);
		return jsonMainConf;
	}
	
	/**
	 * Lee el archivo de ROL.csv para convertir sus valores a JSON
	 * @return
	 * @throws Exception
	 */
	public static JSONArray getRolCsv() throws Exception{
		System.out.println("<getRolCsv>");
		List<String> lsCsv = gLinesFile(ROL_CSV);
		JSONArray jsRoles = new JSONArray();
		if(!lsCsv.isEmpty()){
			Iterator<String> itLine = lsCsv.iterator();
			String line; 
			JSONObject jsonRol;
			
			while(itLine.hasNext()){
				line = itLine.next();
//				System.out.println(line); //Imprimir linea en pantalla
				List<String> items = Arrays.asList(line.split("\\s*;\\s*"));
				//idRol-0;descripcion-1,rolInicial-2;vistaInicial-3
				jsonRol = new JSONObject();
				jsonRol.put("idRol", items.get(0));
				jsonRol.put("descripcion", items.get(1));
				jsonRol.put("rolInicial", items.get(2));
				jsonRol.put("vistaInicial", items.get(3));
				
				jsRoles.put(jsonRol);
			}
		}
		return jsRoles;
	}
	//TODO generar metodo genérico para obtener Catalogo
	
	
	/**
	 * Convierte el archivo CSV de permiso.csv en JsonArray
	 * y devuelve el objeto
	 * @return
	 * @throws Exception
	 */
	public static JSONArray getPermisosCsv() throws Exception {
		List<String> lsCsv = gLinesFile(PERMISO_CSV);
		JSONArray jsPermisos = new JSONArray();
		if(!lsCsv.isEmpty()){
			Iterator<String> itLine = lsCsv.iterator();
			String line; 
			JSONObject jsonPer;
			
			while(itLine.hasNext()){
				line = itLine.next();
				System.out.println(line);
				List<String> items = Arrays.asList(line.split("\\s*;\\s*"));
				jsonPer = new JSONObject();
				jsonPer.put("idPermiso", items.get(0));
				if(!items.get(1).equals("")){
					jsonPer.put("idPermisoRelacionada", items.get(1));
				}
				jsonPer.put("contexto", items.get(2));
				jsonPer.put("valor", items.get(3));
				jsonPer.put("descripcion", items.get(4));
				jsonPer.put("idTipoPermiso", items.get(5));
				
				jsPermisos.put(jsonPer);
			}		
		}
		
		return jsPermisos;
	}
	
	
	
	
	public static void main(String[] args) {
		try {
//			JSONArray jsPermisos = getPermisosCsv(); System.out.println("jsPermisos: \n "+jsPermisos );
			generaMainJsonConf();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	/* >>>>>>>>>>>>>   Auxiliares   <<<<<<<<<<<<<<<<<<<< */
	
	protected static boolean writeJsonFile(String path, String jsTexto) throws Exception {
		return writeFile(path, ClientRestUtily.formtJsonByGoogle(jsTexto));
	}
	
	protected static boolean writeFile(String path, String texto) throws Exception{
		System.out.println("\n  >> Escribiendo archivo: "+path);
		ClientRestUtily.writeStringInFile(path, texto, false);
		return true;
	}
	/**
	 * Lee un archivo y devuelve un arreglo de LIneas para  multiples propósitos
	 * @param fullPath
	 * @return
	 */
	protected static List<String> gLinesFile(String fullPath){
		List<String> lsLine = ClientRestUtily.getLinesFile(fullPath);
		return lsLine;
	}
	/**
	 * Obtiene un Objeto JSONArray a partir de un archivo plano .json
	 * utilizando la utilería de ClientRestUtily
	 * @param jsonFileName
	 * @param jsonDir
	 * @return
	 * @throws Exception
	 */
	protected static JSONArray getJsonArr(String jsonDir, String jsonFileName) throws Exception {
		JSONArray jsArr = ClientRestUtily.getJsonArray(jsonFileName, jsonDir);
		return jsArr;
	}
	
	/* ************************************************************************** */
	/* *****************  Metodos de ejemplo  *********************************** */
	/**
	 * Metodo plantilla para procesar archivo
	 * @throws Exception
	 */
	protected static void procesaCsvPermiso() throws Exception {
		List<String> lsCsv = ClientRestUtily.getLinesFile("permiso");//PERMISO_CSV
		if(!lsCsv.isEmpty()){
			System.out.println("EL archivo tiene "+lsCsv.size()+" registros");
			Iterator<String> itCsv = lsCsv.iterator();
			String linea;
			while(itCsv.hasNext()){
				linea = itCsv.next();
				System.out.println(">> " +linea);
			}
		}
	}
	/**
	 * Despliega el contenido actual de la tabla <b>permiso</b> en la Base XE
	 * @throws Exception
	 */
	protected static void displayPermisoDB() throws Exception {
		if(conn==null){
			conn = new ConnectionBD(); 
		}
		StringBuilder sb = readPermiso();
		System.out.println(sb);
		conn.closeConnection();
	}
	
}
