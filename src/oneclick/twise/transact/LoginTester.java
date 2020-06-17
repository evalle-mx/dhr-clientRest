package oneclick.twise.transact;

import net.dothr.transactional.CurriculumTester;
import netto.AppTester;

import org.apache.log4j.Logger;
import org.json.JSONObject;

public class LoginTester extends AppTester {
	
	private static final String IDCONF = "-1";
	static Logger log4j = Logger.getLogger( CurriculumTester.class );
	
	protected final static String URI_EXISTS= "/admin/login/exists";
			//"/module/curriculumManagement/exists";
	protected static final String URI_INITIAL = "/admin/login/initial";
			//"/module/curriculumManagement/initial";
	protected static final String URI_URICODES="/admin/login/uricodes";
			//"/module/curriculumManagement/uricodes";
	protected final static String URI_MENU="/admin/login/menu";
			//"/admin/management/menu";
	protected final static String URI_LOGOUT = "/admin/login/logout";
	
	
	
	public static void main(String[] args) {
		try {
			existsPersona();
//			initialPersona();
//			uricodes();
//			getMenu();
//			logout();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Prueba para verificar Persona [PERSON.E]
	 * @throws Exception 
	 */
	public static String existsPersona() throws Exception{	
		JSONObject json = new JSONObject();
		json.put("idEmpresaConf", IDCONF);
		json.put("email", "ernesto.valle@dothr.net");
		
		String jSon = getJsonFromService(json.toString(), URI_EXISTS );
		return jSon;
	}
	
	/**
	 * Verifica funcionamiento del uriCode PERSON.I
	 * @throws Exception
	 */
	public static String initialPersona() throws Exception{
		JSONObject json = new JSONObject();
		json.put("idEmpresaConf", IDCONF);
		json.put("email", "ernesto.valle@dothr.net");
		
		String jSon = getJsonFromService(json.toString(), URI_INITIAL );//[{"idPersona":"3","role":"9"}]
		return jSon;
	}
	
	/**
	 * Servicio PERSON_UC
	 * @return
	 * @throws Exception
	 */
	public static String uricodes() throws Exception {
		JSONObject json = new JSONObject();

		json.put("idEmpresaConf", IDCONF);
		json.put("email", "ernesto.valle@dothr.net");
		
		String jSon = getJsonFromService(json.toString(), URI_URICODES );
		return jSon;
	}
	
	/**
	 * Obtener menú por usuario (idPersona)
	 * @return
	 * @throws Exception
	 * 
	 * @uriService admin/management/menu
	 */
	public static String getMenu() throws Exception {
		// ADMIN.M...
		JSONObject json = new JSONObject();

		json.put("idEmpresaConf", IDCONF);
		json.put("idPersona", "2");
				
		String jSon = getJsonFromService(json.toString(), URI_MENU );
		return jSon;
	}
	
	public static String logout() throws Exception {
		// ADMIN.M...
		JSONObject json = new JSONObject();

		json.put("idEmpresaConf", IDCONF);
		json.put("idPersona", "2");
				
		String jSon = getJsonFromService(json.toString(), URI_LOGOUT );
		return jSon;
	}

}
