package net.dothr.transactional;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import net.dothr.MainAppTester;

public class CompensacionTester extends MainAppTester {
	private static final String IDCONF = IDCONF_DOTHR;
	public final static String URI_COMPENSACION = "/module/compensation";
	public final static String URI_AUTOMOVIL="/module/automobile";
	public final static String URI_SUELDO="/module/salary";
	public final static String URI_BONO="/module/bond";
	public final static String URI_VALE="/module/voucher";
	public final static String URI_VACACIONES="/module/vacation";
	public final static String URI_SEGURO="/module/insurance";
	public final static String URI_REFERENCIA="/module/reference";
	
	private static Logger log4j = Logger.getLogger( CompensacionTester.class );

	public static void main(String[] args) {
		
		try {
//			compensacionCreate();
//			compensacionRead();
			compensacionUpdate();
//			compensacionDelete();
			
//			compensacionFullCreate();
//			compensacionGet();	//Solo se usa internamente, a menos que se solicite un listado de formatos existentes
						
//			autoCreate();
//			autoUpdate();
//			autoDelete();
			
//			sueldoCreate();
//			sueldoUpdate();
//			sueldoDelete();
			
//			vacacionesCreate();
//			vacacionesUpdate();
//			vacacionesDelete();
			
//			bonoCreate();
//			bonoUpdate();
//			bonoDelete();
			
//			valeCreate();
//			valeUpdate();
//			valeDelete();
			
//			seguroCreate();
//			seguroUpdate();
//			seguroDelete();
			
//			referenciaCreate();
//			referenciaUpdate();
//			referenciaDelete();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}


	
	/**
	 * Verifica funcionamiento de servicio DOCUMENT.FCR (Read or Create)
	 * @throws Exception
	 */
	public static String compensacionRead() throws Exception { //SOlo pruebas
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );	
		jsCont.put(P_JSON_PERSONA, "2");
		
		String jSon = getJsonFromService(jsCont.toString(), URI_COMPENSACION+URI_READ );	
		return jSon; 
	}
	
	/**
	 * Verifica funcionamiento de servicio  DOCUMENT.FCU
	 * @throws Exception
	 */
	public static String compensacionUpdate() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );
		jsCont.put(P_JSON_PERSONA, "2");
		
		jsCont.put("diasAguinaldo", "17");
		jsCont.put("cantidadFondoAhorro", "2500");	//$
		jsCont.put("comedor", "44");	//$
		jsCont.put("celular", "0"); // 0 | 1
		jsCont.put("clubGym", "1"); // 0 | 1
		jsCont.put("checkUp", "1"); // 0 | 1
		jsCont.put("ultimoMontoUtilidades", "5500");
		jsCont.put("curp", "VAVE800801HDF2G9");
		jsCont.put("otro", "varias cosas");
		jsCont.put("fechaCaptura", "01022018");		
		jsCont.put("numeroHijos", "3");
		
		
		String jSon = getJsonFromService(jsCont.toString(), URI_COMPENSACION+URI_UPDATE );	
		return jSon; 
	}
	
	
	
	/* *************************************** */
	
	/**
	 * Verifica funcionamiento de servicio  DOCUMENT.FCG
	 * @throws Exception
	 */
	public static String compensacionGet() throws Exception { //SOlo pruebas
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );	
		jsCont.put(P_JSON_PERSONA, "2");
		
		String jSon = getJsonFromService(jsCont.toString(), URI_COMPENSACION+URI_GET );	
		return jSon; 
	}
	
	/**
	 * Verifica funcionamiento de servicio  DOCUMENT.FCC
	 * @throws Exception
	 */
	public static String compensacionCreate() throws Exception { //SOlo pruebas
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );	//Enviar solo este para Listado Solo Permiso
		jsCont.put(P_JSON_PERSONA, "2");
		
		jsCont.put("diasAguinaldo", "20");
		jsCont.put("cantidadFondoAhorro", "2900");	//$
		jsCont.put("comedor", "60");	//$
		jsCont.put("celular", "0"); // 0 | 1
		jsCont.put("clubGym", "1"); // 0 | 1
		jsCont.put("checkUp", "1"); // 0 | 1
		jsCont.put("ultimoMontoUtilidades", "6500");
		jsCont.put("curp", "ABCD800801HDF2G9");
		jsCont.put("otro", "varias cosas");
		
		String jSon = getJsonFromService(jsCont.toString(), URI_COMPENSACION+URI_CREATE );	
		return jSon; 
	}
	
	/**
	 * Verifica funcionamiento de servicio  DOCUMENT.FCD
	 * @throws Exception
	 */
	public static String compensacionDelete() throws Exception { //SOlo pruebas
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );	//Enviar solo este para Listado Solo Permiso
		jsCont.put(P_JSON_PERSONA, "2");
		
		String jSon = getJsonFromService(jsCont.toString(), URI_COMPENSACION+URI_DELETE );	
		return jSon; 
	}

	
	/* ********************  AUTOMOVIL  *********************** */

	
	/**
	 * Verifica funcionamiento de servicio 
	 * @throws Exception
	 */
	public static String autoCreate() throws Exception { //SOlo pruebas
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );	
		jsCont.put(P_JSON_PERSONA, "2");
		
		jsCont.put("perteneceCompania", "1"); // 0 | 1
		jsCont.put("marca", "NISSAN");
		jsCont.put("modelo", "tsuru");
		jsCont.put("gastosPagados", "0"); // 0 | 1
		jsCont.put("opcionCompra", "1"); // 0 | 1
		jsCont.put("tiempoCambio", "4");
		
		String jSon = getJsonFromService(jsCont.toString(), URI_AUTOMOVIL+URI_CREATE );	
		return jSon; 
	}
	
	/**
	 * Verifica funcionamiento de servicio  
	 * @throws Exception
	 */
	public static String autoUpdate() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );
		jsCont.put(P_JSON_PERSONA, "2");
		
		jsCont.put("perteneceCompania", "1"); // 0 | 1
		jsCont.put("marca", "NISSAN");
		jsCont.put("modelo", "tsuru");
		jsCont.put("gastosPagados", "0"); // 0 | 1
		jsCont.put("opcionCompra", "1"); // 0 | 1
		jsCont.put("tiempoCambio", "4");
		
		String jSon = getJsonFromService(jsCont.toString(), URI_AUTOMOVIL+URI_UPDATE );	
		return jSon; 
	}
	
	/**
	 * Verifica funcionamiento de servicio 
	 * @throws Exception
	 */
	public static String autoDelete() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );
		jsCont.put(P_JSON_PERSONA, "2");
		
		String jSon = getJsonFromService(jsCont.toString(), URI_AUTOMOVIL+URI_DELETE );	
		return jSon; 
	}
	
/* ********************  SUELDO  *********************** */

	
	/**
	 * Verifica funcionamiento de servicio 
	 * @throws Exception
	 */
	public static String sueldoCreate() throws Exception { //SOlo pruebas
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );	
		jsCont.put(P_JSON_PERSONA, "2");
		
		jsCont.put("idPeriodicidadSueldo", "1"); 
		jsCont.put("cantidad", "5200");
		
		String jSon = getJsonFromService(jsCont.toString(), URI_SUELDO+URI_CREATE );	
		return jSon; 
	}
	
	/**
	 * Verifica funcionamiento de servicio  
	 * @throws Exception
	 */
	public static String sueldoUpdate() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );
		jsCont.put(P_JSON_PERSONA, "2");
		
		jsCont.put("idPeriodicidadSueldo", "2");
		jsCont.put("cantidad", "8900");
		
		String jSon = getJsonFromService(jsCont.toString(), URI_SUELDO+URI_UPDATE );	
		return jSon; 
	}
	
	/**
	 * Verifica funcionamiento de servicio 
	 * @throws Exception
	 */
	public static String sueldoDelete() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );
		jsCont.put(P_JSON_PERSONA, "2");
		
		String jSon = getJsonFromService(jsCont.toString(), URI_SUELDO+URI_DELETE );	
		return jSon; 
	}
	
	
/* ********************  Vacaciones  *********************** */

	
	/**
	 * Verifica funcionamiento de servicio 
	 * @throws Exception
	 */
	public static String vacacionesCreate() throws Exception { //SOlo pruebas
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );	
		jsCont.put(P_JSON_PERSONA, "2");
		
		jsCont.put("valorDias", "16"); 
		jsCont.put("valorPrima", "11"); //%
		
		String jSon = getJsonFromService(jsCont.toString(), URI_VACACIONES+URI_CREATE );	
		return jSon; 
	}
	
	/**
	 * Verifica funcionamiento de servicio  
	 * @throws Exception
	 */
	public static String vacacionesUpdate() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );
		jsCont.put(P_JSON_PERSONA, "2");
		
		jsCont.put("valorDias", "25");
		jsCont.put("valorPrima", "1900");
		
		String jSon = getJsonFromService(jsCont.toString(), URI_VACACIONES+URI_UPDATE );	
		return jSon; 
	}
	
	/**
	 * Verifica funcionamiento de servicio 
	 * @throws Exception
	 */
	public static String vacacionesDelete() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );
		jsCont.put(P_JSON_PERSONA, "2");
		
		String jSon = getJsonFromService(jsCont.toString(), URI_VACACIONES+URI_DELETE );	
		return jSon; 
	}
	
	
/* ********************  Bono  *********************** */

	
	/**
	 * Verifica funcionamiento de servicio 
	 * @throws Exception
	 */
	public static String bonoCreate() throws Exception { //SOlo pruebas
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );	
		jsCont.put(P_JSON_PERSONA, "2");
		
		jsCont.put("idPeriodicidadBono", "1"); 
		jsCont.put("valorCantidad", "2850");
		jsCont.put("porcentajeCantidad", "20");
		jsCont.put("tipoBono", "Puntualidad");
		
		String jSon = getJsonFromService(jsCont.toString(), URI_BONO+URI_CREATE );	
		return jSon; 
	}
	
	/**
	 * Verifica funcionamiento de servicio  
	 * @throws Exception
	 */
	public static String bonoUpdate() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );
		jsCont.put(P_JSON_PERSONA, "2");
		
		jsCont.put("idBono", "2");
		jsCont.put("idPeriodicidadBono", "2"); 
		jsCont.put("valorCantidad", "1850");
		jsCont.put("porcentajeCantidad", "12");
		jsCont.put("tipoBono", "productividad");
		
		String jSon = getJsonFromService(jsCont.toString(), URI_BONO+URI_UPDATE );	
		return jSon; 
	}
	
	/**
	 * Verifica funcionamiento de servicio 
	 * @throws Exception
	 */
	public static String bonoDelete() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );
		jsCont.put(P_JSON_PERSONA, "2");

		jsCont.put("idBono", "2");
		
		String jSon = getJsonFromService(jsCont.toString(), URI_BONO+URI_DELETE );	
		return jSon; 
	}
	
	/* ********************  Vale  *********************** */	
	/**
	 * Verifica funcionamiento de servicio 
	 * @throws Exception
	 */
	public static String valeCreate() throws Exception { //SOlo pruebas
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );	
		jsCont.put(P_JSON_PERSONA, "2");
		
		jsCont.put("idTipoVale", "1"); 
		jsCont.put("cantidad", "1150");
		
		String jSon = getJsonFromService(jsCont.toString(), URI_VALE+URI_CREATE );	
		return jSon; 
	}
	
	/**
	 * Verifica funcionamiento de servicio  
	 * @throws Exception
	 */
	public static String valeUpdate() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );
		jsCont.put(P_JSON_PERSONA, "2");
		
		jsCont.put("idVale", "2");
		jsCont.put("idTipoVale", "2"); 
		jsCont.put("cantidad", "2180");
		
		String jSon = getJsonFromService(jsCont.toString(), URI_VALE+URI_UPDATE );	
		return jSon; 
	}
	
	/**
	 * Verifica funcionamiento de servicio 
	 * @throws Exception
	 */
	public static String valeDelete() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );
		jsCont.put(P_JSON_PERSONA, "2");

		jsCont.put("idVale", "2");
		
		String jSon = getJsonFromService(jsCont.toString(), URI_VALE+URI_DELETE );	
		return jSon; 
	}
	
	/* ********************  Seguro  *********************** */	
	/**
	 * Verifica funcionamiento de servicio 
	 * @throws Exception
	 */
	public static String seguroCreate() throws Exception { //SOlo pruebas
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );	
		jsCont.put(P_JSON_PERSONA, "2");
		
		jsCont.put("familiar", "0"); // 0 | 1 
		jsCont.put("idTipoSeguro", "1");
		
		String jSon = getJsonFromService(jsCont.toString(), URI_SEGURO+URI_CREATE );	
		return jSon; 
	}
	
	/**
	 * Verifica funcionamiento de servicio  
	 * @throws Exception
	 */
	public static String seguroUpdate() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );
		jsCont.put(P_JSON_PERSONA, "2");
		
		jsCont.put("idSeguro", "1");
		jsCont.put("familiar", "1"); // 0 | 1 
		jsCont.put("idTipoSeguro", "2");
		
		String jSon = getJsonFromService(jsCont.toString(), URI_SEGURO+URI_UPDATE );	
		return jSon; 
	}
	
	/**
	 * Verifica funcionamiento de servicio 
	 * @throws Exception
	 */
	public static String seguroDelete() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );
		jsCont.put(P_JSON_PERSONA, "2");

		jsCont.put("idSeguro", "1");
		
		String jSon = getJsonFromService(jsCont.toString(), URI_SEGURO+URI_DELETE );	
		return jSon; 
	}
	
	
	/* ********************  Referencia  *********************** */	
	/**
	 * Verifica funcionamiento de servicio 
	 * @throws Exception
	 */
	public static String referenciaCreate() throws Exception { //SOlo pruebas
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );	
		jsCont.put(P_JSON_PERSONA, "2");
		
		jsCont.put("nombre", "PruebaContactos");
		jsCont.put("empresa", "tester");
		jsCont.put("puesto", "prueba");
		jsCont.put("email", "preuba@mail.com");
		
		
		jsCont.put("contactos","[{idTipoContacto:11,numero:81252525}]");
//		jsCont.put("idTipoContacto", "11"); // 11/13
//		jsCont.put("numero", "81252525");//"81252525");
		
		String jSon = getJsonFromService(jsCont.toString(), URI_REFERENCIA+URI_CREATE );	
		return jSon; 
	}
	
	/**
	 * Verifica funcionamiento de servicio  
	 * @throws Exception
	 */
	public static String referenciaUpdate() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );	
		jsCont.put("idReferencia", "3");
		
		jsCont.put("nombre", "PruebaActualizada");
		jsCont.put("empresa", "tester");
		jsCont.put("puesto", "prueba2");
		jsCont.put("email", "pruebaUPD@mail.com");
		
//		jsCont.put("idTipoContacto", "13"); // 11/13
//		jsCont.put("numero", "123456");//"81252525");
		
		String jSon = getJsonFromService(jsCont.toString(), URI_REFERENCIA+URI_UPDATE );	
		return jSon; 
	}
	
	/**
	 * Verifica funcionamiento de servicio 
	 * @throws Exception
	 */
	public static String referenciaDelete() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );	
		jsCont.put("idReferencia", "3");
		
		String jSon = getJsonFromService(jsCont.toString(), URI_REFERENCIA+URI_DELETE );	
		return jSon; 
	}
	
	
	/* ************************************************************************** */
	/* ************************  FULL-CREATE (solo pruebas) ********************* */
	/* ************************************************************************** */
	public static String multipleVale(JSONArray jsVales, String idPersona) throws Exception{
		StringBuilder sbVales = new StringBuilder();
		JSONObject jsonVale;
//		String idVale;
		for(int x=0; x<jsVales.length();x++){
			jsonVale = jsVales.getJSONObject(x);
//			idVale = jsonVale.getString("idVale");
			
//			JSONObject jsCont = new JSONObject();
			jsonVale.put(P_JSON_IDCONF, IDCONF );
			jsonVale.put(P_JSON_PERSONA, idPersona);			
			jsonVale.put("activo", "1");
			jsonVale.put("cantidad", ""+((x+1)*320) );
			sbVales.append(getJsonFromService(jsonVale.toString(), URI_VALE+URI_UPDATE )	);
		}
		return sbVales.toString();
	}
	
	
	/**
	 * Verifica funcionamiento de servicio  DOCUMENT.FCC
	 * @throws Exception
	 */
	public static String compensacionFullCreate() throws Exception { //SOlo pruebas
		StringBuilder sb = new StringBuilder();
		String idPersona = "2";
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );	//Enviar solo este para Listado Solo Permiso
		jsCont.put(P_JSON_PERSONA, idPersona);
		
		jsCont.put("diasAguinaldo", "17");
		jsCont.put("cantidadFondoAhorro", "2500");	//$
		jsCont.put("comedor", "44");	//$
		jsCont.put("celular", "0"); // 0 | 1
		jsCont.put("clubGym", "1"); // 0 | 1
		jsCont.put("checkUp", "1"); // 0 | 1
		jsCont.put("ultimoMontoUtilidades", "5500");
		jsCont.put("curp", "VAVE800801HDF2G9");
		jsCont.put("otro", "varias cosas");
		jsCont.put("fechaCaptura", "01022018");
		
		String stJson = getJsonFromService(jsCont.toString(), URI_COMPENSACION+URI_CREATE );	
		sb.append("<Create> Res: ").append(stJson ).append("\n");
		
		jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );	//Enviar solo este para Listado Solo Permiso
		jsCont.put(P_JSON_PERSONA, idPersona);
		stJson = getJsonFromService(jsCont.toString(), URI_COMPENSACION+URI_READ);
		
		JSONArray jsResp = new JSONArray(stJson);
		log4j.debug("Obteniendo Json de Compensacion");
		JSONObject jsonCompensacion = jsResp.getJSONObject(0);
		log4j.debug("Obteniendo arreglo de Vales");
		JSONArray jsVales = jsonCompensacion.getJSONArray("vales");
		log4j.debug("Vales: " + jsVales );
		sb.append("<Vales> ").append(multipleVale(jsVales, idPersona) ).append("\n");
		
		sb.append("<Automovil> ").append(autoCreate() ).append("\n");
		sb.append("<Sueldo> ").append(sueldoCreate() ).append("\n");
		sb.append("<Vacaciones> ").append(vacacionesCreate() ).append("\n");
		sb.append("<Bono> ").append(bonoCreate() ).append("\n");
		/*sb.append("<Vale> ").append(valeCreate() ).append("\n"); ahora se crean directo en Compensacion*/		
		sb.append("<Seguro> ").append(seguroCreate() ).append("\n");
		
		log4j.debug("<FullCreate> \n"+ sb.toString() ); 
		
		
		return sb.toString(); 
	}
}
