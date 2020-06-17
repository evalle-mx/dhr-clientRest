package net.dothr.test;

import net.utils.ClientRestUtily;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonProcess {

	
	public static void main(String[] args) {
		ordena();
	}
	

	public static void ordena(){
		try {
			JSONObject jsonMRsc = getJsonObject("testJson.json", "/home/dothr/Documents/SELEX/TMP/");
//			System.out.println("jsonMRsc: "+jsonMRsc);
			JSONArray jsFases = jsonMRsc.getJSONArray("fases");
			
//			System.out.println("jsFases: "+jsFases);
			JSONObject jsonTmp;
			String stOrdenFase, stOrdenTmp="1";
			Integer iOrdenFase, iOrdenTmp =1;
			for(int i=0; i<jsFases.length();i++){
				jsonTmp = jsFases.getJSONObject(i);
//				System.out.println("jsonTmp: " + jsonTmp);
				stOrdenFase = jsonTmp.getString("orden");
				//System.out.println("ordenFase: "+ ordenFase);
				iOrdenFase = Integer.parseInt(stOrdenFase);
				if(!stOrdenFase.equals(stOrdenTmp)){
					iOrdenTmp++;
					stOrdenTmp = stOrdenFase;
				}
				
				System.out.println("iOrdenFase: "+ iOrdenFase +", iOrdenTmp: "+iOrdenTmp);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static JSONObject getJsonObject(String fileName, String fileDir) throws Exception {
		String stPersona = ClientRestUtily.getJsonFile(fileName, fileDir);
		JSONArray jsResp = new JSONArray(stPersona);
//		JSONObject jsPersona = jsResp.getJSONObject(0);		
		return jsResp.getJSONObject(0);
	}
}
