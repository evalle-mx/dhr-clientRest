package net.dothr.transactional;

import net.dothr.MainAppTester;
import org.json.JSONObject;

public class ReportTester extends MainAppTester {
	
	private static final String IDCONF = IDCONF_DOTHR;
	protected static final String URI_REPORT="/module/report";
	
	
	public static void main(String[] args) {
		try {
			personaCvExport();
//			formatoCompExport();
//			personaCvDelete();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Verifica funcionamiento de servicio para generar reporte REPORT.C
	 * @throws Exception
	 */
	public static String personaCvExport() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );
		jsCont.put(P_JSON_PERSONA, "10");
		jsCont.put("idSolicitante", "2"); //idPersona de la persona en Sesión (Contratante)
		jsCont.put("idTipoDocumento", "1"); // "CV Candidato"
//		jsCont.put("idTipoContenido", "3");	//"expediente";"Expediente de la persona"
		jsCont.put("repParams", "imgp,nom,gen,cont,edoc,cdom,dhora,dviaj");
		jsCont.put("tipoArchivo", "doc");	//pdf | 
		
		/* [{"idPersonaIdioma":"13","name":"idPersonaIdioma","value":"21","code":"004","type":"I"}] */
		String jSon = getJsonFromService(jsCont.toString(), URI_REPORT+URI_CREATE );	
		return jSon; 
	}
	
	/**
	 * Verifica funcionamiento de servicio para generar reporte REPORT.C
	 * @throws Exception
	 */
	public static String formatoCompExport() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );
		jsCont.put(P_JSON_PERSONA, "2");
		jsCont.put("idSolicitante", "2"); //idPersona de la persona en Sesión (Contratante)
		jsCont.put("idTipoDocumento", "2"); // FOrmato Compensacion
		jsCont.put("fileTitle", "MyFormato-Test");
		
		String jSon = getJsonFromService(jsCont.toString(), URI_REPORT+URI_CREATE );	
		return jSon; 
	}
	
	
	/**
	 * Verifica funcionamiento de servicio para generar reporte REPORT.C
	 * @throws Exception
	 */
	public static String personaCvDelete() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );
		jsCont.put("idSolicitante", "2");
		jsCont.put("url", "2/"); //URL EN AWS a eliminar
		jsCont.put("idTipoDocumento", "1"); // "CV Candidato"
		jsCont.put("isFile", "0");
		
		/* [{"idPersonaIdioma":"13","name":"idPersonaIdioma","value":"21","code":"004","type":"I"}] */
		String jSon = getJsonFromService(jsCont.toString(), URI_REPORT+URI_DELETE );	
		return jSon; 
	}
	

}
