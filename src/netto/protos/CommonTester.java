package netto.protos;

import org.json.JSONObject;

import netto.AppTester;

public class CommonTester extends AppTester {
	
	public final static String GET_PHONES = "/getPhones";
	public final static String GET_TODOS = "/getTodos";
	
	public static void main(String[] args) {
		try {
//			ping();
			getPhones();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Verifica funcionamiento de servicio Phones en Prototipos
	 * @throws Exception
	 */
	public static String getPhones() throws Exception { //SOlo pruebas
		JSONObject jsonReq = new JSONObject();
		jsonReq.put("idEmpresaConf", IDCONF );	
		
		String stResponse = getJsonFromService(jsonReq.toString(), URI_TEST+GET_PHONES );
		System.out.println(stResponse);
		return stResponse; 
	}
	
	
	

}
