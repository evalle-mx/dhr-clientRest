package net.dothr.transactional;

import org.json.JSONObject;

import net.dothr.MainAppTester;

@SuppressWarnings("unused")
public class ApplicantTester extends MainAppTester {
	
	private static final String IDCONF = IDCONF_DOTHR;

	protected final static String URI_APPLICANT="/module/applicant";
	protected final static String URI_APPLICANT_GET_APPLICANTS="/getApplicants";
	protected final static String URI_APPLICANT_SEARCH_APPLICANTS="/searchApplicants";
	protected final static String URI_APPLICANT_SET_PREPROCESSING="/setPreprocessing";
	protected final static String URI_APPLICANT_SET_RESUME_PUBLICATION="/setResumePublication";
	protected final static String URI_APPLICANT_READ_VACANCY="/readVacancy";
	
	protected final static String URI_MODULE_TASK = "/module/task";
	protected final static String URI_TASK_SEARCHAPPLICANTS = "/searchCandidates";
	
	/**
	 * ****************************************************************************
	 * *******************  M A I N   *********************************************
	 */
	public static void main(String[] args) {
		try {
			
//			searchApplicant();
			getApplicants();
			
//			searchCandidates();	//DEPRECADO porque obtenia el mismo resultado que searchApplicant
//			getVacancy(); //??
		} catch (Exception e) {   e.printStackTrace(); }
	}
	
	
	/**
	 * Verifica funcionamiento de APPLICANT.S
	 * @throws Exception
	 */
	public static void searchApplicant() throws Exception {
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDSELEX);
		json.put("idPosicion", "5");
		json.put("huboCambioPosicion", true);
		
		String jSon = getJsonFromService(json.toString(), URI_APPLICANT+URI_APPLICANT_SEARCH_APPLICANTS );
	}
	
	/**
	 * Verifica funcionamiento de APPLICANT.G
	 * @throws Exception
	 */
	public static void getApplicants() throws Exception{
		String idPosicion = "7";
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDSELEX);
		json.put("idPosicion", idPosicion); 
		
		String jSon = getJsonFromService(json.toString(), URI_APPLICANT+URI_APPLICANT_GET_APPLICANTS );
	}
	
	
	
	
	
	public static void getVacancy() throws Exception {
		String idPersonaCandidato = "5000"; 	// 1 | 5000
		String idPosicion = "13";
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDCONF);
		json.put("idPosicion", idPosicion);
		json.put("idPersona", idPersonaCandidato);
		
		String jSon = getJsonFromService(json.toString(), URI_APPLICANT+URI_APPLICANT_READ_VACANCY );
	}
	
	public static void searchCandidates() throws Exception {
		JSONObject json = new JSONObject();
		String idPosicion = "13";
		
		json.put("idPosicion", idPosicion);
		
		/*  usa URI TASK */
		String jSon = getJsonFromService(json.toString(), URI_MODULE_TASK+URI_TASK_SEARCHAPPLICANTS );
	}
	
}
