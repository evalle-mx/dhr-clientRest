package net.dothr.transactional;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import net.dothr.MainAppTester;

public class SocialTester  extends MainAppTester {
	
	static Logger log4j = Logger.getLogger( SocialTester.class );
	
	protected static final String URI_MOD_LINKEDIN ="module/linkedin/merge";
	
	
	public static void main(String[] args) {
		try {
			pruebaMergeLinkedIn();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	==> Rest.getJsonFromService()/module/linkedin/merge...
	 ==> Json: {"tokenSecret":"9c24f7c4-29b9-4053-a3ba-3e7bf93cfc76","tokenSocial":"c7b8d875-41c2-4d65-b4d1-2357e0bf56ca",
	 "idPersona":"2","idEmpresaConf":"1"}
	 ==> URI: /module/linkedin/merge	
	*/
	
	public static String pruebaMergeLinkedIn() throws Exception {
		JSONObject json = new JSONObject();

		json.put(P_JSON_IDCONF, IDCONF_DOTHR);
		json.put(P_JSON_PERSONA, "2");
		// Admin (EV) (Ernesto.valle@dothr.net)
		json.put("tokenSecret", "f60ce231-2d9c-4dcd-8c33-7034be9c1727");
		json.put("tokenSocial", "e0b9fccd-6e3f-4795-a6d1-a8a3257cdf99");
		//Ornare Lectus (EV) (test@dothr.net)
//		json.put("tokenSecret", "a9ccc733-75b0-43fb-9e7e-b35b452a1b03");json.put("tokenSocial", "50a237f5-12de-4ede-9994-64f747738330");
		
		System.out.println(json.toString());
		String jSon = getJsonFromService(json.toString(), URI_MOD_LINKEDIN);
		return jSon;
	}
}
