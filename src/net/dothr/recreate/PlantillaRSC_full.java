package net.dothr.recreate;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;


import net.dothr.fromjson.PosicionFromJson;
import net.utils.ClientRestUtily;
import net.utils.ConstantesREST;

public class PlantillaRSC_full extends PosicionFromJson {

	private static Logger log4j = Logger.getLogger( PlantillaRSC_full.class );
	public final static String URI_MODELORSC = "/module/modeloRsc"; //"module/tracktemplate";
//	public final static String TRACKING_DIR = ConstantesREST.JSON_HOME+"PersonaRecreate/vacancy/trackings/";
	
	private static final String MODRSC_JSON_DIR = ConstantesREST.JSON_HOME+"module/modeloRsc/";
	
	private final static String PATH_MODRSC_REP = "/home/dothr/workspace/ClientRest/files/out/Rep.PlantillaRSC.txt";
	
	private static JSONArray jsResponse;
	private static JSONObject jsonResp;
	
	public static void main(String[] args) {
		try {
			StringBuilder sb = loadTemplates();
			
			ClientRestUtily.writeStringInFile(PATH_MODRSC_REP, sb.toString(), false);
			log4j.debug("Se escribio resultado de PlantillaRSC_full en \n" + PATH_MODRSC_REP );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static StringBuilder loadTemplates() throws Exception{
		StringBuilder sbMain = new StringBuilder("Carga de Plantillas MOdeloRsc */ \n\n");
		String stReq, stResp, uriService = URI_MODELORSC+URI_CREATE;
		/* lectura del Json de Plantillas */
		String stLsTemplates = ClientRestUtily.getJsonFile("get.json", MODRSC_JSON_DIR);	
		/* Convertir la cadena en Arreeglo de JSON's */
		JSONArray jsTemplates = new JSONArray(stLsTemplates);
		JSONObject jsonModRsc;
//		System.out.println("# templates: "+ jsTemplates.length());
//		System.out.println("jsTemplates: \n"+jsTemplates.toString());
		//
		sbMain.append("Se cargan ").append(jsTemplates.length() - 1).append(" Plantillas/ModRsc \n" );//Se omite la de prototipo nueva id=99
		int nPlant=0;
		String idModeloRsc = null;
		for(int x=0;x<jsTemplates.length();x++){
			jsonModRsc = jsTemplates.getJSONObject(x);
			if(jsonModRsc.has("idModeloRsc") && !jsonModRsc.getString("idModeloRsc").equals("99")){
				jsonModRsc.remove("idModeloRsc");	
				jsonModRsc.put(P_JSON_IDCONF, ConstantesREST.IDEMPRESA_CONF );
				stReq = jsonModRsc.toString();
				sbMain.append(x+1).append(".- ").append(jsonModRsc.getString("nombre"))
					.append("\n ==> uriService: ").append(uriService)
					.append("\n ==> Json: ").append(stReq);
				log4j.debug("Insertando plantilla "+jsonModRsc.getString("nombre"));
				stResp = getJsonFromService(stReq, uriService );
				sbMain.append("\n<== Resp: ").append(stResp).append("\n");		
				
				jsResponse = new JSONArray(stResp);
				jsonResp = jsResponse.getJSONObject(0); 
				if(jsonResp.has("type") && jsonResp.getString("type").equals("I")
						&& jsonResp.has("name") ){
					idModeloRsc = jsonResp.getString("value");
					sbMain.append("Se inserto correctamente modelo RSC: ")
						.append(idModeloRsc).append("\n\n");
					nPlant++;
				}
			}
			else{
				log4j.debug("<> Se omite carga de "+jsonModRsc.toString());
			}
		}
		
		sbMain.append("Se Cargaron "+nPlant+" plantillas");
		
		return sbMain;
	}
	
	
}
