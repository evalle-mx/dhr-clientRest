package net.dothr.transactional;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import net.dothr.MainAppTester;
/**
 * Clase de pruebas para modulos Genericos de <ul>
 * <li>Contacto</li>
 * <li>Localizacion</li>
 * </ul>
 * @author dothr
 *
 */
@SuppressWarnings("unused")
public class ContactLocationTester extends MainAppTester {
	
	private static Logger log4j = Logger.getLogger( ContactLocationTester.class );
	private static final String IDCONF = IDCONF_DOTHR;

	/** ***********************************************************************************
	 *  ******************************    M A I N    **************************************
	 *  ***********************************************************************************
	 * @param args
	 */
	public static void main(String[] args) {
		try {
//			pruebaLocationInsert();
//			pruebaLocationInsertPos(); 
//			pruebaLocationDelete();
			
//			pruebaContactoInsert();
//			pruebaContactoUpdate();
//			pruebaContactoDelete();

//			settlementCreate();
			settlementGet();
			
			
//			casoDomicilio();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/* *********************** HABILIDAD  ******************************************  */
	
	
	
	/* *********************** LOCALIZACION  ******************************************  */
	
	/**
	 * Verifica funcionamiento de Localización/Domicilio  SETTLEMENT.C
	 * @throws Exception
	 */
	public static String pruebaLocationInsert() throws Exception {
		//* Se probaron otros casos en el metodo pruebaSettlement().
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDCONF);
//		json.put(P_JSON_PERSONA, "2");
		
		
		/*json.put("numeroInterior","");
		json.put("googleLatitude","19.3676173");
		json.put("googleLongitude","-99.1715428");
		json.put("idCodigoProceso","5");
		json.put("tempEstado","9");
		json.put("idTipoDomicilio","1");
		json.put("tempAsentamiento","27277");
		json.put("tempCodigoPostal","03100");
		json.put("numeroExterior","154");
		json.put("idPais","1");
		json.put("tempMunicipio","278");		
		json.put("calle","Eje Vial 3 Poniente");//*/
		
		json.put("tempEstado","Distrito Federal");
		json.put("tempPais","México");
		json.put("googleLongitude","-99.15255479999996");
		json.put("calle","Río San Javier");
		json.put("numeroExterior","666");
		json.put("tempAsentamiento","Acueducto de Guadalupe");
		json.put("tempMunicipio","Gustavo A. Madero");
		json.put("googleLatitude","19.5301066");
		json.put("idPersona","172");
		json.put("idCodigoProceso","");
		
		String jSon = getJsonFromService(json.toString(), URI_SETTLEMENT+URI_CREATE );// [{"name":"idDomicilio","value":"26","code":"004","type":"I"}]
		return jSon;
	}
	
	public static String pruebaLocationInsertPos() throws Exception {
		JSONObject json = new JSONObject();
		json.put(P_JSON_POSICION, "6");
		json.put("idPerfil","6");
		json.put("idPersona","2");
		
		json.put("stEstado","Ciudad de México");
		json.put("stColonia","Juárez");
		json.put("idCodigoProceso","6");
		json.put("googleLatitude","19.426753");
		json.put("tempEstado","9");
		json.put("stMunicipio","Cuauhtémoc");
		json.put("idTipoDomicilio","1");
		json.put("tempAsentamiento","27391");
		json.put("tempCodigoPostal","06600");
		json.put("numeroExterior","222");
		json.put("tempMunicipio","279");
		json.put("stPais","México");
		json.put("googleLongitude","-99.1618449");
		json.put("tempPais","1");
		json.put("idEmpresaConf","1");
		json.put("calle","Reforma");
		
		String jSon = getJsonFromService(json.toString(), URI_SETTLEMENT+URI_CREATE );// [{"name":"idDomicilio","value":"26","code":"004","type":"I"}]
		return jSon;
	}

	/**
	 * Verifica funcionalidad para eliminar domicilio LOCATION.D
	 * @throws Exception
	 */
	public static String pruebaLocationDelete() throws Exception {		
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDCONF);
		json.put(P_JSON_PERSONA, "1");
		json.put("idDomicilio", "172");
	
		String jSon = getJsonFromService(json.toString(), URI_LOCATION+URI_DELETE );	// [{"name":"idDomicilio","value":"26","code":"007","type":"I"}]
		return jSon;
	}
	
	/* *********************** CONTACTO  ******************************************  */
	
	/**
	 * Verifica funcionamiento de servicio para agregar contacto a persona CONTACT.C
	 * @throws Exception
	 */
	public static String pruebaContactoInsert() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );
//		jsCont.put(P_JSON_PERSONA, "82");
//		jsCont.put(P_JSON_EMPRESA, "22");
		
		jsCont.put("idReferencia", "1");
		jsCont.put("idTipoContacto", "13");
		jsCont.put("numero", "81252525");//"81252525");
		
		/* [{"idTipoContacto":"13","name":"idContacto","value":"21","code":"004","type":"I"}] */
		String jSon = getJsonFromService(jsCont.toString(), URI_CONTACT+URI_CREATE );	
		return jSon;
	}
	
	/**
	 * Verifica funcionamiento de servicio Update de contacto en Persona  CONTACT.U
	 * @throws Exception
	 */
	public static String pruebaContactoUpdate() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );
		jsCont.put("idContacto", "1041"); 
		jsCont.put("numero", "81252525");//"81252525");
		//{"numero":"25252525","idContacto":"2","idConf":"1","idPersona":"15"}
		
		/* [] */
		String jSon = getJsonFromService(jsCont.toString(), URI_CONTACT+URI_UPDATE );
		return jSon;
	}
	
	/**
	 * Verifica funcionamiento de borrado de contacto Persona 
	 * @throws Exception
	 */
	public static String pruebaContactoDelete() throws Exception {
		
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );
		jsCont.put("idContacto", "1041");
		
		/* [{"name":"idContacto","value":"22","code":"007","type":"I"}] */
		String jSon = getJsonFromService(jsCont.toString(), URI_CONTACT+URI_DELETE );
		return jSon;
	}
	
	
	
	/* **************************************************************************** */
	
	
	public static String settlementGet() throws Exception {
		
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDCONF);
		json.put(P_JSON_PERSONA, "2");
		json.put("postalCode", "50160");
		
		String jSon = getJsonFromService(json.toString(), URI_SETTLEMENT+URI_GET );
		return jSon;
	}
	
	
	public static String settlementCreate() throws Exception {
		
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDCONF);
		json.put(P_JSON_PERSONA, "2");
//		json.put(P_JSON_POSICION, "2");	//En Posicion, solo permite un domicilio (duplicate key value violates unique constraint "dom_1_idx")
		
		/* 1.- Via Google Maps (códigoProceso vacio & Cadenas texto)  */
//		json.put("calle","Río San Javier");
//		json.put("numeroExterior","666");
////		json.put("numeroInterior","");
//		json.put("idDomicilio",""); 	//asi lo envia el servicio en la pagina
//		json.put("idCodigoProceso",""); //asi lo envia el servicio en la pagina
//		json.put("tempEstado","Distrito Federal");
//		json.put("tempMunicipio","Gustavo A. Madero");
//		json.put("tempAsentamiento","Acueducto de Guadalupe");
//		json.put("tempPais","México");
//		json.put("tempCodigoPostal","07264");
//		json.put("googleLatitude","19.298875");
//		json.put("googleLongitude","-99.15255479999996");
		
		/* 2 . Via Manual */
//		json.put("calle","Carretera Picacho Ajusco");
//		json.put("numeroExterior","154");
//		json.put("numeroInterior","");
//		json.put("idDomicilio","   ");	//asi lo envia el servicio en la pagina
//		json.put("idCodigoProceso","7");
//		json.put("tempEstado","9");
//		json.put("tempMunicipio","276");
//		json.put("tempAsentamiento","26949");
//		json.put("tempPais","1");
//		json.put("tempCodigoPostal","14210");
//		json.put("googleLatitude","19.298875");
//		json.put("googleLongitude","-99.20947209999997");	//-99.16792950000001
		
		String jSon = getJsonFromService(json.toString(), URI_SETTLEMENT+URI_CREATE );// [{"name":"idDomicilio","value":"26","code":"004","type":"I"}]
		return jSon;
	}
	
	
	protected static void casoDomicilio() throws Exception {
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDCONF);
		json.put(P_JSON_PERSONA, "2");
		
		/* Create con error 
		 json.put("googleLatitude","25.7095869");
		 json.put("googleLongitude","-100.23320890000002");
		 json.put("idCodigoProceso","");
		 json.put("calle","Cielo");
		 json.put("tempMunicipio","Guadalupe");
		 json.put("tempEstado","Nuevo León");
		 json.put("tempPais","México");
		 json.put("idEmpresaConf","1"); 
		 String jSon = getJsonFromService(json.toString(), URI_SETTLEMENT+URI_CREATE );// [{"name":"idDomicilio","value":"26","code":"004","type":"I"}]
		 //*/
		 
		 //* Delete con error 
		 json.put("idDomicilio","");
		 String jSon = getJsonFromService(json.toString(), URI_LOCATION+URI_DELETE ); //*/
		
		//return jSon;
	}
}
