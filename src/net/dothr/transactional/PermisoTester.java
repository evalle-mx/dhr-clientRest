package net.dothr.transactional;

import org.json.JSONObject;

import net.dothr.MainAppTester;

public class PermisoTester extends MainAppTester {
	
	private static final String IDCONF = IDCONF_DOTHR;
	public final static String URI_PERMISSION = "/admin/permission";
	public final static String URI_ROL = "/admin/rol";
	
	public static void main(String[] args) {
		try {
//			permisoGet();
//			permisoUpdate();
			
//			permisoCreate();
//			permisoDelete();
			
			rolGet();
//			rolCreate();
//			rolUpdate();
//			rolDelete();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Verifica funcionamiento de servicio para Obtener listado de permisos PERMISSION.G
	 * @throws Exception
	 */
	public static String permisoGet() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );	//Enviar solo este para Listado Solo Permiso
		
		//Enviar los siguientes para obtener rol-permiso
		jsCont.put("idTipoPermiso", "1"); // 1 (uriCode) | 2 (Menú)
		jsCont.put("idRol", "3"); //Se pide el Rol por cuestiones de seguridadc
		
		
		String jSon = getJsonFromService(jsCont.toString(), URI_PERMISSION+URI_GET );	
		return jSon; 
	}
	
	/**
	 * Verifica funcionamiento de servicio para Actualizar permiso/TIpoRelacionPermiso PERMISSION.U
	 * @throws Exception
	 */
	public static String permisoUpdate() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );
		jsCont.put("idPermiso", 139 );
		jsCont.put("idPermisoRelacionada", 0 );
		jsCont.put("contexto", "Prueba Actualizada" );
		jsCont.put("valor", "pruebas" );
		jsCont.put("descripcion", "URI nuevo" );
				
		/*		Versión anterior
		jsCont.put("idTipoRelacionPermiso", "302");
		jsCont.put("activo", true);	//"Inhabilitado/Cancelado"*/ 
		
		String jSon = getJsonFromService(jsCont.toString(), URI_PERMISSION+URI_UPDATE );	
		return jSon; 
	}

	
	/**
	 * Verifica funcionamiento de servicio para Crear Permiso PERMISSION.C
	 * @throws Exception
	 */
	public static String permisoCreate() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );
		jsCont.put("idTipoPermiso", "1");
		
		String jSon = getJsonFromService(jsCont.toString(), URI_PERMISSION+URI_CREATE );
		return jSon; 
	}
	
	/**
	 * Verifica funcionamiento de servicio para Eliminar permiso PERMISSION.D
	 * @throws Exception
	 */
	public static String permisoDelete() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );
		jsCont.put("idPermiso", "113");
		
		String jSon = getJsonFromService(jsCont.toString(), URI_PERMISSION+URI_DELETE );	
		return jSon; 
	}
	
	
	
	/* >>>>>>>>>>>>>>>>>>>  ROL  <<<<<<<<<<<<<<<<<<<<<<<<<  */
	/**
	 * Verifica funcionamiento de servicio para Obtener listado de Roles ROL.G
	 * @throws Exception
	 */
	public static String rolGet() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );
		jsCont.put("idRol", "1"); // *opcional
		jsCont.put("idPersona", "2"); //TODO ELIMINAR DEPENDENCIA
		
		
		String jSon = getJsonFromService(jsCont.toString(), URI_ROL+URI_GET );	
		return jSon; 
	}
	
	/**
	 * Verifica funcionamiento de servicio para Crear Rol ROL.C
	 * @throws Exception
	 */
	public static String rolCreate() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );
		jsCont.put("descripcion", "prueba");
		
		String jSon = getJsonFromService(jsCont.toString(), URI_ROL+URI_CREATE );
		return jSon; 
	}
	
	/**
//	 * Verifica funcionamiento de servicio para Actualizar Rol ROL.U
	 * @throws Exception
	 */
	public static String rolUpdate() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );
		jsCont.put("idRol", "5");
		
		jsCont.put("rolInicial", "1");
		jsCont.put("descripcion", "Cliente 2");
		jsCont.put("vistaInicial", "");
		
		String jSon = getJsonFromService(jsCont.toString(), URI_ROL+URI_UPDATE );	
		return jSon; 
	}
	
	/**
	 * Verifica funcionamiento de servicio para Eliminar Rol ROL.D
	 * @throws Exception
	 */
	public static String rolDelete() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );
		jsCont.put("idRol", "9");
		
		String jSon = getJsonFromService(jsCont.toString(), URI_ROL+URI_DELETE );	
		return jSon; 
	}
}
