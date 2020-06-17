package oneclick.ticket;

import org.json.JSONObject;

import netto.AppTester;

public class UsuarioTester extends AppTester {
	
	public final static String SERV_USER = "/admin/user";
	
	public final static String METHOD_PWD_CHANGE="/pwdchange";
	public final static String METHOD_ACTIVATE="/activate";
	
	
	public static void main(String[] args) {
		try {
			getUsers();
//			createUser();
//			readUser();
//			updateUser();
//			activationUser();
//			deleteUser();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Obtiene la lista de usuarios Servicio USUARIO.G
	 * @return
	 * @throws Exception
	 */
	public static String getUsers() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put("idUsuario", "99");
		jsCont.put("idSucursal", "1");
		jsCont.put("idRol", "1");
		//TODO agregar busqueda por nombre
//		jsCont.put("nombre", "99");
		String jSon = getJsonFromService(jsCont.toString(), SERV_USER+METHOD_GET );	
		return jSon; 
	}
	
	
	public static String createUser() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put("username", "dozerDummy");
		jsCont.put("nombre", "dozertest");
		jsCont.put("apellidos", "tester");
		jsCont.put("email", "pruebas@oneclick.net");
		
		jsCont.put("idRol", "2");
		jsCont.put("idSucursal", "2");
		jsCont.put("puesto", "Invitado");
		String jSon = getJsonFromService(jsCont.toString(), SERV_USER+METHOD_CREATE );	
		return jSon; 
	}
	
	public static String readUser() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put("idUsuario", "199");

		String jSon = getJsonFromService(jsCont.toString(), SERV_USER+METHOD_READ );	
		return jSon; 
	}
	
	public static String updateUser() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put("idUsuario", "99");
		jsCont.put("nombre", "NuevaPrueba");
//		jsCont.put("activo", "false");
		String jSon = getJsonFromService(jsCont.toString(), SERV_USER+METHOD_UPDATE );	
		return jSon; 
	}
	
	
	public static String activationUser() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put("idUsuario", "99");
		jsCont.put("activate", "a"); // 0|1
		String jSon = getJsonFromService(jsCont.toString(), SERV_USER+METHOD_ACTIVATE );	
		return jSon; 
	}

	public static String deleteUser() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put("idUsuario", "99");
		String jSon = getJsonFromService(jsCont.toString(), SERV_USER+METHOD_DELETE );	
		return jSon; 
	}
}
