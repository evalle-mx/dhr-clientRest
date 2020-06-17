package netto.jsons;

import org.json.JSONArray;
import org.json.JSONObject;

import net.utils.ClientRestUtily;

public class CommonJsonDemo {

	/**
	 * Metodo genérico para validar y mostrar el contenido de un Json.
	 * (Derivar otros metodos a partir de este)
	 * @param absolutePathFile
	 * @throws Exception
	 */
	protected static void displayJson(String absolutePathFile) throws Exception {
		System.out.println("displayJson1");
		
		JSONArray jsFile;
		JSONObject json;
		String stFileJson = ClientRestUtily.getJsonFile(absolutePathFile);
//		System.out.println("stFileJson: " + stFileJson);
		jsFile = new JSONArray(stFileJson);
		System.out.println("El response contiene "+jsFile.length()+ " json's: ");
		for(int x=0; x<jsFile.length();x++){
			json = jsFile.getJSONObject(x);
			System.out.println(json);
		}
	}
	
	public static void main(String[] args) {
		try {
			displayJson("/home/netto/workspDhr/KredApp/JSONfile/kred/users.json");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
