package net.mock;

import org.json.JSONArray;
import org.json.JSONObject;

import net.utils.ClientRestUtily;

public class ProcesaPersonaMock {
	
	private static String stFileJson;
	
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
		stFileJson = ClientRestUtily.getJsonFile(absolutePathFile);
		jsFile = new JSONArray(stFileJson);
		System.out.println("El response contiene "+jsFile.length()+ " json's: ");
		for(int x=0; x<jsFile.length();x++){
			json = jsFile.getJSONObject(x);
			System.out.println(json);
		}
	}
	
	
	public static void main(String[] args) {
		try {
			displayJson("/home/netto/workspDhr/JSONUI/module/curriculumManagement/_selex-1.octavio.linares.json");
//			generaGet();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static JSONObject jsonTmp, jsonPersona;
	private static JSONArray jsOut, jsAux;
	
//	/**
//	 * Genera la lista de PERSONA.G , a partir del archivo list en la carpeta con los json para incluirlos en el ambiente Demo 
//	 * @throws Exception
//	 */
//	public static void generaGet() throws Exception {
//		String stFileName;
//		stFileName = "/home/netto/workspDhr/JSONUI/module/curriculumManagement/_selex-3.monica.quintero.json";
//		jsOut = new JSONArray();
//		
//		
//		stFileJson = ClientRestUtily.getJsonFile(stFileName);
//		jsAux = new JSONArray(stFileJson);
//		jsonPersona = jsAux.getJSONObject(0);
//		
//		jsonTmp = new JSONObject();
//		jsonTmp.put("idPersona", jsonPersona.getString("idPersona"));
//		jsonTmp.put("email", jsonPersona.getString("email"));
//		jsonTmp.put("nombre", jsonPersona.getString("nombre"));
//		jsonTmp.put("apellidoPaterno", jsonPersona.getString("apellidoPaterno"));
//		jsonTmp.put("apellidoMaterno", jsonPersona.getString("apellidoMaterno"));
//		jsonTmp.put("idEstatusInscripcion", jsonPersona.getString("idEstatusInscripcion"));
//		jsonTmp.put("idRol", jsonPersona.getString("idRol"));
////		jsonTmp.put("idRelacionEmpresaPersona", "7");
//		jsOut.put(jsonTmp);
//		
//		
//		
//		System.out.println(jsOut);
//		
//		
//		
//	}
	
}
