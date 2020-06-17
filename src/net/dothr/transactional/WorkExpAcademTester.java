package net.dothr.transactional;

import org.json.JSONObject;

import net.dothr.MainAppTester;

public class WorkExpAcademTester extends MainAppTester {
	
	private static final String IDCONF = IDSELEX;
	
	/** ***********************************************************************************
	 *  ******************************    M A I N    **************************************
	 *  ***********************************************************************************
	 * @param args
	 */	
	public static void main(String[] args) {
		try {
			
//			pruebaEscolaridadCreate();
//			pruebaEscolaridadUpdate();
//			pruebaEscolaridadGet(); //COMENTADO EN adapter
//			pruebaEscolaridadDelete();
			
//			pruebaWorkExpCreate();
			pruebaWorkExpUpdate();
//			pruebaWorkExpGet(); //COMENTADO EN adapter
//			pruebaWorkExpDelete();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Verifica funcionamiento Agregar academia ACADBACK.C
	 * @throws Exception
	 */
	public static String pruebaEscolaridadCreate()throws Exception{

		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDCONF);
		json.put(P_JSON_PERSONA, "3");
		
		String jSon = getJsonFromService(json.toString(), URI_ESCOLARIDAD+URI_CREATE);
		/* [{"name":"idEscolaridad","value":"7","code":"004","type":"I"}] */
		return jSon;
	}
	
	/**
	 * Verifica funcionamiento Update de Escolaridad ACADBACK.U
	 * @throws Exception
	 */
	public static String pruebaEscolaridadUpdate()throws Exception{
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDCONF);
		json.put(P_JSON_PERSONA, "1");	
		json.put("idEscolaridad", "201");
		
		json.put("anioFin", "2010");
		/* []  */
		String jSon = getJsonFromService(json.toString(), URI_ESCOLARIDAD+URI_UPDATE);
		return jSon;
	}
	
	/**
	 * Verifica funcionamiento listado Experiencia ACADBACK.G
	 * @throws Exception
	 */
	public static String pruebaEscolaridadGet() throws Exception {
		JSONObject jsono = new JSONObject();
		jsono.put(P_JSON_IDCONF, IDCONF );
		jsono.put(P_JSON_PERSONA,"3");
		
		jsono.put("parcial", false);
		/* [{"idEscolaridad":"27","idGradoAcademico":"4","idEstatusEscolar":"1",... }] */
		String jSon = getJsonFromService(jsono.toString(), URI_ESCOLARIDAD.concat(URI_GET) );
		return jSon;
	}
	
	/**
	 * Verifica BOrrado de escolaridad  ACADBACK.
	 * @throws Exception
	 */
	public static String pruebaEscolaridadDelete()throws Exception{
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDCONF);
		json.put(P_JSON_PERSONA, "1");	
		json.put("idEscolaridad", "7");
		
		/* [{"name":"idEscolaridad","code":"007","type":"I"}] */
		String jSon = getJsonFromService(json.toString(), URI_ESCOLARIDAD+URI_DELETE);
		return jSon;
	}
	
	
	/* *********************  WORK-EXPERIENCE ************************************************** */
	
	/**
	 * Verifica Creacion de Experiencia Laboral WORKEXP.C
	 * @throws Exception
	 */
	public static String pruebaWorkExpCreate() throws Exception {
		
		final String nombreEmpresa = "GOOGLE";
		
		JSONObject jsono = new JSONObject();
		jsono.put(P_JSON_IDCONF, IDCONF );
		jsono.put(P_JSON_PERSONA,"3");
		
		jsono.put("nombreEmpresa", nombreEmpresa);
//		jsono.put("idTipoJornada","1");
		jsono.put("texto","Metodología utilizada es mediante Aprendizaje Basado en Problemas (ABP) y Simulación Médica Mi función es ser facilitador en la resolución de casos clínicos durante las sesiones teóricas y ser instructor en simulación durante las sesiones prácticas.");
//		jsono.put("motivoSeparacion","Actual");
//		jsono.put("textoFiltrado"," metodologia utilizada es aprendizaje basado problemas abp y simulacion medica funcion es ser facilitador resolucion casos clinicos sesiones teoricas y ser instructor simulacion sesiones practicas profesor asignatura ");
//		jsono.put("idTipoContrato","1");
		jsono.put("trabajoActual","1");
//		jsono.put("idNivelJerarquico","5");
//		jsono.put("mesFin","8");
//		jsono.put("genteACargo","0");
		jsono.put("anioInicio","2010");
//		jsono.put("idPais","1");
		jsono.put("mesInicio","8");
//		jsono.put("diaInicio","19");
		jsono.put("puesto","Profesor de Asignatura A");
//		jsono.put("idTipoPrestacion","2");
//		jsono.put("diaFin","30");
//		jsono.put("anioFin","2014");
		
		/* [{"name":"idExperienciaLaboral","value":"8","code":"004","type":"I"}] */
		String jSon = getJsonFromService(jsono.toString(), URI_EXP_LABORAL.concat(URI_CREATE) );
		return jSon;
	}
	
	/**
	 * Verifica actualizacion de experiencia laboral WORKEXP.U
	 * @throws Exception
	 */
	public static String pruebaWorkExpUpdate() throws Exception {
		
		JSONObject jsono = new JSONObject();
		jsono.put("idExperienciaLaboral", "1876");
		jsono.put(P_JSON_IDCONF, IDCONF_DOTHR );
		jsono.put(P_JSON_PERSONA,"627");
		
//		jsono.put("nombreEmpresa","Universidad del Valle de México.");
//		jsono.put("idTipoJornada","1");
//		jsono.put("texto","CG MEXICO REMOTE CONTROLS 2015 2016\nOrder Management (Customer Service) role: \n- Receive and manual process, weekly purchase orders submitted by Customer\n- Keep maintain warehouse inventory control to report by week\n- Daily track scrap results\n- Complete weekly forecast and request for BPO release to Customer\n- Schedule trailer to pickup full cargo and direct delivery to Customer; giving proper \nfollow up until customer receive the shipment \n- Creation of weekly report: reconfiguration, scrap, WH, inbound outbound\n- Report every inbound outbound event \n- Coordinator of RGA process: receive, process, follow up resolve all RGA \nrequests\nDBG MEXICO 2014 2015 \nOrder Management (Customer Service) role:\n- Receive and process all manual Purchase Orders from Customers whos EDI transmission doesnt download as expected\n- Daily run all EDI transmissions properly downloaded and share it within Companies Shared File\n- Update monthly, weekly and daily Customers Requirement Release reports, as soon as either customer request it or a shipment was done, and post them at the Companies Shared File for all associates to review them, in order to accomplish the delivery date goals, as a team \n- Daily update monthly, weekly and daily Customers Releases reports at ISeries 300, also known as Future 3 platform\n- Daily inventory review, in order to provide availability for all purchase orders according with delivery date; taking all appropriate measures to constantly improve the companies delivery time score \n- Material purchase request submissions, when on hands werent enough to cover Customers releases \n- All logistic and necessary follow up applied to every purchase order, throughout all along the way since: requested, gathered, shipped and received by final customer \n- Keep all customers informed through all available ways, in order to have them updated regarding all their purchase orders, in time and form\nExternal Providers Coordinator role: \n- Keep track of all Part Numbers within the route for External Service This means: those part numbers have to go out of the plant for a final process like: painting, pickling, deoxidizede-rust, anodizing, among more \n- Facilitated several ways to turn it into a more efficient flow; implemented new views to follow up processes, to make it a smoother final process \n- Creating all paperwork by filling up the required forms, so the material was allowed to leave the plant for the final process at an external provider location, to finally be ret");
//		jsono.put("motivoSeparacion","Actual");
//		jsono.put("textoFiltrado"," metodologia utilizada es aprendizaje basado problemas abp y simulacion medica funcion es ser facilitador resolucion casos clinicos sesiones teoricas y ser instructor simulacion sesiones practicas profesor asignatura ");
//		jsono.put("idTipoContrato","1");		
//		jsono.put("idNivelJerarquico","5");		
//		jsono.put("genteACargo","0");
//		jsono.put("idPais","1");		
//		jsono.put("puesto","Profesor de Asignatura A");
//		jsono.put("idTipoPrestacion","2");
		
//		jsono.put("trabajoActual","0");
		jsono.put("anioInicio","2016");
//		jsono.put("mesInicio","6");
//		jsono.put("diaInicio","13");
		
		jsono.put("anioFin","2017");
//		jsono.put("mesFin","2");
//		jsono.put("diaFin","15");
		
		/* [] */
		String jSon = getJsonFromService(jsono.toString(), URI_EXP_LABORAL.concat(URI_UPDATE) );
		return jSon;
	}
	
	/**
	 * Verifica funcionamiento listado Experiencia WORKEXP.G
	 * @throws Exception
	 */
	public static String pruebaWorkExpGet() throws Exception {
		JSONObject jsono = new JSONObject();
		jsono.put(P_JSON_IDCONF, IDCONF );
		jsono.put(P_JSON_PERSONA,"3");
		
		jsono.put("parcial", false);
		/*  [{"idExperienciaLaboral":"8","anioInicio":"2009","mesInicio":"6","anioFin":"2014","mesFin":"4","trabajoActual":"0","puesto":"Profesor de Asignatura A","texto":"Metodología utilizada es mediante Aprendizaje Basado en Problemas (ABP) y Simulación Médica Mi función es ser facilitador en la resolución de casos clínicos durante las sesiones teóricas y ser instructor en simulación durante las sesiones prácticas."}] */
		String jSon = getJsonFromService(jsono.toString(), URI_EXP_LABORAL.concat(URI_GET) );
		return jSon;
	}
	
	/**
	 * Verifica funcion borrado de experiencia laboral  WORKEXP.D
	 * @throws Exception
	 */
	public static String pruebaWorkExpDelete() throws Exception {
		JSONObject jsono = new JSONObject();
		jsono.put("idExperienciaLaboral", "28");
		jsono.put(P_JSON_IDCONF, IDCONF );
//		jsono.put(P_JSON_PERSONA,"3");
		
		/* [{"name":"idExperienciaLaboral","value":"8","code":"007","type":"I"}] */
		String jSon = getJsonFromService(jsono.toString(), URI_EXP_LABORAL.concat(URI_DELETE) );
		return jSon;
	}
	
}
