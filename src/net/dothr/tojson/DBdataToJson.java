package net.dothr.tojson;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import net.db.QueryXe;
import net.dothr.MainAppTester;
import net.utils.ConnectionBD;
import net.utils.ConstantesREST;

public class DBdataToJson extends MainAppTester {
	static Logger log4j = Logger.getLogger( DBdataToJson.class );
	
	protected static ConnectionBD conn;
	
	
	
	public static void main(String[] args) {
		try{
			conn = new ConnectionBD(); 
			conn.getDbConn();
			/* 1 areas persona */
//			JSONArray jsAreas = getAreasPersonaJson(null, "2");
//			log4j.info("jsAreas: \n"+jsAreas.toString() );
			/*2 Mapa personas con pwd y relEmpPers*/
			Map<String, JSONObject> mapaPersonas = getMapAllPersonaDB(null, "1");
			log4j.info("mapa: \n" + mapaPersonas );
			
			/*3 Uricodes */
//			JSONArray jsPermiso = getUriCodePermiso(conn);
//			log4j.info("jsPermiso: " + jsPermiso.toString() );
			
			conn.closeConnection();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Genera un mapa con json's de Personas PWD RelEmpPersona, con llave idPersona
	 * @param xConn
	 * @param idEmpresa
	 * @return
	 * @throws Exception
	 */
	public static Map<String, JSONObject> getMapAllPersonaDB(ConnectionBD xConn, String idEmpresa) throws Exception {
		boolean autoCloseConn = false;
		if(xConn == null){
			log4j.debug("<getAreasPersonaJson> Creando Nueva Conexion ");
			conn = new ConnectionBD(); 
			conn.getDbConn();
			autoCloseConn = true;
		}
		else{
			conn = xConn;
		}
		String idPersona;
		JSONObject jsPersonaTmp;
		Map<String, JSONObject> mapAllPersonas = null;		
		
		
		ResultSet rs = conn.getQuerySet(QueryXe.SQL_PERSONAS_PWD.toString().replace("<idEmpresa>", idEmpresa));
		if(rs!= null ){			
			mapAllPersonas = new HashMap<String, JSONObject>();
			while (rs.next()){
				idPersona = rs.getString("idPersona");
				jsPersonaTmp = new JSONObject();
				jsPersonaTmp.put("idEmpresaConf", ConstantesREST.IDEMPRESA_CONF);
				jsPersonaTmp.put("idPersona", idPersona );
				//jsPersonaTmp.put("idRol", rs.getString("idRol"));
				jsPersonaTmp.put("idRol", "3");
				jsPersonaTmp.put("idEmpresa", rs.getString("idEmpresa"));	
				
				jsPersonaTmp.put("email", rs.getString("email") );
				jsPersonaTmp.put("nombreCompleto", rs.getString("nombreCompleto") );
				jsPersonaTmp.put("password", setPassword(rs.getString("password")) );
				jsPersonaTmp.put("estatus", rs.getString("estatus"));
				jsPersonaTmp.put("tokenInicio", rs.getString("tokenInicio"));				
				jsPersonaTmp.put("fechaCreacion", rs.getString("fCreacion"));
				jsPersonaTmp.put("fechaModificacion", rs.getString("fMod"));
				
				mapAllPersonas.put(idPersona, jsPersonaTmp);
			}
		}
		if(autoCloseConn){
			conn.closeConnection();
		}
		return mapAllPersonas;
	}
	
	/**
	 * Obtiene el listado de Uricodes (empresa_parametro) sin importar Role
	 * @param xConn
	 * @return
	 * @throws Exception
	 */
	public static JSONArray getUriCodePermiso(ConnectionBD xConn) throws Exception {
		boolean autoCloseConn = false;
		if(xConn == null){
			log4j.debug("<getUriCodePermiso> Creando Nueva Conexion ");
			conn = new ConnectionBD(); 
			conn.getDbConn();
			autoCloseConn = true;
		}
		else{
			conn = xConn;
		}
		JSONArray jsPermiso = new JSONArray();
		JSONObject jsRs = null;
		ResultSet rs = conn.getQuerySet(QueryXe.SQL_URICODES.toString());
		if(rs!= null ){			
			while (rs.next()){
				jsRs = new JSONObject();
				//id_permiso, contexto, valor, descripcion, id_tipo_permiso
				jsRs.put("idPermiso", String.valueOf(rs.getLong("id_permiso")) );
				jsRs.put("contexto", String.valueOf(rs.getString("contexto")) );
				jsRs.put("valor", String.valueOf(rs.getString("valor")) );
				jsRs.put("descripcion", String.valueOf(rs.getString("descripcion")) );
				jsRs.put("idTipoPermiso", String.valueOf(rs.getLong("id_tipo_permiso")) );
				jsPermiso.put(jsRs);
			}
		}
		if(autoCloseConn){
			conn.closeConnection();
		}
		return jsPermiso;
	}
	
	/**
	 * Lee la Base de Datos (Predefinida en ConnectionDB) para obtener registros de AreaPersona
	 * y devuelve un JSONArray con los registros encontrados (por area)
	 * @param xConn
	 * @param idPersona
	 * @return
	 * @throws Exception
	 */
	public static JSONArray getAreasPersonaJson(ConnectionBD xConn, String idPersona) throws Exception {
		boolean autoCloseConn = false;
		if(xConn == null){
			log4j.debug("<getAreasPersonaJson> Creando Nueva Conexion ");
			conn = new ConnectionBD(); 
			conn.getDbConn();
			autoCloseConn = true;
		}
		else{
			conn = xConn;
		}
		JSONArray jsAreas = new JSONArray();
		JSONObject jsRs = null;
		ResultSet rs = conn.getQuerySet(QueryXe.SQL_AREAPERSONA.toString().replace("<idPersona>", idPersona));
		if(rs!= null ){			
			while (rs.next()){
				jsRs = new JSONObject();
				
				jsRs.put("idAreaPersona", rs.getLong("id_area_persona"));
				jsRs.put("idArea", rs.getLong("id_area"));
				jsRs.put("descripcion", rs.getString("descripcion"));
				jsAreas.put(jsRs);
			}
		}
		if(autoCloseConn){
			conn.closeConnection();
		}
		return jsAreas;
	}

}
