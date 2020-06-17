package net.dothr.transactional;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import net.dothr.MainAppTester;

public class TrackingMonitorTester extends MainAppTester {
	
	private static final String IDCONF = IDCONF_DOTHR;
	public final static String URI_TRACK_MONITOR = "/module/trackMonitor";///"/module/trackMonitorPersona";
	public final static String URI_CALENDAR = "/module/calendar/getdates";
	
	
	public static void main(String[] args) {
		try {
//			readTrackMonitor();
//			getTrackMonitor();
//			updateTrackMonitor();
//			getTrackMonitor();
			
			calendarGD();
//			tempCalendar();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void readTrackMonitor() throws Exception{
		
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDCONF);
		json.put(P_JSON_PERSONA, "2");//<== idPersona del Monitor
		
		//Paola: 128, Brenda: 64
		json.put("idCandidato", "59");	//paola p5=>59,p9=>142
		//TODO debe hacerse por tipo de Monitor (idRol) y/o idPosicion
		
//		json.put(P_JSON_POSICION, "5");
		
//		json.put("idCandidato", "59");
//		json.put("idPosibleCandidato", "6");	//6-Mauricio Meza
		
		String jSon = getJsonFromService(json.toString(), URI_TRACK_MONITOR+URI_READ );
	}
	
	
	public static void updateTrackMonitor() throws Exception{
		
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDCONF);
		json.put("idTrackingMonitorPersona", "2");
		json.put("comentario", "Actualizando el estado");
		json.put("idEstadoTracking", "2");
		
		String jSon = getJsonFromService(json.toString(), URI_TRACK_MONITOR+URI_UPDATE );
	}
	
	public static void getTrackMonitor() throws Exception{
		
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDCONF);
//		json.put("idMonitor", "2");
		//Paola: 128, Brenda: 64
		json.put(P_JSON_PERSONA, "128");
//		json.put(P_JSON_POSICION, "5");	
		
		String jSon = getJsonFromService(json.toString(), URI_TRACK_MONITOR+URI_GET );
	}
	
	public static void calendarGD() throws Exception{
		
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDCONF);
		json.put(P_JSON_PERSONA, "2");//Persona en sesion
		
		json.put("idTrackingMonitor", "2");
		json.put("idModeloRscPosFase", "41");
		
		String jSon = getJsonFromService(json.toString(), URI_CALENDAR );
	}
	
	
	private static void tempCalendar() throws Exception {
		ArrayList<String> lsIdMRSCPFase = new ArrayList<String>();
		lsIdMRSCPFase.add("40");
		lsIdMRSCPFase.add("41");
		lsIdMRSCPFase.add("42");
		lsIdMRSCPFase.add("43");
		lsIdMRSCPFase.add("44");
		lsIdMRSCPFase.add("45");
		lsIdMRSCPFase.add("46");
		lsIdMRSCPFase.add("47");
		lsIdMRSCPFase.add("48");
		
		Iterator<String> itMPfase = lsIdMRSCPFase.iterator();
		String idModeloRscPosFase, stResp;
		JSONArray jsResp, jsDias, jsOut = new JSONArray();
		JSONObject jsonReq, jsonFase, jsonDia;
		
		while(itMPfase.hasNext()){
			idModeloRscPosFase = itMPfase.next();
			//System.out.println(idModeloRscPosFase);
			jsonReq = new JSONObject();
			jsonReq.put(P_JSON_IDCONF, IDCONF);
			jsonReq.put(P_JSON_PERSONA, "2");//Persona en sesion
			
			jsonReq.put("idTrackingMonitor", "3");
			jsonReq.put("idModeloRscPosFase", idModeloRscPosFase);
			stResp = getJsonFromService(jsonReq.toString(), URI_CALENDAR );
			jsResp = new JSONArray(stResp);
			jsonFase = new JSONObject(jsResp.getJSONObject(0).toString());
			
			jsDias = jsonFase.getJSONArray("dias");
			for(int x=0; x<jsDias.length();x++){
				jsonDia = jsDias.getJSONObject(x);
				jsonDia.remove("horasLibres");
				jsonDia.put("horasLibres", new JSONArray());
			}
//			System.out.println(json);
			
			jsOut.put(jsonFase);
		}
		
		System.out.println("JsonSalida: \n"+jsOut);
	}
	

}
