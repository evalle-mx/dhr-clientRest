package net.mock;

import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import net.mock.json.GeneraJSONData;
import net.utils.ClientRestUtily;
import net.utils.ConstantesREST;

/**
 * Clase para generar los archivos requeridos en el ambiente emulado (TransactionalMock)
 * @version Octubre 2018
 * @author dothr
 *
 */
public class CreateMockData extends GeneraJSONData {

	private static StringBuilder sbMain;
	private static JSONObject jsonMainConf;
	private static JSONArray jsUricodes;
	private static JSONArray jsMenu;
	
	private static final String HTTP_IMAGE_ROOT="/home/netto/app/webServer/demo/selex/imagenes/";
	private static final String DEFAULT_PASSWORD = "123456789";
	
	
	//Rutas de archivos de entrada [INput]
//	private static final String ROOTPATH = "/home/dothr/workspace/ClientRest/files/";
//	private static final String CSV_Matriz = ROOTPATH+"RolesPermiso.csv";//Se genera manualmente a partir de la matriz
	
	
	private static final String listaUsuarios = MOCK_DATA_FOLDER+"ListUsersDemo.txt";
			//"/home/dothr/JsonUI/module/curriculumManagement/ListaUsers.txt";
	
	//Archivos de Salida
	private static final String fJsonUricode = MOCK_DATA_FOLDER+"output/uricodes.json";
	private static final String fJsonDummyServ = MOCK_DATA_FOLDER+"output/dummyServices_MGen.json";
	
	private static final String fJsonUsers = MOCK_DATA_FOLDER+"output/userData.json";
	private static final String fJsonPersonaGet = MOCK_DATA_FOLDER+"output/persona-get.json";
	
	
	private static final String fJsonPermisoUri = 
			//MOCK_DATA_FOLDER+"get-uriCodes.json";
			JSONFILES_ROOT_DIR+"/admin/permission/get-uriCodes.json";
	private static final String fJsonPermisoMenu = 
			//MOCK_DATA_FOLDER+"get-menu.json"; 
			JSONFILES_ROOT_DIR+"/admin/permission/get-menu.json";
	
	private static final String pathMainReport = MOCK_DATA_FOLDER+"REP.CreateMockData.txt";
	
	//Segmento de codigo json para el archivo estatico
	private final static String plantDummyServ = "else if(uriCode == '<URICODE>'){" +
			"\n			 uriService = '<ENDPOINT>';" +
			"\n			 uriFile = transUriFile(uriService, '/<MODULEADMIN>/');"+
			"\n		}\n		";
	
	private static final String tipoUricode = "1";
	
	
	public static void main(String[] args) {
		try {
//			displayPermisoDB();
//			procesaCsvPermiso();
			
			mainGenerate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private static void mainGenerate() throws Exception{
		sbMain = new StringBuilder("/* **************** PROCESO DE GENERACIÓN DE ARCHIVOS ****************** */ \n");
		System.out.println("<OK> 0. Se obtiene jsonMainConf, previamente generado ");
		try{
			jsonMainConf = getMainJsonConf(); //Obtiene el json de configuración (Parámetros, catalogos)		
			sbMain.append("\t<OK> 0. Se obtiene jsonMainConf, previamente generado \n");
			
			if(generaArchivosPermiso()){
				System.out.println("<OK> 1. Se genero UriCodes (y dummyServices)");
				sbMain.append("\t<OK> 1. Se genero UriCodes (y dummyServices) \n");
				//2.generar userdata (createUserDataJson)
				if(createUserDataJson()){
					sbMain.append("\t<OK> 2. Se generó archivo de usuarios \n");
					//3. generar json de servicio permission/get (uricode):
					if(createPermUricode()){
						sbMain.append("\t<OK> 3. Se generó archivo de permission/get de Uricode \n");
						//4. Generar json de servicio permission/get (menu)
						if(createPermMenu()){
							sbMain.append("\t<OK> 4. Se generó archivo de permission/get de Menu \n");
							System.out.println(":::: Todos los archivos de sistema Generados");
						}
					}
					
				}
			}			
			sbMain.append("/* **************** FIN DE PROCESO DE GENERACIÓN ****************** */ \n ");
		}catch (Exception e){
			e.printStackTrace();
			sbMain.append("<Exception> Error al procesar: ").append(e.getLocalizedMessage());
		}
		
		writeFile(pathMainReport, sbMain.toString());
		System.out.println("FIN: se escribe el reporte general en: "+pathMainReport);
	}
	
	
	/**
	 * Genera la lista de PERSONA.G , a partir del archivo list en la carpeta con los json para incluirlos en el ambiente Demo 
	 * @throws Exception
	 */
	protected static boolean createUserDataJson() throws Exception {
		sbMain.append("/* ++ createUserDataJson ++ */ \n");
		JSONArray jsArray, jsOut = new JSONArray();
		String defRol = "3";	//3. Candidato.
		String idRol;
		JSONObject jsonPersona, jsonUser, jsonImg;
		String fileName;
		List<String> lsRead = gLinesFile( listaUsuarios );
		if(lsRead.isEmpty()){
			System.out.println("No existen datos en el archivo");
			sbMain.append("No existen datos en el archivo lista de Usuarios: ").append(listaUsuarios).append("\n");
			return false;
		}else{
			sbMain.append("La lista contiene ").append(lsRead.size()).append(" elementos \n");
			JSONArray jsRol = jsonMainConf.getJSONArray("rol");
			int x=0;
			JSONObject jsonRol;
			if(jsRol!=null && jsRol.length()>0){
				Iterator<String> itRead = lsRead.iterator();
				while(itRead.hasNext()){
					fileName = itRead.next();
//					System.out.println(fileName);
					if(!fileName.startsWith("/*")){	//comentarios
						System.out.println("archivo: " + fileName);
						jsArray = getJsonArr(ConstantesREST.JSON_HOME+"/module/curriculumManagement/", fileName);
						
						jsonPersona = jsArray.getJSONObject(0);
						jsonUser = new JSONObject();
						jsonUser.put("idPersona", jsonPersona.get("idPersona"));
						jsonUser.put("email", jsonPersona.get("email"));
						jsonUser.put("password", jsonPersona.has("password")?jsonPersona.get("password"):DEFAULT_PASSWORD);
						
						jsonUser.put("idEstatusInscripcion", jsonPersona.get("idEstatusInscripcion"));
						jsonUser.put("idEmpresaConf", jsonPersona.has("idEmpresaConf")?jsonPersona.get("idEmpresaConf"):"1");
						
						jsonUser.put("nombre", jsonPersona.has("nombre")? jsonPersona.get("nombre"):"USUARIO");
						jsonUser.put("apellidoPaterno", jsonPersona.has("apellidoPaterno")? jsonPersona.get("apellidoPaterno"):null);
						jsonUser.put("apellidoMaterno", jsonPersona.has("apellidoMaterno")? jsonPersona.get("apellidoMaterno"):null);

						jsonUser.put("idEmpresa", jsonPersona.has("idEmpresa")? jsonPersona.get("idEmpresa"):"1");
						jsonUser.put("idEmpresaExterno", jsonPersona.has("idEmpresaExterno")?jsonPersona.get("idEmpresaExterno"):"1");
						
						idRol = jsonPersona.has("idRol")?jsonPersona.getString("idRol"):defRol;
						jsonUser.put("idRol", idRol );
						jsonUser.put("role", idRol );

						jsonUser.put("fileName", fileName);
						
						//Obtener vista inicial a partir de idRol:
						for(x=0;x<jsRol.length();x++){
							jsonRol = jsRol.getJSONObject(x);
							if(jsonRol.getString("idRol").equals(idRol)){
								jsonUser.put("vistaInicial", jsonRol.getString("vistaInicial") );
								jsonUser.put("lbRol", jsonRol.getString("descripcion") );
							}
						}
						
						if(jsonPersona.has("imgPerfil")){
							jsonImg = jsonPersona.getJSONObject("imgPerfil");
							jsonUser.put("imgPerfil", HTTP_IMAGE_ROOT+jsonImg.getString("url"));
						}
						
						jsOut.put(jsonUser);
					}
					else{
						//COmentarios:
						System.out.println(fileName);
					}
				}
				System.out.println("Se escribe archivo en: " + fJsonUsers);
				writeJsonFile(fJsonUsers, jsOut.toString());
				sbMain.append("<OK> Se escribio el archivo ").append(fJsonUsers).append("\n \n");
				
				writeJsonFile(fJsonPersonaGet, jsOut.toString());
				sbMain.append("<OK> Se escribio el archivo ").append(fJsonPersonaGet).append("\n \n");
				
			}//Si hay roles en configuración y hay usuarios para insertar
			else{
				System.out.println("No hay datos para continuar");
				sbMain.append("<FAIL> Se requieren ROles y/o usuarios a insertar \n \n");
				return false;
			}
						
			return true;
		}
	}
	/**
	 * Genera el Json que consume el servicio PERMISSION.G [/module/permission/get.json], obteniendolo del json uriCodes
	 * 
	 * @return
	 */
	protected static boolean createPermUricode() throws Exception {
		sbMain.append("Creación de json permisos de UriCode\n");
		Integer idPermiso;
		JSONObject jsonUricode;
		JSONArray jsOut = new JSONArray(); 
		System.out.println("jsUricodes: " + jsUricodes );
		for(int x=0;x<jsUricodes.length();x++){
			jsonUricode =  jsUricodes.getJSONObject(x) ;
			System.out.println("jsonUricode: "+ jsonUricode );
			idPermiso = Integer.valueOf(jsonUricode.getString("idPermiso"));
			
			jsonUricode.remove("idPermiso");
			jsonUricode.put("activo", true);
			jsonUricode.put("idPermiso", idPermiso);
			jsOut.put(jsonUricode);
		}
		sbMain.append("Se crea archivo ").append(fJsonPermisoUri).append("\n");
		writeJsonFile( fJsonPermisoUri, jsOut.toString() );
		return true;
	}
	/**
	 * Genera el Json que consume el servicio PERMISSION.G [/module/permission/get.json], para Menú obteniendolo del json Menu
	 * 
	 * @return
	 */
	protected static boolean createPermMenu() throws Exception {
		Integer idPermiso;
		JSONObject jsonMenu;
		JSONArray jsOut = new JSONArray(); 
		for(int x=0;x<jsMenu.length();x++){
			jsonMenu = jsMenu.getJSONObject(x) ;
			idPermiso = Integer.valueOf(jsonMenu.getString("idPermiso"));
			
			jsonMenu.remove("idPermiso");
			jsonMenu.put("activo", true);
			jsonMenu.put("idPermiso", idPermiso);
			jsOut.put(jsonMenu);
		}
		sbMain.append("Se crea archivo ").append(fJsonPermisoMenu).append("\n");
		writeJsonFile(fJsonPermisoMenu, jsOut.toString() );
		return true;
	}
	
	
	/**
	 * 1. Transcribe los permisos listados en el archivo permisos.csv y <br> 
	 * Genera el archivo json con permisos de uriCode, así como una réplica para el webServer demo.<br> 
	 * <i>(PENDIENTE crear el menu.json por medio de este)</i> 
	 * @throws Exception
	 */
	protected static boolean generaArchivosPermiso() throws Exception {
		sbMain.append("/* 1: GeneraArchivos Permiso */ \n");
		//1. OBtener permisos en objeto Json
		JSONArray jsPermisos = getPermisosCsv();
		if(jsPermisos.length()>0){
			sbMain.append("Se obtuvieron ").append(jsPermisos.length()).append(" permisos del archivo csv \n");
			jsUricodes = new JSONArray();
			jsMenu = new JSONArray();
			StringBuilder sbDummyServ = new StringBuilder(); /* Archivo de servicios para versión estatica en webserver */
			
			JSONObject jsonPer;
			for(int x=0;x<jsPermisos.length();x++){
				jsonPer = jsPermisos.getJSONObject(x);
//				System.out.println(jsonPer);
				if(jsonPer.getString("idTipoPermiso").equals(tipoUricode)){ /* tipo uriCode */
					jsUricodes.put(jsonPer);
						
						//Adicional para generar archivo de servicios en webServer Estático
						if(jsonPer.getString("valor").indexOf("/module/")!=-1){
							sbDummyServ.append(
									plantDummyServ.replace("<URICODE>", jsonPer.getString("contexto"))
										.replace("<ENDPOINT>", jsonPer.getString("valor"))
										.replace("<MODULEADMIN>", "module")
									);
						}else{
							sbDummyServ.append(
									plantDummyServ.replace("<URICODE>", jsonPer.getString("contexto"))
										.replace("<ENDPOINT>", jsonPer.getString("valor"))
										.replace("<MODULEADMIN>", "admin")
									);
						}
						//*/
						
					}else{
						jsMenu.put(jsonPer);
					}
				}
				/* Creación de archivo de uricodes.json  */
				StringBuilder sbUricodes = new StringBuilder("[\n  {\n    \"permiso\": ")
					.append(jsUricodes.toString()).append("\n  }\n]");				
				System.out.println("Se escribe archivo: " + fJsonUricode );
				ClientRestUtily.writeStringInFile(fJsonUricode, sbUricodes.toString(), false);
				sbMain.append("Se escribió archivo ").append(fJsonUricode).append("\n");
				
				//TODO diseñar algoritmo para empatar los permisos con los permisos por rol [rol_permiso.csv]
				
				/* Archivo de servicios dummy en webserver */
				System.out.println("Se escribe archivo: " + fJsonDummyServ );
				ClientRestUtily.writeStringInFile(fJsonDummyServ, sbDummyServ.toString(), false);
				sbMain.append("Se escribió archivo ").append(fJsonDummyServ).append("\n");
				return true;
		}
		else{
			System.out.println("No hay permisos, s");
			sbMain.append("NO HAY PERMISOS A PROCESAR (vacio)");
			return false;
		}
	}
	
	
}
